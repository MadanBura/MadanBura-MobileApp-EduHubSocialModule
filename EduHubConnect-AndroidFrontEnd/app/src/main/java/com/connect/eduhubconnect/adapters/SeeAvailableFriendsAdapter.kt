package com.connect.eduhubconnect.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.connect.eduhubconnect.R
import com.connect.eduhubconnect.dto.UserDTO
import com.connect.eduhubconnect.utils.CONSTANTS
import de.hdodenhof.circleimageview.CircleImageView

class SeeAvailableFriendsAdapter(
    private val list: List<UserDTO>,
    private val onConnectClick: (UserDTO) -> Unit
) : RecyclerView.Adapter<SeeAvailableFriendsAdapter.SeeAvailableViewClassHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeeAvailableViewClassHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.youknowlayout, parent, false)
        return SeeAvailableViewClassHolder(view)
    }

    override fun onBindViewHolder(holder: SeeAvailableViewClassHolder, position: Int) {
        val user = list[position]

        holder.tvName.text = user.name
        holder.tvRole.text = user.bio ?: "No bio"

        val fullImageUrl = if (user.profileImage.contains("localhost"))
            user.profileImage.replace("localhost", CONSTANTS.IP_ADD)
        else (user.profileImage.startsWith("http"))
            "${user.profileImage}"

        // Load profile image using Glide
        Glide.with(holder.itemView.context)
            .load(fullImageUrl)
            .into(holder.ivProfile)


        holder.btnConnect.setOnClickListener {
            onConnectClick(user)
        }
    }

    override fun getItemCount() = list.size

    inner class SeeAvailableViewClassHolder(item: View) : RecyclerView.ViewHolder(item) {
        val ivProfile: CircleImageView = item.findViewById(R.id.ivProfile)
        val tvName: TextView = item.findViewById(R.id.tvName)
        val tvRole: TextView = item.findViewById(R.id.tvRole)
        val btnConnect: Button = item.findViewById(R.id.btnConnect)
    }


//    fun updateConnect
}
