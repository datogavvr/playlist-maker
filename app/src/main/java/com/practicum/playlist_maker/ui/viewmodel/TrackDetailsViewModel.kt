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

    // получение трека по id
    fun getTrack(id: Long): Flow<Track?> =
        tracksRepository.getTrackById(id)

    // получение списка всех плейлистов
    fun getPlaylists(): Flow<List<Playlist>> =
        playlistsRepository.getAllPlaylists()

    // обновление избранного
    fun updateTrackFavoriteStatus(trackId: Long, favorite: Boolean) {
        viewModelScope.launch {
            tracksRepository.updateTrackFavoriteStatus(trackId, favorite)
        }
    }

    // добавление трека в плейлист
    fun addTrackToPlaylist(track: Track?, playlistId: Long) {
        if (track == null) return
        viewModelScope.launch {
            tracksRepository.addTrackToPlaylist(track.id, playlistId)
        }
    }

    // удаление трека из плейлиста
    fun deleteTrackFromPlaylist(track: Track?, playlistId: Long) {
        if (track == null) return
        viewModelScope.launch {
            tracksRepository.deleteTrackFromPlaylist(track.id, playlistId)
        }
    }
}
