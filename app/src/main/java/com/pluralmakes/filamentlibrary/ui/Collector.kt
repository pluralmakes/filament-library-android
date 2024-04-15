package com.pluralmakes.filamentlibrary.ui

import android.content.IntentFilter
import android.content.res.Configuration
import android.os.Build
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pluralmakes.filamentlibrary.model.generateRandomFilaments
import com.pluralmakes.filamentlibrary.ui.theme.FilamentLibraryTheme
import com.pluralmakes.filamentlibrary.util.ConnectionStatus.*
import com.pluralmakes.filamentlibrary.util.impl.ACTION_USB_PERMISSION
import com.pluralmakes.filamentlibrary.util.impl.PermissionReceiver
import com.pluralmakes.filamentlibrary.util.impl.TD1CommunicatorTestImpl
import com.pluralmakes.filamentlibrary.viewModel.CollectorViewModel
import kotlinx.coroutines.launch

@Composable
fun Collector(
    viewModel: CollectorViewModel
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    val receiver = PermissionReceiver {
        if (it) {
            coroutineScope.launch {
                viewModel.connect()
            }
        } else {
            viewModel.connectionStatus.value = PERMISSION_DENIED
        }
    }

    DisposableEffect(context) {
        context.registerReceiver(
            receiver,
            IntentFilter(ACTION_USB_PERMISSION),
            when (Build.VERSION.SDK_INT) {
                in 0..33 -> 0
                else -> 0x2
            }
        )

        onDispose {
            context.unregisterReceiver(receiver)
            viewModel.disconnect()
        }
    }

    Scaffold(
        floatingActionButton = {
            Row {
                TD1FloatingButton(
                    connectionStatus = viewModel.connectionStatus
                ) {
                    when (it) {
                        DISCONNECTED, NONE -> {
                            coroutineScope.launch {
                                viewModel.connect()
                            }
                        }

                        CONNECTED -> viewModel.disconnect()
                        else -> {
                            //TODO: Implement other options
                        }
                    }
                }

                FloatingActionButton(
                    modifier = Modifier.padding(horizontal = 10.dp),
                    onClick = { viewModel.export(context) }
                ) {
                    Text("Save") //TODO Replace with proper icon
                }
            }
        }
    ) {
        FilamentListView(
            filaments = viewModel.filaments,
            modifier = Modifier.padding(it)
        ) { index ->
            viewModel.selectedIndex.value = viewModel.filaments.indexOf(index)
        }
    }

    viewModel.selectedIndex.value?.takeIf { it >= 0 && it <= viewModel.filaments.size }
        ?.let { index ->
            EditorBottomSheet(filament = viewModel.filaments[index], onDismiss = { filament ->
                viewModel.filaments[index] = filament
                viewModel.selectedIndex.value = null
            })
        }
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    apiLevel = 33,
)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    apiLevel = 33,
)
@Composable
fun CollectorPreview() {
    FilamentLibraryTheme {
        Surface {
            val communicator = TD1CommunicatorTestImpl()
            val viewModel = CollectorViewModel(
                communicator = communicator,
                filaments = generateRandomFilaments().toMutableList()
            )

            Collector(viewModel)
        }
    }
}