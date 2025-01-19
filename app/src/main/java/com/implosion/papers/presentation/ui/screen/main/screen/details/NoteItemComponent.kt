package com.implosion.papers.presentation.ui.screen.main.screen.details

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.implosion.domain.model.NoteModel
import com.implosion.papers.R
import com.implosion.papers.presentation.ui.component.AlertDialog
import com.implosion.papers.presentation.ui.screen.main.screen.HashtagContainer
import com.implosion.papers.presentation.ui.screen.main.screen.listener.OnHashTagListener
import com.implosion.papers.presentation.ui.screen.main.screen.listener.OnNoteClickListener
import com.implosion.papers.presentation.ui.screen.main.screen.listener.menu.OnHashTagMenuListener
import com.implosion.papers.presentation.ui.theme.Typography
import kotlinx.collections.immutable.toImmutableList


@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class,
    ExperimentalSharedTransitionApi::class,
)
@Composable
fun MainNoteItem(
    item: NoteModel,

    onClickListener: OnNoteClickListener,
    onHashTagListener: OnHashTagListener,

    focusRequester: FocusRequester,
    focusManager: FocusManager = LocalFocusManager.current,

    modifier: Modifier = Modifier,
    onPopupShow: (Boolean) -> Unit,
    onHashTagMenuListener: OnHashTagMenuListener,
    isClickable: Boolean = true,
) {
    val borderWidth = 1.dp
    val borderColor =
        if (isClickable) {
            MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
        } else {
            MaterialTheme.colorScheme.surface
        }

    var isShowDeleteDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clip(RoundedCornerShape(25))
            .border(
                border = BorderStroke(borderWidth, borderColor),
                shape = RoundedCornerShape(25)
            )
            .combinedClickable(
                enabled = isClickable,
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(),
                onClick = {
                    item.noteId?.let { id ->
                        onClickListener.onNoteClick(id)
                    }
                },
                onLongClick = {
                    item.noteId?.let { id ->
                        onClickListener.onNoteLongClick(id)
                    }
                }
            )
            .padding(start = 4.dp, end = 4.dp, top = 12.dp),
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
                        .clickable(enabled = isClickable) {
                            isShowDeleteDialog = true
                        }
                        .padding(8.dp),
                    imageVector = Icons.Filled.Delete,
                    contentDescription = stringResource(R.string.description_delete)
                )
                AlertDialog(
                    showDialog = isShowDeleteDialog,
                    onConfirm = {
                        item.noteId?.let { id ->
                            onClickListener.onNoteDelete(id)
                        }
                    },
                    onDismiss = {
                        isShowDeleteDialog = false
                    }
                )
            }
        }

        item.noteId
            ?.let { id ->
                HashtagContainer(
                    isClickable = isClickable,
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

@OptIn(ExperimentalSharedTransitionApi::class)
@Preview
@Composable
private fun MainNoteItemPreview() {
    var showDetails by remember {
        mutableStateOf(false)
    }
    SharedTransitionLayout {
        AnimatedContent(
            showDetails,
            label = "basic_transition"
        ) { targetValue ->

            if (!targetValue) {
                MainNoteItem(
                    item = NoteModel(
                        noteId = 0,
                        title = "Title",
                        content = "Content",
                        createdAt = 0L,
                        updatedAt = 0L
                    ),
                    onClickListener = TODO(),
                    onHashTagListener = TODO(),
                    focusRequester = FocusRequester(),
                    onPopupShow = {

                    },
                    onHashTagMenuListener = TODO(),
                )
            } else { }
        }
    }
}
