package com.pluralmakes.td1_collector.util.extensions

fun Number.toTDString(): String {
    return "%.1f".format(this)
}