package com.implosion.papers.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

import androidx.navigation.compose.rememberNavController
import com.implosion.papers.presentation.ui.screen.details.NoteDetailsScreen
import com.implosion.papers.presentation.ui.screen.main.MainScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = NavigationScreen.Main.route) {
        composable(NavigationScreen.Main.route) { MainScreen(navController = navController) }
        composable(NavigationScreen.DetailsNote.route) { NoteDetailsScreen(navController = navController) }
    }
}