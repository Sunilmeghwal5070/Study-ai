package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(navController: NavController, innerPadding: PaddingValues) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("About & Developer") },
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
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Study AI Assistant", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Text("Intelligent Learning Companion", color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.height(32.dp))
            
            Text("Developer", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Sunil Meghwal", style = MaterialTheme.typography.titleMedium)
            Text("Full Stack Developer & AI Enthusiast", color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "Passionate about creating innovative educational tools that make learning accessible to everyone. " +
                "With expertise in web development and artificial intelligence, I built Study AI to help students " +
                "overcome academic challenges and learn more effectively.",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("App Version: 2.0.1")
                    Text("AI Technology: GPT-based AI / Gemini")
                    Text("Supported Subjects: 11+ Subjects")
                }
            }
        }
    }
}
