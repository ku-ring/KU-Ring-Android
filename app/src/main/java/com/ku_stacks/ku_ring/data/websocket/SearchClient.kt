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

    private lateinit var webSocketClient: WebSocketClient

    private var staffList: StaffListResponse = StaffListResponse(
        isSuccess = false,
        resultMsg = "",
        resultCode = 0,
        staffList = emptyList()
    )


    fun subscribeStaff(): Flowable<StaffListResponse> {

        return Flowable.interval(300, TimeUnit.MILLISECONDS)
            .flatMap {
                Flowable.just(staffList)
            }
            //.distinctUntilChanged()

//        return Flowable.create({ emitter ->
//            emitter?.onNext(staffList)
//            Thread.sleep(300)
//            emitter.onNext(staffList)
//        }, BackpressureStrategy.LATEST)
    }

    fun searchStaff(keyword: String) {

        webSocketClient.send(
            Gson().toJson(
                StaffRequest(type = "search", content = keyword)
            )
        )

        subscribeStaff().subscribeOn(Schedulers.io())
            .subscribe({
                if(it.isSuccess)
                    Timber.e("subscribing data : ${it.staffList[0].email}")
            }, {
                Timber.e("subscribe error : $it")
            })
    }

    private fun createWebSocketClient(coinbaseUri: URI) {
        webSocketClient = object : WebSocketClient(coinbaseUri) {
            override fun onOpen(handshakedata: ServerHandshake?) {
                Timber.e("WebSocket onOpen")
            }

            override fun onMessage(message: String?) {
                Timber.e("WebSocket onMessage : $message")
                staffList = Gson().fromJson(message, StaffListResponse::class.java)
            }

            override fun onClose(code: Int, reason: String?, remote: Boolean) {
                Timber.e("WebSocket onClose : $code, $reason, $remote")
                //unsubscribe()

            }

            override fun onError(ex: Exception?) {
                Timber.e("WebSocket error ${ex?.message}")

            }

        }

    }

    fun initWebSocket() {
        val coinbaseUri: URI = URI(Companion.url)
        createWebSocketClient(coinbaseUri)

        val socketFactory: SSLSocketFactory = SSLSocketFactory.getDefault() as SSLSocketFactory
        webSocketClient.setSocketFactory(socketFactory)
        webSocketClient.connect()
    }

    companion object {
        const val url = "wss://kuring-dev.herokuapp.com/kuring/staff"
    }


}