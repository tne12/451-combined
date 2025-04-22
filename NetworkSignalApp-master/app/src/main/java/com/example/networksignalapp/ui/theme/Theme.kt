package com.example.networksignalapp.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

@Composable
fun NetworkSignalAppTheme(
    darkTheme: Boolean, // ← this is now dynamic
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        darkColorScheme(
            primary = Blue,
            secondary = DarkGray,
            tertiary = LightBlue,
            background = Black,
            surface = DarkGray,
            onPrimary = White,
            onSecondary = White,
            onTertiary = White,
            onBackground = White,
            onSurface = White
        )
    } else {
        lightColorScheme(
            primary = Blue,
            secondary = LBlue,
            tertiary = Gray, // Light widget background
            background = White,
            surface = Gray, // Widget/card color
            onPrimary = Black,
            onSecondary = Black,
            onTertiary = Black,
            onBackground = Black,
            onSurface = Black
        )
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view)
                .isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}