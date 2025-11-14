package com.practicum.playlist_maker


import android.app.Application

class PlaylistMakerApp : Application() {

    override fun onCreate() {
        super.onCreate()
        // инициализируем менеджер тем при запуске приложения
        ThemeManager.initialize(this)
    }
}