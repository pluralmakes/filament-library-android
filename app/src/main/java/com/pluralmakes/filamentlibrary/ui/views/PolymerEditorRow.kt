package com.pluralmakes.filamentlibrary.ui.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import com.pluralmakes.filamentlibrary.model.filamentTypes

@Composable
fun PolymerEditorRow(
    initialValue: String,
    onValueChange: (String) -> Unit
) {
    var polymer by remember(initialValue) { mutableStateOf(initialValue) }

    EditorRow {
        LazyRow {
            items(filamentTypes.chunked(3)) { chunk ->
                Column {
                    chunk.forEach { filamentType ->
                        val isSelected = filamentType == polymer
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
                                polymer = filamentType
                                onValueChange(polymer)
                            },
                        )
                    }
                }
            }
        }
    }
}