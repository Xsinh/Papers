package com.implosion.papers.presentation.ui.screen.details

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.implosion.papers.R
import com.implosion.papers.presentation.ui.theme.Typography

@Composable
fun NoteDetailCreateComponent(
    modifier: Modifier = Modifier,
    onButtonClick: (String) -> Unit,
    focusRequester: FocusRequester,
    noteText: String = "",
) {
    var noteText by remember { mutableStateOf(noteText) }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Column(
        modifier = modifier
            .focusRequester(focusRequester),
    ) {
        TextField(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth(),
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
            ),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                focusedTextColor = MaterialTheme.colorScheme.inverseSurface
            )
        )

        val borderWidth = 1.dp
            val borderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
        OutlinedButton(
            modifier = Modifier
                .align(Alignment.End)
                .padding(4.dp),

            shape = RoundedCornerShape(64),
            border = BorderStroke(borderWidth, borderColor),
            onClick = {
                onButtonClick.invoke(noteText)
            }) {
            Text(stringResource(R.string.title_write_down))
        }
    }
}

@Composable
fun NoteDetailReadComponent(
    modifier: Modifier = Modifier,
    note: String,
    onButtonClick: () -> Unit,
) {
    Column(
        modifier = modifier
    ) {
        SelectionContainer {
            Text(
                modifier = Modifier
                    .padding(16.dp)
                    .wrapContentHeight()
                    .fillMaxWidth(),
                text = note,
                style = Typography.bodyLarge,
            )
        }

        val borderWidth = 1.dp
        val borderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)

        OutlinedButton(
            modifier = Modifier
                .align(Alignment.End)
                .padding(4.dp),
            shape = RoundedCornerShape(64),
            border = BorderStroke(borderWidth, borderColor),
            onClick = {
                onButtonClick.invoke()
            }) {
            Text(stringResource(R.string.title_edit))
        }
    }
}

@SuppressLint("RememberInComposition")
@Preview
@Composable
private fun NoteDetailComponentPreview() {
    NoteDetailCreateComponent(
        focusRequester = FocusRequester(),
        onButtonClick = {

        }
    )
}

@Preview
@Composable
private fun NoteDetailReadComponentPreview() {
    NoteDetailReadComponent(
        note = "Text, text, text",
        onButtonClick = {

        }
    )
}