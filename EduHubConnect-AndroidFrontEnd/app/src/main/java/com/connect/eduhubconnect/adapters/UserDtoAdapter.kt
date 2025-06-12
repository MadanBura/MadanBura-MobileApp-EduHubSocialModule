package com.connect.eduhubconnect.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.connect.eduhubconnect.R
import com.connect.eduhubconnect.dto.UserDTO

class UserDtoAdapter(
    private val onUserClick: (UserDTO) -> Unit
) : ListAdapter<UserDTO, UserDtoAdapter.UserViewHolder>(UserDiffCallback()) {

    private var fullUserList: List<UserDTO> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_singleuser, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user)
        holder.itemView.setOnClickListener { onUserClick(user) }
    }

    override fun submitList(list: List<UserDTO>?) {
        fullUserList = list ?: emptyList()
        super.submitList(ArrayList(fullUserList)) // prevent mutation issues
    }

    fun filterList(query: String) {
        val filtered = if (query.isEmpty()) {
            fullUserList
        } else {
            fullUserList.filter { it.name.contains(query, ignoreCase = true) }
        }
        super.submitList(ArrayList(filtered))
    }

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val usernameTextView: TextView = itemView.findViewById(R.id.username)

        fun bind(user: UserDTO) {
            usernameTextView.text = user.name
        }
    }

    class UserDiffCallback : DiffUtil.ItemCallback<UserDTO>() {
        override fun areItemsTheSame(oldItem: UserDTO, newItem: UserDTO): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: UserDTO, newItem: UserDTO): Boolean {
            return oldItem == newItem
        }
    }
}
