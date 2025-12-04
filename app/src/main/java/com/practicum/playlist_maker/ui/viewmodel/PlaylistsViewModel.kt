package com.practicum.playlist_maker.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlist_maker.creator.Creator
import com.practicum.playlist_maker.data.network.Playlist
import com.practicum.playlist_maker.data.network.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class PlaylistsViewModel() : ViewModel() {
    private val playlistsRepository = Creator.getPlaylistsRepository()
    private val tracksRepository = Creator.getTracksRepository()

    private val _playlists = MutableStateFlow<List<Playlist>>(emptyList())
    val playlists: StateFlow<List<Playlist>> = _playlists.asStateFlow()

    val favoriteList: Flow<List<Track>> =
        tracksRepository.getFavoriteTracks()

    init {
        loadPlaylists()
    }

    private fun loadPlaylists() {
        viewModelScope.launch {
            playlistsRepository.getAllPlaylists().collectLatest { list ->
                _playlists.value = list
            }
        }
    }

    fun createNewPlayList(namePlaylist: String, description: String) {
        viewModelScope.launch(Dispatchers.IO) {
            playlistsRepository.addNewPlaylist(namePlaylist, description)
        }
    }

    suspend fun addTrackToPlaylist(trackId: Long, playlistId: Long) {
        tracksRepository.addTrackToPlaylist(trackId, playlistId)
    }

    suspend fun toggleFavorite(trackId: Long, isFavorite: Boolean) {
        tracksRepository.updateTrackFavoriteStatus(trackId, isFavorite)
    }

    suspend fun deleteTrackFromPlaylist(trackId: Long, playlistId: Long) {
        tracksRepository.deleteTrackFromPlaylist(trackId, playlistId)
    }

    suspend fun deletePlaylistById(id: Long) {
        tracksRepository.deleteTracksByPlaylistId(id)
        playlistsRepository.deletePlaylistById(id)
    }

    suspend fun isExist(track: Track): Track? {
        return tracksRepository.getTrackByNameAndArtist(track = track).firstOrNull()
    }
}