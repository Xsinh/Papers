package com.implosion.papers.presentation.ui.screen.details.edit

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.navigation.NavController
import com.implosion.papers.presentation.navigation.NavigationScreen
import com.implosion.papers.presentation.ui.screen.details.NoteDetailCreateComponent
import com.implosion.papers.presentation.ui.screen.details.NoteDetailsViewModel
import com.implosion.papers.presentation.ui.theme.PapersTheme
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteDetailsEditScreen(
    noteId: Int? = null,
    modifier: Modifier = Modifier,
    navController: NavController? = null
) {
    val viewModel: NoteDetailsViewModel = koinViewModel()
    val bottomSheetState = rememberModalBottomSheetState()
    val focusRequester = remember { FocusRequester() }

    noteId?.let { id ->
        viewModel.getNote(id)

        PapersTheme {
            ModalBottomSheet(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                sheetState = bottomSheetState,
                content = {
                    viewModel.note
                        .collectAsState().value?.content?.let { note ->
                            NoteDetailCreateComponent(
                                modifier = modifier,
                                noteText = note,
                                focusRequester = focusRequester,
                                onButtonClick = { note ->
                                    viewModel
                                        .editNote(
                                            content = note
                                        ).also {
                                            navController?.navigate(NavigationScreen.Main.route)
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