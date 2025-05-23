package com.implosion.papers.presentation.ui.screen.main.screen.main

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.implosion.domain.model.NoteModel
import com.implosion.domain.model.TagModel
import com.implosion.papers.R
import com.implosion.papers.presentation.ui.component.AnimationText
import com.implosion.papers.presentation.ui.component.HashtagPopupComponent
import com.implosion.papers.presentation.ui.screen.main.screen.details.MainNoteItem
import com.implosion.papers.presentation.ui.screen.main.screen.details.NoteItemDetailsForMenuScreen
import com.implosion.papers.presentation.ui.screen.main.screen.listener.OnHashTagListener
import com.implosion.papers.presentation.ui.screen.main.screen.listener.OnNoteClickListener
import com.implosion.papers.presentation.ui.screen.main.screen.listener.OnNoteListListener
import com.implosion.papers.presentation.ui.screen.main.screen.listener.menu.OnHashTagMenuListener
import com.implosion.papers.presentation.ui.screen.main.screen.listener.menu.OnNoteItemMenuListener
import com.implosion.papers.presentation.ui.theme.Typography
import kotlinx.collections.immutable.ImmutableList
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
    onHashTagMenuListener: OnHashTagMenuListener,
    onNoteItemMenuListener: OnNoteItemMenuListener,

    noteListListener: OnNoteListListener,
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
            onHashTagMenuListener = onHashTagMenuListener,
            onNoteItemMenuListener = onNoteItemMenuListener,
            noteListListener = noteListListener,
        )
    }
}

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalFoundationApi::class)
@SuppressLint("UnrememberedMutableInteractionSource")
@Composable
private fun NotesMainScreen(
    modifier: Modifier,

    paddingValues: PaddingValues,
    itemsList: List<NoteModel>,

    focusRequester: FocusRequester,
    focusManager: FocusManager,

    onPopupShow: (Boolean) -> Unit,

    onClickListener: OnNoteClickListener,
    onHashTagListener: OnHashTagListener,
    onHashTagMenuListener: OnHashTagMenuListener,
    onNoteItemMenuListener: OnNoteItemMenuListener,

    noteListListener: OnNoteListListener,
) {
    var selectedSnack by remember { mutableStateOf<NoteModel?>(null) }

    val listState = rememberLazyListState()
    var isShake = noteListListener.isShake

    SharedTransitionLayout(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = modifier
                .clip(RoundedCornerShape(4)),
            horizontalAlignment = Alignment.End,
        ) {
            ShakeNotes(noteListListener = noteListListener)

            LaunchedEffect(isShake) {
                if (isShake) {
                    listState.scrollToItem(0) // Скроллим к началу списка
                }
            }

            LazyColumn(
                modifier = Modifier,
                contentPadding = paddingValues,
                state = listState,
            ) {
                items(
                    items = if (isShake) {
                        itemsList.reversed()
                    } else {
                        itemsList
                    },
                    key = { item -> item.noteId ?: 0 }
                ) { item ->
                    AnimatedVisibility(
                        visible = item != selectedSnack,
                        enter = fadeIn() + scaleIn(),
                        exit = fadeOut() + scaleOut(),
                        modifier = Modifier.animateItem()
                    ) {
                        MainNoteItem(
                            modifier = Modifier
                                .sharedBounds(
                                    sharedContentState = rememberSharedContentState(key = "${item.noteId}-bounds"),
                                    animatedVisibilityScope = this@AnimatedVisibility,
                                    clipInOverlayDuringTransition = OverlayClip(
                                        RoundedCornerShape(25)
                                    )
                                )
                                .sharedElement(
                                    sharedContentState = rememberSharedContentState(key = item.noteId.toString()),
                                    animatedVisibilityScope = this@AnimatedVisibility
                                )
                                .animateItem(),
                            item = item,
                            onClickListener = object : OnNoteClickListener {

                                override fun onNoteClick(id: Int) {
                                    onClickListener.onNoteClick(id)
                                }

                                override fun onNoteLongClick(id: Int) {
                                    selectedSnack = item
                                    onClickListener.onNoteLongClick(id)
                                }

                                override fun onNoteLongClickDismiss() {
                                    onClickListener.onNoteLongClickDismiss()
                                }

                                override fun onNoteMenuClick(id: Int) {
                                    selectedSnack = item
                                    onClickListener.onNoteLongClick(id)
                                }
                            },
                            onHashTagListener = onHashTagListener,
                            focusRequester = focusRequester,
                            focusManager = focusManager,
                            onPopupShow = onPopupShow,
                            onHashTagMenuListener = onHashTagMenuListener,
                        )
                    }
                }
            }

        }
        NoteItemDetailsForMenuScreen(
            item = selectedSnack,
            modifier = modifier,
            onClickDismiss = {
                selectedSnack = null
                onClickListener.onNoteLongClickDismiss()
            },
            noteItemMenuListener = onNoteItemMenuListener
        )
    }
}

