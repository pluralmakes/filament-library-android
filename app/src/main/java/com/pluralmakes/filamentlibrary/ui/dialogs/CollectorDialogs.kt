package com.pluralmakes.filamentlibrary.ui.dialogs

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import com.pluralmakes.filamentlibrary.model.viewModel.CollectorViewModel
import com.pluralmakes.filamentlibrary.util.ConnectionStatus
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun CollectorDialogs(
    viewModel: CollectorViewModel
) {
    val coroutineScope = rememberCoroutineScope()
    val retryConnection = { delayConnection: Boolean ->
        viewModel.connectionStatus.value = ConnectionStatus.CONNECTING

        coroutineScope.launch {
            if (delayConnection) {
                delay(1000L)
            }

            viewModel.connect()
        }
    }

    when (viewModel.connectionStatus.value) {
        ConnectionStatus.DEVICE_NOT_FOUND -> NotFoundDialog(onDismissRequest = {
            viewModel.connectionStatus.value = ConnectionStatus.NONE
        }, onConfirmation = {
            retryConnection(true)
        })
        ConnectionStatus.PERMISSION_DENIED -> PermissionDeniedDialog(
            onDismissRequest = {
                viewModel.connectionStatus.value = ConnectionStatus.NONE
            }, onConfirmation = {
                retryConnection(false)
            })
        else -> {}
    }
}