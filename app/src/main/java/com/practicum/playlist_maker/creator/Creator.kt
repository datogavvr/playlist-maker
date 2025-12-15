package com.practicum.playlist_maker.creator

import com.practicum.playlist_maker.data.DatabaseMock
import com.practicum.playlist_maker.data.ITunesApiService
import com.practicum.playlist_maker.data.network.PlaylistsRepositoryImpl
import com.practicum.playlist_maker.data.network.RetrofitNetworkClient
import com.practicum.playlist_maker.data.network.TracksRepositoryImpl
import com.practicum.playlist_maker.domain.PlaylistsRepository
import com.practicum.playlist_maker.domain.TracksRepository
import kotlinx.coroutines.MainScope
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Creator {

    private val appScope = MainScope()
    private val database = DatabaseMock(appScope)

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://itunes.apple.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val apiService: ITunesApiService =
        retrofit.create(ITunesApiService::class.java)

    private val networkClient = RetrofitNetworkClient(apiService)

    fun getTracksRepository(): TracksRepository {
        return TracksRepositoryImpl(
            networkClient = networkClient,
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
