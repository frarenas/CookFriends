package com.devapp.cookfriends

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.devapp.cookfriends.presentation.navigation.AppNavGraph
import com.devapp.cookfriends.ui.theme.CookFriendsTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CookFriendsTheme {
                AppNavGraph()
            }
        }
    }
}
