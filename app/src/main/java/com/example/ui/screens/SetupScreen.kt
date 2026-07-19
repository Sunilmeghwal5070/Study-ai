package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ui.StudyViewModel
import com.example.ui.navigation.Screen
import com.example.ui.utils.AppStrings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetupScreen(navController: NavController, viewModel: StudyViewModel) {
    val settings by viewModel.userSettings.collectAsState()
    var name by remember { mutableStateOf(settings?.name ?: "") }
    var userClass by remember { mutableStateOf(settings?.userClass ?: "") }
    var language by remember { mutableStateOf(settings?.language ?: "en") }
    var expanded by remember { mutableStateOf(false) }

    val classOptions = listOf(
        "Class 1", "Class 2", "Class 3", "Class 4", "Class 5", 
        "Class 6", "Class 7", "Class 8", "Class 9", "Class 10", 
        "Class 11", "Class 12", "College", "Other"
    )

    LaunchedEffect(settings) {
        if (settings != null) {
            name = settings?.name ?: ""
            userClass = settings?.userClass ?: ""
            language = settings?.language ?: "en"
        }
    }

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.School,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(80.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = AppStrings.get("welcome", language),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = AppStrings.get("personalize", language),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text(AppStrings.get("your_name", language)) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = it }
            ) {
                OutlinedTextField(
                    value = userClass,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text(AppStrings.get("class_grade", language)) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier.fillMaxWidth().menuAnchor(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    classOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                userClass = option
                                expanded = false
                            }
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Language
            Text(text = AppStrings.get("pref_app_lang", language), modifier = Modifier.align(Alignment.Start))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                FilterChip(
                    selected = language == "en",
                    onClick = { language = "en" },
                    label = { Text("English") }
                )
                FilterChip(
                    selected = language == "hi",
                    onClick = { language = "hi" },
                    label = { Text("Hindi (हिंदी)") }
                )
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Button(
                onClick = {
                    if (name.isNotBlank() && userClass.isNotBlank()) {
                        viewModel.completeSetup(name, userClass, language)
                        if (settings?.setupCompleted == true) {
                            navController.popBackStack()
                        } else {
                            navController.popBackStack()
                            navController.navigate(Screen.Home.route)
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Icon(Icons.Default.Check, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(AppStrings.get("save_continue", language), fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
