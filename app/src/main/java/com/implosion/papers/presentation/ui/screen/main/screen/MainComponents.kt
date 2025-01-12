package com.implosion.papers.presentation.ui.screen.main.screen

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.implosion.domain.model.NoteModel
import com.implosion.domain.model.TagModel
import com.implosion.papers.R
import com.implosion.papers.presentation.ui.component.HashtagPopupComponent
import com.implosion.papers.presentation.ui.screen.main.screen.listener.OnHashTagListener
import com.implosion.papers.presentation.ui.screen.main.screen.listener.OnHashTagMenuListener
import com.implosion.papers.presentation.ui.screen.main.screen.listener.OnNoteClickListener
import com.implosion.papers.presentation.ui.theme.Typography
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.delay
import kotlin.String

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LazyListContent(
    modifier: Modifier,
    paddingValues: PaddingValues,

    noteList: List<NoteModel>,

    onClickListener: OnNoteClickListener,
    onHashTagListener: OnHashTagListener,

    onPopupShow: (Boolean) -> Unit,
    onHashTagMenuListener: OnHashTagMenuListener
) {
    val itemsList = noteList
    val focusManager = LocalFocusManager.current
    val focusRequester: FocusRequester = remember { FocusRequester() }

    if (noteList.isEmpty()) {
        EmptyNoteScreen()
    } else {
        NotesMainScreen(
            modifier = modifier,
            focusManager = focusManager,
            paddingValues = paddingValues,
            itemsList = itemsList,
            onClickListener = onClickListener,
            onHashTagListener = onHashTagListener,
            focusRequester = focusRequester,
            onPopupShow = onPopupShow,
            onHashTagMenuListener = onHashTagMenuListener
        )
    }
}

