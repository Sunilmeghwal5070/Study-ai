package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.ui.utils.MediaUtils

@Composable
fun TtsIconButton(text: String, lang: String, voiceEnabled: Boolean = true) {
    val playingId by MediaUtils.playingUtteranceId.collectAsState()
    var currentUtteranceId by remember { mutableStateOf<String?>(null) }
    
    val isPlaying = playingId != null && playingId == currentUtteranceId
    
    val infiniteTransition = rememberInfiniteTransition(label = "tts_pulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (isPlaying) 1.2f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_anim"
    )

    IconButton(
        onClick = {
            if (isPlaying) {
                MediaUtils.stopSpeaking()
                currentUtteranceId = null
            } else {
                currentUtteranceId = MediaUtils.speak(text, lang, voiceEnabled)
            }
        },
        modifier = Modifier.scale(scale)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(Icons.AutoMirrored.Filled.VolumeUp, contentDescription = "Listen", tint = MaterialTheme.colorScheme.primary)
            if (isPlaying) {
                CircularProgressIndicator(
                    modifier = Modifier.size(36.dp),
                    color = MaterialTheme.colorScheme.primary,
                    strokeWidth = 2.dp
                )
            }
        }
    }
}
