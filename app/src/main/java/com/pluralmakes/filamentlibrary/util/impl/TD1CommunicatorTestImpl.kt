package com.pluralmakes.filamentlibrary.util.impl

import androidx.compose.runtime.MutableState
import com.pluralmakes.filamentlibrary.model.Filament
import com.pluralmakes.filamentlibrary.util.ConnectionStatus
import com.pluralmakes.filamentlibrary.util.TD1Communicator

class TD1CommunicatorTestImpl: TD1Communicator {
    override fun connect(isConnected: MutableState<ConnectionStatus>) {
        TODO("Not yet implemented")
    }

    override suspend fun startReading(
        onReadingEnd: () -> Unit,
        onFilamentReceived: (Filament) -> Unit
    ) {
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