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

    private val _trackState = MutableStateFlow<Track?>(null)
    val trackState: StateFlow<Track?> = _trackState.asStateFlow()

    val playlists: StateFlow<List<Playlist>> =
        playlistsRepository.getAllPlaylists()
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                emptyList()
            )

    fun loadTrack(trackId: Long) {
        viewModelScope.launch {
            combine(
                tracksRepository.getTrackById(trackId),
                playlistsRepository.getFavoritePlaylist(),
                playlistsRepository.getAllPlaylists()
            ) { track, favoritePlaylist, allPlaylists ->
                if (track == null) return@combine null

                // проверка избранного
                val isFavorite =
                    favoritePlaylist?.tracks?.any { it.id == track.id } == true

                // все плейлисты, в которых есть трек
                val playlistIds = allPlaylists
                    .filter { playlist -> playlist.tracks.any { it.id == track.id } }
                    .map { it.id }

                track.copy(
                    favorite = isFavorite,
                    playlistId = playlistIds as MutableList<Long>
                )
            }.collect { updatedTrack ->
                _trackState.value = updatedTrack
            }
        }
    }

    fun toggleFavorite() {
        val current = _trackState.value ?: return

        _trackState.value = current.copy(favorite = !current.favorite)

        viewModelScope.launch {
            tracksRepository.updateTrackFavoriteStatus(
                trackId = current.id,
                isFavorite = !current.favorite
            )
        }
    }

    // добавление трека в плейлист
    fun addTrackToPlaylist(playlistId: Long) {
        val current = _trackState.value ?: return
        viewModelScope.launch {
            tracksRepository.addTrackToPlaylist(current.id, playlistId)
        }
    }

    // удаление трека из плейлиста
    fun deleteTrackFromPlaylist(playlistId: Long) {
        val current = _trackState.value ?: return
        viewModelScope.launch {
            tracksRepository.deleteTrackFromPlaylist(current.id, playlistId)
        }
    }
}
