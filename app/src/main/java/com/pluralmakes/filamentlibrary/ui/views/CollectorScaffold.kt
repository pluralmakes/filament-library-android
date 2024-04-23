package com.pluralmakes.filamentlibrary.ui.views

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.pluralmakes.filamentlibrary.R
import com.pluralmakes.filamentlibrary.model.Filament
import com.pluralmakes.filamentlibrary.model.annotations.Previews
import com.pluralmakes.filamentlibrary.model.generateRandomFilaments
import com.pluralmakes.filamentlibrary.model.viewModel.CollectorViewModel
import com.pluralmakes.filamentlibrary.ui.dialogs.CollectorDialogs
import com.pluralmakes.filamentlibrary.ui.dialogs.GenericDialog
import com.pluralmakes.filamentlibrary.ui.pages.collector.Collector
import com.pluralmakes.filamentlibrary.ui.theme.FilamentLibraryTheme
import com.pluralmakes.filamentlibrary.ui.views.bottomSheets.BulkEditorBottomSheet
import com.pluralmakes.filamentlibrary.ui.views.bottomSheets.MergeBottomSheet
import com.pluralmakes.filamentlibrary.util.ConnectionStatus
import com.pluralmakes.filamentlibrary.util.StorageUtil
import com.pluralmakes.filamentlibrary.util.impl.TD1CommunicatorTestImpl
import kotlinx.coroutines.launch
import kotlin.random.Random

@Composable
fun CollectorScaffold(
    collectorViewModel: CollectorViewModel
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    var showMergeDialog by remember { mutableStateOf(false) }
    var showBulkEditor by remember { mutableStateOf(false) }
    var showBulkDelete by remember { mutableStateOf(false) }

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
                        showBulkEditor = true
                    }) {
                        Image(
                            painter = painterResource(id = R.drawable.edit),
                            contentDescription = "Edit"
                        )
                    }

                    Spacer(Modifier.padding(horizontal = 2.5.dp))

                    Button(onClick = {
                        showMergeDialog = true
                    }) {
                        Image(
                            painter = painterResource(id = R.drawable.merge),
                            contentDescription = "Merge"
                        )
                    }

                    Spacer(Modifier.padding(horizontal = 2.5.dp))

                    Button(onClick = {
                        showBulkDelete = true
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
                            collectorViewModel.share(context, onError = { _, _ ->
                                // TODO: Handle error
                            })
                        }
                    }
                )

                CollectorDialogs(collectorViewModel)
            }
        }
    )

    if (showMergeDialog) {
        MergeBottomSheet(
            selectedFilament = collectorViewModel.selectedIndexes.map { collectorViewModel.filaments[it] }.toMutableList(),
            onMergeFilament = { mergedFilament ->
                collectorViewModel
                    .filaments
                    .removeAll { collectorViewModel.selectedIndexes.contains(collectorViewModel.filaments.indexOf(it)) }

                collectorViewModel.filaments.add(mergedFilament)
                collectorViewModel.selectedIndexes.clear()
                StorageUtil.save(
                    collectorViewModel.filaments,
                    context,
                    onError = { _, _ -> }
                )
            },
            onDismiss = {
                showMergeDialog = false
            }
        )
    }

    if (showBulkEditor) {
        BulkEditorBottomSheet(
            filamentsToEdit = collectorViewModel.selectedIndexes.map { collectorViewModel.filaments[it] }.toMutableList(),
            filamentsToUseForSuggestions = collectorViewModel.filaments,
            onEditFilament = {
                collectorViewModel.selectedIndexes.forEach { i ->
                    val filament = collectorViewModel.filaments[i]
                    collectorViewModel.filaments[i] = filament.copy(
                        brand = it.brand ?: filament.brand,
                        name = it.name ?: filament.name,
                        color = it.color ?: filament.color,
                        td = it.td ?: filament.td,
                        type = it.polymer ?: filament.type
                    )
                }

                collectorViewModel.selectedIndexes.clear()
                StorageUtil.save(
                    collectorViewModel.filaments,
                    context,
                    onError = { _, _ -> }
                )
            },
            onDismiss = {
                showBulkEditor = false
            }
        )
    }

    if (showBulkDelete) {
        GenericDialog(
            title = "Delete Filament",
            body = pluralStringResource(
                id = R.plurals.bulk_delete_confirm,
                count = collectorViewModel.selectedIndexes.size,
                collectorViewModel.selectedIndexes.size
            ),
            onConfirmation = {
                collectorViewModel.selectedIndexes.forEach { index ->
                    collectorViewModel.filaments.removeAt(index)
                }

                collectorViewModel.selectedIndexes.clear()
                StorageUtil.save(
                    collectorViewModel.filaments,
                    context,
                    onError = { _, _ -> }
                )

                showBulkDelete = false
            },
            onDismissRequest = {
                showBulkDelete = false
            }
        )
    }
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

@Previews
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

@Previews
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