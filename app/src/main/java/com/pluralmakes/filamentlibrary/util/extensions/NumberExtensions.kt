package com.pluralmakes.filamentlibrary.util.extensions

import androidx.compose.ui.graphics.Color

fun Number.toTDString(): String {
    return "%.1f".format(this)
}

fun Int.toRGBString(): String {
    val color = Color(this)
    val r = (color.red * 255).toInt()
    val g = (color.green * 255).toInt()
    val b = (color.blue * 255).toInt()

    return "($r, $g, $b)"
}