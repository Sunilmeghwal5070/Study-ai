package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_settings")
data class UserSettings(
    @PrimaryKey val id: Int = 1,
    val name: String = "Student",
    val userClass: String = "AI Study Assistant",
    val language: String = "en",
    val credits: Int = 20,
    val lastCreditReset: Long = 0,
    val darkMode: Boolean = false,
    val voiceEnabled: Boolean = true,
    val autoOcr: Boolean = true,
    val vibrationEnabled: Boolean = true,
    val setupCompleted: Boolean = false,
    val favoriteSubjects: String = ""
)

@Entity(tableName = "history")
data class HistoryItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val question: String,
    val answer: String,
    val language: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "chat")
data class ChatMessage(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val role: String, // "user" or "ai"
    val content: String,
    val timestamp: Long = System.currentTimeMillis()
)
