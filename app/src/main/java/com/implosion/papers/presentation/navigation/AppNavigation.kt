package com.implosion.papers.presentation.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.implosion.papers.presentation.ui.screen.details.screen.edit.NoteDetailsEditScreen
import com.implosion.papers.presentation.ui.screen.details.screen.read.NoteDetailsReadScreen
import com.implosion.papers.presentation.ui.screen.details.screen.write.NoteDetailsWriteScreen
import com.implosion.papers.presentation.ui.screen.main.screen.main.MainScreen
import com.implosion.papers.presentation.ui.screen.voice.VoiceScreen
import java.net.URLDecoder

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = NavigationScreen.Main.route) {
        composable(NavigationScreen.Main.route) { MainScreen(navController = navController) }
        composable(route = "${NavigationScreen.DetailsWriteNote.route}?text={text}", arguments = listOf(
            navArgument("text") {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            }
        )) {backStackEntry ->
            //val text = backStackEntry.arguments?.getString("text").orEmpty()
            val text = backStackEntry.arguments?.getString("text")?.let {
                URLDecoder.decode(it, "UTF-8")
            }.orEmpty()
            Log.d("###", text)
            NoteDetailsWriteScreen(navController = navController, previewText = text)
        }
        composable(NavigationScreen.VoiceScreen.route) { VoiceScreen(navController = navController) }
        composable("${NavigationScreen.DetailsReadNote.route}/{noteId}") { backStackEntry ->
            val noteId = backStackEntry.arguments?.getString("noteId")?.toInt()

            NoteDetailsReadScreen(
                navController = navController,
                noteId = noteId,
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