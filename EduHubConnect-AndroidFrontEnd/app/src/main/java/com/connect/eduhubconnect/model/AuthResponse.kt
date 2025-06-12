package com.connect.eduhubconnect.model

data class AuthResponse(
    val code: Int,
    val userId:Long,
    val message: String,
    val token: String
)