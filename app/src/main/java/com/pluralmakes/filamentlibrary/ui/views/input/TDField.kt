package com.pluralmakes.filamentlibrary.ui.views.input

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.KeyboardType
import com.pluralmakes.filamentlibrary.util.extensions.toTDString

@Suppress("PrivatePropertyName")
private val TD_REGEX : Regex = Regex("^[0-9]{0,500}.?[0-9]?\$")

@Composable
fun TDField(
    initialTD: Float,
    onTDChange: (Float) -> Unit,
) {
    var text by remember { mutableStateOf(initialTD.toTDString()) }

    TextField(
        value = text,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        onValueChange = {
            if (it.isEmpty() || it.matches(TD_REGEX)) {
                text = it

                val output = if (it.isEmpty()) {
                    0f
                } else if (it.endsWith(".")) {
                    it.dropLast(1).toFloat()
                } else {
                    it.toFloat()
                }

                onTDChange(output)
            }
        }
    )
}