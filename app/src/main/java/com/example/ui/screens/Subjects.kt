package com.example.ui.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class Subject(
    val id: String,
    val nameKey: String,
    val icon: ImageVector,
    val color1: Color,
    val color2: Color
)

val subjects = listOf(
    Subject("math", "math", Icons.Default.Calculate, Color(0xFF4CAF50), Color(0xFF2E7D32)),
    Subject("physics", "physics", Icons.Default.Science, Color(0xFF2196F3), Color(0xFF1565C0)),
    Subject("chemistry", "chemistry", Icons.Default.Science, Color(0xFF9C27B0), Color(0xFF6A1B9A)),
    Subject("biology", "biology", Icons.Default.BugReport, Color(0xFFE91E63), Color(0xFFAD1457)),
    Subject("history", "history_subj", Icons.Default.HistoryEdu, Color(0xFFFF9800), Color(0xFFEF6C00)),
    Subject("geography", "geography", Icons.Default.Public, Color(0xFF00BCD4), Color(0xFF00838F)),
    Subject("computer", "computer", Icons.Default.Computer, Color(0xFF607D8B), Color(0xFF37474F)),
    Subject("economics", "economics", Icons.Default.AttachMoney, Color(0xFFFFEB3B), Color(0xFFF9A825)),
    Subject("gk", "gk", Icons.Default.Lightbulb, Color(0xFFFF5722), Color(0xFFD84315)),
    Subject("hindi_literature", "hindi_literature", Icons.Default.MenuBook, Color(0xFF8D6E63), Color(0xFF5D4037)),
    Subject("english_literature", "english_literature", Icons.Default.MenuBook, Color(0xFF795548), Color(0xFF4E342E)),
    Subject("sanskrit", "sanskrit", Icons.Default.AutoStories, Color(0xFFFF7043), Color(0xFFD84315)),
    Subject("political_science", "political_science", Icons.Default.AccountBalance, Color(0xFF26A69A), Color(0xFF00695C)),
    Subject("hindi", "hindi_subj", Icons.Default.Language, Color(0xFFAB47BC), Color(0xFF6A1B9A)),
    Subject("english", "english_subj", Icons.Default.Language, Color(0xFF5C6BC0), Color(0xFF283593))
)
