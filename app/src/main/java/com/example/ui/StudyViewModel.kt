package com.example.ui

import android.content.Context
import android.graphics.Bitmap
import android.util.Base64
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.BuildConfig
import com.example.api.Candidate
import com.example.api.Content
import com.example.api.GenerateContentRequest
import com.example.api.InlineData
import com.example.api.Part
import com.example.api.RetrofitClient
import com.example.data.ChatMessage
import com.example.data.HistoryItem
import com.example.data.StudyRepository
import com.example.data.UserSettings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream

class StudyViewModel(private val repository: StudyRepository) : ViewModel() {

    val userSettings: StateFlow<UserSettings?> = repository.userSettings.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    val history: StateFlow<List<HistoryItem>> = repository.allHistory.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val chatMessages: StateFlow<List<ChatMessage>> = repository.chatMessages.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun clearError() {
        _error.value = null
    }

    fun saveSettings(settings: UserSettings) {
        viewModelScope.launch {
            repository.saveUserSettings(settings)
        }
    }

    fun completeSetup(name: String, userClass: String, language: String) {
        viewModelScope.launch {
            val current = userSettings.value ?: UserSettings()
            repository.saveUserSettings(
                current.copy(
                    name = name,
                    userClass = userClass,
                    language = language,
                    setupCompleted = true
                )
            )
        }
    }
    
    fun deductCredit() {
        viewModelScope.launch {
            val current = userSettings.value ?: return@launch
            if (current.credits > 0) {
                repository.saveUserSettings(current.copy(credits = current.credits - 1))
            }
        }
    }

    fun checkAndResetCredits() {
        viewModelScope.launch {
            val current = userSettings.value ?: return@launch
            
            val lastResetCal = java.util.Calendar.getInstance()
            lastResetCal.timeInMillis = current.lastCreditReset
            
            val nowCal = java.util.Calendar.getInstance()
            
            val isSameDay = lastResetCal.get(java.util.Calendar.YEAR) == nowCal.get(java.util.Calendar.YEAR) &&
                            lastResetCal.get(java.util.Calendar.DAY_OF_YEAR) == nowCal.get(java.util.Calendar.DAY_OF_YEAR)
            
            if (!isSameDay) {
                // Reset daily free credits up to 20. If they have more than 20 (e.g. bought), leave them as is.
                val newCredits = kotlin.math.max(20, current.credits)
                repository.saveUserSettings(current.copy(credits = newCredits, lastCreditReset = System.currentTimeMillis()))
            }
        }
    }

    fun addCredits(amount: Int) {
        viewModelScope.launch {
            val current = userSettings.value ?: return@launch
            repository.saveUserSettings(current.copy(credits = current.credits + amount))
        }
    }

