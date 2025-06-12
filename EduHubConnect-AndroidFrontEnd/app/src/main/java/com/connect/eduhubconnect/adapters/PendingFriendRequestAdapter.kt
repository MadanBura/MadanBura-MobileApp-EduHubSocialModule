package com.connect.eduhubconnect.adapters

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.connect.eduhubconnect.R
import com.connect.eduhubconnect.dto.FriendRequestDTO
import com.bumptech.glide.Glide
import com.connect.eduhubconnect.utils.CONSTANTS
import com.connect.eduhubconnect.utils.getTimeAgo
import java.time.format.DateTimeFormatter

class PendingFriendRequestAdapter(
    private val list: List<FriendRequestDTO>,
    private val onAcceptClick: (FriendRequestDTO) -> Unit,
    private val onRejectClick: (FriendRequestDTO) -> Unit
) : RecyclerView.Adapter<PendingFriendRequestAdapter.PFRViewClassHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PFRViewClassHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_receivedfriendreq, parent, false)
        return PFRViewClassHolder(view)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: PFRViewClassHolder, position: Int) {
        val request = list[position]

        holder.userName.text = request.senderName
        holder.bioUser.text = request.userBio

        // Format time
//        val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a")
        holder.reqReceivedTime.text = getTimeAgo(request.sentAt)


//       val imageUrl = request.senderProfileImage?.replace("localhost", "10.0.102.134")
        val fullImageUrl = if (request.senderProfileImage.contains("localhost"))
            request.senderProfileImage.replace("localhost", CONSTANTS.IP_ADD)
        else (request.senderProfileImage.startsWith("http"))
        "${request.senderProfileImage}"


        Glide.with(holder.itemView.context)
            .load(fullImageUrl)
            .into(holder.proPic)

        // Button click handlers
        holder.acceptBtn.setOnClickListener { onAcceptClick(request) }
        holder.rejectBtn.setOnClickListener { onRejectClick(request) }
    }

    override fun getItemCount() = list.size

    inner class PFRViewClassHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val proPic: ImageView = itemView.findViewById(R.id.proPic)
        val userName: TextView = itemView.findViewById(R.id.userName)
        val bioUser: TextView = itemView.findViewById(R.id.bioUser)
        val reqReceivedTime: TextView = itemView.findViewById(R.id.req_receivedTime)
        val acceptBtn: ImageView = itemView.findViewById(R.id.btnAccept)
        val rejectBtn: ImageView = itemView.findViewById(R.id.btnReject)
    }
}
