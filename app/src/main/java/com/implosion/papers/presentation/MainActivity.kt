package com.implosion.papers.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.implosion.papers.presentation.ui.theme.PapersTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PapersTheme {
//                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    MainScreen(modifier = Modifier.padding(innerPadding))
//                    Greeting(
//                        name = "Android",
//                        modifier = Modifier.padding(innerPadding)
//                    )
//                }
            }
        }
    }
}
