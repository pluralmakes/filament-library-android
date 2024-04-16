package com.pluralmakes.filamentlibrary.util.impl

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.compose.runtime.MutableState
import com.felhr.usbserial.UsbSerialDevice
import com.felhr.usbserial.UsbSerialInterface
import com.pluralmakes.filamentlibrary.model.Filament
import com.pluralmakes.filamentlibrary.model.TD1Constants
import com.pluralmakes.filamentlibrary.util.ConnectionStatus
import com.pluralmakes.filamentlibrary.util.ConnectionStatus.*
import com.pluralmakes.filamentlibrary.util.TD1Communicator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

const val ACTION_USB_PERMISSION = "com.pluralmakes.filamentlibrary.USB_PERMISSION"

class TD1CommunicatorImpl(private val context: Context): TD1Communicator {
    private var serialPort: UsbSerialDevice? = null

    override fun connect(isConnected: MutableState<ConnectionStatus>) {
        val usbManager = context.getSystemService(Context.USB_SERVICE) as UsbManager
        val device = getUsbDevice(usbManager)

        if (device != null) {
            try {
                val connection = usbManager.openDevice(device)
                serialPort = UsbSerialDevice.createUsbSerialDevice(device, connection)

                if (serialPort != null) {
                    if (serialPort!!.syncOpen()) {
                        serialPort!!.setBaudRate(TD1Constants.baudRate)
                        serialPort!!.setDataBits(UsbSerialInterface.DATA_BITS_8)
                        serialPort!!.setStopBits(UsbSerialInterface.STOP_BITS_1)
                        serialPort!!.setParity(UsbSerialInterface.PARITY_NONE)
                        serialPort!!.setFlowControl(UsbSerialInterface.FLOW_CONTROL_OFF)

                        isConnected.value = CONNECTED
                    } else {
                        isConnected.value = DISCONNECTED
                    }
                } else {
                    isConnected.value = DISCONNECTED
                }
            } catch (e: SecurityException) {
                isConnected.value = PERMISSION_DENIED
            } catch (e: IOException) {
                isConnected.value = CONNECTION_FAILED
            }
        } else {
            isConnected.value = DEVICE_NOT_FOUND
        }
    }

    @Throws(IOException::class)
    override suspend fun startReading(
        onReadingEnd: () -> Unit,
        onFilamentReceived: (Filament) -> Unit
    ) : Unit = withContext(Dispatchers.IO) {
        val read = {
            val buffer = ByteArray(50)
            val numBytesRead = serialPort?.syncRead(buffer, -1)

            val output = if (numBytesRead != null && numBytesRead > 0) {
                String(buffer, 0, numBytesRead).trim()
            } else {
                null
            }

            Log.d("TD-1 Output", "startReading: $output")
            output
        }
        val write = { command: String ->
            serialPort?.syncWrite("$command\n".toByteArray(), -1)
        }

        write("connect")

        var response = read()
        if (response == "ready") {
            write("P")

            while (serialPort?.isOpen == true) {
                response = read()

                if (response != null) {
                    val dataList = response.split(',')
                    val filteredDataList = filterData(dataList.drop(1))
                        .filter { it.isNotEmpty() }
                        .map { it.replace("(", "").replace(")", "") }

                    if (filteredDataList.isNotEmpty()) {
                        Handler(Looper.getMainLooper()).post {
                            onFilamentReceived(
                                Filament(
                                    brand = dataList[1].takeIf { it.isNotEmpty() } ?: "Unknown",
                                    type = dataList[2].takeIf { it.isNotEmpty() } ?: "Unknown",
                                    name = dataList[3],
                                    td = dataList[4].toFloat(),
                                    color = "#${dataList[5]}",
                                )
                            )
                        }

                        Log.d("TD-1 Output", "Received new filament")
                    }
                }
            }
        } else {
            print("Failed because $response")
        }

        Handler(Looper.getMainLooper()).post {
            onReadingEnd()
        }
    }

    override fun hasPermission(): Boolean {
        val usbManager = context.getSystemService(Context.USB_SERVICE) as UsbManager
        val device = getUsbDevice(usbManager)

        return device != null && usbManager.hasPermission(device)
    }

    override fun requestPermission(permissionStatus: MutableState<ConnectionStatus>) {
        val usbManager = context.getSystemService(Context.USB_SERVICE) as UsbManager
        val device = getUsbDevice(usbManager)

        if (device != null) {
            val permissionIntent = if (Build.VERSION.SDK_INT >= 34) {
                PendingIntent.getBroadcast(
                    context,
                    0,
                    Intent(ACTION_USB_PERMISSION),
                    PendingIntent.FLAG_ALLOW_UNSAFE_IMPLICIT_INTENT or PendingIntent.FLAG_MUTABLE
                )
            } else {
                PendingIntent.getBroadcast(
                    context,
                    0,
                    Intent(ACTION_USB_PERMISSION),
                    PendingIntent.FLAG_MUTABLE
                )
            }

            usbManager.requestPermission(device, permissionIntent)
        } else {
            permissionStatus.value = DEVICE_NOT_FOUND
        }
    }

    override fun disconnect() {
        serialPort?.close()
        serialPort = null
    }

    private fun getUsbDevice(usbManager: UsbManager): UsbDevice? {
        return usbManager.deviceList.values.firstOrNull { device ->
            device.vendorId == TD1Constants.targetVid && device.productId == TD1Constants.targetPid
        }
    }

    // Function to filter unwanted characters from data
    private fun filterData(dataList: List<String>): List<String> {
        return dataList.map { it.replace("(", "").replace(")", "") }
    }
}