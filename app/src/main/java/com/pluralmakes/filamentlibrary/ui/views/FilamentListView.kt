package com.pluralmakes.filamentlibrary.ui.views

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pluralmakes.filamentlibrary.model.Filament
import com.pluralmakes.filamentlibrary.model.annotations.Previews
import com.pluralmakes.filamentlibrary.model.generateRandomFilaments
import com.pluralmakes.filamentlibrary.ui.theme.FilamentLibraryTheme
import com.pluralmakes.filamentlibrary.util.extensions.toTDString

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FilamentListView(
    filaments: MutableList<Filament>,
    selectedIndexes: MutableList<Int>,
    modifier: Modifier = Modifier,
    onBulkSelect: (multiSelect: Boolean, Int) -> Unit,
) {
    Column(modifier) {
        val groupedFilaments = filaments
            .groupBy { it.type }
            .mapValues { filament -> filament.value
                .groupBy { it.brand }
                .mapValues { it.value.chunked(3) }
                .toList().sortedBy { it.first } }
            .toList()
            .sortedBy { it.first }

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
        ) {
            groupedFilaments.forEach { type ->
                item {
                    Text( // Type
                        text = type.first,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.Gray.copy(alpha = 0.5f))
                    )
                }

                type.second.forEach { brand ->
                    item {
                        Text( // Brand
                            text = brand.first,
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.Gray.copy(alpha = 0.15f))
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(5.dp))
                    }

                    brand.second.forEach {
                        item {
                            LazyRow(modifier = Modifier.fillMaxWidth()) {
                                items(it) { filament ->
                                    val index = filaments.indexOf(filament)
                                    val isSelected = selectedIndexes.contains(index)

                                    Column(
                                        Modifier
                                            .weight(0.3f)
                                            .fillParentMaxWidth(1f / 3f)
                                            .combinedClickable(
                                                onClick = {
                                                    onBulkSelect(isSelected, index)
                                                },
                                                onLongClick = {
                                                    onBulkSelect(true, index)
                                                }
                                            )
                                            .background(if (isSelected) Color.Gray.copy(alpha = 0.2f) else Color.Transparent)
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
                                }
                            }
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

@Previews
@Composable
fun FilamentListViewPreview() {
    FilamentLibraryTheme {
        Surface {
            FilamentListView(
                generateRandomFilaments().toMutableStateList(),
                mutableListOf(),
                onBulkSelect = { _, _ -> },
            )
        }
    }
}