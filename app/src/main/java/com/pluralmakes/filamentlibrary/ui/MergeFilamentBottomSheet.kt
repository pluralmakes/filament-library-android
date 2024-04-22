package com.pluralmakes.filamentlibrary.ui

import android.content.res.Configuration
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.pluralmakes.filamentlibrary.model.Filament
import com.pluralmakes.filamentlibrary.ui.theme.FilamentLibraryTheme
import com.pluralmakes.filamentlibrary.ui.views.editors.MergeFilamentEditor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MergeFilamentBottomSheet(
    filaments: List<Filament>,
    onMergeFilament: (Filament) -> Unit,
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
        MergeFilamentEditor(
            filaments = filaments,
            onMergeFilament = onMergeFilament,
            onDismiss = onDismiss,
        )
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    name = "Default Preview Dark",
    apiLevel = 33
)
@Composable
fun MergeFilamentBottomSheetPreview() {
    FilamentLibraryTheme {
        MergeFilamentBottomSheet(
            filaments = listOf(Filament("Overture", "PLA", "Generic", "#00fff7", 1.75f)),
            onMergeFilament = {},
            onDismiss = {},
        )
    }
}