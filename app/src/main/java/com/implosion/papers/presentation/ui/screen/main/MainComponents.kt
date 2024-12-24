package com.implosion.papers.presentation.ui.screen.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.implosion.papers.R
import com.implosion.papers.domain.model.NoteModel
import com.implosion.papers.presentation.ui.theme.Typography
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.delay

@Composable
fun LazyListContent(
    modifier: Modifier,
    paddingValues: PaddingValues,
    noteList: List<NoteModel>
) {
    val itemsList = noteList

    if (noteList.isEmpty()) {
        EmptyNoteScreen()
    } else {
        LazyColumn(
            contentPadding = paddingValues,
            modifier = modifier
        ) {

            items(itemsList) { item ->
                Text(
                    text = item.content, maxLines = 3, modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }
        }
    }
}

@Composable
fun EmptyNoteScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        var ellipsis by remember { mutableStateOf("") }

        LaunchedEffect(Unit) {
            while (true) {
                ellipsis = ""
                delay(800)
                ellipsis = "."
                delay(900)
                ellipsis = ".."
                delay(900)
                ellipsis = "..."
                delay(1000)
            }
        }

        Text(
            modifier = Modifier,
            text = stringResource(R.string.title_empty_notes) + ellipsis,
            style = Typography.bodyLarge,
            textAlign = TextAlign.Center
        )
    }
}

@Preview
@Composable
private fun LazyListContentPreview() {
    LazyListContent(
        modifier = Modifier,
        paddingValues = PaddingValues(),
        noteList = persistentListOf()
    )
}

@Preview
@Composable
private fun EmptyNoteScreenPreview() {
    EmptyNoteScreen()
}