@Composable
private fun NotesMainScreen(
    modifier: Modifier,
    focusManager: FocusManager,
    paddingValues: PaddingValues,
    itemsList: List<NoteModel>,
    onClickListener: OnNoteClickListener,
    onHashTagListener: OnHashTagListener,
    focusRequester: FocusRequester,

    onPopupShow: (Boolean) -> Unit,
    onHashTagMenuListener: OnHashTagMenuListener
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(4)),
        horizontalAlignment = Alignment.End,
    ) {
        LazyColumn(
            contentPadding = paddingValues,
        ) {
            items(
                items = itemsList,
                key = { item -> item.noteId ?: 0 }
            ) { item ->

                MainNoteItem(
                    modifier = Modifier.animateItem(),
                    item = item,
                    onClickListener = onClickListener,
                    onHashTagListener = onHashTagListener,
                    focusRequester = focusRequester,
                    focusManager = focusManager,
                    onPopupShow = onPopupShow,
                    onHashTagMenuListener = onHashTagMenuListener
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainNoteItem(
    item: NoteModel,

    onClickListener: OnNoteClickListener,
    onHashTagListener: OnHashTagListener,

    focusRequester: FocusRequester,
    focusManager: FocusManager = LocalFocusManager.current,

    modifier: Modifier = Modifier,
    onPopupShow: (Boolean) -> Unit,
    onHashTagMenuListener: OnHashTagMenuListener
) {
    val borderWidth = 1.dp
    val borderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)

    Column(
        modifier = modifier
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
            .padding(start = 16.dp, end = 16.dp, top = 16.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Absolute.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .weight(1f)
            ) {
                if (item.title.isNullOrEmpty()) {
                    Text(
                        text = item.content,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                        style = Typography.bodyLarge,
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
                        overflow = TextOverflow.Ellipsis,
                        style = Typography.bodyLarge,
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

        item.noteId
            ?.let { id ->
                HashtagContainer(
                    focusRequester = focusRequester,
                    focusManager = focusManager,
                    onHashTagListener = onHashTagListener,
                    noteId = id,
                    item = item,
                    hashTagList = item.hashTagList.toImmutableList(),
                    onPopupShow = onPopupShow,
                    onHashTagMenuListener = onHashTagMenuListener
                )
            }
    }
}

@Composable
fun HashtagContainer(
    item: NoteModel,
    focusRequester: FocusRequester,
    focusManager: FocusManager,
    noteId: Int,

    onHashTagListener: OnHashTagListener,
    hashTagList: ImmutableList<TagModel>,

    onPopupShow: (Boolean) -> Unit,
    onHashTagMenuListener: OnHashTagMenuListener
) {
    var hashTagText by remember { mutableStateOf("") }

    TextField(
        modifier = Modifier
            .wrapContentSize()
            .focusRequester(focusRequester),
        label = {
            Row {

                Text(
                    modifier = Modifier.padding(top = 4.dp),
                    text = stringResource(R.string.description_click_for_hashtag),
                    color = MaterialTheme.colorScheme.inverseSurface
                        .copy(alpha = 0.4f)
                )

                Spacer(modifier = Modifier.padding(horizontal = 8.dp))

                LazyRow {
                    items(items = hashTagList, key = { item -> item.tagId }) { chipText ->

                        HashTagItem(
                            item = item,
                            tagModel = chipText,
                            onPopupShow = onPopupShow,
                            onHashTagMenuListener = onHashTagMenuListener
                        )
                    }
                }
            }
        },
        value = hashTagText,
        onValueChange = { newText ->
            hashTagText = newText
        },
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                if (hashTagText.isNotBlank()) {

                    onHashTagListener.onHashTagWritten(noteId = noteId, tagName = hashTagText)
                    hashTagText = ""
                    onHashTagListener.onHashTagWritten(noteId = noteId, tagName = hashTagText)
                }
                focusManager.clearFocus() // Remove focus after "Done" is pressed
            }
        ),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        ),
    )
}

@Composable
private fun LazyItemScope.HashTagItem(
    item: NoteModel,
    tagModel: TagModel,
    onPopupShow: (Boolean) -> Unit,
    onHashTagMenuListener: OnHashTagMenuListener
) {
    var showMenu by remember { mutableStateOf(false) }

    val pulseSizeAnimation = getPulseAnimation(showMenu)
    LaunchedEffect(showMenu) {
        onPopupShow(showMenu)
    }

    HashtagPopupComponent(
        noteId = item.noteId ?: 0,
        tagModel = tagModel,
        showMenu = showMenu,
        onHashTagMenuListener = object : OnHashTagMenuListener {

            override fun findNote(tagModel: TagModel) {
                onHashTagMenuListener.findNote(tagModel)
                showMenu = false
                onPopupShow(false)
            }

            override fun deleteHashTag(hashTagId: Int, noteId: Int) {
                onHashTagMenuListener.deleteHashTag(hashTagId = hashTagId, noteId = noteId)
                onHashTagMenuListener.dismissMenu()
                showMenu = false
                onPopupShow(false)
            }

            override fun dismissMenu() {
                onHashTagMenuListener.dismissMenu()
                showMenu = false
                onPopupShow(false)
            }
        },
    )
    Text(
        text = tagModel.name,
        color = MaterialTheme.colorScheme.tertiary.copy(
            alpha = if (showMenu) pulseSizeAnimation else 1f
        ),
        modifier = Modifier
            .animateItem()
            .clip(RoundedCornerShape(25))
            .clickable {
                showMenu = true
            }
            .padding(4.dp)
    )
}

@Composable
private fun getPulseAnimation(enabled: Boolean): Float {
    return if (enabled) {
        val infiniteTransition = rememberInfiniteTransition(label = "infiniteTransition")
        val pulseSizeAnimation by infiniteTransition.animateFloat(
            initialValue = 0.2f,
            targetValue = 2f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 900, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            ), label = "pulseSizeAnimation"
        )
        pulseSizeAnimation
    } else {
        1f
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
fun NotesMainScreenPreview() {
    NotesMainScreen(
        modifier = Modifier,
        focusRequester = FocusRequester(),
        focusManager = LocalFocusManager.current,
        paddingValues = PaddingValues(2.dp),
        onClickListener = object : OnNoteClickListener {
            override fun onNoteClick(id: Int) {
                TODO("Not yet implemented")
            }

            override fun onNoteDelete(id: Int) {
                TODO("Not yet implemented")
            }

        },
        onHashTagListener = object : OnHashTagListener {
            override fun onHashTagWritten(noteId: Int, tagName: String) {
                TODO("Not yet implemented")
            }

            override fun onHashtagClick(tagId: Int) {
                TODO("Not yet implemented")
            }
        },
        itemsList = listOf(
            NoteModel(
                noteId = 0,
                title = "Title",
                content = "Content",
                createdAt = 0L,
                updatedAt = 0L
            ), NoteModel(
                noteId = 0,
                title = "Title",
                content = "Content",
                createdAt = 0L,
                updatedAt = 0L
            )
        ),
        onPopupShow = {},
        onHashTagMenuListener = object : OnHashTagMenuListener {

            override fun findNote(tagModel: TagModel) {
                TODO("Not yet implemented")
            }

            override fun deleteHashTag(hashTagId: Int, noteId: Int) {
                TODO("Not yet implemented")
            }

            override fun dismissMenu() {
                TODO("Not yet implemented")
            }
        }
    )
}

@Preview
@Composable
private fun MainNoteItemPreview() {
    MainNoteItem(
        item = NoteModel(
            noteId = 0,
            title = "Title",
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
        },
        onHashTagListener = object : OnHashTagListener {

            override fun onHashTagWritten(noteId: Int, tagName: String) {
                TODO("Not yet implemented")
            }

            override fun onHashtagClick(tagId: Int) {
                TODO("Not yet implemented")
            }
        },
        focusRequester = FocusRequester(),
        onPopupShow = {

        },
        onHashTagMenuListener = object : OnHashTagMenuListener {

            override fun findNote(tagModel: TagModel) {
                TODO("Not yet implemented")
            }

            override fun deleteHashTag(hashTagId: Int, noteId: Int) {
                TODO("Not yet implemented")
            }

            override fun dismissMenu() {
                TODO("Not yet implemented")
            }
        }
    )
}

@Preview
@Composable
private fun LazyListContentPreview() {
    LazyListContent(
        modifier = Modifier,
        paddingValues = PaddingValues(),
        noteList = emptyList(),
        onClickListener = object : OnNoteClickListener {
            override fun onNoteClick(id: Int) {
                TODO("Not yet implemented")
            }

            override fun onNoteDelete(id: Int) {
                TODO("Not yet implemented")
            }
        },
        onHashTagListener = object : OnHashTagListener {

            override fun onHashTagWritten(noteId: Int, tagName: String) {
                TODO("Not yet implemented")
            }

            override fun onHashtagClick(tagId: Int) {
                TODO("Not yet implemented")
            }
        },
        onPopupShow = {},
        onHashTagMenuListener = object : OnHashTagMenuListener {
            override fun findNote(tagModel: TagModel) {
                TODO("Not yet implemented")
            }

            override fun deleteHashTag(hashTagId: Int, noteId: Int) {
                TODO("Not yet implemented")
            }

            override fun dismissMenu() {
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
