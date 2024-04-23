package com.pluralmakes.filamentlibrary.ui.views

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.KeyboardType

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