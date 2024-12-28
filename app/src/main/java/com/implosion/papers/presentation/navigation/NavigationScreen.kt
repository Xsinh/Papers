package com.implosion.papers.presentation.navigation

sealed class NavigationScreen(val route: String) {

    data object Main : NavigationScreen("main_screen")

    data object DetailsNote : NavigationScreen("note_details_screen")

    data object DetailsReadNote : NavigationScreen("note_details_read_screen")
}