package com.example

import android.app.PictureInPictureParams
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.util.Rational
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.ui.MainViewModel
import com.example.ui.TelfazetnaApp
import com.example.ui.theme.TelfazetnaTheme

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            TelfazetnaTheme {
                val playerState by viewModel.playerManager.uiState.collectAsState()

                val isMediaActive = playerState.currentChannel != null || playerState.currentVodItem != null
                BackHandler(enabled = playerState.isFullscreen || isMediaActive) {
                    if (playerState.isFullscreen) {
                        toggleFullscreen(false)
                    } else if (isMediaActive) {
                        viewModel.closePlayer()
                    }
                }

                TelfazetnaApp(
                    viewModel = viewModel,
                    onToggleFullscreen = {
                        toggleFullscreen(!playerState.isFullscreen)
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }

    private fun toggleFullscreen(fullscreen: Boolean) {
        viewModel.playerManager.setFullscreen(fullscreen)
        if (fullscreen) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
        } else {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        }
    }

    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        // Support Picture-in-Picture when user goes home while watching
        val playerState = viewModel.playerManager.uiState.value
        if (playerState.isPlaying && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                val params = PictureInPictureParams.Builder()
                    .setAspectRatio(Rational(16, 9))
                    .build()
                enterPictureInPictureMode(params)
            } catch (e: Exception) {
                // PiP not supported on this device/configuration
            }
        }
    }

    override fun onStop() {
        super.onStop()
        // Pause player if not in PiP mode
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && !isInPictureInPictureMode) {
            viewModel.playerManager.getPlayer().pause()
        }
    }
}
