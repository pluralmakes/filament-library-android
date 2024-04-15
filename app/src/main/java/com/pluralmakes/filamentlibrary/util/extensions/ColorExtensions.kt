package com.pluralmakes.filamentlibrary.util.extensions

import androidx.compose.ui.graphics.Color

fun Color.isLight(): Boolean {
    val luminance = 0.299 * red + 0.587 * green + 0.114 * blue

    return luminance > 0.5
}