package com.ku_stacks.ku_ring.data.websocket

import com.google.gson.Gson
import com.ku_stacks.ku_ring.data.websocket.request.StaffRequest
import com.ku_stacks.ku_ring.data.websocket.response.StaffListResponse
import io.reactivex.rxjava3.core.*
import io.reactivex.rxjava3.schedulers.Schedulers
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import timber.log.Timber
import java.lang.Exception
import java.net.URI
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLSocketFactory


class SearchClient {

    private var webSocketClient: WebSocketClient? = null

    private var staffList = StaffListResponse(false, "", 0, emptyList())
    private var preparingFlag = false
    private var lastKeyword = ""

    fun isOpen() = webSocketClient?.isOpen == true
    fun isPreparing() = preparingFlag
    fun setLastKeyword(keyword: String) {
        lastKeyword = keyword
    }

    fun subscribeStaff(): Flowable<StaffListResponse> {
        return Flowable.interval(300, TimeUnit.MILLISECONDS)
            .flatMap {
                Flowable.just(staffList)
            }
            .distinctUntilChanged()
    }

    fun searchStaff(keyword: String) {
        webSocketClient?.send(
            Gson().toJson(
                StaffRequest(type = "search", content = keyword)
            )
        )
    }

    private fun createWebSocketClient(coinbaseUri: URI) {
        webSocketClient = object : WebSocketClient(coinbaseUri) {
            override fun onOpen(handshakedata: ServerHandshake?) {
                Timber.e("WebSocket onOpen")
                preparingFlag = false
                searchStaff(lastKeyword)
            }

            override fun onMessage(message: String?) {
                Timber.e("WebSocket onMessage : $message")
                staffList = Gson().fromJson(message, StaffListResponse::class.java)
            }

            override fun onClose(code: Int, reason: String?, remote: Boolean) {
                Timber.e("WebSocket onClose : $code, $reason, $remote")
                preparingFlag = false
            }

            override fun onError(ex: Exception?) {
                Timber.e("WebSocket error ${ex?.message}")
                preparingFlag = false

            }
        }
    }

    fun initWebSocket() {
        val coinbaseUri = URI(Companion.url)
        createWebSocketClient(coinbaseUri)

        val socketFactory: SSLSocketFactory = SSLSocketFactory.getDefault() as SSLSocketFactory
        webSocketClient!!.setSocketFactory(socketFactory)
        webSocketClient!!.connect()
        preparingFlag = true
    }

    fun disconnectWebSocket() {
        webSocketClient!!.close()
    }

    companion object {
        const val url = "wss://kuring-dev.herokuapp.com/kuring/staff"
    }
}