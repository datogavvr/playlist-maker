package com.practicum.playlist_maker

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.edit

@Stable
object ThemeManager {
    private const val PREF_NAME = "theme_preferences"
    private const val KEY_THEME_MODE = "theme_mode"

    // режимы темы
    const val THEME_SYSTEM = 0
    const val THEME_LIGHT = 1
    const val THEME_DARK = 2

    var currentTheme by mutableIntStateOf(THEME_SYSTEM)
        private set

    fun initialize(context: Context) {
        currentTheme = getSavedTheme(context)
        applyTheme(currentTheme)
    }

    fun getSavedTheme(context: Context): Int {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getInt(KEY_THEME_MODE, THEME_SYSTEM)
    }

    fun setTheme(context: Context, themeMode: Int) {
        currentTheme = themeMode
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit { putInt(KEY_THEME_MODE, themeMode) }
        applyTheme(themeMode)
    }

    private fun applyTheme(themeMode: Int) {
        when (themeMode) {
            THEME_SYSTEM -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            THEME_LIGHT -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            THEME_DARK -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
    }

    @SuppressLint("LocalContextConfigurationRead")
    @Composable
    fun isDarkTheme(): Boolean {
        val context = LocalContext.current
        return when (currentTheme) {
            THEME_SYSTEM -> {
                val configuration = context.resources.configuration
                (configuration.uiMode and android.content.res.Configuration.UI_MODE_NIGHT_MASK) ==
                        android.content.res.Configuration.UI_MODE_NIGHT_YES
            }
            THEME_LIGHT -> false
            THEME_DARK -> true
            else -> false
        }
    }
}