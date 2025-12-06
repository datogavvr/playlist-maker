package com.practicum.playlist_maker.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.practicum.playlist_maker.creator.Creator
import com.practicum.playlist_maker.data.network.SearchHistoryRepositoryImpl
import com.practicum.playlist_maker.domain.TracksRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okio.IOException

class SearchViewModel(
    private val tracksRepository: TracksRepository
) : ViewModel() {
    private val searchHistoryRepository = SearchHistoryRepositoryImpl(scope = viewModelScope)
    private val _searchScreenState = MutableStateFlow<SearchState>(SearchState.Initial)
    val searchScreenState  = _searchScreenState.asStateFlow()
    private val _history = MutableStateFlow<List<String>>(emptyList())
    val history = _history.asStateFlow()


    fun search(whatSearch: String){
        if (whatSearch.isBlank()) {
            _searchScreenState.update { SearchState.Initial }
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _searchScreenState.update { SearchState.Searching }
                val list = tracksRepository.searchTracks(expression = whatSearch)
                val lower = whatSearch.lowercase()
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

    // загрузка 3 последних запросов
    suspend fun loadHistory() {
        _history.value = searchHistoryRepository.getHistoryRequests().take(3)
    }

    // сохранение запроса в историю
    suspend fun saveQueryToHistory(query: String) {
        if (query.isNotBlank()) {
            searchHistoryRepository.addToHistory(query)
            loadHistory()
        }
    }

    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return SearchViewModel(Creator.getTracksRepository()) as T
                }
            }
    }
}