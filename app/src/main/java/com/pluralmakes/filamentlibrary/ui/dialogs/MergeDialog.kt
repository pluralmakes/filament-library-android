package com.pluralmakes.filamentlibrary.ui.dialogs

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.pluralmakes.filamentlibrary.model.CollectorViewModel

@Composable
fun MergeDialog(
    viewModel: CollectorViewModel,
    onConfirmation: () -> Unit,
    onDismissRequest: () -> Unit,
) {
    AlertDialog(
        title = {
            Text("Would you like to merge the selected filaments?")
        },
        text = {
            Text("Would you like to merge the selected filaments?")
        },
        confirmButton = {
            Button(onClick = {
                onConfirmation()
            }, content = {
                Text("Confirm")
            })
        },
        dismissButton = {
            Button(onClick = {
                onDismissRequest()
            }, content = {
                Text("Cancel")
            })
        },
        onDismissRequest = {
            onDismissRequest()
        }
    )
}