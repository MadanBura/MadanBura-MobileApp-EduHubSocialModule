package com.connect.eduhubconnect.utils

import android.content.Context
import android.util.Base64
import android.util.Log
import com.connect.eduhubconnect.e2eeUtil.AESUtil
import com.connect.eduhubconnect.e2eeUtil.ChatMessageEncrypted
import com.connect.eduhubconnect.model.ChatMessage
import com.google.gson.Gson
import okhttp3.*

object WebSocketManager {
    private var webSocket: WebSocket? = null
    private val client by lazy { OkHttpClient() }
    private val gson = Gson()

//    private lateinit var context: Context
//    fun initialize(appContext: Context) {
//        context = appContext.applicationContext
//    }

    // Any class can implement this to receive messages:
    interface MessageListener {
        fun onMessage(message: ChatMessage)
        fun onOpen() = Unit
        fun onFailure(t: Throwable) = Unit
        fun onClosed() = Unit
    }

    // Thread‑safe set of listeners
    private val listeners = mutableSetOf<MessageListener>()

    /** Call once (e.g. from Application.onCreate or Login) */
    fun connect(token: String, context:Context) {
        if (webSocket != null) return  // already connected

        val req = Request.Builder()
            .url("ws://${CONSTANTS.BASEURL}/ws?token=$token")
            .addHeader("Authorization", "Bearer $token")
            .build()

        webSocket = client.newWebSocket(req, object : WebSocketListener() {
            override fun onOpen(ws: WebSocket, resp: Response) {
                listeners.forEach { it.onOpen() }
            }

            override fun onMessage(ws: WebSocket, text: String) {
                // parse and dispatch to everyone
                val msg = gson.fromJson(text, ChatMessage::class.java)
                listeners.forEach { it.onMessage(msg) }
//                val encrypted = Gson().fromJson(text, ChatMessageEncrypted::class.java)
//                val privateKey = RSAUtil.loadKeyPair(context)?.private ?: return
//                val aesKeyBytes = RSAUtil.decrypt(Base64.decode(encrypted.encryptedAesKey, Base64.DEFAULT), privateKey)
//                val aesKey = AESUtil.secretKeyFromBytes(aesKeyBytes)
//                val decryptedMessage = AESUtil.decrypt(encrypted.encryptedMessage, aesKey)
//                Log.d("ReceivedMessage", "From ${encrypted.sender}: $decryptedMessage")
//                val decryptedMsg = gson.fromJson(decryptedMessage, ChatMessage::class.java)
//                listeners.forEach { it.onMessage(decryptedMsg) }

            }

            override fun onFailure(ws: WebSocket, t: Throwable, resp: Response?) {
                listeners.forEach { it.onFailure(t) }
            }

            override fun onClosed(ws: WebSocket, code: Int, reason: String) {
                listeners.forEach { it.onClosed() }
            }
        })
    }

    fun sendMessage(message: ChatMessage) {
        val payload = gson.toJson(message)
        webSocket?.send(payload)
    }

    fun disconnect() {
        webSocket?.close(1000, null)
        webSocket = null
    }

    /** register to start receiving messages */
    fun registerListener(l: MessageListener) {
        listeners.add(l)
    }

    /** unregister whenever a screen goes away */
    fun unregisterListener(l: MessageListener) {
        listeners.remove(l)
    }
}
