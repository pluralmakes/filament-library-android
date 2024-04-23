package com.pluralmakes.filamentlibrary.ui.dialogs

import androidx.compose.runtime.Composable

@Composable
fun NotFoundDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
) {
    GenericDialog(
        title = "Not found",
        body = "Your TD-1 was not found. Would you like to try to reconnect?",
        onDismissRequest = onDismissRequest,
        confirmText = "Try again",
        onConfirmation = onConfirmation,
    )
}