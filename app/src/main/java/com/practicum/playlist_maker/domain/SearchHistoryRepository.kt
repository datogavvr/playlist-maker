package com.practicum.playlist_maker.domain

interface SearchHistoryRepository {

    suspend fun getHistoryRequests(): List<String>

    fun addToHistory(word: String)

    suspend fun removeFromHistory(word: String)
}