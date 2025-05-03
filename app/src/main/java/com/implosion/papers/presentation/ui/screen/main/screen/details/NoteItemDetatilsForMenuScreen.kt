package com.implosion.papers.presentation.ui.screen.main.screen.details

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.implosion.domain.model.NoteModel
import com.implosion.domain.model.TagModel
import com.implosion.papers.R
import com.implosion.papers.presentation.ui.screen.main.screen.listener.OnHashTagListener
import com.implosion.papers.presentation.ui.screen.main.screen.listener.OnNoteClickListener
import com.implosion.papers.presentation.ui.screen.main.screen.listener.menu.OnHashTagMenuListener
import com.implosion.papers.presentation.ui.screen.main.screen.listener.menu.OnNoteItemMenuListener
import com.implosion.papers.presentation.ui.theme.PapersTheme
import com.implosion.papers.presentation.ui.theme.PurpleGrey40
import com.implosion.papers.presentation.ui.theme.Red70
import com.implosion.papers.presentation.ui.theme.Typography

private val shapeForSharedElement = RoundedCornerShape(16.dp)

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.NoteItemDetailsForMenuScreen(
    modifier: Modifier = Modifier,
    item: NoteModel?,
    onClickDismiss: () -> Unit,
    noteItemMenuListener: OnNoteItemMenuListener,
) {

    PapersTheme {
        AnimatedContent(
            modifier = modifier,
            targetState = item,
            transitionSpec = {
                fadeIn() togetherWith fadeOut()
            },
            label = "SnackEditDetails"
        ) { targetSnack ->
            Box(
                modifier = modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                if (targetSnack != null) {
                    Box(
                        modifier = modifier
                            .fillMaxSize()
                            .clickable {
                                onClickDismiss()
                            }
                    )
                    Column(
                        modifier = Modifier
                            .sharedBounds(
                                sharedContentState = rememberSharedContentState(key = "${targetSnack.noteId}-bounds"),
                                animatedVisibilityScope = this@AnimatedContent,
                                clipInOverlayDuringTransition = OverlayClip(
                                    shapeForSharedElement
                                )
                            )
                            .wrapContentSize()
                    ) {
                        MainNoteItem(
                            isClickable = false,
                            modifier = Modifier
                                .padding(14.dp)
                                .clip(RoundedCornerShape(25))
                                .background(MaterialTheme.colorScheme.surface),
                            item = targetSnack,
                            onClickListener = object : OnNoteClickListener {
                                override fun onNoteClick(id: Int) {

                                }

                                override fun onNoteLongClick(id: Int) {

                                }

                                override fun onNoteLongClickDismiss() {

                                }

                                override fun onNoteMenuClick(id: Int) {

                                }
                            },
                            onHashTagListener = object : OnHashTagListener {

                                override fun onHashTagWritten(noteId: Int, tagName: String) {

                                }

                                override fun onHashtagClick(tagId: Int) {

                                }
                            },
                            focusRequester = FocusRequester(),
                            onPopupShow = {

                            },
                            onHashTagMenuListener = object : OnHashTagMenuListener {

                                override fun findNote(tagModel: TagModel) {

                                }

                                override fun deleteHashTag(hashTagId: Int, noteId: Int) {

                                }

                                override fun dismissMenu() {

                                }
                            },
                        )

                        item?.let {
                            Column(
                                modifier = Modifier.padding(horizontal = 28.dp)
                            ) {
                                NoteMenuMarkItem(
                                    item = it,
                                    noteItemMenuListener = noteItemMenuListener,
                                    onClickDismiss = onClickDismiss,
                                )
                                NoteMenuDeleteItem(
                                    item = it,
                                    noteItemMenuListener = noteItemMenuListener,
                                    onClickDismiss = onClickDismiss,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NoteMenuMarkItem(
    noteItemMenuListener: OnNoteItemMenuListener,
    item: NoteModel,
    onClickDismiss: () -> Unit
) {
    var isMarkNoteAsDone by remember { mutableStateOf(item.isMarkedAsComplete) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clip(RoundedCornerShape(35))
            .clickable {
                isMarkNoteAsDone = !isMarkNoteAsDone
                item.noteId?.let { noteItemMenuListener.markedNote(it, isMarkNoteAsDone) }
                onClickDismiss()
            }
            .background(MaterialTheme.colorScheme.surface)
            .padding(10.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        Icon(
            modifier = Modifier
                .clip(RoundedCornerShape(25)),
            imageVector = if (isMarkNoteAsDone) {
                Icons.Filled.Clear
            } else {
                Icons.Filled.Done
            },
            contentDescription = stringResource(R.string.title_mark_as_done),
            tint = PurpleGrey40
        )

        Text(
            modifier = Modifier
                .padding(start = 8.dp),
            text = if (isMarkNoteAsDone) {
                stringResource(R.string.title_remove_the_completed_mark)
            } else {
                stringResource(
                    R.string.title_mark_as_done
                )
            },
            style = Typography.bodyLarge,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun NoteMenuDeleteItem(
    noteItemMenuListener: OnNoteItemMenuListener,
    item: NoteModel,
    onClickDismiss: () -> Unit
) {
    var isMarkNoteAsDone by remember { mutableStateOf(item.isMarkedAsComplete) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clip(RoundedCornerShape(35))
            .clickable {
                isMarkNoteAsDone = !isMarkNoteAsDone
                item.noteId?.let { noteItemMenuListener.onNoteDelete(it) }
                onClickDismiss()
            }
            .background(Red70)
            .padding(10.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        Icon(
            modifier = Modifier
                .clip(RoundedCornerShape(25)),
            imageVector = Icons.Filled.Delete,
            contentDescription = stringResource(R.string.description_delete),
            tint = Color.White.copy(alpha = 0.7f)
        )

        Text(
            modifier = Modifier
                .padding(start = 8.dp),
            text = stringResource(R.string.description_delete),
            style = Typography.bodyLarge,
            color = Color.White.copy(alpha = 0.8f),
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Preview
@Composable
private fun NoteItemDetailsForMenuScreenPreview() {
    SharedTransitionLayout {
        NoteItemDetailsForMenuScreen(
            item = NoteModel(
                noteId = 0,
                title = "Title",
                content = "Content",
                createdAt = 0L,
                updatedAt = 0L,
                isMarkedAsComplete = false
            ),
            onClickDismiss = {},
            noteItemMenuListener = object : OnNoteItemMenuListener {

                override fun markedNote(noteId: Int, isMark: Boolean) {

                }

                override fun onNoteDelete(id: Int) {

                }
            }
        )
    }
}