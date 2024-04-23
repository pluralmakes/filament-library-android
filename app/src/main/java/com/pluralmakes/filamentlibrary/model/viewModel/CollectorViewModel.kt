package com.pluralmakes.filamentlibrary.model.viewModel

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import com.pluralmakes.filamentlibrary.model.Filament
import com.pluralmakes.filamentlibrary.util.ConnectionStatus
import com.pluralmakes.filamentlibrary.util.ConnectionStatus.CONNECTING
import com.pluralmakes.filamentlibrary.util.ConnectionStatus.DISCONNECTED
import com.pluralmakes.filamentlibrary.util.ConnectionStatus.NONE
import com.pluralmakes.filamentlibrary.util.ErrorType
import com.pluralmakes.filamentlibrary.util.StorageUtil
import com.pluralmakes.filamentlibrary.util.TD1Communicator

// TODO: There's a state management bug somewhere when the TD-1 communication.
//  When it's reading filament and you try to change values the whole app freezes
class CollectorViewModel(
    val connectionStatus: MutableState<ConnectionStatus> = mutableStateOf(NONE),
    var selectedIndexes: MutableList<Int> = mutableStateListOf(),
    var selectedIndex: MutableState<Int?> = mutableStateOf(null),
    var filaments: MutableList<Filament> = mutableStateListOf(),
    val communicator: TD1Communicator,
): ViewModel() {
    var isReading : Boolean = false
        private set

    fun connect() {
        connectionStatus.value = CONNECTING
        if (!communicator.hasPermission()) {
            communicator.requestPermission(connectionStatus)
            return
        }

        communicator.connect(connectionStatus)
    }

    suspend fun startReading(onFilamentReceived: () -> Unit) {
        isReading = true

        communicator.startReading(
            onReadingEnd = {
                isReading = false
            },
            onFilamentReceived = { filament ->
                filaments.add(filament)
                selectedIndex.value = filaments.indexOf(filament)

                onFilamentReceived()
            }
        )
    }

    fun disconnect() {
        isReading = false
        communicator.disconnect()
        connectionStatus.value = DISCONNECTED
    }

    fun share(context: Context, onError: (ErrorType, Exception?) -> Unit) {
        // Save the current filaments then open the share sheet for it
        val file = StorageUtil.save(filaments, context, onError)

        file?.let {
            val uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)

            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_STREAM, uri)
                type = "application/json"
                flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            }

            context.startActivity(Intent.createChooser(shareIntent, "Export filament"))
        } ?: {
            // TODO: Handle no file saved
        }
    }
}