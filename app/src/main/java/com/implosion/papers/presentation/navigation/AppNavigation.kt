package com.implosion.papers.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.implosion.papers.presentation.ui.screen.details.screen.edit.NoteDetailsEditScreen
import com.implosion.papers.presentation.ui.screen.details.screen.read.NoteDetailsReadScreen
import com.implosion.papers.presentation.ui.screen.details.screen.write.NoteDetailsWriteScreen
import com.implosion.papers.presentation.ui.screen.main.screen.main.MainScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = NavigationScreen.Main.route) {
        composable(NavigationScreen.Main.route) { MainScreen(navController = navController) }
        composable(NavigationScreen.DetailsWriteNote.route) { NoteDetailsWriteScreen(navController = navController) }
        composable("${NavigationScreen.DetailsReadNote.route}/{noteId}") { backStackEntry ->
            val noteId = backStackEntry.arguments?.getString("noteId")?.toInt()

            NoteDetailsReadScreen(
                navController = navController,
                noteId = noteId
            )
        }
        composable("${NavigationScreen.DetailsEditNote.route}/{noteId}") { backStackEntry ->
            val noteId = backStackEntry.arguments?.getString("noteId")?.toInt()

            NoteDetailsEditScreen(
                navController = navController,
                noteId = noteId
            )
        }
    }
}