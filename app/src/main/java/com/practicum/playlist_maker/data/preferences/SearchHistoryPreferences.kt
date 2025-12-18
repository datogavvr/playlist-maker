package com.practicum.playlist_maker.data.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import androidx.datastore.preferences.core.Preferences

class SearchHistoryPreferences(
    private val dataStore: DataStore<Preferences>,
    private val coroutineScope: CoroutineScope = CoroutineScope(CoroutineName("search-history-preferences") + SupervisorJob())
) {
    fun addEntry(word: String) {
        val query = word.trim().lowercase()
        if (query.isEmpty()) return

        coroutineScope.launch {
            dataStore.edit { preferences ->
                val historyString = preferences[HISTORY_KEY].orEmpty()
                val history = if (historyString.isNotEmpty()) {
                    historyString.split(SEPARATOR).toMutableList()
                } else {
                    mutableListOf()
                }

                history.remove(query)
                history.add(0, query)

                preferences[HISTORY_KEY] = history.take(MAX_ENTRIES).joinToString(SEPARATOR)
            }
        }
    }

    suspend fun removeEntry(word: String) {
        dataStore.edit { preferences ->
            val historyString = preferences[HISTORY_KEY].orEmpty()
            if (historyString.isEmpty()) return@edit

            val history = historyString
                .split(SEPARATOR)
                .toMutableList()

            history.remove(word)

            preferences[HISTORY_KEY] =
                history.take(MAX_ENTRIES).joinToString(SEPARATOR)
        }
    }

    suspend fun getEntries(): List<String> {
        val preferences = dataStore.data.first()
        val historyString = preferences[HISTORY_KEY].orEmpty()

        return if (historyString.isNotEmpty()) {
            historyString.split(SEPARATOR)
        } else {
            emptyList()
        }
    }

    companion object {
        private const val MAX_ENTRIES = 10
        private const val SEPARATOR = ","
        private val HISTORY_KEY = stringPreferencesKey("search_history")
    }
}