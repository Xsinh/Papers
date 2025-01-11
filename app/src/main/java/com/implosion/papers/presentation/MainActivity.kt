package com.implosion.papers.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.implosion.papers.presentation.navigation.AppNavigation
import com.implosion.papers.presentation.ui.theme.PapersTheme
import org.koin.core.component.KoinComponent


class MainActivity : ComponentActivity(), KoinComponent {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()
        enableEdgeToEdge()


        setContent {
            PapersTheme {
                AppNavigation()
            }
        }
    }
}
