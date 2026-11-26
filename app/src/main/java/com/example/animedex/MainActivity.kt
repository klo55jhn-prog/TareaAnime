package com.example.animedex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.animedex.data.repository.SettingsRepository
import com.example.animedex.data.repository.SortOrder
import com.example.animedex.data.repository.UserSettings
import com.example.animedex.ui.catalog.MainAppScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val settingsRepository = SettingsRepository(applicationContext)

        setContent {
            val userSettings by settingsRepository.settings.collectAsStateWithLifecycle(
                initialValue = UserSettings(
                    sort = SortOrder.RANK,
                    minScore = 0f,
                    darkTheme = isSystemInDarkTheme(),
                    favorites = emptySet(),
                ),
            )

            val useDarkTheme = userSettings.darkTheme

            MaterialTheme(
                colorScheme = if (useDarkTheme) darkColorScheme() else lightColorScheme(),
            ) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    MainAppScreen()
                }
            }
        }
    }
}
