package com.pluralmakes.filamentlibrary.ui.views.editors

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pluralmakes.filamentlibrary.model.Filament
import com.pluralmakes.filamentlibrary.model.annotations.Previews
import com.pluralmakes.filamentlibrary.ui.theme.FilamentLibraryTheme
import com.pluralmakes.filamentlibrary.ui.views.ColorPicker
import com.pluralmakes.filamentlibrary.ui.views.PolymerEditorRow
import com.pluralmakes.filamentlibrary.ui.views.TDEditorRow
import com.pluralmakes.filamentlibrary.ui.views.TextEditorRow

@Composable
fun Editor(
    filament: Filament,
    onFilamentChange: (Filament) -> Unit,
    onDismiss: () -> Unit,
) {
    var editingFilament by remember { mutableStateOf(filament) }

    Column(Modifier.padding(start = 25.dp, end = 25.dp, bottom = 20.dp)) {
        TextEditorRow(title = "Brand", initialValue = filament.brand) {
            editingFilament = editingFilament.copy(brand = it)
        }

        TextEditorRow(title = "Name", initialValue = filament.name) {
            editingFilament = editingFilament.copy(name = it)
        }

        TDEditorRow(initialValue = filament.td) {
            editingFilament = editingFilament.copy(td = it)
        }

        PolymerEditorRow(initialValue = filament.type) {
            editingFilament = editingFilament.copy(type = it)
        }

        ColorPicker(initialColor = filament.color) {
            editingFilament = editingFilament.copy(color = it)
        }

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            Button(onClick = {
                onFilamentChange(editingFilament)
                onDismiss()
            }) {
                Text("Save")
            }

            Button(onClick = {
                onDismiss()
            }) {
                Text("Cancel")
            }
        }
    }
}

@Previews
@Composable
fun EditorPreview() {
    FilamentLibraryTheme {
        Editor(
            filament = Filament(
                brand = "Overture",
                name = "Generic",
                color = "#00fff7",
                td = 1.75f,
                type = "PLA"
            ),
            onFilamentChange = {},
            onDismiss = {},
        )
    }
}