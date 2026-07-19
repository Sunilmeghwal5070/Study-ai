package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.ui.utils.NetworkStatus
import kotlinx.coroutines.delay

@Composable
fun NetworkBanner(networkStatus: NetworkStatus) {
    var isVisible by remember { mutableStateOf(false) }
    var backgroundColor by remember { mutableStateOf(Color.Red) }
    var message by remember { mutableStateOf("No Internet Connection") }

    LaunchedEffect(networkStatus) {
        if (networkStatus == NetworkStatus.Unavailable) {
            backgroundColor = Color(0xFFE53935)
            message = "No Internet Connection"
            isVisible = true
        } else if (networkStatus == NetworkStatus.Available) {
            if (isVisible) { // Only show back online if we were previously showing offline
                backgroundColor = Color(0xFF43A047)
                message = "Back Online"
                delay(3000)
                isVisible = false
            }
        }
    }

    AnimatedVisibility(
        visible = isVisible,
        enter = expandVertically(animationSpec = tween(500)),
        exit = shrinkVertically(animationSpec = tween(500))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(backgroundColor)
                .padding(vertical = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = message,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
    }
}
