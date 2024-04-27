package com.pluralmakes.filamentlibrary.ui.views

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pluralmakes.filamentlibrary.model.Filament
import com.pluralmakes.filamentlibrary.model.organizeFilamentForDisplay
import com.pluralmakes.filamentlibrary.util.extensions.toTDString

@Composable
fun FilamentListView(
    modifier: Modifier = Modifier,
    unorganizedFilament: MutableList<Filament>,
    bulkSelectedFilament: MutableList<Filament>,
    onSelect: (filament: Filament, longPress: Boolean) -> Unit,
) {
    Column(modifier) {
        val groupedFilaments = organizeFilamentForDisplay(unorganizedFilament)

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
        ) {
            groupedFilaments.forEach { polymer ->
                item {
                    PolymerView(polymer.first)
                }

                polymer.second.forEach { brand ->
                    item {
                        BrandView(brand.first)
                    }

                    item {
                        Spacer(modifier = Modifier.height(5.dp))
                    }

                    brand.second.forEach { filament ->
                        item {
                            FilamentGrid(
                                filament = filament,
                                onSelect = onSelect,
                                bulkSelectedFilament = bulkSelectedFilament,
                            )
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(5.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun PolymerView(
    polymer: String
) {
    Text(
        text = polymer,
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Gray.copy(alpha = 0.5f))
    )
}

@Composable
private fun BrandView(
    brand: String
) {
    Text( // Brand
        text = brand,
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Gray.copy(alpha = 0.15f))
    )
}

@Composable
@OptIn(ExperimentalFoundationApi::class)
private fun ColumnScope.FilamentGrid(
    filament: List<Filament>,
    bulkSelectedFilament: MutableList<Filament>,
    onSelect: (filament: Filament, longPress: Boolean) -> Unit
) {
    LazyRow(modifier = Modifier.fillMaxWidth()) {
        items(filament) { filament ->
            val isSelected = bulkSelectedFilament.contains(filament)

            Column(
                Modifier.Companion
                    .weight(0.3f)
                    .fillParentMaxWidth(1f / 3f)
                    .combinedClickable(
                        onClick = {
                            onSelect(filament, false)
                        },
                        onLongClick = {
                            onSelect(filament, true)
                        }
                    )
                    .background(if (isSelected) Color.Gray.copy(alpha = 0.2f) else Color.Transparent)
            ) {
                FilamentView(filament)
            }
        }
    }
}

@Composable
private fun ColumnScope.FilamentView(
    filament: Filament
) {
    Box(
        Modifier
            .size(100.dp)
            .align(Alignment.CenterHorizontally)
    ) {
        Canvas(modifier = Modifier.size(100.dp), onDraw = {
            val strokeWidth = 10f
            drawCircle(
                color = filament.getComposeColor(),
                radius = 125f,
                style = Stroke(
                    width = strokeWidth,
                )
            )
        })

        Text(
            text = filament.td.toTDString(),
            modifier = Modifier.align(Alignment.Center),
            fontWeight = FontWeight.Black,
            fontSize = 20.sp
        )
    }

    Text(
        text = filament.name,
        modifier = Modifier.fillMaxWidth(),
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        textAlign = TextAlign.Center,
    )
}