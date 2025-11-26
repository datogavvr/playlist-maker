package com.practicum.playlist_maker.ui.theme


import android.app.Application
import com.practicum.playlist_maker.ui.theme.ThemeManager

class PlaylistMakerApp : Application() {

    override fun onCreate() {
        super.onCreate()
        // инициализируем менеджер тем при запуске приложения
        ThemeManager.initialize(this)
    }
}