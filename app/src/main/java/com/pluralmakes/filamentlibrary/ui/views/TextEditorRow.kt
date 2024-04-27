package com.pluralmakes.filamentlibrary.ui.views

import android.util.Log
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged

@Composable
fun TextEditorRow(
    title: String,
    initialValue: String,
    onValueChange: (String) -> Unit
) {
    var value by remember(initialValue) { mutableStateOf(initialValue) }
    Log.d("TextEditorRow", "TextEditorRow Value: $value")

    EditorRow(title) {
        Log.d("TextEditorRow", "EditorRow Composed")

        TextField(value = value, onValueChange = {
            Log.d("TextEditorRow", "TextEditorRow Value Changed: $it")
            value = it

            onValueChange(it)
        }, modifier = Modifier.onFocusChanged {
            Log.d("TextEditorRow", "TextEditorRow Focus Changed: $value $it")
        })
    }
}