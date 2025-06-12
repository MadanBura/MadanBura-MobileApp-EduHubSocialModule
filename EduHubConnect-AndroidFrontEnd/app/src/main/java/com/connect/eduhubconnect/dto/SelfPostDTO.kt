package com.connect.eduhubconnect.dto

data class SelfPostDTO(
    val commentCount: Int,
    val comments: List<Any>,
    val content: String,
    val createdAt: String,
    val id: Int,
    val imageUrl: String,
    val likeCount: Int,
    val likedBy: List<Any>,
    val likedByUser: Boolean,
    val userId: Int,
    val userName: String
)