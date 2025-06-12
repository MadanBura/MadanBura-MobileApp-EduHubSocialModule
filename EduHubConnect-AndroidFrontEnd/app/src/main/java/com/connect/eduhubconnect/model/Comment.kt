package com.connect.eduhubconnect.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "comments")
data class Comment(
    @PrimaryKey val id: Long,
    val content: String,
    val userId: Long,
    val postId: Long,
    val userName: String,
    val userProfileImage:String,
    val userJobTitle:String,
    val createdAt: String,
    val updatedAt:String
)
