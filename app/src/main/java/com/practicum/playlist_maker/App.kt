package com.practicum.playlist_maker

import android.app.Application
import com.practicum.playlist_maker.creator.Creator
import com.practicum.playlist_maker.data.di.appModule
import com.practicum.playlist_maker.ui.theme.ThemeManager
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        // инициализируем базу данных
        Creator.initDatabase(this)

        // инициализируем менеджер тем при запуске приложения
        ThemeManager.initialize(this)

        // настройка koin
        startKoin {
            androidContext(this@App)
            modules(appModule)
        }
    }
}
