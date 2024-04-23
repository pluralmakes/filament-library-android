package com.pluralmakes.filamentlibrary.ui.views.bottomSheets

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import com.pluralmakes.filamentlibrary.model.Filament
import com.pluralmakes.filamentlibrary.ui.dialogs.BottomSheet
import com.pluralmakes.filamentlibrary.ui.views.editors.BulkEditCallback
import com.pluralmakes.filamentlibrary.ui.views.editors.BulkEditor

@Composable
fun BulkEditorBottomSheet(
    filamentsToEdit: List<Filament>,
    filamentsToUseForSuggestions: List<Filament> = filamentsToEdit,
    onEditFilament: (BulkEditCallback) -> Unit,
    onDismiss: () -> Unit,
) {
    BottomSheet(
        content = {
            BulkEditor(
                filamentsToEdit = filamentsToEdit,
                filamentsToUseForSuggestions = filamentsToUseForSuggestions,
                onEditFilament = onEditFilament,
                onDismiss = onDismiss,
            )
        },
        onDismiss = onDismiss,
    )
}