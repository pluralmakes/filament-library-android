package com.pluralmakes.filamentlibrary.ui.pages.library.scaffold

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import com.pluralmakes.filamentlibrary.model.Filament
import com.pluralmakes.filamentlibrary.ui.dialogs.LibraryScaffoldDialogs
import com.pluralmakes.filamentlibrary.util.ConnectionStatus

@Composable
fun LibraryScaffold(
    connectionStatus: MutableState<ConnectionStatus>,
    bulkSelectedFilament: SnapshotStateList<Filament>,
    selectedFilament: MutableState<Filament?>,
    filaments: SnapshotStateList<Filament>,
    onDisconnectClick: () -> Unit,
    onConnectClick: () -> Unit,
    onShareClick: () -> Unit,
) {
    val showMergeDialog = remember { mutableStateOf(false) }
    val showBulkEditor = remember { mutableStateOf(false) }
    val showBulkDelete = remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            LibraryScaffoldTopBar(
                numOfSelectedFilament = bulkSelectedFilament.size,
                onShowBulkEditor = {
                    showBulkEditor.value = true
                },
                onShowMergeDialog = {
                    showMergeDialog.value = true
                },
                onShowBulkDelete = {
                    showBulkDelete.value = true
                }
            )
        },
        content = {
            LibraryScaffoldContent(
                modifier = Modifier.padding(it),
                bulkSelectedFilament = bulkSelectedFilament,
                selectedFilament = selectedFilament,
                filaments = filaments,
            )
        },
        floatingActionButton = {
            LibraryScaffoldFAB(
                connectionStatus = connectionStatus,
                onConnectClick = onConnectClick,
                onDisconnectClick = onDisconnectClick,
                onShareClick = onShareClick,
            )
       },
    )

    LibraryScaffoldDialogs(
        bulkSelectedFilamentState = bulkSelectedFilament,
        selectedFilamentState = selectedFilament,
        showMergeDialog = showMergeDialog,
        showBulkEditor = showBulkEditor,
        showBulkDelete = showBulkDelete,
        filaments = filaments,
    )
}