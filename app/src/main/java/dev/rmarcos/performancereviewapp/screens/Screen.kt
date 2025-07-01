package dev.rmarcos.performancereviewapp.screens

sealed class MainScreen(val route: String) {
    data object Profile : MainScreen("profile")
    data object Goals: MainScreen("goals")
    data object Users: MainScreen("users")
}