package com.ku_stacks.ku_ring.data.websocket

import com.google.gson.Gson
import com.ku_stacks.ku_ring.BuildConfig.WEB_SOCKET_URL
import com.ku_stacks.ku_ring.data.websocket.request.HeartBeatRequest
import com.ku_stacks.ku_ring.data.websocket.request.SearchRequest
import com.ku_stacks.ku_ring.data.websocket.response.DefaultSearchResponse
import com.ku_stacks.ku_ring.data.websocket.response.SearchNoticeListResponse
import com.ku_stacks.ku_ring.data.websocket.response.SearchStaffListResponse
import io.reactivex.rxjava3.core.*
import io.reactivex.rxjava3.subjects.PublishSubject
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import timber.log.Timber
import java.lang.Exception
import java.net.URI
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean
import javax.net.ssl.SSLSocketFactory


class SearchClient {

    private var webSocketClient: WebSocketClient? = null

    var publishStaff: PublishSubject<SearchStaffListResponse> = PublishSubject.create()
    var publishNotice: PublishSubject<SearchNoticeListResponse> = PublishSubject.create()

    private var preparingFlag = AtomicBoolean(false)
    private var lastKeyword = ""

    fun isOpen() = webSocketClient?.isOpen == true
    fun isPreparing() = preparingFlag.get()

    fun setLastKeyword(keyword: String) {
        lastKeyword = keyword
    }

    fun searchStaff(keyword: String) {
        webSocketClient?.send(
            Gson().toJson(
                SearchRequest(type = staffType, content = keyword)
            )
        )
    }

    fun searchNotice(keyword: String) {
        webSocketClient?.send(
            Gson().toJson(
                SearchRequest(type = noticeType, content = keyword)
            )
        )
    }

    fun makeHeartBeat() {
        webSocketClient?.send(
            Gson().toJson(
                HeartBeatRequest(type = heartbeatType, content = "")
            )
        )
    }

    private fun createWebSocketClient(coinbaseUri: URI) {
        webSocketClient = object : WebSocketClient(coinbaseUri) {
            override fun onOpen(handshakedata: ServerHandshake?) {
                Timber.e("WebSocket onOpen")
                preparingFlag.set(false)
                if(lastKeyword.isNotEmpty()) {
                    searchStaff(lastKeyword)
                    searchNotice(lastKeyword)
                }
            }

            override fun onMessage(message: String?) {
                Timber.e("WebSocket onMessage : $message")
                val gson = Gson()

                val response = gson.fromJson(message, DefaultSearchResponse::class.java)
                when (response.type) {
                    noticeType -> {
                        publishNotice.onNext(gson.fromJson(message, SearchNoticeListResponse::class.java))
                    }
                    staffType -> {
                        publishStaff.onNext(gson.fromJson(message, SearchStaffListResponse::class.java))
                    }
                    heartbeatType -> {
                        Timber.e("received PONG")
                    }
                }
            }

            override fun onClose(code: Int, reason: String?, remote: Boolean) {
                Timber.e("WebSocket onClose : $code, $reason, $remote")
                preparingFlag.set(false)
            }

            override fun onError(ex: Exception?) {
                Timber.e("Search Client WebSocket error ${ex?.message}")
                preparingFlag.set(false)
            }
        }
    }

    fun initWebSocket() {
        val coinbaseUri = URI(WEB_SOCKET_URL)
        createWebSocketClient(coinbaseUri)

        val socketFactory: SSLSocketFactory = SSLSocketFactory.getDefault() as SSLSocketFactory
        webSocketClient!!.setSocketFactory(socketFactory)
        webSocketClient!!.connect()
        preparingFlag.set(true)
    }

    fun disconnectWebSocket() {
        webSocketClient!!.close()
    }

    companion object {
        const val staffType = "staff"
        const val noticeType = "notice"
        const val heartbeatType = "heartbeat"
    }
}