package com.connect.eduhubconnect.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "posts")
data class Post(
    @PrimaryKey val id: Long,
    val content: String,
    val imageUrl: String?,
    val createdAt: String,
    val userId: Long,
    val userName:String,
    val likeCount: Int,
    val commentCount: Int,
    val likedByUser: Boolean
)