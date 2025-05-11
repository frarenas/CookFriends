package com.devapp.cookfriends

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.devapp.cookfriends.presentation.home.HomeScreen
import com.devapp.cookfriends.ui.theme.CookFriendsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CookFriendsTheme {
                HomeScreen()
            }
        }
    }
}
