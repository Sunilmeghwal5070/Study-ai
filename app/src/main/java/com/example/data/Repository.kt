package com.example.data

import kotlinx.coroutines.flow.Flow

class StudyRepository(private val studyDao: StudyDao) {
    val userSettings: Flow<UserSettings?> = studyDao.getUserSettings()
    val allHistory: Flow<List<HistoryItem>> = studyDao.getAllHistory()
    val chatMessages: Flow<List<ChatMessage>> = studyDao.getAllChatMessages()

    suspend fun saveUserSettings(settings: UserSettings) {
        studyDao.saveUserSettings(settings)
    }

    suspend fun insertHistoryItem(item: HistoryItem) {
        studyDao.insertHistoryItem(item)
    }

    suspend fun clearHistory() {
        studyDao.clearHistory()
    }

    suspend fun insertChatMessage(message: ChatMessage) {
        studyDao.insertChatMessage(message)
    }

    suspend fun clearChat() {
        studyDao.clearChat()
    }
}
