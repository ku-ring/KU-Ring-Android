package com.ku_stacks.ku_ring.place.datasource

import android.content.Context
import com.ku_stacks.ku_ring.place.model.JsonPlace
import com.ku_stacks.ku_ring.util.IODispatcher
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import timber.log.Timber
import javax.inject.Inject

class PlaceDataSource @Inject constructor(
    @param:ApplicationContext private val context: Context,
    @param:IODispatcher private val ioDispatcher: CoroutineDispatcher,
) {
    internal suspend fun getJsonPlaces() : List<JsonPlace> = withContext(ioDispatcher){
        val assetManager = context.assets
        val inputStream = assetManager.open("KonkukPlaces.json")
        val jsonString = inputStream.bufferedReader().use { it.readText() }
        val jsonParser = Json { ignoreUnknownKeys = true }

        try {
            jsonParser.decodeFromString<List<JsonPlace>>(jsonString)
        } catch (e: Exception) {
            Timber.e(e)
            emptyList()
        }
    }
}