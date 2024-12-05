package com.implosion.papers.presentation.ui.screen.main

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.Create
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.implosion.papers.presentation.ui.theme.PapersTheme

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    PapersTheme {
        Scaffold(
            floatingActionButton = {
                FloatingActionButton(onClick = { /* Обработчик нажатия */ }) {
                    Icon(Icons.Filled.Create, contentDescription = "Добавить")
                }
            },
            content = { paddingValues ->
                LazyListContent(
                    modifier = modifier
                        .padding(paddingValues)
                        .fillMaxSize(),
                    paddingValues = paddingValues
                )
            }
        )
    }
}

@Composable
fun LazyListContent(
    modifier: Modifier,
    paddingValues: PaddingValues
) {
    val itemsList = List(100) { "Элемент $it" }
    LazyColumn(
        contentPadding = paddingValues,
        modifier = modifier
    ) {
        items(itemsList) { item ->
            Text(text = item, modifier = Modifier.padding(16.dp))
        }
    }
}

@Preview
@Composable
fun MainScreenPreview() {
    MainScreen()
}