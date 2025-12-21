package com.practicum.playlist_maker.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlist_maker.creator.Creator
import com.practicum.playlist_maker.data.network.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class FavoritesViewModel : ViewModel() {

    private val tracksRepository = Creator.getTracksRepository()
    private val playlistsRepository = Creator.getPlaylistsRepository()

    val favoritePlaylistId: Long

    init {
        favoritePlaylistId = runBlocking {
            playlistsRepository.getFavoritePlaylistOnce()!!.id
        }
    }

    // список всех избранных треков
    val favoriteList: Flow<List<Track>> =
        tracksRepository.getFavoriteTracks()

    // убрать / добавить в избранное
    fun toggleFavorite(trackId: Long, isFavorite: Boolean) {
        viewModelScope.launch {
            tracksRepository.updateTrackFavoriteStatus(
                trackId = trackId,
                isFavorite = isFavorite
            )
        }
    }
}
