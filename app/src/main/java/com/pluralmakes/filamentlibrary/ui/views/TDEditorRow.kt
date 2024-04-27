package com.pluralmakes.filamentlibrary.ui.views

import androidx.compose.runtime.Composable
import com.pluralmakes.filamentlibrary.ui.views.input.TDField

@Composable
fun TDEditorRow(
    initialValue: Float,
    onValueChange: (Float) -> Unit
) {
    EditorRow("TD") {
        TDField(
            initialTD = initialValue,
            onTDChange = onValueChange
        )
    }
}

