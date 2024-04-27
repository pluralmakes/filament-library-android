package com.pluralmakes.filamentlibrary.ui.pages.library.scaffold

import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import com.pluralmakes.filamentlibrary.ui.views.TD1FloatingButton
import com.pluralmakes.filamentlibrary.util.ConnectionStatus

@Composable
fun LibraryScaffoldFAB(
    connectionStatus: MutableState<ConnectionStatus>,
    onConnectClick: () -> Unit,
    onDisconnectClick: () -> Unit,
    onShareClick: () -> Unit,
) {
    Row {
       TD1FloatingButton(
           connectionStatus = connectionStatus,
           onShareClick = onShareClick,
           onConnectClick = {
                when (it) {
                    ConnectionStatus.DISCONNECTED, ConnectionStatus.NONE -> onConnectClick()
                    ConnectionStatus.CONNECTED -> onDisconnectClick()
                    else -> {}
                }
           },
       )
    }
}