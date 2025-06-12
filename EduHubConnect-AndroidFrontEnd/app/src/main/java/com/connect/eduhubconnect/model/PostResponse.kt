package com.connect.eduhubconnect.model

import com.connect.eduhubconnect.dto.CommentDTO
import com.connect.eduhubconnect.dto.UserDTO

data class PostResponse(
    val commentCount: Int,
    val comments: List<CommentDTO>,
    val content: String,
    val createdAt: String,
    val id: Int,
    val imageUrl: String,
    val likeCount: Int,
    val likedBy: List<UserDTO>,
    val likedByUser: Boolean,
    val userId: Int,
    val userName: String
)