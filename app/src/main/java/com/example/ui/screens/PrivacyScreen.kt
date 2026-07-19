package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrivacyScreen(navController: NavController, innerPadding: PaddingValues) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Privacy Policy") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Privacy Policy & Disclaimer", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            
            Text("Data Collection", fontWeight = FontWeight.Bold)
            Text("Study AI collects minimal user data necessary for app functionality. This includes your name, class, and language preference which are stored locally on your device. We do not collect personal identification information or share your data with third parties.")
            
            Text("Data Storage", fontWeight = FontWeight.Bold)
            Text("All user data is stored locally on your device using Room Database. Your question history and chat conversations remain on your device and are not transmitted to our servers.")
            
            Text("AI Technology", fontWeight = FontWeight.Bold)
            Text("Study AI uses Gemini API to provide educational assistance. Questions are sent to an AI API for processing, but no personal data is included with these requests.")
            
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("⚠️ IMPORTANT DISCLAIMER", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onErrorContainer)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("If this Study AI gives an incorrect answer to any question, we will not be responsible. The AI may occasionally provide inaccurate or incomplete information. Always verify important information from trusted sources.", color = MaterialTheme.colorScheme.onErrorContainer)
                }
            }
        }
    }
}
