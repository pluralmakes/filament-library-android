package com.pluralmakes.filamentlibrary.util

import androidx.compose.runtime.MutableState
import androidx.compose.ui.graphics.Color
import com.pluralmakes.filamentlibrary.model.Filament
import java.io.IOException

interface TD1Communicator {
    /**
     * Handle connecting to the TD-1 over USB
     */
    fun connect(isConnected: MutableState<ConnectionStatus>)

    /**
     * Start attempting to read filament data from the TD-1
     */
    @Throws(IOException::class)
    suspend fun startReading(onFilamentReceive: (Filament) -> Unit)

    /**
     * Checks if your app has permission to access the USB device
     */
    fun hasPermission(): Boolean

    /**
     * Request permission to access the USB device
     */
    fun requestPermission(permissionStatus: MutableState<ConnectionStatus>)

    /**
     * Handle disconnecting from the TD-1
     */
    fun disconnect()
}

enum class ConnectionStatus {
    NONE,
    DISCONNECTED,
    DEVICE_NOT_FOUND,
    PERMISSION_DENIED,
    CONNECTING,
    CONNECTED,
    CONNECTION_FAILED;

    fun toColor(): Color {
        return if (this == CONNECTED) {
            Color.Green
        } else if (this == CONNECTING) {
            Color.Yellow
        } else if (this == CONNECTION_FAILED || this == PERMISSION_DENIED) {
            Color.Red
        } else {
            Color.Gray
        }
    }
}