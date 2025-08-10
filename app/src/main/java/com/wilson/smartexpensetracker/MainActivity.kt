package com.wilson.smartexpensetracker.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.wilson.smartexpensetracker.presentation.navigation.NavGraph
import com.wilson.smartexpensetracker.presentation.theme.LocalThemeState
import com.wilson.smartexpensetracker.presentation.theme.SmartExpenseTrackerTheme
import com.wilson.smartexpensetracker.presentation.theme.ThemeState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {
            var isDarkTheme by remember { mutableStateOf(false) }

            CompositionLocalProvider(
                LocalThemeState provides ThemeState(
                    isDarkTheme = isDarkTheme,
                    toggleTheme = { isDarkTheme = !isDarkTheme }
                )
            ) {
                SmartExpenseTrackerTheme(darkTheme = isDarkTheme) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        val navController = rememberNavController()
                        NavGraph(navController = navController)
                    }
                }
            }

        }
    }
}
