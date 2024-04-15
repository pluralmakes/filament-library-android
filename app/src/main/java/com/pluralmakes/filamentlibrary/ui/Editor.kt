package com.pluralmakes.filamentlibrary.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import com.godaddy.android.colorpicker.ClassicColorPicker
import com.godaddy.android.colorpicker.HsvColor
import com.pluralmakes.filamentlibrary.model.filamentTypes
import com.pluralmakes.filamentlibrary.ui.theme.FilamentLibraryTheme

@Composable
fun Editor(
    brand: MutableState<String>,
    name: MutableState<String>,
    color: MutableState<String>,
    td: MutableState<Float>,
    type: MutableState<String>
) {
    Column(Modifier.padding(start = 25.dp, end = 25.dp, bottom = 20.dp)) {
        EditorRow("Brand") {
            TextField(value = brand.value, onValueChange = { brand.value = it })
        }

        EditorRow("Name") {
            TextField(value = name.value, onValueChange = { name.value = it })
        }

        EditorRow("TD") {
            TextField(
                value = "${td.value}",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                onValueChange = {
                    val newTD = it.replace(Regex("[^\\d.]"), "")
                    if (newTD.matches(Regex("^-?\\d*(\\.\\d+)?$"))) {
                        td.value = newTD.toFloat()
                    }
                },
            )
        }

        EditorRow {
            LazyRow {
                items(filamentTypes.chunked(3)) { chunk ->
                    Column {
                        chunk.forEach { filamentType ->
                            val isSelected = filamentType == type.value
                            val buttonColors = ButtonDefaults.buttonColors(
                                containerColor = if (isSelected) Color.Gray else Color.Transparent
                            )

                            Button(
                                colors = buttonColors,
                                content = {
                                    Text(
                                        text = filamentType,
                                        textAlign = TextAlign.Center,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                },
                                onClick = {
                                    type.value = filamentType
                                },
                            )
                        }
                    }
                }
            }
        }

        ClassicColorPicker(
            color = HsvColor.from(Color(color.value.toColorInt())),
            showAlphaBar = false,
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .padding(bottom = 15.dp),
            onColorChanged = {
                color.value = String.format("#%06X", it.toColor().toArgb())
            }
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
fun EditorPreview() {
    FilamentLibraryTheme {
        Editor(
            brand = mutableStateOf("Overture"),
            name = mutableStateOf("Generic"),
            color = mutableStateOf("#00fff7"),
            td = mutableStateOf(1.75f),
            type = mutableStateOf("PLA")
        )
    }
}