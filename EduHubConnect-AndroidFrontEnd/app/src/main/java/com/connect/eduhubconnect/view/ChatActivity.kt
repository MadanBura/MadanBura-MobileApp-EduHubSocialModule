package com.connect.eduhubconnect.view

import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.connect.eduhubconnect.R
import com.connect.eduhubconnect.adapters.ChatAdapter
import com.connect.eduhubconnect.model.ChatMessage
import com.connect.eduhubconnect.utils.ResultState
import com.connect.eduhubconnect.viewmodel.ChatViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDateTime

@AndroidEntryPoint
class ChatActivity : AppCompatActivity() {

    private val vm: ChatViewModel by viewModels()
    private val messages = mutableListOf<ChatMessage>()
    private lateinit var adapter: ChatAdapter
    private lateinit var senderEmail: String
    private lateinit var recipientEmail: String

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        senderEmail = intent.getStringExtra("senderEmail").orEmpty()
        recipientEmail = intent.getStringExtra("recipientEmail").orEmpty()

        adapter = ChatAdapter(messages, senderEmail)
        findViewById<RecyclerView>(R.id.recyclerView).apply {
            layoutManager = LinearLayoutManager(this@ChatActivity)
            adapter = this@ChatActivity.adapter
        }

        vm.fetchMessagesBetweenUsers(senderEmail, recipientEmail)
        vm.chatMessagesLiveData.observe(this) { result ->
            if (result is ResultState.Success) {
                messages.clear()
                messages.addAll(result.data)
                adapter.notifyDataSetChanged()
                scrollToBottom()
            }
        }

        vm.newIncomingMessage.observe(this) { msg ->
            if ((msg.sender == senderEmail && msg.recipient == recipientEmail) ||
                (msg.sender == recipientEmail && msg.recipient == senderEmail)) {
                messages.add(msg)
                adapter.notifyItemInserted(messages.lastIndex)
                scrollToBottom()
            }
        }

        findViewById<Button>(R.id.sendButton).setOnClickListener {
            val edit = findViewById<EditText>(R.id.messageEditText)
            val text = edit.text.toString().trim()
            if (text.isNotEmpty()) {
                val chat = ChatMessage(senderEmail, recipientEmail, text, LocalDateTime.now().toString())
                vm.sendMessage(chat)
                messages.add(chat)
                adapter.notifyItemInserted(messages.lastIndex)
                scrollToBottom()
                edit.text.clear()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        vm.registerSocketListener()
    }

    override fun onStop() {
        vm.unregisterSocketListener()
        super.onStop()
    }

    private fun scrollToBottom() {
        findViewById<RecyclerView>(R.id.recyclerView)
            .scrollToPosition(messages.lastIndex)
    }
}
