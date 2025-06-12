package com.connect.eduhubconnect.adapters

import android.os.Build
import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import com.connect.eduhubconnect.model.RecentChat
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.connect.eduhubconnect.R
import com.connect.eduhubconnect.dto.RecentChatRes
import com.connect.eduhubconnect.utils.CONSTANTS
import com.connect.eduhubconnect.utils.getTimeAgo

class RecentChatAdapter(
    private val recentChats: List<RecentChatRes>,
    val onClick: (RecentChatRes) -> Unit
) :
    RecyclerView.Adapter<RecentChatAdapter.RecentChatViewHolder>() {

    inner class RecentChatViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val userProPic: ImageView = view.findViewById(R.id.recentUserProPic)
        val userNameTextView: TextView = view.findViewById(R.id.recentUserName)
        val lastMessageTextView: TextView = view.findViewById(R.id.lastMessage)
        val timeTextView: TextView = view.findViewById(R.id.timestamp)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentChatViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_recentchat, parent, false)
        return RecentChatViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: RecentChatViewHolder, position: Int) {
        val recentChat = recentChats[position]
        holder.userNameTextView.text = recentChat.username
        holder.lastMessageTextView.text = recentChat.lastMessage
        holder.timeTextView.text = getTimeAgo(recentChat.timestamp)


        val profilePic = recentChat.profilePicUrl?.replace("localhost", CONSTANTS.IP_ADD)

        Glide.with(holder.itemView.context)
            .load(profilePic)
            .into(holder.userProPic)

        holder.itemView.setOnClickListener {
            onClick(recentChat) // Trigger the click event to navigate to the ChatActivity
        }
    }

    override fun getItemCount(): Int = recentChats.size
}
