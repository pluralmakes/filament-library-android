package com.pluralmakes.filamentlibrary.ui.views.bottomSheets

import androidx.compose.runtime.Composable
import com.pluralmakes.filamentlibrary.model.Filament
import com.pluralmakes.filamentlibrary.ui.dialogs.BottomSheet
import com.pluralmakes.filamentlibrary.ui.views.editors.MergeEditor

@Composable
fun LibraryMerge(
    selectedFilament: List<Filament>,
    onMergeFilament: (Filament) -> Unit,
    onDismiss: () -> Unit,
) {
    BottomSheet(
        content = {
            MergeEditor(
                selectedFilament = selectedFilament,
                onMergeFilament = onMergeFilament,
                onDismiss = onDismiss,
            )
        },
        onDismiss = onDismiss,
    )
}