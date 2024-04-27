package com.pluralmakes.filamentlibrary.ui.pages.library

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import com.pluralmakes.filamentlibrary.model.receivers.PermissionReceiver
import com.pluralmakes.filamentlibrary.model.viewModel.CollectorViewModel
import com.pluralmakes.filamentlibrary.ui.dialogs.NotFoundDialog
import com.pluralmakes.filamentlibrary.ui.dialogs.PermissionDeniedDialog
import com.pluralmakes.filamentlibrary.ui.pages.library.scaffold.LibraryScaffold
import com.pluralmakes.filamentlibrary.util.ConnectionStatus
import com.pluralmakes.filamentlibrary.util.ConnectionStatus.CONNECTED
import com.pluralmakes.filamentlibrary.util.ConnectionStatus.DEVICE_NOT_FOUND
import com.pluralmakes.filamentlibrary.util.ConnectionStatus.NONE
import com.pluralmakes.filamentlibrary.util.ConnectionStatus.PERMISSION_DENIED
import com.pluralmakes.filamentlibrary.util.StorageUtil
import kotlinx.coroutines.launch

@Composable
fun Library(
    viewModel: CollectorViewModel,
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    LibraryScaffold(
        connectionStatus = viewModel.td1ConnectionStatus,
        selectedFilament = viewModel.selectedFilament,
        bulkSelectedFilament = viewModel.bulkSelectedIndexes,
        filaments = viewModel.filaments,
        onDisconnectClick = {
            viewModel.disconnectTD1()
        },
        onConnectClick = {
            viewModel.connectTD1()
        },
        onShareClick = {
            coroutineScope.launch {
                viewModel.share(
                    context = context,
                ) { errorType, exception ->
                    Toast.makeText(context, "Error sharing filaments", Toast.LENGTH_SHORT).show()

                    // TODO: Properly handle error
                }
            }
        }
    )

    LibraryConnectionHandler(
        connectionStatus = viewModel.td1ConnectionStatus,
        isReading = viewModel.isReading,
        retryConnection = {
            viewModel.connectTD1()
        },
        requestPermissions = {
            viewModel.communicator.requestPermission(viewModel.td1ConnectionStatus)
        },
        startReading = {
            viewModel.startReadingTD1(
                onReadFilament = {
                    StorageUtil.save(
                        filaments = viewModel.filaments,
                        context = context,
                        onError = { _, _ ->
                            Toast.makeText(context, "Error saving filaments", Toast.LENGTH_SHORT).show()

                            // TODO: Properly handle error
                        }
                    )
                }
            )
        },
    )

    PermissionReceiver(
        onPermissionChange = { granted ->
            if (granted) {
                viewModel.connectTD1()
            } else {
                viewModel.td1ConnectionStatus.value = ConnectionStatus.PERMISSION_DENIED
            }
        },
        onDispose = {
            viewModel.disconnectTD1()
        },
    )
}

@Composable
private fun LibraryConnectionHandler(
    connectionStatus: MutableState<ConnectionStatus>,
    isReading: Boolean,
    retryConnection: () -> Unit,
    requestPermissions: () -> Unit,
    startReading: () -> Unit,
) {
    if (connectionStatus.value == CONNECTED) {
        if (!isReading) {
            startReading()
        }
    } else if (connectionStatus.value == DEVICE_NOT_FOUND) {
        NotFoundDialog(
            onConfirmation = retryConnection,
            onDismissRequest = {
                connectionStatus.value = NONE
            }
        )
    } else if (connectionStatus.value == PERMISSION_DENIED) {
        PermissionDeniedDialog(
            onConfirmation = {
                connectionStatus.value = NONE
                requestPermissions()
            },
            onDismissRequest = {
                connectionStatus.value = NONE
            }
        )
    }
}