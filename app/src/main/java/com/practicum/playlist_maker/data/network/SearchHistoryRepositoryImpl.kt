package com.practicum.playlist_maker.data.network

import com.practicum.playlist_maker.data.preferences.SearchHistoryPreferences
import com.practicum.playlist_maker.domain.SearchHistoryRepository

class SearchHistoryRepositoryImpl(
    private val preferences: SearchHistoryPreferences
): SearchHistoryRepository {

    override suspend fun getHistoryRequests(): List<String> {
        return preferences.getEntries()
    }

    override fun addToHistory(word: String) {
        preferences.addEntry(word = word)
    }

    override suspend fun removeFromHistory(word: String) {
        preferences.removeEntry(word = word)
    }
}