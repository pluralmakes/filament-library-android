package com.pluralmakes.td1_collector.util.impl

import androidx.compose.runtime.MutableState
import com.pluralmakes.td1_collector.model.Filament
import com.pluralmakes.td1_collector.util.ConnectionStatus
import com.pluralmakes.td1_collector.util.TD1Communicator

class TD1CommunicatorTestImpl: TD1Communicator {
    override fun connect(isConnected: MutableState<ConnectionStatus>) {
        TODO("Not yet implemented")
    }

    override suspend fun startReading(onFilamentReceive: (Filament) -> Unit) {
        TODO("Not yet implemented")
    }

    override fun hasPermission(): Boolean {
        TODO("Not yet implemented")
    }

    override fun requestPermission(permissionStatus: MutableState<ConnectionStatus>) {
        TODO("Not yet implemented")
    }

    override fun disconnect() {
        TODO("Not yet implemented")
    }
}