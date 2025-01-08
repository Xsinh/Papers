package com.implosion.papers.presentation.ui.screen.main.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
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
    val focusManager = LocalFocusManager.current

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
            Column(
                modifier = modifier
                    .clickable {
                        focusManager.clearFocus()
                    }
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                CustomSearchView(
                    onValueChange = { query ->
                        searchQuery = query
                        viewModel.searchNotes(query)
                    },
                    search = searchQuery,
                    focusManager = focusManager
                )
                LazyListContent(modifier = modifier
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
                        }
                    })
            }
        })
    }
}

@Composable
fun CustomSearchView(
    search: String,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit,
    focusManager: FocusManager,
) {
    val borderWidth = 1.dp
    val borderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)

    Box(
        modifier = modifier
            .padding(top = 10.dp)
            .padding(4.dp)
            .clip(RoundedCornerShape(28))
            .border(
                border = BorderStroke(borderWidth, borderColor),
                shape = RoundedCornerShape(28)
            ),
    ) {
        TextField(
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text(stringResource(R.string.description_search))
            },
            value = search,
            onValueChange = onValueChange,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                focusedTextColor = MaterialTheme.colorScheme.inverseSurface
            ),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = stringResource(R.string.description_search)
                )
            },
            trailingIcon = {

                if (search.isNotEmpty()) {

                    Icon(
                        modifier = Modifier
                            .clip(RoundedCornerShape(25))
                            .alpha(0.25f)
                            .clickable {
                                onValueChange("")
                                focusManager.clearFocus()
                            }
                            .padding(8.dp),
                        imageVector = Icons.Default.Clear, // Иконка очистки (можно заменить)
                        contentDescription = stringResource(R.string.description_clear)
                    )
                }
            }
        )
    }
}

@Preview
@Composable
private fun MainScreenPreview() {
    MainScreen()
}