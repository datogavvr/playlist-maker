package com.practicum.playlist_maker.creator

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.practicum.playlist_maker.data.ITunesApiService
import com.practicum.playlist_maker.data.database.AppDatabase
import com.practicum.playlist_maker.data.network.PlaylistsRepositoryImpl
import com.practicum.playlist_maker.data.network.RetrofitNetworkClient
import com.practicum.playlist_maker.data.network.SearchHistoryRepositoryImpl
import com.practicum.playlist_maker.data.network.TracksRepositoryImpl
import com.practicum.playlist_maker.data.preferences.SearchHistoryPreferences
import com.practicum.playlist_maker.domain.PlaylistsRepository
import com.practicum.playlist_maker.domain.SearchHistoryRepository
import com.practicum.playlist_maker.domain.TracksRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Creator {

    private lateinit var database: AppDatabase
    private val Context.searchHistoryDataStore by preferencesDataStore(
        name = "search_history_prefs"
    )
    private var searchHistoryPreferences: SearchHistoryPreferences? = null

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val apiService: ITunesApiService by lazy {
        retrofit.create(ITunesApiService::class.java)
    }

    private val networkClient: RetrofitNetworkClient by lazy {
        RetrofitNetworkClient(apiService)
    }

    fun initDatabase(context: Context) {
        if (!::database.isInitialized) {
            database = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "playlist_maker_db"
            ).build()
        }

        if (searchHistoryPreferences == null) {
            searchHistoryPreferences = SearchHistoryPreferences(
                dataStore = context.searchHistoryDataStore
            )
        }
    }

    fun getTracksRepository(): TracksRepository {
        check(::database.isInitialized) { "Database is not initialized. Call initDatabase(context) first." }

        return TracksRepositoryImpl(
            networkClient = networkClient,
            database = database
        )
    }

    fun getPlaylistsRepository(): PlaylistsRepository {
        check(::database.isInitialized) { "Database is not initialized. Call initDatabase(context) first." }

        return PlaylistsRepositoryImpl(
            database = database
        )
    }

    fun getSearchHistoryRepository(): SearchHistoryRepository {
        check(searchHistoryPreferences != null) { "SearchHistoryPreferences is not initialized. Call initDatabase(context) first." }
        return SearchHistoryRepositoryImpl(
            preferences = searchHistoryPreferences!!
        )
    }
}
