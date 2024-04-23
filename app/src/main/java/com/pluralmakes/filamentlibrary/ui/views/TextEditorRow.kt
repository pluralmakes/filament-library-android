package com.pluralmakes.filamentlibrary.ui.views

import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

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