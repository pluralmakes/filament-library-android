package com.pluralmakes.filamentlibrary.ui.views.editors

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import com.pluralmakes.filamentlibrary.model.Filament
import com.pluralmakes.filamentlibrary.model.annotations.Previews
import com.pluralmakes.filamentlibrary.ui.dialogs.GenericDialog
import com.pluralmakes.filamentlibrary.ui.theme.FilamentLibraryTheme
import com.pluralmakes.filamentlibrary.ui.views.MergeEditorRow
import com.pluralmakes.filamentlibrary.util.extensions.toRGBString
import com.pluralmakes.filamentlibrary.util.extensions.toTDString

@Composable
fun MergeEditor(
    selectedFilament: List<Filament>,
    onMergeFilament: (Filament) -> Unit,
    onDismiss: () -> Unit,
) {
    val polymers = selectedFilament.map { it.type }.distinct()
    val brands = selectedFilament.map { it.brand }.distinct()
    val colors = selectedFilament.map { it.color }.distinct()
    val names = selectedFilament.map { it.name }.distinct()
    val tds = selectedFilament.map { it.td }.distinct()

    Column(Modifier.padding(start = 25.dp, end = 25.dp, bottom = 20.dp)) {
        val polymer = remember { mutableStateOf(polymers.first()) }
        val brand = remember { mutableStateOf(brands.first()) }
        val color = remember { mutableStateOf(colors.first()) }
        val name = remember { mutableStateOf(names.first()) }
        val td = remember { mutableStateOf(tds.average().toTDString()) }

        BrandView(brands, brand)
        NameView(names, name)
        TDView(tds, td)
        PolymerView(polymers, polymer)
        ColorView(colors, color)

        Footer(onMergeFilament, brand, name, color, td, polymer, onDismiss)
    }
}

@Composable
private fun BrandView(
    brands: List<String>,
    brand: MutableState<String>
) {
    MergeEditorRow(
        title = "Brand",
        values = brands,
        initialValue = brand.value
    ) {
        brand.value = it
    }
}

@Composable
private fun NameView(
    names: List<String>,
    name: MutableState<String>
) {
    MergeEditorRow(
        title = "Name",
        values = names,
        initialValue = name.value
    ) {
        name.value = it
    }
}

@Composable
private fun TDView(
    tds: List<Float>,
    td: MutableState<String>
) {
    MergeEditorRow(
        title = "TD",
        values = tds
            .plus(tds.average())
            .map { it.toTDString() }
            .distinct()
            .sorted(),
        initialValue = td.value
    ) {
        td.value = it
    }
}

@Composable
private fun PolymerView(
    polymers: List<String>,
    polymer: MutableState<String>
) {
    MergeEditorRow(
        title = "Polymer",
        values = polymers,
        initialValue = polymer.value
    ) {
        polymer.value = it
    }
}

@Composable
private fun ColorView(
    colors: List<String>,
    color: MutableState<String>
) {
    MergeEditorRow(
        title = "Color",
        values = colors,
        initialValue = color.value,
        valueContent = {
            Text(
                text = "$it ${it.toColorInt().toRGBString()}",
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )

            Box(
                Modifier
                    .height(15.dp)
                    .padding(end = 5.dp)
                    .fillMaxWidth()
                    .background(Color(it.toColorInt()))
            )
        }
    ) {
        color.value = it
    }
}

@Composable
private fun Footer(
    onMergeFilament: (Filament) -> Unit,
    brand: MutableState<String>,
    name: MutableState<String>,
    color: MutableState<String>,
    td: MutableState<String>,
    polymer: MutableState<String>,
    onDismiss: () -> Unit
) {
    var showConfirmDialog by remember { mutableStateOf(false) }
    if (showConfirmDialog) {
        GenericDialog(
            title = "Merge Filaments",
            body = "Are you sure you want to merge these filaments into a new one?",
            onDismissRequest = {
                showConfirmDialog = false
            },
            confirmText = "Merge",
            onConfirmation = {
                showConfirmDialog = false
                onMergeFilament(
                    Filament(
                        brand = brand.value,
                        name = name.value,
                        color = color.value,
                        td = td.value.toFloat(),
                        type = polymer.value
                    )
                )

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
            Text("Merge")
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
fun MergePreview() {
    FilamentLibraryTheme {
        Surface {
            MergeEditor(
                selectedFilament = listOf(
                    Filament(
                        brand = "Overture",
                        name = "Generic",
                        color = "#00fff7",
                        td = 1.75f,
                        type = "PLA"
                    )
                ),
                onMergeFilament = {},
                onDismiss = {},
            )
        }
    }
}