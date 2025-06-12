package com.connect.eduhubconnect.adapters

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.connect.eduhubconnect.R
import com.connect.eduhubconnect.model.ChatMessage
import com.connect.eduhubconnect.utils.getTimeAgo

class ChatAdapter(
    private var messages: List<ChatMessage>,
    private val sender: String
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_SENT = 1
    private val VIEW_TYPE_RECEIVED = 2

    override fun getItemViewType(position: Int): Int {
        return if (messages[position].sender == sender) VIEW_TYPE_SENT else VIEW_TYPE_RECEIVED
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(
            if (viewType == VIEW_TYPE_SENT) R.layout.item_container_sentmessage
            else R.layout.item_container_messagereceived,
            parent,
            false
        )
        return if (viewType == VIEW_TYPE_SENT) SentViewHolder(view) else ReceivedViewHolder(view)
    }

    override fun getItemCount(): Int = messages.size

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messages[position]
        if (holder is SentViewHolder) {
            holder.bind(message)
        } else if (holder is ReceivedViewHolder) {
            holder.bind(message)
        }
    }

    fun updateMessages(newMessages: List<ChatMessage>) {
        this.messages = newMessages
        notifyDataSetChanged()
    }

    inner class SentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val messageTextView: TextView = itemView.findViewById(R.id.textMessageSent)
        private val timeTextView: TextView = itemView.findViewById(R.id.textDateTime)

        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(msg: ChatMessage) {
            messageTextView.text = msg.content
            timeTextView.text = formatTime(msg.timestamp)
        }
    }

    inner class ReceivedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val messageTextView: TextView = itemView.findViewById(R.id.textMessage)
        private val timeTextView: TextView = itemView.findViewById(R.id.textDateTime)

        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(msg: ChatMessage) {
            messageTextView.text = msg.content
            timeTextView.text = formatTime(msg.timestamp)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun formatTime(timestamp: String): String {
        return getTimeAgo(timestamp) // Replace with real formatting if needed
    }
}
