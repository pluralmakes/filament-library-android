package com.pluralmakes.filamentlibrary.ui.dialogs

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun GenericDialog(
    title: String,
    body: String,
    dismissText: String = "Cancel",
    onDismissRequest: () -> Unit,
    confirmText: String = "Confirm",
    onConfirmation: () -> Unit,
) {
    AlertDialog(
        title = { Text(title) },
        text = { Text(body) },
        dismissButton = {
            Button(onClick = {
                onDismissRequest()
            }, content = {
                Text(dismissText)
            })
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            Button(onClick = {
                onConfirmation()
            }, content = {
                Text(confirmText)
            })
        }
    )
}