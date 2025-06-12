package com.connect.eduhubconnect.dto

data class CommentDTO(
    val id: Long,
    val content: String,
    val userId: Long,
    val postId: Long,
    val userName: String,
    val userProfileImage:String,
    val userJobTitle:String,
    val createdAt: String,
    val updatedAt:String
)
