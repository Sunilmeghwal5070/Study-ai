package com.example.ui.screens

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.ui.StudyViewModel
import com.example.ui.navigation.Screen
import com.example.ui.utils.AppStrings

@Composable
fun SettingsScreen(navController: NavController, viewModel: StudyViewModel, innerPadding: PaddingValues) {
    val settings by viewModel.userSettings.collectAsState()
    val lang = settings?.language ?: "en"
    var showRequestDialog by remember { mutableStateOf(false) }
    var requestSubjectName by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(AppStrings.get("settings", lang), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        SettingsSection(title = AppStrings.get("profile", lang), icon = Icons.Default.Person) {
            SettingsItem(title = settings?.name ?: AppStrings.get("student", lang), subtitle = settings?.userClass ?: AppStrings.get("student", lang), icon = Icons.Default.Edit) {
                navController.navigate(Screen.Setup.route)
            }
            SettingsItem(title = AppStrings.get("daily_credits", lang), subtitle = "${settings?.credits ?: 0} ${AppStrings.get("available", lang)}") {}
            if ((settings?.credits ?: 0) <= 0) { com.example.ui.components.CreditRefreshTimer(lang = lang) }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        SettingsSection(title = AppStrings.get("preferences", lang), icon = Icons.Default.Settings) {
            val currentSettings = settings ?: com.example.data.UserSettings()
            
            // App Language Selector
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(AppStrings.get("app_language", lang), fontWeight = FontWeight.Bold)
                    Text(AppStrings.get("app_language_desc", lang), color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodySmall)
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(
                        selected = currentSettings.language == "en",
                        onClick = { viewModel.saveSettings(currentSettings.copy(language = "en")) },
                        label = { Text("EN") }
                    )
                    FilterChip(
                        selected = currentSettings.language == "hi",
                        onClick = { viewModel.saveSettings(currentSettings.copy(language = "hi")) },
                        label = { Text("HI") }
                    )
                }
            }

            SettingsToggle(title = AppStrings.get("dark_mode", lang), subtitle = AppStrings.get("dark_mode_desc", lang), checked = currentSettings.darkMode) {
                viewModel.saveSettings(currentSettings.copy(darkMode = it))
            }

            SettingsToggle(title = AppStrings.get("voice_responses", lang), subtitle = AppStrings.get("voice_responses_desc", lang), checked = currentSettings.voiceEnabled) {
                viewModel.saveSettings(currentSettings.copy(voiceEnabled = it))
            }

            SettingsToggle(title = AppStrings.get("vibration", lang), subtitle = AppStrings.get("vibration_desc", lang), checked = currentSettings.vibrationEnabled) {
                viewModel.saveSettings(currentSettings.copy(vibrationEnabled = it))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        
        SettingsSection(title = AppStrings.get("about_privacy", lang), icon = Icons.Default.Info) {
            SettingsItem(title = AppStrings.get("about_dev", lang), subtitle = AppStrings.get("about_dev_desc", lang), icon = Icons.Default.ChevronRight) {
                navController.navigate(Screen.About.route)
            }
            SettingsItem(title = AppStrings.get("privacy_policy", lang), subtitle = AppStrings.get("privacy_policy_desc", lang), icon = Icons.Default.Shield) {
                navController.navigate(Screen.Privacy.route)
            }
            SettingsItem(title = AppStrings.get("request_subject", lang), subtitle = AppStrings.get("request_subject_desc", lang), icon = Icons.Default.AddCircle) {
                showRequestDialog = true
            }
        }
    }

    if (showRequestDialog) {
        AlertDialog(
            onDismissRequest = { showRequestDialog = false },
            title = { Text(AppStrings.get("request_subject_title", lang)) },
            text = { 
                TextField(
                    value = requestSubjectName,
                    onValueChange = { requestSubjectName = it },
                    label = { Text(AppStrings.get("subject_name", lang)) }
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    Toast.makeText(context, AppStrings.get("subject_requested", lang), Toast.LENGTH_SHORT).show()
                    showRequestDialog = false
                    requestSubjectName = ""
                }) {
                    Text(AppStrings.get("submit", lang))
                }
            },
            dismissButton = {
                TextButton(onClick = { showRequestDialog = false }) {
                    Text(AppStrings.get("cancel", lang))
                }
            }
        )
    }
}

@Composable
fun SettingsSection(title: String, icon: ImageVector, content: @Composable ColumnScope.() -> Unit) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.width(12.dp))
                Text(title, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            }
            Spacer(modifier = Modifier.height(16.dp))
            content()
        }
    }
}

@Composable
fun SettingsItem(title: String, subtitle: String, icon: ImageVector? = null, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontWeight = FontWeight.Bold)
            Text(subtitle, color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodySmall)
        }
        if (icon != null) {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
fun SettingsToggle(title: String, subtitle: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontWeight = FontWeight.Bold)
            Text(subtitle, color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodySmall)
        }
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}
