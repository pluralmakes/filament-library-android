package com.pluralmakes.filamentlibrary.ui.dialogs

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun PermissionDeniedDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
) {
    AlertDialog(
        title = {
            Text("Permission Denied")
        },
        text = {
            Text("We do not have the permissions required to connect to your TD-1. Please try again.")
        },
        confirmButton = {
            Button(onClick = { onConfirmation() }) {
                Text("Try again")
            }
        },
        dismissButton = {
            Button(onClick = { onDismissRequest() }) {
                Text("Close")
            }
        },
        onDismissRequest = {
            onDismissRequest()
        },
    )
}