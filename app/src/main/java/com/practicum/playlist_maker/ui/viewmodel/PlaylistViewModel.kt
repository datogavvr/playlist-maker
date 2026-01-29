package com.practicum.playlist_maker.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.practicum.playlist_maker.data.network.Playlist
import com.practicum.playlist_maker.data.network.Track
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

    // информация о плейлисте
    val playlist: Flow<Playlist?> =
        playlistsRepository.getPlaylist(playlistId)
            .onEach { _isLoading.value = false }
            .stateIn(viewModelScope, SharingStarted.Lazily, null)

    // список треков в плейлисте
    val tracks: Flow<List<Track>> =
        tracksRepository.getTracksByPlaylistId(playlistId)

    // суммарная длительность в минутах
    val totalTime: StateFlow<String> = tracks
        .map { tracks ->
            val totalSeconds = tracks.sumOf { it.trackTimeMillis ?: 0L } / 1000
            val hours = totalSeconds / 3600
            val minutes = (totalSeconds % 3600) / 60

            if (hours > 0) {
                val hoursWord = when {
                    hours % 10 == 1L && hours % 100 != 11L -> "час"
                    hours % 10 in 2..4 && hours % 100 !in 12..14 -> "часа"
                    else -> "часов"
                }
                "$hours $hoursWord"
            } else {
                val minutesWord = when {
                    minutes % 10 == 1L && minutes % 100 != 11L -> "минута"
                    minutes % 10 in 2..4 && minutes % 100 !in 12..14 -> "минуты"
                    else -> "минут"
                }
                "$minutes $minutesWord"
            }
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, "0 минут")

    // количество треков
    val trackCountText: StateFlow<String> = tracks
        .map { tracks ->
            val count = tracks.size
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

    fun updatePlaylist(
        playlistId: Long,
        playlistName: String,
        description: String,
        coverUri: String?
    ) {
        viewModelScope.launch {
            playlistsRepository.updatePlaylist(
                playlistId = playlistId,
                playlistName = playlistName,
                description = description,
                coverUri = coverUri
            )
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