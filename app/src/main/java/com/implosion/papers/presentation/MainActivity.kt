package com.implosion.papers.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.implosion.papers.presentation.navigation.AppNavigation
import com.implosion.papers.presentation.ui.theme.PapersTheme


class MainActivity : ComponentActivity() {

    private val splitInstallManager by lazy {
        SplitInstallManagerFactory.create(this)
    }

    private val moduleName = "feature-paint"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()
        enableEdgeToEdge()


        setContent {
            PapersTheme {
                AppNavigation()
            }
        }

//        loadDynamicFeature()
    }

//    private fun loadDynamicFeature() {
//        val splitInstallManager = SplitInstallManagerFactory.create(this)
//        val request = SplitInstallRequest.newBuilder()
//            .addModule("feature:paint")
//            .build()
//
//        splitInstallManager.startInstall(request)
//            .addOnSuccessListener {
//                // После успешной загрузки зарегистрировать модуль Koin
//                loadPaintFeature()
//
//                Toast.makeText(this, "Zaebiz", Toast.LENGTH_LONG).show()
//            }
//            .addOnFailureListener { f ->
//                Toast.makeText(
//                    this,
//                    "Failed to load settings module: ${f.message}",
//                    Toast.LENGTH_LONG
//                ).show()
//
//                Log.e("###", f.toString())
//            }
//    }
//
//    fun loadPaintFeature() {
//        val paintDynamicFeature =
//            Class.forName("com.implosion.paint.DynamicFeatureModuleImpl")
//                .getDeclaredConstructor()
//                .newInstance() as DynamicFeatureModuleApi
//
//        // Зарегистрировать модуль в Koin
//        loadKoinModules(paintDynamicFeature.loadModule())
//    }
}
