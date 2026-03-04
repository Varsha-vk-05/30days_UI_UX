package com.example.a30days

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.a30days.ui.theme._30daysTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var darkTheme by remember { mutableStateOf(false) }
            val systemInDarkTheme = isSystemInDarkTheme()
            
            // Use Crossfade to animate the entire theme switch
            Crossfade(targetState = darkTheme) { isDark ->
                _30daysTheme(darkTheme = isDark) {
                    Surface(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        TipsApp(
                            isDarkTheme = isDark,
                            onThemeToggle = { darkTheme = !darkTheme }
                        )
                    }
                }
            }
        }
    }
}
