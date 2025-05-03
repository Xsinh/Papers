package com.implosion.papers.presentation.ui.screen.main.screen.details

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.MoreVert
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.implosion.domain.model.NoteModel
import com.implosion.domain.model.TagModel
import com.implosion.papers.R
import com.implosion.papers.presentation.ui.animation.animateCrosses
import com.implosion.papers.presentation.ui.animation.drawCrosses
import com.implosion.papers.presentation.ui.animation.generateCrosses
import com.implosion.papers.presentation.ui.screen.main.screen.main.HashtagContainer
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
            MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
        } else {
            MaterialTheme.colorScheme.surface
        }

    var isShowDeleteDialog by remember { mutableStateOf(false) }
    var isPopupShow by remember { mutableStateOf(false) }

    val crosses = remember { generateCrosses(96) } // Генерируем 50 крестиков
    val transition = rememberInfiniteTransition()

    // Создаем время анимации
    val time by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 7000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    val crossColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f)


    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clip(RoundedCornerShape(25))
            .background(
                if (item.isMarkedAsComplete) {
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                } else {
                    Color.Transparent
                }
            )
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
            .drawBehind {
                if (item.isMarkedAsComplete) {
                    animateCrosses(crosses, time)
                    drawCrosses(crosses)
                    drawCross(
                        color = crossColor,
                        strokeWidth = 12f,
                    )
                }
            }
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
                            item.noteId?.let { id ->
                                onClickListener.onNoteMenuClick(id)
                            }
                        }
                        .padding(8.dp),
                    imageVector = Icons.Filled.MoreVert,
                    contentDescription = stringResource(R.string.description_delete)
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
                    onPopupShow = { isShow ->
                        isPopupShow = isShow
                        onPopupShow.invoke(isShow)
                    },
                    onHashTagMenuListener = onHashTagMenuListener
                )
            }
    }
}

private fun DrawScope.drawCross(color: Color, strokeWidth: Float) {
    // Take full width and height of the composable
    val width = size.width
    val height = size.height

    // Draw two lines crossing the center (diagonals)
    drawLine(
        color = color,
        start = Offset(0f, 0f), // Top-left corner
        end = Offset(width, height), // Bottom-right corner
        strokeWidth = strokeWidth,
    )
    drawLine(
        color = color,
        start = Offset(width, 0f), // Top-right corner
        end = Offset(0f, height), // Bottom-left corner
        strokeWidth = strokeWidth
    )
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
                        updatedAt = 0L,
                        isMarkedAsComplete = false,
                    ),
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
            } else {
            }
        }
    }
}
