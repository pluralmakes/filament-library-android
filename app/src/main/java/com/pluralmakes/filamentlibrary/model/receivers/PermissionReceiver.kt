package com.pluralmakes.filamentlibrary.model.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.hardware.usb.UsbManager
import com.pluralmakes.filamentlibrary.model.CollectorViewModel
import com.pluralmakes.filamentlibrary.util.ConnectionStatus
import com.pluralmakes.filamentlibrary.util.impl.ACTION_USB_PERMISSION
import kotlinx.coroutines.launch

class PermissionReceiver(
    private val collectorViewModel: CollectorViewModel,
): BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == ACTION_USB_PERMISSION) {
            val granted = intent.extras?.getBoolean(UsbManager.EXTRA_PERMISSION_GRANTED) ?: false
            if (granted) {
                collectorViewModel.connect()
            } else {
                collectorViewModel.connectionStatus.value = ConnectionStatus.PERMISSION_DENIED
            }
        }
    }
}