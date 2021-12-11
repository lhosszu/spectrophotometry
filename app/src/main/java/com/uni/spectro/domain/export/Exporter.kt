package com.uni.spectro.domain.export

import android.util.Log
import com.uni.spectro.persistence.RealmAttributes
import com.uni.spectro.persistence.RealmPersistence
import com.uni.spectro.wrapper.JsonConverter
import java.io.File

class Exporter {

    private val database: RealmPersistence = RealmPersistence()
    private val converter: JsonConverter = JsonConverter()

    fun dumpAll(directory: File): Boolean {
        return try {
            val allExperiments = database.queryAll()
            val serializedExperiments: String = converter.toJson(allExperiments)
            writeToFile(serializedExperiments, directory)
            true
        } catch (e: Exception) {
            Log.e(TAG, "Cannot dump json: ${e.message}", e)
            false
        }
    }

    private fun writeToFile(content: String, directory: File) {
        Log.i(TAG, "Dumping experiments to JSON")
        val file = File(directory, "${RealmAttributes.REALM_NAME}.json")
        file.bufferedWriter().use { out -> out.write(content) }
        Log.i(TAG, "Dump successful")
    }

    private companion object {
        private val TAG = Exporter::class.java.name
    }
}