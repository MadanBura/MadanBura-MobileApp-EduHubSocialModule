package com.connect.eduhubconnect.dto

data class PostDTO(
    val id: Long,
    val content: String,
    val imageUrl: String?,
    val createdAt: String,
    val userId: Long,
    val userName:String,

    var likeCount: Int,
    val commentCount: Int,
    var likedByUser: Boolean,

    val likedBy: List<UserDTO>,
    val comments: List<CommentDTO>
)
