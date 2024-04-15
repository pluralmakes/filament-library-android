package com.pluralmakes.filamentlibrary.util.extensions

fun Number.toTDString(): String {
    return "%.1f".format(this)
}