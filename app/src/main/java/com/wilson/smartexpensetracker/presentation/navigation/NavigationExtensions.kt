package com.wilson.smartexpensetracker.presentation.navigation

import androidx.navigation.NavController

fun NavController.safeNavigate(route: String) {
    if (currentDestination?.route != route) {
        navigate(route)
    }
}
