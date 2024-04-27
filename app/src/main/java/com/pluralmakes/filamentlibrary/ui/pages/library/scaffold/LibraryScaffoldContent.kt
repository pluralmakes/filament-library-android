package com.pluralmakes.filamentlibrary.ui.pages.library.scaffold

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import com.pluralmakes.filamentlibrary.model.Filament
import com.pluralmakes.filamentlibrary.ui.views.FilamentListView

@Composable
fun LibraryScaffoldContent(
    modifier : Modifier,
    filaments: MutableList<Filament>,
    selectedFilament: MutableState<Filament?>,
    bulkSelectedFilament: MutableList<Filament>,
) {
    FilamentListView(
        modifier = modifier,
        unorganizedFilament = filaments,
        bulkSelectedFilament = bulkSelectedFilament,
        onSelect = { filament, longPress ->
            if (longPress || bulkSelectedFilament.isNotEmpty()) {
                if (bulkSelectedFilament.contains(filament)) {
                    bulkSelectedFilament.remove(filament)
                } else {
                    bulkSelectedFilament.add(filament)
                }
            } else {
                selectedFilament.value = filament
            }
        }
    )
}