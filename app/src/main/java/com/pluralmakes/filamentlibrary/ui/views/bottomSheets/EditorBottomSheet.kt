package com.pluralmakes.filamentlibrary.ui.views.bottomSheets

import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import com.pluralmakes.filamentlibrary.model.Filament
import com.pluralmakes.filamentlibrary.model.annotations.Previews
import com.pluralmakes.filamentlibrary.ui.theme.FilamentLibraryTheme
import com.pluralmakes.filamentlibrary.ui.views.editors.Editor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditorBottomSheet(
    filament: Filament,
    onFilamentChange: (Filament) -> Unit,
    onDismiss: () -> Unit,
) {
    val modalBottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        sheetState = modalBottomSheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() },
        onDismissRequest = {
            onDismiss()
        },
    ) {
        Editor(
            filament,
            onFilamentChange,
            onDismiss
        )
    }
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