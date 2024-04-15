package com.pluralmakes.filamentlibrary.ui.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.pluralmakes.filamentlibrary.R
import com.pluralmakes.filamentlibrary.util.ConnectionStatus
import com.pluralmakes.filamentlibrary.util.ConnectionStatus.*

@Composable
fun TD1FloatingButton(
    connectionStatus: MutableState<ConnectionStatus>,
    onConnectClick: (ConnectionStatus) -> Unit,
    onExportClick: () -> Unit,
) {
    if (connectionStatus.value == CONNECTING) {
        FloatingActionButton(onClick = {}) {
            CircularProgressIndicator()
        }
    } else {
        ExpandableFloatingActionButton(
            icon = ImageVector.vectorResource(id = R.drawable.settings_cog),
            label = "Settings",
            barColor = connectionStatus.value.toColor(),
            content = {
                Column {
                    FABRow(when (connectionStatus.value) {
                        CONNECTED -> "Disconnect from TD-1"
                        else -> "Connect to TD-1"
                    }) {
                        onConnectClick(connectionStatus.value)
                    }

                    FABRow("Export Filaments") {
                        onExportClick()
                    }
                }
            }
        )
    }
}

@Composable
fun FABRow(
    text: String,
    onClick: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .padding(10.dp)
            .clickable { onClick?.invoke() },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text)
    }
}

