package com.pluralmakes.filamentlibrary.ui.views.editors

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.pluralmakes.filamentlibrary.R
import com.pluralmakes.filamentlibrary.model.Filament
import com.pluralmakes.filamentlibrary.model.annotations.Previews
import com.pluralmakes.filamentlibrary.model.generateRandomFilaments
import com.pluralmakes.filamentlibrary.ui.dialogs.GenericDialog
import com.pluralmakes.filamentlibrary.ui.theme.FilamentLibraryTheme
import com.pluralmakes.filamentlibrary.ui.views.BulkEditorRow
import com.pluralmakes.filamentlibrary.util.extensions.toHexCode
import com.pluralmakes.filamentlibrary.util.extensions.toTDString

data class BulkEditCallback(
    val polymer: String?,
    val brand: String?,
    val color: String?,
    val name: String?,
    val td: Float?,
)

@Composable
fun BulkEditor(
    filamentsToEdit: List<Filament>,
    filamentsToUseForSuggestions: List<Filament> = filamentsToEdit,
    onEditFilament: (BulkEditCallback) -> Unit,
    onDismiss: () -> Unit,
) {
    val polymers = filamentsToUseForSuggestions.map { it.type }.distinct().sorted()
    val brands = filamentsToUseForSuggestions.map { it.brand }.distinct().sorted()
    val names = filamentsToUseForSuggestions.map { it.name }.distinct().sorted()

    Column(Modifier.padding(start = 25.dp, end = 25.dp, bottom = 20.dp)) {
        val polymer = remember { mutableStateOf<String?>(null) }
        val brand = remember { mutableStateOf<String?>(null) }
        val color = remember { mutableStateOf<String?>(null) }
        val name = remember { mutableStateOf<String?>(null) }
        val td = remember { mutableStateOf<Float?>(null) }

        Text(
            text = pluralStringResource(
                id = R.plurals.bulk_edit_title,
                count = filamentsToEdit.size,
                filamentsToEdit.size
            ),
            textAlign = TextAlign.Center,
            fontStyle = FontStyle.Italic,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 5.dp)
        )

        HorizontalDivider(Modifier.fillMaxWidth().padding(bottom = 5.dp), 1.dp)

        BulkEditorRow(
            title = "Brand",
            values = brands,
            onValueChange = {
                brand.value = it
            }
        )

        BulkEditorRow(
            title = "Name",
            values = names,
            onValueChange = {
                name.value = it
            }
        )

        BulkEditorRow(
            title = "TD",
            values = null,
            defaultValue = 0f,
            validateValue = {
                it.toFloatOrNull() != null
            },
            onValueChange = {
                td.value = it
            }
        )

        BulkEditorRow(
            title = "Polymer",
            values = polymers,
            onValueChange = {
                polymer.value = it
            }
        )

        BulkEditorRow(
            title = "Color",
            values = null,
            defaultValue = Color.White,
            onValueChange = {
                color.value = when (it) {
                    null -> null
                    else -> it.toHexCode()
                }
            }
        )

        Footer(
            bulkEdit = BulkEditCallback(
                polymer = polymer.value,
                brand = brand.value,
                color = color.value,
                name = name.value,
                td = td.value
            ),
            onDismiss = onDismiss,
            onEditFilament = onEditFilament,
        )
    }
}

@Composable
private fun Footer(
    bulkEdit: BulkEditCallback,
    onEditFilament: (BulkEditCallback) -> Unit,
    onDismiss: () -> Unit
) {
    var showConfirmDialog by remember { mutableStateOf(false) }
    if (showConfirmDialog) {
        GenericDialog(
            title = "Bulk Edit",
            body = StringBuilder("Are you sure you want to apply this changes to these filaments?\n\n").apply {
                bulkEdit.brand?.let { this.append("Brand: $it\n") }
                bulkEdit.name?.let { this.append("Name: $it\n") }
                bulkEdit.color?.let { this.append("Color: $it\n") }
                bulkEdit.td?.let { this.append("TD: ${it.toTDString()}\n") }
                bulkEdit.polymer?.let { this.append("Polymer: $it") }
            }.toString(),
            onDismissRequest = {
                showConfirmDialog = false
            },
            onConfirmation = {
                showConfirmDialog = false
                onEditFilament(bulkEdit)
                onDismiss()
            }
        )
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Button(onClick = {
            showConfirmDialog = true
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

@Previews
@Composable
fun BulkEditor() {
    FilamentLibraryTheme {
        Surface {
            BulkEditor(
                filamentsToEdit = generateRandomFilaments(),
                onEditFilament = { _ -> },
                onDismiss = {},
            )
        }
    }
}