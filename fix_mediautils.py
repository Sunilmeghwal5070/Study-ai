import re

with open("app/src/main/java/com/example/ui/utils/MediaUtils.kt", "r") as f:
    content = f.read()

# Add UtteranceProgressListener import
content = content.replace('import java.util.Locale', 'import java.util.Locale\nimport android.speech.tts.UtteranceProgressListener')

# Add playingUtteranceId
content = content.replace('val isTtsReady = MutableStateFlow(false)', 'val isTtsReady = MutableStateFlow(false)\n    val playingUtteranceId = MutableStateFlow<String?>(null)')

# Update initTTS
old_inittts = """    fun initTTS(context: Context) {
        if (tts == null) {
            tts = TextToSpeech(context.applicationContext) { status ->
                if (status == TextToSpeech.SUCCESS) {
                    isTtsReady.value = true
                }
            }
        }
    }"""
new_inittts = """    fun initTTS(context: Context) {
        if (tts == null) {
            tts = TextToSpeech(context.applicationContext) { status ->
                if (status == TextToSpeech.SUCCESS) {
                    isTtsReady.value = true
                    tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                        override fun onStart(utteranceId: String?) {
                            playingUtteranceId.value = utteranceId
                        }
                        override fun onDone(utteranceId: String?) {
                            if (playingUtteranceId.value == utteranceId) {
                                playingUtteranceId.value = null
                            }
                        }
                        @Deprecated("Deprecated in Java")
                        override fun onError(utteranceId: String?) {
                            if (playingUtteranceId.value == utteranceId) {
                                playingUtteranceId.value = null
                            }
                        }
                    })
                }
            }
        }
    }"""
content = content.replace(old_inittts, new_inittts)

# Update speak to return utteranceId
old_speak = """    fun speak(text: String, lang: String, voiceEnabled: Boolean) {
        if (!voiceEnabled) return
        val ttsInstance = tts ?: return
        if (!isTtsReady.value) return
        val locale = if (lang == "hi") Locale("hi", "IN") else Locale.US
        ttsInstance.language = locale
        
        // Improve voice quality
        ttsInstance.setSpeechRate(0.95f)
        ttsInstance.setPitch(1.05f)
        
        // Try to find a high quality voice if available
        try {
            val voices = ttsInstance.voices
            if (voices != null) {
                val bestVoice = voices.asSequence()
                    .filter { it.locale.language == locale.language }
                    .filter { !it.isNetworkConnectionRequired }
                    .filter { it.quality >= Voice.QUALITY_HIGH || it.quality >= Voice.QUALITY_NORMAL }
                    .sortedByDescending { it.quality }
                    .firstOrNull()
                
                if (bestVoice != null) {
                    ttsInstance.voice = bestVoice
                }
            }
        } catch (e: Exception) {
            // Ignore
        }
        ttsInstance.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
    }"""
new_speak = """    fun speak(text: String, lang: String, voiceEnabled: Boolean): String? {
        if (!voiceEnabled) return null
        val ttsInstance = tts ?: return null
        if (!isTtsReady.value) return null
        val locale = if (lang == "hi") Locale("hi", "IN") else Locale.US
        ttsInstance.language = locale
        
        // Improve voice quality
        ttsInstance.setSpeechRate(0.95f)
        ttsInstance.setPitch(1.05f)
        
        // Try to find a high quality voice if available
        try {
            val voices = ttsInstance.voices
            if (voices != null) {
                val bestVoice = voices.asSequence()
                    .filter { it.locale.language == locale.language }
                    .filter { !it.isNetworkConnectionRequired }
                    .filter { it.quality >= Voice.QUALITY_HIGH || it.quality >= Voice.QUALITY_NORMAL }
                    .sortedByDescending { it.quality }
                    .firstOrNull()
                
                if (bestVoice != null) {
                    ttsInstance.voice = bestVoice
                }
            }
        } catch (e: Exception) {
            // Ignore
        }
        val utteranceId = "utterance_${System.currentTimeMillis()}"
        ttsInstance.speak(text, TextToSpeech.QUEUE_FLUSH, null, utteranceId)
        return utteranceId
    }"""
content = content.replace(old_speak, new_speak)

# Update stopSpeaking
old_stop = """    fun stopSpeaking() {
        tts?.stop()
    }"""
new_stop = """    fun stopSpeaking() {
        tts?.stop()
        playingUtteranceId.value = null
    }"""
content = content.replace(old_stop, new_stop)

with open("app/src/main/java/com/example/ui/utils/MediaUtils.kt", "w") as f:
    f.write(content)
