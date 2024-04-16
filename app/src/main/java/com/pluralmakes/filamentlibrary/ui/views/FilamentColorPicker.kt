package com.pluralmakes.filamentlibrary.ui.views

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import com.godaddy.android.colorpicker.ClassicColorPicker
import com.godaddy.android.colorpicker.HsvColor

@Composable
fun FilamentColorPicker(
    initialColor: String,
    onColorChange: (String) -> Unit
) {
    var color by remember { mutableStateOf(initialColor) }

    ClassicColorPicker(
        color = HsvColor.from(Color(color.toColorInt())),
        showAlphaBar = false,
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .padding(bottom = 15.dp),
        onColorChanged = {
            color = String.format("#%06X", it.toColor().toArgb())
            onColorChange(color)
        }
    )
}