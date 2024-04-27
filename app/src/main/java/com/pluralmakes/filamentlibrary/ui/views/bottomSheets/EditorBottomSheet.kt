package com.pluralmakes.filamentlibrary.ui.views.bottomSheets

import androidx.compose.runtime.Composable
import com.pluralmakes.filamentlibrary.model.Filament
import com.pluralmakes.filamentlibrary.model.annotations.Previews
import com.pluralmakes.filamentlibrary.ui.dialogs.BottomSheet
import com.pluralmakes.filamentlibrary.ui.theme.FilamentLibraryTheme
import com.pluralmakes.filamentlibrary.ui.views.editors.Editor

@Composable
fun EditorBottomSheet(
    filament: Filament,
    onFilamentChange: (Filament) -> Unit,
    onDismiss: () -> Unit,
) {
    BottomSheet(
        content = {
            Editor(
                filament = filament,
                onFilamentChange = onFilamentChange,
                onDismiss = onDismiss
            )
        },
        onDismiss = onDismiss,
    )
}

@Previews
@Composable
fun EditorBottomSheetPreview() {
    FilamentLibraryTheme {
        EditorBottomSheet(
            filament = Filament("Overture", "PLA", "Generic", "#00fff7", 1.75f),
            onDismiss = {},
            onFilamentChange = {}
        )
    }
}