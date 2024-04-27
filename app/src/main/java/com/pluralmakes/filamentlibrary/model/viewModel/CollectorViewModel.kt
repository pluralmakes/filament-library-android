package com.pluralmakes.filamentlibrary.model.viewModel

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pluralmakes.filamentlibrary.model.Filament
import com.pluralmakes.filamentlibrary.util.ConnectionStatus
import com.pluralmakes.filamentlibrary.util.ConnectionStatus.CONNECTING
import com.pluralmakes.filamentlibrary.util.ConnectionStatus.DISCONNECTED
import com.pluralmakes.filamentlibrary.util.ConnectionStatus.NONE
import com.pluralmakes.filamentlibrary.util.ErrorType
import com.pluralmakes.filamentlibrary.util.StorageUtil
import com.pluralmakes.filamentlibrary.util.TD1Communicator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// TODO: There's a state management bug somewhere when the TD-1 communication.
//  When it's reading filament and you try to change values the whole app freezes
class CollectorViewModel(
    val td1ConnectionStatus: MutableState<ConnectionStatus> = mutableStateOf(NONE),
    var selectedFilament: MutableState<Filament?> = mutableStateOf(null),
    var bulkSelectedIndexes: SnapshotStateList<Filament> = mutableStateListOf(),
    var filaments: SnapshotStateList<Filament> = mutableStateListOf(),
    val communicator: TD1Communicator,
): ViewModel() {
    var isReading : Boolean = false
        private set

    fun share(context: Context, onError: (ErrorType, Exception?) -> Unit) {
        // Save the current filaments then open the share sheet for it
        val file = StorageUtil.save(filaments, context, onError)

        if (file == null) {
            onError(ErrorType.IO, null)
            return
        }

        val uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)

        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_STREAM, uri)
            type = "application/json"
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        }

        context.startActivity(Intent.createChooser(shareIntent, "Export filament"))
    }

    fun connectTD1() {
        td1ConnectionStatus.value = CONNECTING
        if (!communicator.hasPermission()) {
            communicator.requestPermission(td1ConnectionStatus)
            return
        }

        communicator.connect(td1ConnectionStatus)
    }

    fun startReadingTD1(
        onReadFilament: () -> Unit,
    ) {
        isReading = true

        viewModelScope.launch(Dispatchers.IO) {
            communicator.startReading(
                onReadFilament = { filament ->
                    val isSelectedAlready = selectedFilament.value != null
                    viewModelScope.launch(Dispatchers.Main) {
                        filaments.add(filament)
                        selectedFilament.value = if (isSelectedAlready) null else filament
                    }

                    onReadFilament()

                    if (isSelectedAlready) {
                        delay(500)
                        viewModelScope.launch {
                            selectedFilament.value = filament
                        }
                    }
                },
                onReadingComplete = {
                    isReading = false
                }
            )
        }
    }

    fun disconnectTD1() {
        isReading = false
        communicator.disconnect()
        td1ConnectionStatus.value = DISCONNECTED
    }
}