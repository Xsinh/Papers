package com.implosion.papers.presentation.ui.screen.main.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.implosion.papers.R
import com.implosion.papers.presentation.navigation.NavigationScreen
import com.implosion.papers.presentation.ui.screen.main.MainViewModel
import com.implosion.papers.presentation.ui.screen.main.screen.listener.OnHashTagListener
import com.implosion.papers.presentation.ui.screen.main.screen.listener.OnNoteClickListener
import com.implosion.papers.presentation.ui.theme.PapersTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainScreen(modifier: Modifier = Modifier, navController: NavController? = null) {
    val viewModel: MainViewModel = koinViewModel()
    val notes = viewModel.noteList.collectAsState().value

    var searchQuery by remember { mutableStateOf("") }
    var searchTag by remember { mutableStateOf("") }

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
            Column(modifier = modifier.fillMaxSize().padding(paddingValues)) {
                // Строка поиска
                Row(
                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextField(
                        value = searchQuery,
                        onValueChange = { query ->
                            searchQuery = query
                            viewModel.searchNotes(query)
                        },
                        modifier = Modifier.weight(1f),
                        label = { Text("Поиск заметок") },
                        singleLine = true
                    )
                }
                LazyListContent(
                    modifier = modifier
                        .padding(paddingValues)
                        .fillMaxSize(),
                    paddingValues = paddingValues,
                    noteList = notes,
                    onClickListener = object : OnNoteClickListener {
                        override fun onNoteClick(id: Int) {
                            navController?.navigate("${NavigationScreen.DetailsReadNote.route}/${id}")
                        }

                        override fun onNoteDelete(id: Int) {
                            viewModel.deleteNote(id)
                        }
                    },
                    onHashTagListener = object : OnHashTagListener {

                        override fun onHashTagWritten(noteId: Int, tagName: String) {
                            viewModel.setHashTag(noteId = noteId, hashTag = tagName)
                            searchTag = tagName
                        }
                    }
                )
            }
        })
    }
}

@Preview
@Composable
private fun MainScreenPreview() {
    MainScreen()
}