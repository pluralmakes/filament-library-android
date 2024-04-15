package com.pluralmakes.filamentlibrary.ui.dialogs

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun NotFoundDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
) {
    AlertDialog(
        title = {
            Text(text = "Not found")
        },
        text = {
            Text("Your TD-1 was not found. Would you like to try to reconnect?");
        },
        confirmButton = {
            Button(onClick = {
                onConfirmation()
            }, content = {
                Text("Try again")
            })
        },
        dismissButton = {
            Button(onClick = {
                onDismissRequest()
            }, content = {
                Text("Close")
            })
        },
        onDismissRequest = {
            onDismissRequest()
        },
    )
}