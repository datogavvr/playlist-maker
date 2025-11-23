package com.practicum.playlist_maker

import com.practicum.playlist_maker.creator.Storage
import com.practicum.playlist_maker.data.network.RetrofitNetworkClient
import com.practicum.playlist_maker.data.network.TracksRepositoryImpl
import com.practicum.playlist_maker.domain.TracksRepository

object Creator {
    fun getTracksRepository(): TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient(Storage()))
    }
}