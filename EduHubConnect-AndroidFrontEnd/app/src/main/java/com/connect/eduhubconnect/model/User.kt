package com.connect.eduhubconnect.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey val id: Long,
    val name: String,
    val email: String,
    val bio: String,
    val profileImage: String
)