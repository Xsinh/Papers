package com.implosion.papers.presentation.ui.screen.details

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.implosion.papers.presentation.navigation.NavigationScreen
import com.implosion.papers.presentation.ui.theme.PapersTheme
import org.koin.androidx.compose.koinViewModel


@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun NoteDetailsScreen(modifier: Modifier = Modifier, navController: NavController? = null) {
    val viewModel: NoteDetailsViewModel = koinViewModel()

    val bottomSheetState = rememberModalBottomSheetState(
        confirmValueChange = {
            it != SheetValue.Hidden
        }
    )
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    PapersTheme {
        ModalBottomSheet(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            sheetState = bottomSheetState,
            content = {
                NoteDetailCreateComponent(
                    modifier = modifier,
                    focusRequester = focusRequester,
                    onButtonClick = { note ->
                        viewModel
                            .createNote(
                                content = note
                            ).also {
                                navController?.navigate(NavigationScreen.Main.route)
                            }
                    })
            },
            onDismissRequest = {
                navController?.navigate(NavigationScreen.Main.route)
            },
        )
    }
}

@Preview
@Composable
fun NoteDetailsScreenPreview() {
    NoteDetailsScreen()
}