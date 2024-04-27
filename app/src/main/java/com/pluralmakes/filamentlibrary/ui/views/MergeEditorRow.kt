package com.pluralmakes.filamentlibrary.ui.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

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