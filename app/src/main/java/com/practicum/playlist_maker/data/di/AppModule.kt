package com.practicum.playlist_maker.data.di

import android.content.Context
import androidx.room.Room
import com.practicum.playlist_maker.data.database.AppDatabase
import com.practicum.playlist_maker.data.network.PlaylistsRepositoryImpl
import com.practicum.playlist_maker.data.network.TracksRepositoryImpl
import com.practicum.playlist_maker.domain.PlaylistsRepository
import com.practicum.playlist_maker.domain.TracksRepository
import com.practicum.playlist_maker.ui.viewmodel.PlaylistViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single<CoroutineScope> {
        CoroutineScope(Dispatchers.IO)
    }

    single<PlaylistsRepository> {
        PlaylistsRepositoryImpl(
            database = get()
        )
    }

    single<TracksRepository> {
        TracksRepositoryImpl(
            networkClient = get(),
            database = get()
        )
    }

    viewModel { (playlistId: Long) ->
        PlaylistViewModel(
            playlistId = playlistId,
            playlistsRepository = get(),
            tracksRepository = get()
        )
    }
}

val databaseModule = module {
    single {
        Room.databaseBuilder(
                get<Context>(),
                AppDatabase::class.java,
                "playlists_maker"
            ).build()
    }
}
