package com.implosion.papers.presentation.ui.screen.details

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.implosion.papers.presentation.navigation.NavigationScreen
import com.implosion.papers.presentation.ui.theme.PapersTheme
import org.koin.androidx.compose.koinViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteDetailsReadScreen(
    noteId: Int? = null,
    modifier: Modifier = Modifier,
    navController: NavController? = null
) {
    val viewModel: NoteDetailsViewModel = koinViewModel()
    val bottomSheetState = rememberModalBottomSheetState()

    noteId?.let { id ->
        viewModel.getNote(id)

        PapersTheme {
            ModalBottomSheet(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                sheetState = bottomSheetState,
                content = {
                    viewModel.note
                        .collectAsState().value?.content?.let { note ->
                        NoteDetailReadComponent(
                            modifier = modifier,
                            note = note,
                            onButtonClick = {
                                //отправить на редактирование
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