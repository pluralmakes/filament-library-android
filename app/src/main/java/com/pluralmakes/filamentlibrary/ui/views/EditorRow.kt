package com.pluralmakes.filamentlibrary.ui.views

import androidx.annotation.ColorInt
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
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
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.pluralmakes.filamentlibrary.model.filamentTypes

@Composable
fun TextEditorRow(
    title: String,
    initialValue: String,
    onValueChange: (String) -> Unit
) {
    var value by remember { mutableStateOf(initialValue) }

    EditorRow(title) {
        TextField(value = value, onValueChange = {
            value = it

            onValueChange(it)
        })
    }
}

@Composable
fun TDEditorRow(
    initialValue: Float,
    onValueChange: (Float) -> Unit
) {
    var td by remember { mutableStateOf(initialValue) }
    
    EditorRow("TD") {
        TextField(
            value = "$td",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            onValueChange = {
                val newTD = it.replace(Regex("[^\\d.]"), "")
                if (newTD.matches(Regex("^-?\\d*(\\.\\d+)?$"))) {
                    td = newTD.toFloat()
                    onValueChange(td)
                }
            },
        )
    }
}

@Composable
fun PolymerEditorRow(
    initialValue: String,
    onValueChange: (String) -> Unit
) {
    var polymer by remember { mutableStateOf(initialValue) }

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

@Composable
fun EditorRow(
    title: String? = null,
    @ColorInt color: Int? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(bottom = 15.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (title != null || color != null) {
            Column(
                Modifier
                    .weight(0.2f)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                title?.let {
                    Text(it)
                }

                color?.let {
                    Box(
                        Modifier
                            .height(15.dp)
                            .padding(end = 5.dp)
                            .fillMaxWidth()
                            .background(Color(it))
                    )
                }
            }
        }

        Column(
            Modifier
                .weight(if (title == null) 0.8f else 1.0f)
                .fillMaxWidth(),
            content = content
        )
    }
}

@Composable
fun <T> MergeEditorRow(
    title: String,
    values: List<T>,
    initialValue: T,
    valueContent: @Composable (ColumnScope.(T) -> Unit)? = null,
    onValueChange: (T) -> Unit,
) {
    var showOptionSelection by remember { mutableStateOf(false) }
    var value by remember { mutableStateOf(initialValue) }

    Column {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(bottom = 15.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                Modifier
                    .weight(0.5f)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(title)
            }

            Column(
                Modifier
                    .weight(1.0f)
                    .fillMaxWidth()
                    .clickable {
                        if (values.size > 1) {
                            showOptionSelection = !showOptionSelection
                        }
                    }
            ) {
                if (valueContent != null) {
                    valueContent(value)
                } else {
                    Text(
                        text = "$value",
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        if (showOptionSelection && values.size > 1) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(bottom = 15.dp)
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
    }
}