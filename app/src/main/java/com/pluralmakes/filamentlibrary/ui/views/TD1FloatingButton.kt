package com.pluralmakes.filamentlibrary.ui.views

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import com.pluralmakes.filamentlibrary.model.TD1Constants
import com.pluralmakes.filamentlibrary.util.ConnectionStatus
import com.pluralmakes.filamentlibrary.util.ConnectionStatus.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun TD1FloatingButton(
    connectionStatus: MutableState<ConnectionStatus>,
    onClick: (ConnectionStatus) -> Unit
) {
    when (connectionStatus.value) {
        CONNECTING -> FloatingActionButton(onClick = {}) {
            CircularProgressIndicator()
        }
        PERMISSION_DENIED -> {}
        DEVICE_NOT_FOUND -> FloatingActionButton(onClick = {}) {
            Text("404")
        }
        else -> FloatingActionButton(
            containerColor = connectionStatus.value.toColor(),
            onClick = {
                onClick(connectionStatus.value)
            }
        ) {
            Text("TD-1")
        }
    }
}