@Composable
fun ShakeNotes(noteListListener: OnNoteListListener) {
    var turn by remember { mutableStateOf(noteListListener.isShake) }

    val sortText =
        if (turn) stringResource(R.string.title_new) else stringResource(R.string.title_old)

    var oldSortText by remember { mutableStateOf(sortText) }

    val rotationAngle by animateFloatAsState(
        targetValue = if (turn) 90f else 270f, // Угол поворота в зависимости от состояния
        animationSpec = tween(
            durationMillis = 500,
            easing = FastOutSlowInEasing
        ) // Настройка анимации
    )

    SideEffect {
        oldSortText = sortText
    }
    Row(
        modifier = Modifier
            .padding(top = 2.dp)
            .clip(RoundedCornerShape(35))
            .clickable {
                turn = !turn
                noteListListener.shakeNoteList(turn)
            }
            .padding(8.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        AnimationText(sortText, oldSortText)

        Icon(
            modifier = Modifier
                .alpha(0.9f)
                .size(16.dp)
                .rotate(rotationAngle)
                .animateContentSize(),
            painter = painterResource(R.drawable.ic_two_arrows),
            contentDescription = "test",
            tint = MaterialTheme.colorScheme.inverseSurface
        )
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
    onHashTagMenuListener: OnHashTagMenuListener,
    isClickable: Boolean = true,
) {
    var hashTagText by remember { mutableStateOf("") }

    TextField(
        enabled = isClickable,
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

                Spacer(modifier = Modifier.padding(horizontal = 2.dp))

                LazyRow {
                    items(items = hashTagList, key = { item -> item.tagId }) { chipText ->

                        HashTagItem(
                            isClickable = isClickable,
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
            disabledIndicatorColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
        ),
    )
}

@Composable
private fun LazyItemScope.HashTagItem(
    item: NoteModel,
    tagModel: TagModel,
    onPopupShow: (Boolean) -> Unit,
    onHashTagMenuListener: OnHashTagMenuListener,
    isClickable: Boolean = true,
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
            .clickable(enabled = isClickable) {
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
private fun ShakeNotesPreview() {
    ShakeNotes(
        noteListListener = object : OnNoteListListener {
            override fun shakeNoteList(isShake: Boolean) {
                TODO("Not yet implemented")
            }

            override val isShake: Boolean
                get() = TODO("Not yet implemented")

        },
    )
}

@Preview
@Composable
private fun NotesMainScreenPreview() {
    NotesMainScreen(
        modifier = Modifier,
        focusRequester = FocusRequester(),
        focusManager = LocalFocusManager.current,
        paddingValues = PaddingValues(2.dp),
        onClickListener = object : OnNoteClickListener {
            override fun onNoteClick(id: Int) {
                TODO("Not yet implemented")
            }

            override fun onNoteLongClick(id: Int) {
                TODO("Not yet implemented")
            }

            override fun onNoteLongClickDismiss() {
                TODO("Not yet implemented")
            }

            override fun onNoteMenuClick(id: Int) {
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
                updatedAt = 0L,
                isMarkedAsComplete = false,
            ), NoteModel(
                noteId = 0,
                title = "Title",
                content = "Content",
                createdAt = 0L,
                updatedAt = 0L,
                isMarkedAsComplete = false,
            )
        ),
        onPopupShow = {},
        onHashTagMenuListener = TODO(),
        onNoteItemMenuListener = TODO(),
        noteListListener = object : OnNoteListListener {
            override fun shakeNoteList(isShake: Boolean) {
                TODO("Not yet implemented")
            }

            override val isShake: Boolean
                get() = TODO("Not yet implemented")

        },
    )
}

@Preview
@Composable
private fun LazyListContentPreview() {
    LazyListContent(
        modifier = Modifier,
        paddingValues = PaddingValues(),
        noteList = emptyList(),
        onClickListener = TODO(),
        onHashTagListener = TODO(),
        onPopupShow = {},
        onHashTagMenuListener = TODO(),
        onNoteItemMenuListener = TODO(),
        noteListListener = object : OnNoteListListener {
            override fun shakeNoteList(isShake: Boolean) {
                TODO("Not yet implemented")
            }

            override val isShake: Boolean
                get() = TODO("Not yet implemented")

        },
    )
}

@Preview
@Composable
private fun EmptyNoteScreenPreview() {
    EmptyNoteScreen()
}
