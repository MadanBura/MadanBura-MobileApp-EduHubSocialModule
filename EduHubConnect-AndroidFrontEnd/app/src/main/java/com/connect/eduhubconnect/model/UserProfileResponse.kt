package com.connect.eduhubconnect.model

import com.connect.eduhubconnect.dto.FriendRequestDTO
import com.connect.eduhubconnect.dto.PostDTO
import com.connect.eduhubconnect.dto.UserDTO

data class UserProfileResponse(
    val userProfile: UserDTO,
    val friends: List<UserDTO>,
    val feedPosts: List<PostDTO>,
    val pendingRequests: List<FriendRequestDTO>,
    val unread_notificationCount : Int = 0
)
