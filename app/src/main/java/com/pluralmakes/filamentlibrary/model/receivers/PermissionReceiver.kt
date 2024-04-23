package com.pluralmakes.filamentlibrary.model.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.hardware.usb.UsbManager
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import com.pluralmakes.filamentlibrary.util.impl.ACTION_USB_PERMISSION

class PermissionReceiver(
    private val onReceive: (granted: Boolean) -> Unit,
): BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == ACTION_USB_PERMISSION) {
            onReceive(intent.extras?.getBoolean(UsbManager.EXTRA_PERMISSION_GRANTED) ?: false)
        }
    }
}

@Composable
fun CompPermissionReceiver(
    context: Context,
    onPermissionChange: (Boolean) -> Unit,
    onDispose: () -> Unit,
) {
    val receiver = PermissionReceiver(onPermissionChange)
    DisposableEffect(context) {
        context.registerReceiver(
            receiver,
            IntentFilter(ACTION_USB_PERMISSION),
            when (Build.VERSION.SDK_INT) {
                in 0..33 -> 0
                else -> 0x2
            }
        )

        onDispose {
            onDispose()

            context.unregisterReceiver(receiver)
        }
    }
}