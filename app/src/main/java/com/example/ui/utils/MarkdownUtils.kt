package com.example.ui.utils

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle

object MarkdownUtils {
    fun parseMarkdown(text: String): AnnotatedString {
        return buildAnnotatedString {
            var currentIndex = 0
            val boldPattern = "\\*\\*(.*?)\\*\\*".toRegex()
            
            val matches = boldPattern.findAll(text)
            
            for (match in matches) {
                // Append text before the match
                if (match.range.first > currentIndex) {
                    append(text.substring(currentIndex, match.range.first))
                }
                
                // Append the bold text
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(match.groupValues[1])
                }
                
                currentIndex = match.range.last + 1
            }
            
            // Append remaining text
            if (currentIndex < text.length) {
                append(text.substring(currentIndex))
            }
        }
    }
}
