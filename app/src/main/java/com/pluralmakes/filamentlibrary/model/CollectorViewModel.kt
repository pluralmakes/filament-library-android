package com.pluralmakes.filamentlibrary.model

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.google.gson.JsonSyntaxException
import com.pluralmakes.filamentlibrary.util.ConnectionStatus
import com.pluralmakes.filamentlibrary.util.ConnectionStatus.*
import com.pluralmakes.filamentlibrary.util.TD1Communicator
import java.io.File
import java.io.IOException
import java.io.OutputStreamWriter

class CollectorViewModel(
    val connectionStatus: MutableState<ConnectionStatus> = mutableStateOf(NONE),
    var selectedIndexes: MutableList<Int> = mutableStateListOf(),
    var selectedIndex: MutableState<Int?> = mutableStateOf(null),
    var filaments: MutableList<Filament> = mutableStateListOf(),
    val communicator: TD1Communicator,
): ViewModel() {
    var isReading : Boolean = false
        private set

    fun connect() {
        connectionStatus.value = CONNECTING
        if (!communicator.hasPermission()) {
            communicator.requestPermission(connectionStatus)
            return
        }

        communicator.connect(connectionStatus)
    }

    private fun save(
        context: Context,
        // TODO: Implement error handlers
    ) {
        //TODO: Implement proper error handling
        val file = getFilamentFile(context)

        try {
            if (!file.exists() && !file.createNewFile()) {
                print("Failed to create filaments file")

                return
            }
        } catch (e: IOException) {
            print("IO Exception thrown when creating file: $e")

            return
        }

        val fileOutputStream = file.outputStream()
        val outputWriter = OutputStreamWriter(fileOutputStream)
        try {
            val hueForgeFilamentFormat = mapOf("Filaments" to filaments)
            val json = Gson().toJson(hueForgeFilamentFormat)

            outputWriter.write(json.toString())
        } catch (e: IOException) {
            print("IO Exception thrown when trying to write filaments file: $e")
        } finally {
            outputWriter.close()
        }
    }

    fun export(context: Context) {
        // Save the file really quickly before exporting
        save(context)

        val file = getFilamentFile(context)
        val uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)

        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_STREAM, uri)
            type = "application/json"
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        }

        context.startActivity(Intent.createChooser(shareIntent, "Export filament"))
    }

    suspend fun startReading() {
        isReading = true

        communicator.startReading(
            onReadingEnd = {
                isReading = false
            },
            onFilamentReceived = { filament ->
                filaments.add(filament)
                selectedIndex.value = filaments.indexOf(filament)
            }
        )
    }

    fun disconnect() {
        isReading = false
        communicator.disconnect()
        connectionStatus.value = DISCONNECTED
    }

    companion object {
        fun load(
            context: Context,
            onError: () -> Unit = {}
        ): Array<Filament> {
            val file = getFilamentFile(context)

            try {
                if (!file.exists()) {
                    file.createNewFile()
                }

                if (file.exists() && file.length() > 0L) {
                    val inputStreamReader = file.reader()
                    val bufferedReader = inputStreamReader.buffered()
                    val json = bufferedReader.use {
                        it.readText()
                    }

                    if (json.isNotEmpty()) {
                        try {
                            return Gson().fromJson(json, Array<Filament>::class.java)
                        } catch (e: JsonSyntaxException) {
                            print("Failed to read filaments file: $e")

                            onError()
                        }
                    }
                }
            } catch (e: IOException) {
                print("Failed to read filaments file: $e")

                onError()
            } catch (e: JsonParseException) {
                print("Failed to parse filaments file: $e")

                onError()
            }

            return emptyArray()
        }

        private fun getFilamentFile(context: Context): File {
            return File(context.filesDir, Constants.FILE_NAME)
        }
    }
}