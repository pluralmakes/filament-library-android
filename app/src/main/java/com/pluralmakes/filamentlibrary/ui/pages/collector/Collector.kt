package com.pluralmakes.filamentlibrary.ui.pages.collector

import android.content.res.Configuration
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.pluralmakes.filamentlibrary.model.generateRandomFilaments
import com.pluralmakes.filamentlibrary.model.receivers.CompPermissionReceiver
import com.pluralmakes.filamentlibrary.model.viewModel.CollectorViewModel
import com.pluralmakes.filamentlibrary.ui.theme.FilamentLibraryTheme
import com.pluralmakes.filamentlibrary.ui.views.CollectorScaffold
import com.pluralmakes.filamentlibrary.ui.views.bottomSheets.EditorBottomSheet
import com.pluralmakes.filamentlibrary.util.ConnectionStatus
import com.pluralmakes.filamentlibrary.util.impl.TD1CommunicatorTestImpl
import kotlinx.coroutines.launch

@Composable
fun Collector(viewModel: CollectorViewModel) {
    CompPermissionReceiver(
        context = LocalContext.current,
        onPermissionChange = {
            if (it) {
                viewModel.connect()
            } else {
                viewModel.connectionStatus.value = ConnectionStatus.PERMISSION_DENIED
            }
        },
        onDispose = {
            viewModel.disconnect()
        }
    )

    CollectorScaffold(
        viewModel
    )

    key(viewModel.selectedIndex.value) {
        viewModel.selectedIndex.value
            ?.takeIf { it >= 0 && it <= viewModel.filaments.size }
            ?.let { index ->
                EditorBottomSheet(
                    filament = viewModel.filaments[index],
                    onFilamentChange = {
                        viewModel.filaments[index] = it
                    },
                    onDismiss = {
                        viewModel.selectedIndex.value = null
                    }
                )
            }
    }

    key (viewModel.connectionStatus.value) {
        if (viewModel.connectionStatus.value == ConnectionStatus.CONNECTED) {
            val coroutineScope = rememberCoroutineScope()
            coroutineScope.launch {
                if (!viewModel.isReading) {
                    viewModel.startReading {

                    }
                }
            }
        }
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