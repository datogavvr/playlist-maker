package com.practicum.playlist_maker.creator

import com.practicum.playlist_maker.data.DatabaseMock
import com.practicum.playlist_maker.data.network.PlaylistsRepositoryImpl
import com.practicum.playlist_maker.data.network.TracksRepositoryImpl
import com.practicum.playlist_maker.domain.PlaylistsRepository
import com.practicum.playlist_maker.domain.TracksRepository
import kotlinx.coroutines.MainScope

object Creator {
    private val appScope = MainScope()
    private val database = DatabaseMock(appScope)

    fun getTracksRepository(): TracksRepository {
        return TracksRepositoryImpl(
            scope = appScope,
            database = database
        )
    }

    fun getPlaylistsRepository(): PlaylistsRepository {
        return PlaylistsRepositoryImpl(
            scope = appScope,
            database = database
        )
    }
}