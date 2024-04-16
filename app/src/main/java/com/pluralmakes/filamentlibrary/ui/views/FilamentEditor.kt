package com.pluralmakes.filamentlibrary.ui.views

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pluralmakes.filamentlibrary.model.Filament
import com.pluralmakes.filamentlibrary.ui.theme.FilamentLibraryTheme

@Composable
fun FilamentEditor(
    filament: Filament,
    onFilamentChange: (Filament) -> Unit,
) {
    Column(Modifier.padding(start = 25.dp, end = 25.dp, bottom = 20.dp)) {
        TextEditorRow(title = "Brand", initialValue = filament.brand) {
            onFilamentChange(filament.copy(brand = it))
        }

        TextEditorRow(title = "Name", initialValue = filament.name) {
            onFilamentChange(filament.copy(name = it))
        }

        TDEditorRow(initialValue = filament.td) {
            onFilamentChange(filament.copy(td = it))
        }

        PolymerEditorRow(initialValue = filament.type) {
            onFilamentChange(filament.copy(type = it))
        }

        FilamentColorPicker(initialColor = filament.color) {
            onFilamentChange(filament.copy(color = it))
        }
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    name = "Default Preview Dark",
    apiLevel = 33
)
@Composable
fun EditorPreview() {
    FilamentLibraryTheme {
        FilamentEditor(
            filament = Filament(
                brand = "Overture",
                name = "Generic",
                color = "#00fff7",
                td = 1.75f,
                type = "PLA"
            )
        ) {
            // Do nothing
        }
    }
}