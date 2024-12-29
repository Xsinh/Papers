package com.implosion.papers.presentation.ui.screen.main.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.implosion.domain.model.NoteModel
import com.implosion.papers.R
import com.implosion.papers.presentation.ui.theme.Typography
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.delay

@Composable
fun LazyListContent(
    modifier: Modifier,
    paddingValues: PaddingValues,
    noteList: List<NoteModel>,
    onClickListener: OnNoteClickListener,
) {
    val itemsList = noteList

    if (noteList.isEmpty()) {
        EmptyNoteScreen()
    } else {
        LazyColumn(
            contentPadding = paddingValues,
            modifier = modifier,
        ) {
            items(items = itemsList, key = { item -> item.noteId ?: 0 }) { item ->
                MainNoteItem(item, onClickListener)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainNoteItem(
    item: NoteModel,
    onClickListener: OnNoteClickListener,
) {
    val borderWidth = 1.dp
    val borderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clip(RoundedCornerShape(25))
            .border(
                border = BorderStroke(borderWidth, borderColor),
                shape = RoundedCornerShape(25)
            )
            .clickable {
                item.noteId?.let { id ->
                    onClickListener.onNoteClick(id)
                }
            }
            .padding(16.dp),
        horizontalArrangement = Arrangement.Absolute.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            if (item.title.isNullOrEmpty()) {
                Text(
                    text = item.content,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            } else {
                Text(
                    text = item.title.orEmpty(),
                    maxLines = 3,
                    style = Typography.labelSmall,
                    color = MaterialTheme.colorScheme.tertiary
                )
                Text(
                    text = item.content,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }

        }
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.End,
        ) {
            Icon(
                modifier = Modifier
                    .clip(RoundedCornerShape(25))
                    .alpha(0.25f)
                    .clickable {
                        item.noteId?.let { id ->
                            onClickListener.onNoteDelete(id)
                        }
                    }
                    .padding(8.dp),
                imageVector = Icons.Filled.Delete,
                contentDescription = stringResource(R.string.description_delete)
            )
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
        noteList = persistentListOf(),
        onClickListener = object : OnNoteClickListener {
            override fun onNoteClick(id: Int) {
                TODO("Not yet implemented")
            }

            override fun onNoteDelete(id: Int) {
                TODO("Not yet implemented")
            }
        }
    )
}

@Preview
@Composable
private fun EmptyNoteScreenPreview() {
    EmptyNoteScreen()
}

@Preview
@Composable
private fun MainNoteItemPreview() {
    MainNoteItem(
        item = NoteModel(
            noteId = 0,
//            title = "Title",
            content = "Content",
            createdAt = 0L,
            updatedAt = 0L
        ),
        onClickListener = object : OnNoteClickListener {
            override fun onNoteClick(id: Int) {
                TODO("Not yet implemented")
            }

            override fun onNoteDelete(id: Int) {
                TODO("Not yet implemented")
            }
        }
    )
}
