package com.pluralmakes.filamentlibrary.util

import android.content.Context
import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import com.pluralmakes.filamentlibrary.model.Constants
import com.pluralmakes.filamentlibrary.model.Filament
import java.io.File
import java.io.IOException
import java.io.OutputStreamWriter

enum class ErrorType {
    IO, PERMISSIONS, PARSING
}

class StorageUtil {
    companion object {
        fun load(
            context: Context,
            onError: (ErrorType, Exception?) -> Unit,
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
                            val type = object : TypeToken<Map<String, List<Filament>>>() {}.type
                            val data = Gson().fromJson<Map<String, List<Filament>>>(json, type)

                            return (data["Filaments"] ?: emptyList()).toTypedArray()
                        } catch (e: JsonSyntaxException) {
                            onError(ErrorType.PARSING, e)
                        }
                    }
                }
            } catch (e: IOException) {
                onError(ErrorType.IO, e)
            } catch (e: JsonParseException) {
                onError(ErrorType.PARSING, e)
            }

            return emptyArray()
        }

        fun save(
            filaments: List<Filament>,
            context: Context,
            onError: (ErrorType, Exception?) -> Unit,
        ): File? {
            val file = getFilamentFile(context)

            try {
                if (!file.exists() && !file.createNewFile()) {
                    onError(ErrorType.PERMISSIONS, null)

                    return null
                }
            } catch (e: IOException) {
                onError(ErrorType.IO, e)

                return null
            }

            val fileOutputStream = file.outputStream()
            val outputWriter = OutputStreamWriter(fileOutputStream)
            try {
                val hueForgeFilamentFormat = mapOf("Filaments" to filaments)
                val json = Gson().toJson(hueForgeFilamentFormat)
                outputWriter.write(json.toString())

                return file
            } catch (e: IOException) {
                onError(ErrorType.IO, e)
            } catch (e: JsonParseException) {
                onError(ErrorType.PARSING, e)
            } finally {
                outputWriter.close()
            }

            return null
        }

        private fun getFilamentFile(context: Context): File {
            return File(context.filesDir, Constants.FILE_NAME)
        }
    }
}