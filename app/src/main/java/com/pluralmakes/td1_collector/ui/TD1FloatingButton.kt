package com.pluralmakes.td1_collector.ui

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import com.pluralmakes.td1_collector.util.ConnectionStatus
import com.pluralmakes.td1_collector.util.ConnectionStatus.*

@Composable
fun TD1FloatingButton(
    connectionStatus: MutableState<ConnectionStatus>,
    onClick: (ConnectionStatus) -> Unit
) {
    when (connectionStatus.value) {
        CONNECTING -> CircularProgressIndicator()
        PERMISSION_DENIED -> FloatingActionButton(
            onClick = { onClick(connectionStatus.value) }
        ) {
            //TODO: Implement permission denied alert
            Text("Err")
        }
        DEVICE_NOT_FOUND -> FloatingActionButton(onClick = {}) {
            //TODO: Implement proper error message
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