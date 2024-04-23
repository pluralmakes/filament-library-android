package com.pluralmakes.filamentlibrary

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.pluralmakes.filamentlibrary.model.generateRandomFilaments
import com.pluralmakes.filamentlibrary.model.viewModel.CollectorViewModel
import com.pluralmakes.filamentlibrary.ui.Collector
import com.pluralmakes.filamentlibrary.ui.theme.FilamentLibraryTheme
import com.pluralmakes.filamentlibrary.util.StorageUtil
import com.pluralmakes.filamentlibrary.util.impl.TD1CommunicatorImpl

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val filaments = StorageUtil.load(
            context = this,
            onError = { _, _ ->
                // TODO: Handle error
            }
        ).toMutableList()

        setContent {
            FilamentLibraryTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Collector(
                        viewModel = CollectorViewModel(
                            communicator = TD1CommunicatorImpl(this),
                            filaments = generateRandomFilaments(100).toMutableList()
                        )
                    )
                }
            }
        }
    }
}