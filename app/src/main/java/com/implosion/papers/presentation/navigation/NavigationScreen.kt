package com.implosion.papers.presentation.navigation

sealed class NavigationScreen(val route: String) {

    data object Main : NavigationScreen("main_screen")

    data object DetailsWriteNote : NavigationScreen("note_details_screen")

    data object DetailsReadNote : NavigationScreen("note_details_read_screen")

    data object DetailsEditNote : NavigationScreen("note_details_edit_screen")
}