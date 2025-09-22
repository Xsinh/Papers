package com.implosion.papers.presentation.ui.screen.details.screen.read

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.implosion.papers.presentation.navigation.NavigationScreen
import com.implosion.papers.presentation.ui.screen.details.NoteDetailReadComponent
import com.implosion.papers.presentation.ui.screen.details.NoteDetailsViewModel
import com.implosion.papers.presentation.ui.theme.PapersTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteDetailsReadScreen(
    modifier: Modifier = Modifier,
    noteId: Int? = null,
    navController: NavController? = null
) {
    val viewModel: NoteDetailsViewModel = koinViewModel()
    val bottomSheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    noteId?.let { id ->
        viewModel.getNote(id)

        PapersTheme {
            ModalBottomSheet(
                sheetState = bottomSheetState,
                content = {
                    viewModel.note
                        .collectAsState().value?.content?.let { note ->
                            NoteDetailReadComponent(
                                modifier = modifier,
                                note = note,
                                onButtonClick = {
                                    //viewModel.magic() на будущее
                                    scope.launch(Dispatchers.Main) {
                                        bottomSheetState.hide()
                                        navController?.navigate("${NavigationScreen.DetailsEditNote.route}/${noteId}")
                                    }
                                })
                        }
                },
                onDismissRequest = {
                    navController?.navigate(NavigationScreen.Main.route)
                },
            )
        }
    }
}

@Preview
@Composable
private fun NoteDetailsReadScreenPreview() {
    NoteDetailsReadScreen()
}