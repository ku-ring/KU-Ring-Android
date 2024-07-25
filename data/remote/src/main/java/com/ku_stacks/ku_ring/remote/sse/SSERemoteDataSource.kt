package com.ku_stacks.ku_ring.remote.sse

import com.ku_stacks.ku_ring.remote.BuildConfig
import com.ku_stacks.ku_ring.remote.sse.response.KuringBotResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsChannel
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.readUTF8Line
import javax.inject.Inject

class SSERemoteDataSource @Inject constructor(
    private val client: HttpClient,
) : SSEDataSource {

    override suspend fun openKuringBotSSESession(
        query: String,
        token: String,
        onData: (String) -> Unit,
    ) {
        val response = kuringBotResponse(query, token)

        val contentType = response.headers["Content-Type"]
        when {
            contentType?.contains(CONTENT_TYPE_STREAM) == true -> {
                val byteReadChannel = response.bodyAsChannel()
                readFromChannel(byteReadChannel, onData)
            }

            contentType?.contains(CONTENT_TYPE_JSON) == true -> {
                val jsonResponse = response.body<KuringBotResponse>()
                onData(jsonResponse.resultMsg)
            }

            else -> {
                onData(ERROR_DATA)
            }
        }
    }

    private suspend fun kuringBotResponse(query: String, token: String): HttpResponse {
        val url = BuildConfig.API_BASE_URL + "v2/ai/messages"
        return client.get(url) {
            parameter("question", query)
            header("User-Token", token)
            header("Accept", CONTENT_TYPE_STREAM)
            header("Cache-Control", "no-cache")
        }
    }

    private suspend fun readFromChannel(
        byteReadChannel: ByteReadChannel,
        onData: (String) -> Unit
    ) {
        while (!byteReadChannel.isClosedForRead) {
            byteReadChannel.awaitContent()

            val line = byteReadChannel.readUTF8Line() ?: continue
            if (line.startsWith("data:")) {
                onData(line)
            }
        }
    }

    companion object {
        private const val CONTENT_TYPE_STREAM = "text/event-stream"
        private const val CONTENT_TYPE_JSON = "application/json"

        private const val ERROR_DATA = "알 수 없는 에러가 발생했어요. 잠시 후에 다시 시도해 주세요."
    }
}