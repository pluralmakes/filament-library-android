package com.pluralmakes.filamentlibrary

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