package com.practicum.playlist_maker.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.practicum.playlist_maker.creator.Creator
import com.practicum.playlist_maker.domain.SearchHistoryRepository
import com.practicum.playlist_maker.domain.TracksRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okio.IOException

class SearchViewModel(
    private val tracksRepository: TracksRepository,
    private val historyRepository: SearchHistoryRepository
) : ViewModel() {
    private val _searchScreenState = MutableStateFlow<SearchState>(SearchState.Initial)
    val searchScreenState  = _searchScreenState.asStateFlow()
    private val _history = MutableStateFlow<List<String>>(emptyList())
    val history = _history.asStateFlow()
    private var lastQuery: String? = null


    fun search(whatSearch: String){
        if (whatSearch.isBlank()) {
            _searchScreenState.update { SearchState.Initial }
            return
        }
        lastQuery = whatSearch
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _searchScreenState.update { SearchState.Searching }
                val list = tracksRepository.searchTracks(expression = whatSearch)
                val lower = whatSearch.trim().lowercase()
                val filtered = list.filter { track ->
                    track.trackName.lowercase().contains(lower) ||
                            track.artistName.lowercase().contains(lower)
                }
                _searchScreenState.update { SearchState.Success(list = filtered) }
            } catch (e: IOException){
                _searchScreenState.update { SearchState.Fail(e.message.toString()) }
            }
        }
    }

    fun retryLastSearch() {
        lastQuery?.let { search(it) }
    }

    // загрузка 3 последних запросов
    suspend fun loadHistory() {
        _history.value = historyRepository.getHistoryRequests().take(3)
    }

    // сохранение запроса в историю
    suspend fun saveQueryToHistory(query: String) {
        if (query.isNotBlank()) {
            historyRepository.addToHistory(query)
            loadHistory()
        }
    }

    // удаление запроса из истории
    fun removeQueryFromHistory(query: String) {
        viewModelScope.launch {
            historyRepository.removeFromHistory(query)
            loadHistory()
        }
    }

    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return SearchViewModel(
                        tracksRepository = Creator.getTracksRepository(),
                        historyRepository = Creator.getSearchHistoryRepository()
                        ) as T
                }
            }
    }
}