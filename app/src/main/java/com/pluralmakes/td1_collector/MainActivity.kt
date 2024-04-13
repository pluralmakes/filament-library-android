package com.pluralmakes.td1_collector

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.pluralmakes.td1_collector.model.generateRandomFilaments
import com.pluralmakes.td1_collector.ui.Collector
import com.pluralmakes.td1_collector.ui.theme.TD1CollectorTheme
import com.pluralmakes.td1_collector.util.impl.TD1CommunicatorImpl
import com.pluralmakes.td1_collector.viewModel.CollectorViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TD1CollectorTheme {
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