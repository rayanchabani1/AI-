package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.example.data.local.AppDatabase
import com.example.data.repository.AppRepository
import com.example.ui.screens.MainScreen
import com.example.ui.theme.CommunityAppHubTheme
import com.example.ui.viewmodel.CommunityViewModel

class MainActivity : ComponentActivity() {

    private lateinit var viewModel: CommunityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val database = AppDatabase.getDatabase(applicationContext, lifecycleScope)
        val repository = AppRepository(database.appDao())
        val viewModelFactory = CommunityViewModel.Factory(repository)

        val vm: CommunityViewModel by viewModels { viewModelFactory }
        viewModel = vm

        checkIntentForGameHud(intent)

        setContent {
            CommunityAppHubTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen(viewModel = viewModel)
                }
            }
        }
    }

    override fun onNewIntent(intent: android.content.Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        checkIntentForGameHud(intent)
    }

    private fun checkIntentForGameHud(intent: android.content.Intent?) {
        if (intent?.getBooleanExtra("OPEN_GAME_HUD", false) == true) {
            viewModel.toggleGameHudDialog(true)
        }
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        if (level >= TRIM_MEMORY_BACKGROUND || level >= TRIM_MEMORY_MODERATE) {
            System.gc()
        }
    }
}
