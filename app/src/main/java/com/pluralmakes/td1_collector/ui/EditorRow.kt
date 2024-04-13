package com.pluralmakes.td1_collector.ui

import androidx.annotation.ColorInt
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun EditorRow(
    title: String? = null,
    @ColorInt color: Int? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(bottom = 15.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (title != null || color != null) {
            Column(
                Modifier
                    .weight(0.2f)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                title?.let {
                    Text(it)
                }

                color?.let {
                    Box(
                        Modifier.height(15.dp).padding(end = 5.dp).fillMaxWidth()
                            .background(Color(it))
                    )
                }
            }
        }

        Column(
            Modifier
                .weight(if (title == null) 0.8f else 1.0f)
                .fillMaxWidth(),
            content = content
        )
    }
}