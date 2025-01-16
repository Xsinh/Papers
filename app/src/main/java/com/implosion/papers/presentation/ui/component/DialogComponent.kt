package com.implosion.papers.presentation.ui.component

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.implosion.papers.R

@Composable
fun AlertDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { onDismiss() },
            title = { Text(text = stringResource(R.string.title_dialog_confirmation)) },
            text = { Text(text = stringResource(R.string.title_dialog_question_delete)) },
            confirmButton = {
                TextButton(onClick = { onConfirm() }) {
                    Text(text = stringResource(R.string.title_yes))
                }
            },
            dismissButton = {
                TextButton(onClick = { onDismiss() }) {
                    Text(text = stringResource(R.string.title_no))
                }
            }
        )
    }
}

@Preview
@Composable
fun AlertDialogPreview() {
    AlertDialog(showDialog = true, onConfirm = {}, onDismiss = {})
}