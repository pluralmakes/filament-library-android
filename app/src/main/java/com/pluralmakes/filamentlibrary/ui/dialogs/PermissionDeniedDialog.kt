package com.pluralmakes.filamentlibrary.ui.dialogs

import androidx.compose.runtime.Composable

@Composable
fun PermissionDeniedDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
) {
    GenericDialog(
        title = "Permission Denied",
        body = "We do not have the permissions required to connect to your TD-1. Please try again.",
        confirmText = "Try again",
        onDismissRequest = onDismissRequest,
        onConfirmation = onConfirmation
    )
}