package com.implosion.papers.presentation.ui.screen.main.screen

import android.app.Activity
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.implosion.domain.model.TagModel
import com.implosion.papers.R
import com.implosion.papers.presentation.navigation.NavigationScreen
import com.implosion.papers.presentation.ui.screen.main.MainViewModel
import com.implosion.papers.presentation.ui.screen.main.screen.listener.OnHashTagListener
import com.implosion.papers.presentation.ui.screen.main.screen.listener.OnNoteClickListener
import com.implosion.papers.presentation.ui.screen.main.screen.listener.OnNoteListListener
import com.implosion.papers.presentation.ui.screen.main.screen.listener.menu.OnHashTagMenuListener
import com.implosion.papers.presentation.ui.screen.main.screen.listener.menu.OnNoteItemMenuListener
import com.implosion.papers.presentation.ui.theme.PapersTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainScreen(modifier: Modifier = Modifier, navController: NavController? = null) {
    val viewModel: MainViewModel = koinViewModel()
    val notes = viewModel.noteList.collectAsState().value

    var searchQuery by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    BackHandler {
        (navController?.context as? Activity)?.finish()
    }

    var showBackground by remember { mutableStateOf(false) }
    val animatedValue = rememberBackgroundAnimation(showBackground)

    val isListShake by viewModel.isShakeNoteList.collectAsState()

    PapersTheme {
        Scaffold(
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        navController?.navigate(NavigationScreen.DetailsWriteNote.route)
                    }) {
                    Icon(
                        Icons.Filled.Create,
                        contentDescription = stringResource(R.string.description_add)
                    )
                }
            }, content = { paddingValues ->
                Column(
                    modifier = modifier
                        .background(
                            MaterialTheme.colorScheme.inverseSurface.copy(alpha = animatedValue)
                        )
                        .clickable(
                            onClick = {
                                focusManager.clearFocus()
                            },
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        )
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    CustomSearchView(
                        onValueChange = { query ->
                            searchQuery = query
                            viewModel.searchNotes(query)
                        },
                        search = searchQuery,
                        focusManager = focusManager
                    )
                    LazyListContent(
                        modifier = modifier
                            .fillMaxSize(),
                        paddingValues = paddingValues,
                        noteList = notes,
                        onClickListener = object : OnNoteClickListener {
                            override fun onNoteClick(id: Int) {
                                navController?.navigate("${NavigationScreen.DetailsReadNote.route}/${id}")
                            }

                            override fun onNoteLongClick(id: Int) {
                                showBackground = true
                            }

                            override fun onNoteLongClickDismiss() {
                                showBackground = false
                            }

                            override fun onNoteMenuClick(id: Int) {
                                viewModel.deleteNote(id)
                            }
                        },
                        onHashTagListener = object : OnHashTagListener {

                            override fun onHashTagWritten(noteId: Int, tagName: String) {
                                viewModel.setHashTag(noteId = noteId, hashTag = tagName)
                            }

                            override fun onHashtagClick(tagId: Int) {

                            }
                        },
                        onPopupShow = { isShow ->
                            showBackground = isShow
                        },
                        onHashTagMenuListener = object : OnHashTagMenuListener {

                            override fun findNote(tagModel: TagModel) {
                                searchQuery = tagModel.name
                                viewModel.searchNotes(tagModel.name)
                            }

                            override fun deleteHashTag(hashTagId: Int, noteId: Int) {
                                viewModel.deleteHashTag(hashTagId, noteId)
                            }

                            override fun dismissMenu() {

                            }
                        },
                        onNoteItemMenuListener = object : OnNoteItemMenuListener {

                            override fun markedNote(noteId: Int, isMark: Boolean) {
                                viewModel.markNoteItemAsComplete(
                                    noteId = noteId,
                                    isComplete = isMark
                                )
                            }

                            override fun onNoteDelete(id: Int) {
                                viewModel.deleteNote(id)
                            }
                        },
                        noteListListener = object : OnNoteListListener {
                            override fun shakeNoteList(isShake: Boolean) {
                                viewModel.setShakeNoteListState(isShake)
                            }

                            override val isShake: Boolean
                                get() = isListShake
                        }
                    )
                }
            })
    }
}

@Composable
fun CustomSearchView(
    search: String,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit,
    focusManager: FocusManager,
) {
    val borderWidth = 1.dp
    val borderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)

    Box(
        modifier = modifier
            .padding(top = 10.dp)
            .padding(4.dp)
            .clip(RoundedCornerShape(28))
            .border(
                border = BorderStroke(borderWidth, borderColor),
                shape = RoundedCornerShape(28)
            ),
    ) {
        TextField(
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text(stringResource(R.string.description_search))
            },
            value = search,
            onValueChange = onValueChange,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                focusedTextColor = MaterialTheme.colorScheme.inverseSurface
            ),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = stringResource(R.string.description_search)
                )
            },
            trailingIcon = {

                if (search.isNotEmpty()) {

                    Icon(
                        modifier = Modifier
                            .clip(RoundedCornerShape(25))
                            .alpha(0.25f)
                            .clickable {
                                onValueChange("")
                                focusManager.clearFocus()
                            }
                            .padding(8.dp),
                        imageVector = Icons.Default.Clear, // Иконка очистки (можно заменить)
                        contentDescription = stringResource(R.string.description_clear)
                    )
                }
            },
        )
    }
}

//На потом
@Composable
fun highlightHashtags(search: String): SpannableString {
    val regex = "#\\w+".toRegex() // Ищет слова, начинающиеся с #
    val spannable = SpannableString(search)

    // Находим все хэштеги и закрашиваем их
    regex.findAll(search).forEach { matchResult ->
        val start = matchResult.range.first
        val end =
            matchResult.range.last + 1 // Конец диапазона (+1, так как range является полуоткрытым)

        // Применяем красный цвет
        spannable.setSpan(
            ForegroundColorSpan(MaterialTheme.colorScheme.tertiary.value.toInt()), // Красный цвет
            start,
            end,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }

    // Устанавливаем текст с выделением в TextView
    return spannable
}

@Composable
private fun rememberBackgroundAnimation(showBackground: Boolean): Float {
    var targetValue by remember { mutableFloatStateOf(0f) }
    val animatedValue by animateFloatAsState(
        targetValue = targetValue,
        animationSpec = tween(durationMillis = 300),
        label = "animated_background"
    )
    targetValue = if (showBackground) 0.58f else 0f
    return animatedValue
}

@Preview
@Composable
private fun MainScreenPreview() {
    MainScreen()
}