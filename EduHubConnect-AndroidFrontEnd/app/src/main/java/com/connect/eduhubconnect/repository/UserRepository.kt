package com.connect.eduhubconnect.repository

import com.connect.eduhubconnect.dto.CommentDTO
import com.connect.eduhubconnect.dto.RecentChatRes
import com.connect.eduhubconnect.dto.UserDTO
import com.connect.eduhubconnect.dto.UserProfileDTO
import com.connect.eduhubconnect.model.AuthRequest
import com.connect.eduhubconnect.model.AuthResponse
import com.connect.eduhubconnect.model.ChatMessage
import com.connect.eduhubconnect.model.NotificationResponseItem
import com.connect.eduhubconnect.model.PostResponse
import com.connect.eduhubconnect.model.UserProfileResponse
import com.connect.eduhubconnect.utils.ResultState
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response

interface UserRepository {
    suspend fun login(authRequest: AuthRequest): Response<AuthResponse>
    suspend fun getDashboard(userId: Long): Response<UserProfileResponse>
    suspend fun uploadPost(
        userId: Long,
        content: RequestBody,
        image: MultipartBody.Part,
    ): Response<PostResponse>

    //network
    suspend fun seeAllAvailable(userId: Long) : Response<List<UserDTO>>
    suspend fun seeUserProfile(userId: Long):Response<UserProfileDTO>


    //like
    suspend fun toggleLike(userId: Long, postId: Long): Response<String>
    suspend fun getLikeCount(postId: Long): Response<Int>

    //comment
    suspend fun addComment(userId: Long, postId: Long, content: String): Response<CommentDTO>
    suspend fun getCommentsForPost(postId: Long): Response<List<CommentDTO>>

    //
//    suspend fun getFriendsOfUser(userId: Long): List<Long>
    suspend fun getNotifications(userId: Long) :Response<List<NotificationResponseItem>>

    suspend fun saveFcmToken(userId: Long, fcmToken:String):Response<ResponseBody>

    suspend fun getRecentChats(username: String): ResultState<List<RecentChatRes>>
    suspend fun getMessagesBetweenUsers(user1: String, user2: String): ResultState<List<ChatMessage>>
    suspend fun sendChatMessage(chatMessage: ChatMessage): ResultState<ChatMessage>

    suspend fun getAllUsers(userId: Long) : Response<List<UserDTO>>

    suspend fun getPublicKey(useremail:String):Response<String>

    suspend fun sendFriendRequest(senderId:Long, receiverId:Long):Response<String>

//    fun parseWebSocketMessage(json:String) : ChatMessage
}