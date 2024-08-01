package com.ku_stacks.ku_ring.remote.sse

import com.ku_stacks.ku_ring.remote.BuildConfig
import io.ktor.client.HttpClient
import io.ktor.client.plugins.sse.sse
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

class SSERemoteDataSource @Inject constructor(
    private val client: HttpClient,
) : SSEDataSource {

    override suspend fun openKuringBotSSESession(
        query: String,
        token: String,
        onReceived: (String) -> Unit,
    ) {
        val url = BuildConfig.API_BASE_URL + "v2/ai/messages"
        client.sse(
            urlString = url,
            request = {
                parameter("question", query)
                header("User-Token", token)
                header("Accept", "text/event-stream")
                header("Cache-Control", "no-cache")
            }
        ) {
            incoming.collectLatest { incoming ->
                incoming.data?.ifEmpty { " " }?.let(onReceived)
            }
        }
    }
}