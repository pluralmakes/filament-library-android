package com.pluralmakes.td1_collector.viewModel

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.pluralmakes.td1_collector.model.Filament
import com.pluralmakes.td1_collector.util.ConnectionStatus
import com.pluralmakes.td1_collector.util.ConnectionStatus.*
import com.pluralmakes.td1_collector.util.TD1Communicator

class CollectorViewModel(
    val connectionStatus: MutableState<ConnectionStatus> = mutableStateOf(NONE),
    var selectedIndex: MutableState<Int?> = mutableStateOf(null),
    var filaments: MutableList<Filament> = mutableListOf(),
    val communicator: TD1Communicator,
): ViewModel() {
    suspend fun connect() {
        connectionStatus.value = CONNECTING
        if (!communicator.hasPermission()) {
            communicator.requestPermission(connectionStatus)
            return
        }

        communicator.connect(connectionStatus)

        if (connectionStatus.value == CONNECTED) {
            startReading {
                filaments.add(it)

                selectedIndex.value = filaments.size - 1
            }
        }
    }

    fun export(context: Context) {
        //TODO: Implement export share
    }

    suspend fun startReading(onFilamentReceive: (Filament) -> Unit) {
        communicator.startReading(onFilamentReceive)
    }

    fun disconnect() {
        communicator.disconnect()
        connectionStatus.value = DISCONNECTED
    }
}