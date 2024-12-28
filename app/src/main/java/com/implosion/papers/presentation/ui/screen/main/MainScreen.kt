package com.implosion.papers.presentation.ui.screen.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.implosion.papers.R
import com.implosion.papers.presentation.navigation.NavigationScreen
import com.implosion.papers.presentation.ui.theme.PapersTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainScreen(modifier: Modifier = Modifier, navController: NavController? = null) {
    val viewModel: MainViewModel = koinViewModel()

    LaunchedEffect(Unit) {
        viewModel.loadNotes()
    }

    PapersTheme {
        Scaffold(floatingActionButton = {
            FloatingActionButton(onClick = {
                navController?.navigate(NavigationScreen.DetailsWriteNote.route)
            }) {
                Icon(
                    Icons.Filled.Create,
                    contentDescription = stringResource(R.string.description_add)
                )
            }
        }, content = { paddingValues ->
            LazyListContent(
                modifier = modifier
                    .padding(paddingValues)
                    .fillMaxSize(),
                paddingValues = paddingValues,
                noteList = viewModel.noteList.collectAsState().value,
                onNoteClick = { noteId ->
                    navController?.navigate("${NavigationScreen.DetailsReadNote.route}/${noteId}")
                }
            )
        })
    }
}

@Preview
@Composable
private fun MainScreenPreview() {
    MainScreen()
}