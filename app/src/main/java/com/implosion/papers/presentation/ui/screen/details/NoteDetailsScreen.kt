package com.implosion.papers.presentation.ui.screen.details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.implosion.papers.R
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


    var noteText by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    PapersTheme {
        ModalBottomSheet(
            sheetState = bottomSheetState,
            content = {
                Column(
                    modifier = modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester),
                ) {
                    TextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = noteText,
                        onValueChange = { newText ->
                            noteText = newText
                        },
                        label = {
                            Text(stringResource(R.string.label_write_down_a_note))
                        },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            capitalization = KeyboardCapitalization.Sentences,
                            autoCorrectEnabled = true
                        )
                    )

                    Button(
                        modifier = Modifier.align(Alignment.End),
                        onClick = {
                            viewModel
                                .createNote(
                                    content = noteText
                                ).also {
                                    navController?.navigate(NavigationScreen.Main.route)
                                }
                        }) {
                        Text(stringResource(R.string.title_write_down))
                    }
                }
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