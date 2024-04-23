package com.pluralmakes.filamentlibrary.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import com.pluralmakes.filamentlibrary.ui.theme.FilamentLibraryTheme
import com.pluralmakes.filamentlibrary.util.extensions.toHexCode

@Composable
inline fun <reified T: Any> BulkEditorRow(
    title: String,
    values: List<T>? = null,
    defaultValue: T? = null,
    selectedValue: T? = null,
    noinline onValueChange: (T?) -> Unit,
    noinline validateValue: ((String) -> Boolean)? = null,
) {
    var value by remember { mutableStateOf(selectedValue) }
    val isColor = T::class == Color::class

    Column {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(bottom = 15.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = title,
                modifier = Modifier
                    .weight(0.35f)
                    .fillMaxWidth()
                    .padding(end = 5.dp),
                textAlign = TextAlign.End
            )

            Column(
                Modifier
                    .weight(1.0f)
                    .fillMaxWidth()
            ) {
                when (value) {
                    null -> {
                        TextButton(
                            onClick = {
                                value = (values?.first() ?: defaultValue)
                                onValueChange(value)
                            }
                        ) {
                            Text("Unchanged", fontStyle = FontStyle.Italic)
                        }
                    }
                    else -> {
                        if (isColor) {
                            Box(
                                Modifier
                                    .height(25.dp)
                                    .padding(end = 5.dp)
                                    .fillMaxWidth()
                                    .background(value as Color)
                            )
                        } else {
                            TextField(
                                value = "$value",
                                onValueChange = {
                                    if (validateValue?.invoke(it) != false) {
                                        value = when (T::class) {
                                            String::class -> it as T
                                            Float::class -> it.toFloatOrNull() as T
                                            else -> throw IllegalArgumentException("Unsupported type")
                                        }

                                        onValueChange(value)
                                    }
                                },
                                keyboardOptions = when (T::class) {
                                    Float::class -> KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                                    else -> KeyboardOptions.Default
                                }
                            )
                        }
                    }
                }
            }

            if (value != null) {
                TextButton(
                    modifier = Modifier
                        .padding(horizontal = 5.dp),
                    onClick = {
                        value = null
                        onValueChange(null)
                    }
                ) {
                    Text("X")
                }
            }
        }

        values?.takeIf { value != null && it.size > 1 }?.let {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(bottom = 5.dp)
            ) {
                HorizontalDivider(Modifier.fillMaxWidth(), 1.dp)

                LazyRow {
                    items(values) {
                        val isSelected = it == value
                        val buttonColors = ButtonDefaults.buttonColors(
                            containerColor = if (isSelected) Color.Gray else Color.Transparent
                        )

                        Button(
                            colors = buttonColors,
                            content = {
                                Text(
                                    text = "$it",
                                    textAlign = TextAlign.Center,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            },
                            onClick = {
                                value = it
                                onValueChange(it)
                            },
                        )
                    }
                }

                HorizontalDivider(Modifier.fillMaxWidth(), 1.dp)
            }
        }

        if (value != null && isColor) {
            ColorPicker(
                initialColor = (value as Color).toHexCode(),
                onColorChange = {
                    value = Color(it.toColorInt()) as T
                    onValueChange(value)
                }
            )
        }
    }
}

@Preview(apiLevel = 33)
@Composable
fun BulkEditorPreview() {
    FilamentLibraryTheme {
        Surface {
            BulkEditorRow(
                title = "Abc",
                selectedValue = null,
                defaultValue = "",
                values = listOf("Overture", "Polymaker"),
                onValueChange = {
                    // Do nothing
                }
            )
        }
    }
}

@Preview(apiLevel = 33)
@Composable
fun BulkEditorPreview1() {
    FilamentLibraryTheme {
        Surface {
            BulkEditorRow(
                title = "Abc",
                selectedValue = "Overture",
                defaultValue = "",
                values = listOf("Overture", "Polymaker"),
                onValueChange = {
                    // Do nothing
                }
            )
        }
    }
}