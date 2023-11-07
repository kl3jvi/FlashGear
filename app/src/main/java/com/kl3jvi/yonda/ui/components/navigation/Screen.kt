package com.kl3jvi.yonda.ui.components.navigation

sealed class Screen(val route: String) {
    data object Home : Screen("/home")

    data object Permission : Screen("/permission")

    data object Device : Screen("/device/{deviceId}") {
        fun createRoute(deviceId: String): String = "/device/$deviceId"
    }
}
