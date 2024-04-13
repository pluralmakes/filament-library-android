package com.pluralmakes.td1_collector

import android.content.IntentFilter
import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import com.pluralmakes.td1_collector.model.Filament
import com.pluralmakes.td1_collector.util.impl.ACTION_USB_PERMISSION
import com.pluralmakes.td1_collector.util.impl.PermissionReceiver
import com.pluralmakes.td1_collector.viewModel.CollectorViewModel

// Original test page
//TODO: Move most functionality to Collection.kt
//@Composable
//fun CollectorComposable(
//    viewModel: CollectorViewModel
//) {
//    val isPermissionGranted: MutableState<Boolean?> = remember { mutableStateOf(null) }
//    val isConnected: MutableState<Boolean> = remember { mutableStateOf(false) }
//    val data = remember { mutableStateListOf<Filament>() }
//
//    val context = LocalContext.current
//    val receiver = PermissionReceiver(isPermissionGranted)
//
//    DisposableEffect(context)  {
//        isPermissionGranted.value = viewModel.communicator.hasPermission()
//        context.registerReceiver(
//            receiver,
//            IntentFilter(ACTION_USB_PERMISSION),
//            when (Build.VERSION.SDK_INT) {
//                in 0..33 -> 0
//                else -> 0x2
//            }
//        )
//
//        onDispose {
//            context.unregisterReceiver(receiver)
//            viewModel.disconnect()
//        }
//    }
//
//    when (isPermissionGranted.value) {
//        null -> Text("Loading...")
//        false -> {
//            Text("Requesting USB permission")
//
//            viewModel.requestPermission()
//        }
//        true -> Column {
//            when (isConnected.value) {
//                true -> {
//                    Column {
//                        Text("Connected to TD-1")
//                        data.forEach {
//                            Column {
//                                Column() {
//                                    Row(
//                                        modifier = Modifier
//                                            .fillMaxWidth()
//                                            .padding(10.dp),
//                                        horizontalArrangement = Arrangement.SpaceBetween
//                                    ) {
//                                        Column {
//                                            Text("Brand")
//                                            Text(it.brand ?: "")
//                                        }
//                                        Column {
//                                            Text("Type")
//                                            Text(it.type ?: "")
//                                        }
//                                        Column {
//                                            Text("Name")
//                                            Text(it.name ?: "")
//                                        }
//                                        Column {
//                                            Text("Color")
//                                            Text("#${it.color}")
//                                        }
//                                        Column {
//                                            Text("TD")
//                                            Text(it.td?.toString() ?: "")
//                                        }
//                                    }
//                                }
//
//                                it.color?.takeIf { it.isNotBlank() }?.let { hex ->
//                                    val color = Color(android.graphics.Color.parseColor("#$hex"))
//
//                                    // https://thenounproject.com/icon/filament-spool-3981473/
//                                    Image(
//                                        painter = painterResource(R.drawable.filament_spool),
//                                        contentDescription = null,
//                                        colorFilter = ColorFilter.tint(color)
//                                    )
//                                }
//                            }
//                        }
//                    }
//
//                    LaunchedEffect(Unit) {
//                        viewModel.startReading { newData ->
//                            data.add(newData)
//                        }
//                    }
//                }
//                false -> Text("Connecting...")
//            }
//
//            LaunchedEffect(Unit) {
//                viewModel.connect(isConnected)
//            }
//        }
//    }
//}