package com.connect.eduhubconnect.adapters

import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import com.connect.eduhubconnect.model.NotificationResponseItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.connect.eduhubconnect.R
import com.connect.eduhubconnect.utils.CONSTANTS

class NotificationAdapter(
    private val notificationList: List<NotificationResponseItem> // Assuming Notification is a data class
) : RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

    inner class NotificationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userProfileImage: ImageView = itemView.findViewById(R.id.userProfileImage)
        val notificationText: TextView = itemView.findViewById(R.id.notificationText)
        val notificationCard : CardView = itemView.findViewById(R.id.notificationCard)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_notification, parent, false)
        return NotificationViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val notification = notificationList[position]

        // Set profile image (use Glide or Picasso to load image dynamically)

        val profileImageUrl = notification.profileImage?.replace("localhost", CONSTANTS.IP_ADD)

        Glide.with(holder.itemView.context)
            .load(profileImageUrl)
            .circleCrop()
            .into(holder.userProfileImage)

        // Set notification text
        holder.notificationText.text = notification.message

        if (!notification.read) {
            // If the notification is not read, set the background color to primaryContainer
            holder.notificationCard.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.primaryContainer))
        } else {
            // If the notification is read, set the background color to a different color (e.g., default background color)
            holder.notificationCard.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.white)) // Use the default background color
        }

    }

    override fun getItemCount(): Int = notificationList.size
}
