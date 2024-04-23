package com.pluralmakes.filamentlibrary.ui.pages.collector

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.pluralmakes.filamentlibrary.R

@Composable
fun CollectorHeader(
    numSelected: Int,
    onClickEdit: () -> Unit,
    onClickMerge: () -> Unit,
    onClickDelete: () -> Unit,
) {
    Row(Modifier.padding(5.dp), verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = "$numSelected selected",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
        )

        Spacer(
            Modifier
                .weight(1f)
                .fillMaxWidth()
        )

        Button(onClickEdit) {
            Image(
                painter = painterResource(id = R.drawable.edit),
                contentDescription = "Edit"
            )
        }

        Spacer(Modifier.padding(horizontal = 2.5.dp))

        Button(onClickMerge) {
            Image(
                painter = painterResource(id = R.drawable.merge),
                contentDescription = "Merge"
            )
        }

        Spacer(Modifier.padding(horizontal = 2.5.dp))

        Button(onClickDelete) {
            Image(
                painter = painterResource(id = R.drawable.delete),
                contentDescription = "Delete"
            )
        }
    }
}