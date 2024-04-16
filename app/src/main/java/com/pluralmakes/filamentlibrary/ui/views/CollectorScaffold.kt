package com.pluralmakes.filamentlibrary.ui.views

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pluralmakes.filamentlibrary.R
import com.pluralmakes.filamentlibrary.model.CollectorViewModel
import com.pluralmakes.filamentlibrary.model.Filament
import com.pluralmakes.filamentlibrary.model.generateRandomFilaments
import com.pluralmakes.filamentlibrary.ui.Collector
import com.pluralmakes.filamentlibrary.ui.dialogs.CollectorDialogs
import com.pluralmakes.filamentlibrary.ui.theme.FilamentLibraryTheme
import com.pluralmakes.filamentlibrary.util.ConnectionStatus
import com.pluralmakes.filamentlibrary.util.impl.TD1CommunicatorTestImpl
import kotlinx.coroutines.launch
import kotlin.random.Random

@Composable
fun CollectorScaffold(
    collectorViewModel: CollectorViewModel
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            val selectedIndexes = collectorViewModel.selectedIndexes
            if (selectedIndexes.isNotEmpty()) {
                Row(Modifier.padding(5.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "${selectedIndexes.size} selected",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                    )

                    Spacer(
                        Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    )

                    Button(onClick = {
                        // TODO: Implement Bulk Edit
                    }) {
                        Image(
                            painter = painterResource(id = R.drawable.edit),
                            contentDescription = "Edit"
                        )
                    }

                    Spacer(Modifier.padding(horizontal = 2.5.dp))

                    Button(onClick = {
                        // TODO: Implement Merge
                        // Use average TD value and hex color between all
                    }) {
                        Image(
                            painter = painterResource(id = R.drawable.merge),
                            contentDescription = "Merge"
                        )
                    }

                    Spacer(Modifier.padding(horizontal = 2.5.dp))

                    Button(onClick = {
                        // TODO: Implement Bulk Delete
                    }) {
                        Image(
                            painter = painterResource(id = R.drawable.delete),
                            contentDescription = "Delete"
                        )
                    }
                }
            }
        },
        content = CollectorScaffoldContent(
            collectorViewModel.filaments,
            collectorViewModel.selectedIndexes,
            selectFilament = { multiSelect: Boolean, index: Int ->
                if (multiSelect || collectorViewModel.selectedIndexes.isNotEmpty()) {
                    if (collectorViewModel.selectedIndexes.contains(index)) {
                        collectorViewModel.selectedIndexes.remove(index)
                    } else {
                        collectorViewModel.selectedIndexes.add(index)
                    }
                } else {
                    collectorViewModel.selectedIndex.value = index
                }
            }
        ),
        floatingActionButton = {
            Row {
                TD1FloatingButton(
                    connectionStatus = collectorViewModel.connectionStatus,
                    onConnectClick = {
                        when (it) {
                            ConnectionStatus.DISCONNECTED, ConnectionStatus.NONE -> {
                                collectorViewModel.connect()
                            }

                            ConnectionStatus.CONNECTED -> collectorViewModel.disconnect()
                            else -> {}
                        }
                    },
                    onExportClick = {
                        coroutineScope.launch {
                            collectorViewModel.export(context)
                        }
                    }
                )

                CollectorDialogs(collectorViewModel)
            }
        }
    )
}

@Composable
fun CollectorScaffoldContent(
    filaments: MutableList<Filament>,
    selectedIndexes: MutableList<Int>,
    selectFilament: (multiSelect: Boolean, Int) -> Unit,
): @Composable (PaddingValues) -> Unit {
    return {
        FilamentListView(
            filaments = filaments,
            selectedIndexes = selectedIndexes,
            modifier = Modifier.padding(it),
            onBulkSelect = selectFilament,
        )
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
fun CollectorScaffoldPreviewNoneSelected() {
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

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    apiLevel = 33,
)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    apiLevel = 33,
)
@Composable
fun CollectorScaffoldPreviewWithSelections() {
    FilamentLibraryTheme {
        Surface {
            val communicator = TD1CommunicatorTestImpl()

            val filaments = generateRandomFilaments().toMutableList()
            val viewModel = CollectorViewModel(
                communicator = communicator,
                filaments = filaments,
                selectedIndexes = (0..Random.nextInt(filaments.size - 5)).toMutableList()
            )

            Collector(viewModel)
        }
    }
}