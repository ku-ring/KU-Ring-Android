package com.ku_stacks.ku_ring.place.datasource

import android.content.Context
import com.ku_stacks.ku_ring.place.model.JsonPlace
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.serialization.json.Json
import javax.inject.Inject

class PlaceDataSource @Inject constructor(
    @ApplicationContext private val context: Context
) {
    internal fun getJsonPlaces() : List<JsonPlace> {
        val assetManager = context.assets
        val inputStream = assetManager.open("KonkukPlaces.json")
        val jsonString = inputStream.bufferedReader().use { it.readText() }
        val jsonParser = Json { ignoreUnknownKeys = true }

        return try {
            jsonParser.decodeFromString<List<JsonPlace>>(jsonString)
        } catch (e: Exception) {
            emptyList()
        }
    }
}