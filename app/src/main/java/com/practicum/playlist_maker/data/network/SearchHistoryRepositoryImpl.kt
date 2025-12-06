package com.practicum.playlist_maker.data.network

import com.practicum.playlist_maker.data.DatabaseMock
import com.practicum.playlist_maker.domain.SearchHistoryRepository
import kotlinx.coroutines.CoroutineScope

class SearchHistoryRepositoryImpl(private val scope: CoroutineScope): SearchHistoryRepository {
    private val database = DatabaseMock(scope = scope)

    override suspend fun getHistoryRequests(): List<String> {
        return database.getHistoryRequests()
    }

    override fun addToHistory(word: String) {
        database.addToHistory(word = word)
    }
}