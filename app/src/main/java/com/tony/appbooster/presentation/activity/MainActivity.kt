package com.tony.appbooster.presentation.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.alkemy.boxapp.presentation.navigation.interfaces.NavigationManager
import com.tony.appbooster.presentation.navigation.AppNavigator
import com.tony.appbooster.presentation.ui.theme.AppBoosterTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var navigationManager: NavigationManager

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppBoosterTheme {
                AppNavigator(navigationManager = navigationManager)
            }
        }
    }
}
