package com.pluralmakes.filamentlibrary.ui.dialogs

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.pluralStringResource
import com.pluralmakes.filamentlibrary.R

@Composable
fun LibraryBulkDelete(
    numOfFilamentToDelete: Int,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    GenericDialog(
        title = "Delete Filament",
        body = pluralStringResource(
            id = R.plurals.bulk_delete_confirm,
            count = numOfFilamentToDelete,
            numOfFilamentToDelete
        ),
        onDismissRequest = onDismiss,
        onConfirmation = onConfirm,
    )
}