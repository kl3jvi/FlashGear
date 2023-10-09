package com.kl3jvi.yonda.ui.screens

sealed class Screen(val route: String) {
    data object Home : Screen("/home")
    data object Dashboard : Screen("/dashboard")
}
