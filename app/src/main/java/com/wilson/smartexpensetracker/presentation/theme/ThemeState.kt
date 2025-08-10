package com.wilson.smartexpensetracker.presentation.theme

import androidx.compose.runtime.staticCompositionLocalOf

data class ThemeState(
    val isDarkTheme: Boolean,
    val toggleTheme: () -> Unit
)

val LocalThemeState = staticCompositionLocalOf<ThemeState> {
    error("No ThemeState provided")
}