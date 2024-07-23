package com.ku_stacks.ku_ring.remote.sse

import com.ku_stacks.ku_ring.remote.BuildConfig
import io.ktor.client.HttpClient
import io.ktor.client.plugins.sse.sseSession
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SSERemoteDataSource @Inject constructor(
    private val client: HttpClient,
) : SSEDataSource {
    override suspend fun openKuringBotSSESession(query: String, token: String): Flow<String>? {
        return try {
            tryOpenKuringBotSSESession(query, token)
        } catch (e: Throwable) {
            null
        }
    }

    private suspend fun tryOpenKuringBotSSESession(query: String, token: String): Flow<String> {
        val url = BuildConfig.API_BASE_URL + "v2/ai/messages"
        return client.sseSession(urlString = url) {
            parameter("question", query)
            header("User-Token", token)
            header("Accept", "text/event-stream")
            header("Cache-Control", "no-cache")
        }.incoming.map { it.data.orEmpty() }
    }
}