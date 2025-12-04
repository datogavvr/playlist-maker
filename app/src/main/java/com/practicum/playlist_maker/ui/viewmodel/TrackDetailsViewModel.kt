package com.practicum.playlist_maker.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlist_maker.creator.Creator
import com.practicum.playlist_maker.data.network.Playlist
import com.practicum.playlist_maker.data.network.Track
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class TrackDetailsViewModel : ViewModel() {

    private val tracksRepository = Creator.getTracksRepository()
    private val playlistsRepository = Creator.getPlaylistsRepository()

    // текущее состояние выбранного трека
    private val _trackState = MutableStateFlow<Track?>(null)
    val trackState: StateFlow<Track?> = _trackState.asStateFlow()

    // получение трека по id
    fun getTrack(id: Long): Flow<Track?> {
        viewModelScope.launch {
            tracksRepository.getTrackById(id).collect { track ->
                _trackState.value = track
            }
        }
        return trackState
    }

    // получение списка всех плейлистов
    fun getPlaylists(): Flow<List<Playlist>> = playlistsRepository.getAllPlaylists()

    // обновление избранного
    fun updateTrackFavoriteStatus(trackId: Long, favorite: Boolean) {
        viewModelScope.launch {
            tracksRepository.updateTrackFavoriteStatus(trackId, favorite)

            _trackState.update { old ->
                old?.copy(favorite = favorite)
            }
        }
    }

    // добавление трека в плейлист
    fun addTrackToPlaylist(track: Track?, playlistId: Long) {
        if (track == null) return

        viewModelScope.launch {

            if (!track.playlistId.contains(playlistId)) {
                track.playlistId.add(playlistId)
            }

            tracksRepository.addTrackToPlaylist(track.id, playlistId)

            _trackState.value = track.copy(
                playlistId = track.playlistId.toMutableList()
            )
        }
    }

}
