package com.connect.eduhubconnect.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.connect.eduhubconnect.R
import com.connect.eduhubconnect.dto.SelfPostDTO
import com.connect.eduhubconnect.utils.CONSTANTS

class SelfPostAdapter(private val postList: List<SelfPostDTO>) :
    RecyclerView.Adapter<SelfPostAdapter.SelfPostViewHolder>() {

    class SelfPostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val postImage: ImageView = itemView.findViewById(R.id.selfPostImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelfPostViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_selfposts, parent, false)
        return SelfPostViewHolder(view)
    }

    override fun onBindViewHolder(holder: SelfPostViewHolder, position: Int) {
        val post = postList[position]

        val fullImageUrl = if (post.imageUrl.contains("localhost"))
            post.imageUrl.replace("localhost", CONSTANTS.IP_ADD)
        else (post.imageUrl.startsWith("http"))
        "${post.imageUrl}"

        Glide.with(holder.itemView.context)
            .load(fullImageUrl)
            .centerCrop()
            .into(holder.postImage)
    }

    override fun getItemCount(): Int = postList.size
}
