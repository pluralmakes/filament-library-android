package com.pluralmakes.filamentlibrary.ui.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import com.godaddy.android.colorpicker.ClassicColorPicker
import com.godaddy.android.colorpicker.HsvColor
import com.pluralmakes.filamentlibrary.model.annotations.Previews
import com.pluralmakes.filamentlibrary.ui.theme.FilamentLibraryTheme
import com.pluralmakes.filamentlibrary.util.extensions.toHexCode

@Composable
fun ColorPicker(
    initialColor: String,
    showRGBInput: Boolean = false,
    onColorChange: (String) -> Unit
) {
    val color = remember { mutableStateOf(Color(initialColor.toColorInt())) }
    val hsvColor = HsvColor.from(color.value)

    Column(Modifier.fillMaxWidth()) {
        ClassicColorPicker(
            color = hsvColor,
            showAlphaBar = false,
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .padding(bottom = 15.dp),
            onColorChanged = {
                color.value = it.toColor()
                onColorChange(it.toColor().toHexCode())
            }
        )

        if (showRGBInput) {
            Row(horizontalArrangement = Arrangement.SpaceBetween) {
                RGBField("Red", color.value.red) {
                    color.value = color.value.copy(red = it / 255f)
                    onColorChange(color.value.toHexCode())
                }()

                RGBField("Green", color.value.green) {
                    color.value = color.value.copy(green = it / 255f)
                    onColorChange(color.value.toHexCode())
                }()

                RGBField("Blue", color.value.blue) {
                    color.value = color.value.copy(blue = it / 255f)
                    onColorChange(color.value.toHexCode())
                }()
            }
        }
    }
}

@Composable
fun RGBField(
    label: String,
    value: Float,
    onValueChange: (Int) -> Unit
): @Composable RowScope.() -> Unit {
    // TODO: This isn't properly updating the ClassicColorPicker value
    return {
        TextField(
            modifier = Modifier
                .weight(0.3f)
                .fillMaxWidth()
                .padding(horizontal = 5.dp),
            label = {
                Text(label)
            },
            value = "${(value * 255).toInt()}",
            onValueChange = { it ->
                it.takeIf { it.isNotEmpty() }
                    ?.toIntOrNull()
                    ?.takeIf { it in 0..255 }
                    ?.let { rgb ->
                        onValueChange(rgb)
                    }
            }
        )
    }
}

@Previews
@Composable
fun ColorPickerPreview() {
    FilamentLibraryTheme {
        Surface {
            ColorPicker(initialColor = "#00fff7", showRGBInput = true) {
                // Do nothing
            }
        }
    }
}