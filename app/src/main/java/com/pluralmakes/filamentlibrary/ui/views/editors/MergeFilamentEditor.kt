package com.pluralmakes.filamentlibrary.ui.views.editors

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import com.pluralmakes.filamentlibrary.model.Filament
import com.pluralmakes.filamentlibrary.ui.theme.FilamentLibraryTheme
import com.pluralmakes.filamentlibrary.ui.views.MergeEditorRow
import com.pluralmakes.filamentlibrary.util.extensions.toRGBString
import com.pluralmakes.filamentlibrary.util.extensions.toTDString

@Composable
fun MergeFilamentEditor(
    filaments: List<Filament>,
    onMergeFilament: (Filament) -> Unit,
    onDismiss: () -> Unit,
) {
    Column(Modifier.padding(start = 25.dp, end = 25.dp, bottom = 20.dp)) {
        val brands = filaments.map { it.brand }.distinct()
        var brand by remember { mutableStateOf(brands.first()) }

        MergeEditorRow(
            title = "Brand",
            values = brands,
            initialValue = brand
        ) {
            brand = it
        }

        val names = filaments.map { it.name }.distinct()
        var name by remember { mutableStateOf(names.first()) }

        MergeEditorRow(
            title = "Name",
            values = names,
            initialValue = name
        ) {
            name = it
        }

        val tds = filaments.map { it.td }.distinct()
        var td by remember { mutableStateOf(tds.average().toTDString()) }
        
        MergeEditorRow(
            title = "TD",
            values = tds
                .plus(tds.average())
                .map { it.toTDString() }
                .distinct()
                .sorted(),
            initialValue = td
        ) {
            td = it
        }

        val polymers = filaments.map { it.type }.distinct()
        var polymer by remember { mutableStateOf(polymers.first()) }

        MergeEditorRow(
            title = "Polymer",
            values = polymers,
            initialValue = polymer
        ) {
            polymer = it
        }

        val colors = filaments.map { it.color }.distinct()
        var color by remember { mutableStateOf(colors.first()) }

        MergeEditorRow(
            title = "Color",
            values = colors,
            initialValue = color,
            valueContent = {
                Text(
                    text = "$it ${it.toColorInt().toRGBString()}",
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }
        ) {
            color = it
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = {
                onMergeFilament(
                    Filament(
                        brand = brand,
                        name = name,
                        color = color,
                        td = td.toFloat(),
                        type = polymer
                    )
                )

                onDismiss()
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
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    name = "Default Preview Dark",
    device = Devices.PIXEL_4_XL,
    showBackground = true,
    showSystemUi = true,
    apiLevel = 33,
)
@Composable
fun MergePreview() {
    FilamentLibraryTheme {
        MergeFilamentEditor(
            filaments = listOf(Filament(
                brand = "Overture",
                name = "Generic",
                color = "#00fff7",
                td = 1.75f,
                type = "PLA"
            )),
            onMergeFilament = {},
            onDismiss = {},
        )
    }
}