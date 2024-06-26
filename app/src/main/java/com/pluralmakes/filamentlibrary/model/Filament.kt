package com.pluralmakes.filamentlibrary.model

import androidx.compose.ui.graphics.Color
import androidx.core.graphics.toColorInt
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import kotlin.random.Random

val filamentTypes = arrayListOf(
    "PLA",
    "ABS",
    "PETG",
    "TPU",
    "Nylon",
    "PC",
    "HIPS",
    "PVA",
    "ASA",
    "PP",
    "PEEK",
    "PEI",
    "POM",
    "PLA+",
    "ABS+",
    "PETG+",
    "TPU+",
    "Nylon+",
    "PC+",
    "HIPS+",
    "PVA+",
    "ASA+",
    "PP+",
    "PEEK+",
    "PEI+",
    "POM+"
)

data class Filament (
    @SerializedName("Brand")
    var brand: String,

    @SerializedName("Type")
    var type: String,

    @SerializedName("Name")
    var name: String,

    @SerializedName("Color")
    var color: String,

    @SerializedName("Transmissivity")
    var td: Float,

    @SerializedName("Owned")
    val owned: Boolean = true,
): Serializable {
    fun getComposeColor(): Color {
        return Color(color.toColorInt())
    }
}

/**
 * Organizes the filament into a group of polymers, then brands and finally a list of filament
 */
fun organizeFilamentForDisplay(
    filaments: MutableList<Filament>
): List<Pair<String, List<Pair<String, List<List<Filament>>>>>> {
    return filaments
        .groupBy { it.type }
        .mapValues { filament -> filament.value
            .groupBy { it.brand }
            .mapValues { it.value.chunked(3) }
            .toList().sortedBy { it.first } }
        .toList()
        .sortedBy { it.first }
}

fun generateRandomFilaments(
    count: Int = 5
): List<Filament> {
    val filaments = mutableListOf<Filament>()
    val brands = listOf("Overture", "Polymaker", "Prusament")
    val types = filamentTypes
    val names = listOf("Name1", "Name2", "Name3")

    for (i in 1..count) {
        val brand = brands[Random.nextInt(brands.size)]
        val type = types[Random.nextInt(types.size)]
        val name = names[Random.nextInt(names.size)]
        val color = String.format("#%06X", Random.nextInt(0xFFFFFF + 1))
        val td = Random.nextFloat()

        filaments.add(Filament(brand, type, name, color, td))
    }

    return filaments
}