    private fun Bitmap.toBase64(): String {
        val outputStream = ByteArrayOutputStream()
        // Resize bitmap if too large to save tokens
        val maxDim = 1024
        val ratio = kotlin.math.min(maxDim.toFloat() / width, maxDim.toFloat() / height)
        val resized = if (ratio < 1) {
            Bitmap.createScaledBitmap(this, (width * ratio).toInt(), (height * ratio).toInt(), true)
        } else {
            this
        }
        resized.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)
        return Base64.encodeToString(outputStream.toByteArray(), Base64.NO_WRAP)
    }

    suspend fun getSolverAnswer(
        subject: String,
        question: String,
        image: Bitmap?,
        lang: String,
        onSuccess: (String) -> Unit
    ) {
        _isLoading.value = true
        _error.value = null
        try {
            val apiKey = BuildConfig.GEMINI_API_KEY
            if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY") {
                _error.value = "API Key is missing. Please configure it in Secrets."
                return
            }

            val parts = mutableListOf<Part>()
            val langInstruction = if (lang == "hi") "Please answer in Hindi." else "Please answer in English."
            var promptText = "Subject: $subject. "
            if (question.isNotEmpty()) {
                promptText += "Question: $question. "
            } else if (image != null) {
                promptText += "Analyze the provided image and solve the problem or describe it. "
            }
            promptText += langInstruction
            
            parts.add(Part(text = promptText))
            
            if (image != null) {
                parts.add(Part(inlineData = InlineData(mimeType = "image/jpeg", data = image.toBase64())))
            }

            val request = GenerateContentRequest(
                contents = listOf(Content(parts = parts, role = "user")),
                systemInstruction = Content(parts = listOf(Part(text = "You are a helpful tutor. Provide a very short, direct, and simple final answer to the student without any complex formatting, markdown, or LaTeX symbols. Do not explain the steps, just give the final answer in plain text.")), role = "model")
            )
            
            val response = RetrofitClient.service.generateContent("v1beta/models/gemini-1.5-flash:generateContent", apiKey, request)
            val answer = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
            
            if (answer != null) {
                repository.insertHistoryItem(HistoryItem(question = question.ifEmpty { "Image Question" }, answer = answer, language = lang))
                deductCredit()
                onSuccess(answer)
            } else {
                _error.value = "Failed to generate answer."
            }
        } catch (e: Exception) {
            _error.value = "Error: ${e.message}"
        } finally {
            _isLoading.value = false
        }
    }
    
    suspend fun explainAnswer(answer: String, lang: String, onSuccess: (String) -> Unit) {
        _isLoading.value = true
        _error.value = null
        try {
            val apiKey = BuildConfig.GEMINI_API_KEY
            val langInstruction = if (lang == "hi") "कृपया हिंदी में समझाएं।" else "Please explain in English."
            val prompt = "Explain this answer in simple steps for a student: \"$answer\". $langInstruction Provide a clear, step-by-step breakdown that helps understand the concept. Do NOT use any special LaTeX math symbols like $$ or markdown symbols. Use plain text formatting and simple structure so anyone can understand."
            
            val request = GenerateContentRequest(
                contents = listOf(Content(parts = listOf(Part(text = prompt))))
            )
            
            val response = RetrofitClient.service.generateContent("v1beta/models/gemini-1.5-flash:generateContent", apiKey, request)
            val explanation = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
            
            if (explanation != null) {
                onSuccess(explanation)
            } else {
                _error.value = "Failed to generate explanation."
            }
        } catch (e: Exception) {
            _error.value = "Error: ${e.message}"
        } finally {
            _isLoading.value = false
        }
    }

    suspend fun translateText(text: String, fromLang: String, toLang: String, onSuccess: (String) -> Unit) {
        _isLoading.value = true
        _error.value = null
        try {
            val apiKey = BuildConfig.GEMINI_API_KEY
            val prompt = "Translate the following text from $fromLang to $toLang: \"$text\". Only provide the translation, no explanations."
            
            val request = GenerateContentRequest(
                contents = listOf(Content(parts = listOf(Part(text = prompt))))
            )
            
            val response = RetrofitClient.service.generateContent("v1beta/models/gemini-1.5-flash:generateContent", apiKey, request)
            val translation = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
            
            if (translation != null) {
                onSuccess(translation)
            } else {
                _error.value = "Failed to generate translation."
            }
        } catch (e: Exception) {
            _error.value = "Error: ${e.message}"
        } finally {
            _isLoading.value = false
        }
    }

    suspend fun sendChatMessage(message: String, lang: String, onSuccess: (String) -> Unit = {}) {
        _isLoading.value = true
        _error.value = null
        try {
            val apiKey = BuildConfig.GEMINI_API_KEY
            repository.insertChatMessage(ChatMessage(role = "user", content = message))
            deductCredit()
            
            val langContext = if (lang == "hi") "Reply in Hindi." else "Reply in English."
            val sysInstruction = "You are a helpful AI study assistant. $langContext"
            
            // Build conversation history (limit to last 10 messages)
            val currentHistory = chatMessages.value.takeLast(10).filter { it.id > 0 } // exclude pending
            val contents = currentHistory.map { msg ->
                val apiRole = if (msg.role == "ai") "model" else "user"
                Content(parts = listOf(Part(text = msg.content)), role = apiRole)
            }.toMutableList()
            
            // In case the flow hasn't updated yet, ensure the latest message is there
            if (contents.isEmpty() || contents.last().parts.firstOrNull()?.text != message) {
                contents.add(Content(parts = listOf(Part(text = message)), role = "user"))
            }
            
            val request = GenerateContentRequest(
                contents = contents,
                systemInstruction = Content(parts = listOf(Part(text = sysInstruction)), role = "model")
            )
            
            val response = RetrofitClient.service.generateContent("v1beta/models/gemini-1.5-flash:generateContent", apiKey, request)
            val reply = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
            
            if (reply != null) {
                repository.insertChatMessage(ChatMessage(role = "ai", content = reply))
                onSuccess(reply)
            } else {
                _error.value = "Failed to get AI reply."
            }
        } catch (e: Exception) {
            _error.value = "Error: ${e.message}"
        } finally {
            _isLoading.value = false
        }
    }

    fun clearChat() {
        viewModelScope.launch {
            repository.clearChat()
        }
    }

    fun clearHistory() {
        viewModelScope.launch {
            repository.clearHistory()
        }
    }
}

class StudyViewModelFactory(private val repository: StudyRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StudyViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return StudyViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
