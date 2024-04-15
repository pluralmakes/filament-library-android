package com.pluralmakes.filamentlibrary.ui

import android.content.res.Configuration
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import com.pluralmakes.filamentlibrary.model.Filament
import com.pluralmakes.filamentlibrary.ui.theme.FilamentLibraryTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditorBottomSheet(
    filament: Filament,
    onDismiss: (Filament) -> Unit
) {
    val modalBottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val brand = remember { mutableStateOf(filament.brand) }
    val name = remember { mutableStateOf(filament.name) }
    val color = remember { mutableStateOf(filament.color) }
    val td = remember { mutableStateOf(filament.td) }
    val type = remember { mutableStateOf(filament.type) }

    ModalBottomSheet(
        sheetState = modalBottomSheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() },
        onDismissRequest = {
            onDismiss(
                Filament(
                    brand = brand.value,
                    name = name.value,
                    color = color.value,
                    td = td.value,
                    type = type.value
                )
            )
        },
    ) {
        Editor(
            brand, name, color, td, type
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
fun EditorBottomSheetPreview() {
    FilamentLibraryTheme {
        EditorBottomSheet(
            filament = Filament("Overture", "PLA", "Generic", "#00fff7", 1.75f),
            onDismiss = {}
        )
    }
}