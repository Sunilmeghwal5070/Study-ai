package com.example.ui.utils

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.speech.tts.TextToSpeech
import android.speech.tts.Voice
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.Locale

object MediaUtils {
    var tts: TextToSpeech? = null
    val isTtsReady = MutableStateFlow(false)

    fun initTTS(context: Context) {
        if (tts == null) {
            tts = TextToSpeech(context.applicationContext) { status ->
                if (status == TextToSpeech.SUCCESS) {
                    isTtsReady.value = true
                }
            }
        }
    }

    fun speak(text: String, lang: String, voiceEnabled: Boolean) {
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
    }

    fun stopSpeaking() {
        tts?.stop()
    }

    fun vibrate(context: Context, vibrationEnabled: Boolean) {
        if (!vibrationEnabled) return
        try {
            val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
                vibratorManager.defaultVibrator
            } else {
                @Suppress("DEPRECATION")
                context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(150, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                vibrator.vibrate(150)
            }
        } catch (e: Exception) {
            // Ignore
        }
    }
}
