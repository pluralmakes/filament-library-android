package com.pluralmakes.filamentlibrary.ui.dialogs

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.platform.LocalContext
import com.pluralmakes.filamentlibrary.model.Filament
import com.pluralmakes.filamentlibrary.ui.views.bottomSheets.EditorBottomSheet
import com.pluralmakes.filamentlibrary.ui.views.bottomSheets.LibraryBulkEditor
import com.pluralmakes.filamentlibrary.ui.views.bottomSheets.LibraryMerge
import com.pluralmakes.filamentlibrary.util.StorageUtil

@Composable
fun LibraryScaffoldDialogs(
    bulkSelectedFilamentState: SnapshotStateList<Filament>,
    selectedFilamentState: MutableState<Filament?>,
    showMergeDialog: MutableState<Boolean>,
    showBulkEditor: MutableState<Boolean>,
    showBulkDelete: MutableState<Boolean>,
    filaments: SnapshotStateList<Filament>,
) {
    val context : Context = LocalContext.current

    val individualFilament = selectedFilamentState.value
    if (individualFilament != null) {
        val selectedFilamentIndex = filaments.indexOf(individualFilament)

        EditorBottomSheet(
            filament = individualFilament,
            onFilamentChange = { editedFilament ->
                filaments[selectedFilamentIndex] = editedFilament
            },
            onDismiss = {
                selectedFilamentState.value = null
            }
        )
    } else if (showMergeDialog.value) {
        LibraryMerge(
            selectedFilament = bulkSelectedFilamentState,
            onMergeFilament = { mergedFilament ->
                bulkSelectedFilamentState.forEach {
                    filaments.remove(it)
                }

                filaments.add(mergedFilament)
                bulkSelectedFilamentState.clear()
                storeFilament(context, filaments)
            },
            onDismiss = {
                showMergeDialog.value = false
            }
        )
    } else if (showBulkEditor.value) {
        LibraryBulkEditor(
            filamentsToEdit = bulkSelectedFilamentState,
            filamentsToUseForSuggestions = filaments,
            onEditFilament = { filamentChanges ->
                bulkSelectedFilamentState.forEach { filament ->
                    val i = filaments.indexOf(filament)

                    filaments[i] = filament.copy(
                        brand = filamentChanges.brand ?: filament.brand,
                        name = filamentChanges.name ?: filament.name,
                        color = filamentChanges.color ?: filament.color,
                        td = filamentChanges.td ?: filament.td,
                        type = filamentChanges.polymer ?: filament.type
                    )
                }

                bulkSelectedFilamentState.clear()
                storeFilament(context, filaments)
            },
            onDismiss = {
                showBulkEditor.value = false
            }
        )
    } else if (showBulkDelete.value) {
        LibraryBulkDelete(
            numOfFilamentToDelete = bulkSelectedFilamentState.size,
            onConfirm = {
                filaments.removeIf { bulkSelectedFilamentState.contains(it) }
                bulkSelectedFilamentState.clear()
                showBulkDelete.value = false

                storeFilament(context, filaments)
            },
            onDismiss = {
                showBulkDelete.value = false
            }
        )
    }
}

private fun storeFilament(
    context: Context,
    filaments: SnapshotStateList<Filament>,
) {
    StorageUtil.save(
        context = context,
        filaments = filaments,
    ) { _, _ ->
        // TODO: Handle errors
        Toast.makeText(context, "Error saving filaments", Toast.LENGTH_SHORT).show()
    }
}