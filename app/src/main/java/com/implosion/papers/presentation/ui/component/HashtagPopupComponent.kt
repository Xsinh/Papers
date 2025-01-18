package com.implosion.papers.presentation.ui.component

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.implosion.domain.model.TagModel
import com.implosion.papers.R
import com.implosion.papers.presentation.ui.screen.main.screen.listener.menu.OnHashTagMenuListener
import com.implosion.papers.presentation.ui.theme.PurpleGrey40
import com.implosion.papers.presentation.ui.theme.Red80
import com.implosion.papers.presentation.ui.theme.Typography

@SuppressLint("UnrememberedMutableInteractionSource")
@Composable
fun HashtagPopupComponent(
    noteId: Int,
    tagModel: TagModel,
    showMenu: Boolean, // Состояние отображения/скрытия Popup
    onHashTagMenuListener: OnHashTagMenuListener,
) {
    var menuOffset by remember { mutableStateOf(Offset.Zero) } // Для произвольной позиции

    if (showMenu) { // Проверяем, нужно ли отображать Popup
        Box(
            modifier = Modifier
                .fillMaxSize() // Фон занимает весь экран
                .clickable(
                    onClick = {
                        onHashTagMenuListener.dismissMenu()
                    },
                    indication = null, // Без визуального эффекта нажатия
                    interactionSource = MutableInteractionSource() // Источник взаимодействий
                )
        ) {
            Popup(
                offset = IntOffset(menuOffset.x.toInt(), menuOffset.y.toInt() + 78),
                onDismissRequest = {
                    onHashTagMenuListener.dismissMenu()
                }, // Вызываем переданный обработчик закрытия
                properties = PopupProperties(focusable = true)
            ) {
                Column(
                    modifier = Modifier,
                    verticalArrangement = Arrangement.Bottom
                ) {

                    Column(
                        modifier = Modifier
                            .width(IntrinsicSize.Min)
                            .clip(RoundedCornerShape(26))
                            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.96f)),
                        verticalArrangement = Arrangement.Bottom,
                    ) {

                        MarkedDoneItem(onHashTagMenuListener, tagModel)

                        Spacer(Modifier.padding(vertical = 2.dp))

                        DeleteItem(
                            onHashTagMenuListener = onHashTagMenuListener,
                            hashTagId = tagModel.tagId,
                            noteId = noteId
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MarkedDoneItem(
    onHashTagMenuListener: OnHashTagMenuListener,
    tagModel: TagModel
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onHashTagMenuListener.findNote(tagModel)
            }
            .padding(bottom = 4.dp, start = 8.dp, end = 18.dp, top = 8.dp),
        verticalAlignment = Alignment.Bottom // Это определяет, что всё содержимое скроется к нижней границе строки
    ) {
        Icon(
            modifier = Modifier
                .clip(RoundedCornerShape(25)),
            imageVector = Icons.Filled.Search,
            contentDescription = stringResource(R.string.description_find),
            tint = PurpleGrey40
        )

        Text(
            modifier = Modifier
                .padding(start = 8.dp),
            text = stringResource(R.string.description_find),
            style = Typography.bodyLarge,
            color = PurpleGrey40,
        )
    }
}

@Composable
private fun DeleteItem(
    onHashTagMenuListener: OnHashTagMenuListener,
    hashTagId: Int,
    noteId: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onHashTagMenuListener.deleteHashTag(hashTagId, noteId)
            }
            .padding(bottom = 10.dp, start = 8.dp, end = 18.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        Icon(
            modifier = Modifier
                .clip(RoundedCornerShape(25))
                .alpha(0.5f),
            imageVector = Icons.Filled.Delete,
            contentDescription = stringResource(R.string.description_delete),
            tint = Red80
        )

        Text(
            modifier = Modifier
                .padding(start = 8.dp)
                .alpha(0.8f),
            text = stringResource(R.string.description_delete),
            style = Typography.bodyLarge,
            color = Red80,
        )
    }
}

@Composable
@Preview
private fun HashtagPopupComponentPreview() {
    HashtagPopupComponent(
        noteId = 0,
        tagModel = TagModel(0, ""),
        showMenu = true,
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
        },
    )
}