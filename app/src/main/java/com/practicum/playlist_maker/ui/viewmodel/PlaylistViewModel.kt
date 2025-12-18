package com.practicum.playlist_maker.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.practicum.playlist_maker.data.network.Playlist
import com.practicum.playlist_maker.domain.PlaylistsRepository
import com.practicum.playlist_maker.domain.TracksRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class PlaylistViewModel(
    private val playlistId: Long,
    private val playlistsRepository: PlaylistsRepository,
    private val tracksRepository: TracksRepository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    val playlist: StateFlow<Playlist?> =
        playlistsRepository.getPlaylist(playlistId)
            .onEach { _isLoading.value = false }
            .stateIn(viewModelScope, SharingStarted.Lazily, null)

    // суммарная длительность в минутах
    val totalMinutes: StateFlow<Long> = playlist
        .map { playlist ->
            playlist?.tracks?.sumOf { it.trackTimeMillis ?: 0L }?.div(1000)?.div(60) ?: 0L
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, 0L)

    // количество треков
    val trackCountText: StateFlow<String> = playlist
        .map { pl ->
            val count = pl?.tracks?.size ?: 0
            "$count ${trackWord(count)}"
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, "0 треков")

    // склонения
    private fun trackWord(count: Int): String {
        val rem100 = count % 100
        val rem10 = count % 10
        return when {
            rem100 in 11..19 -> "треков"
            rem10 == 1 -> "трек"
            rem10 in 2..4 -> "трека"
            else -> "треков"
        }
    }

    fun deleteTrackFromPlaylist(trackId: Long) {
        viewModelScope.launch {
            tracksRepository.deleteTrackFromPlaylist(
                trackId = trackId,
                playlistId = playlistId
            )
        }
    }

    fun deletePlaylist(onComplete: () -> Unit = {}) {
        viewModelScope.launch {
            playlistsRepository.deletePlaylistById(playlistId)
            onComplete()
        }
    }

    class Factory(
        private val playlistId: Long,
        private val playlistsRepository: PlaylistsRepository,
        private val tracksRepository: TracksRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return PlaylistViewModel(
                playlistId,
                playlistsRepository,
                tracksRepository
            ) as T
        }
    }
}