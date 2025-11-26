package com.practicum.playlist_maker.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.practicum.playlist_maker.ui.theme.ThemeManager

private val DarkColorScheme = darkColorScheme(
    primary = MyLightBlue,
    onPrimary = Color.White,
    primaryContainer = MyBlack,
    onPrimaryContainer = Color.White,

    secondary = MyGray,
    onSecondary = Color.White,
    secondaryContainer = MyGray,
    onSecondaryContainer = Color.White,

    tertiary = MyGray,
    onTertiary = Color.White,
    tertiaryContainer = MyGray,
    onTertiaryContainer = Color.White,

    background = MyBlack,
    onBackground = Color.White,

    surface = MyBlack,
    onSurface = Color.White,
    surfaceVariant = MyGray,
    onSurfaceVariant = MyLightGray,

    outline = MyGray,
    outlineVariant = MyLightGray,

    error = Color(0xFFCF6679),
    onError = Color.Black,
    errorContainer = Color(0xFFB00020),
    onErrorContainer = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = MyLightBlue,
    onPrimary = Color.White,
    primaryContainer = MyLightBlue,
    onPrimaryContainer = Color.White,

    secondary = MyBlue,
    onSecondary = Color.White,
    secondaryContainer = MyBlue,
    onSecondaryContainer = Color.White,

    tertiary = MyDarkBlue,
    onTertiary = Color.White,
    tertiaryContainer = MyDarkBlue,
    onTertiaryContainer = Color.White,

    background = Color.White,
    onBackground = Color.Black,

    surface = Color.White,
    onSurface = Color.Black,
    surfaceVariant = Color(0xFFF5F5F5),
    onSurfaceVariant = Color(0xFF757575),

    outline = Color(0xFFE0E0E0),
    outlineVariant = Color(0xFFEEEEEE),

    error = Color(0xFFB00020),
    onError = Color.White,
    errorContainer = Color(0xFFFCD8DF),
    onErrorContainer = Color(0xFF690005)
)

@Composable
fun PlaylistmakerTheme(
    darkTheme: Boolean = ThemeManager.isDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false, // Отключим dynamic color для контроля цветов
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        val window = (view.context as Activity).window
        WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}