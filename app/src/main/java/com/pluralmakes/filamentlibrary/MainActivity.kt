package com.pluralmakes.filamentlibrary

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.pluralmakes.filamentlibrary.ui.Collector
import com.pluralmakes.filamentlibrary.ui.theme.FilamentLibraryTheme
import com.pluralmakes.filamentlibrary.util.impl.TD1CommunicatorImpl
import com.pluralmakes.filamentlibrary.model.CollectorViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FilamentLibraryTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Collector(
                        viewModel = CollectorViewModel(
                            communicator = TD1CommunicatorImpl(this)
                        )
                    )
                }
            }
        }
    }
}