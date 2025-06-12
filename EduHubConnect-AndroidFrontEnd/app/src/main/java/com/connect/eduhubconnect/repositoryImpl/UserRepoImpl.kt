package com.connect.eduhubconnect.repositoryImpl

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.connect.eduhubconnect.datasource.remote.EduHubApiService
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
import com.connect.eduhubconnect.repository.UserRepository
import com.connect.eduhubconnect.utils.ResultState
import com.connect.eduhubconnect.utils.TokenManager
//import com.connect.eduhubconnect.utils.WebSocketManager
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import org.json.JSONObject
import retrofit2.Response
import java.time.LocalDateTime
import javax.inject.Inject

class UserRepoImpl @Inject constructor(
    private val eduHubApiService: EduHubApiService,
    private val tokenManager: TokenManager
) : UserRepository {



    override suspend fun login(authRequest: AuthRequest): Response<AuthResponse> {
        val response = eduHubApiService.login(authRequest)
        return if (response.isSuccessful) {
            response.body()?.let { tokenResponse ->
                tokenManager.saveTokens(tokenResponse.token)
                tokenManager.saveUserId(tokenResponse.userId)
                Response.success(tokenResponse)
            } ?: Response.error(response.code(), response.errorBody()!!)
        } else {
            Response.error(response.code(), response.errorBody()!!)
        }
    }


    override suspend fun getDashboard(userId: Long): Response<UserProfileResponse> {
        val response = eduHubApiService.getUserDashboard(userId)
        return if (response.isSuccessful) {
            Response.success(response.body())
        } else {
            Response.error(response.code(), response.errorBody()!!)
        }
    }

    override suspend fun uploadPost(
        userId: Long,
        content: RequestBody,
        image: MultipartBody.Part
    ): Response<PostResponse> {
        val response = eduHubApiService.createPost(userId, content, image)
        return if (response.isSuccessful) {
            Response.success(response.body())
        } else {
            Response.error(response.code(), response.errorBody()!!)
        }
    }

    override suspend fun seeAllAvailable(userId: Long): Response<List<UserDTO>> {
        val response = eduHubApiService.seeAllAvailable(userId)
        return if (response.isSuccessful) {
            Response.success(response.body())
        } else {
            Response.error(response.code(), response.errorBody()!!)
        }
    }


    override suspend fun seeUserProfile(userId: Long): Response<UserProfileDTO> {
        val response = eduHubApiService.seeUserProfile(userId)
        return if (response.isSuccessful) {
            Response.success(response.body())
        } else {
            Response.error(response.code(), response.errorBody()!!)
        }
    }


    override suspend fun toggleLike(userId: Long, postId: Long): Response<String> {
        val response = eduHubApiService.toggleLike(userId, postId)
        return if (response.isSuccessful) {
            Response.success(response.body())
        } else {
            Response.error(response.code(), response.errorBody()!!)
        }
    }

    override suspend fun getLikeCount(postId: Long): Response<Int> {
        val response = eduHubApiService.getLikeCount(postId)
        return if (response.isSuccessful) {
            Response.success(response.body())
        } else {
            Response.error(response.code(), response.errorBody()!!)
        }
    }

    override suspend fun addComment(
        userId: Long,
        postId: Long,
        content: String
    ): Response<CommentDTO> {
        val response: Response<CommentDTO> = eduHubApiService.addComment(userId, postId, content)
        return if (response.isSuccessful) {
            Response.success(response.body())

        } else {
            Response.error(response.code(), response.errorBody()!!)
        }
    }

    override suspend fun getCommentsForPost(postId: Long): Response<List<CommentDTO>> {
        val response: Response<List<CommentDTO>> = eduHubApiService.getCommentsForPost(postId)
        return if (response.isSuccessful) {
            Response.success(response.body())
        } else {
            Response.error(response.code(), response.errorBody()!!)
        }
    }

//    override suspend fun getFriendsOfUser(userId: Long): List<Long> {
//
//    }

    override suspend fun getNotifications(userId: Long): Response<List<NotificationResponseItem>> {
        val response: Response<List<NotificationResponseItem>> =
            eduHubApiService.getNotifications(userId)
        return if (response.isSuccessful) {
            Response.success(response.body())
        } else {
            Response.error(response.code(), response.errorBody()!!)
        }
    }

    override suspend fun saveFcmToken(userId: Long, fcmToken: String): Response<ResponseBody> {
        val response: Response<ResponseBody> = eduHubApiService.saveFcmToken(userId, fcmToken)
        return if (response.isSuccessful) {
            val result = response.body()

            response
        } else {
            val errorBody = response.errorBody()?.string() ?: "Unknown error"
            Log.e("saveFcmToken", "Error: $errorBody")
            Response.error(response.code(), response.errorBody() ?: "".toResponseBody(null))
        }
    }

    suspend override fun getRecentChats(username: String): ResultState<List<RecentChatRes>> {
        return try {
            val response = eduHubApiService.getRecentChats(username)
            if (response.isSuccessful) {
                response.body()?.let {
                    ResultState.Success(it)
                } ?: ResultState.Error((Throwable("No recent chats found.")))
            } else {
                ResultState.Error(Throwable("Failed to fetch recent chats."))
            }
        } catch (e: Exception) {
            ResultState.Error(Throwable("An error occurred: ${e.message}"))
        }
    }

    suspend override fun getMessagesBetweenUsers(
        user1: String,
        user2: String
    ): ResultState<List<ChatMessage>> {
        return try {
            val response = eduHubApiService.getMessagesBetweenUsers(user1, user2)
            if (response.isSuccessful) {
                response.body()?.let {
                    ResultState.Success(it)
                } ?: ResultState.Error(Throwable("No messages found between users."))
            } else {
                ResultState.Error(Throwable("Failed to fetch messages between users."))
            }
        } catch (e: Exception) {
            ResultState.Error(Throwable(e.message))
        }
    }

    override suspend fun sendChatMessage(chatMessage: ChatMessage): ResultState<ChatMessage> {
        return try {
            // Use the WebSocketManager or your backend API to send the message
//            WebSocketManager.sendMessage(chatMessage)  // If you are sending via WebSocket
            ResultState.Success(chatMessage)  // Return the sent message
        } catch (e: Exception) {
            ResultState.Error(Throwable("Failed to send message: ${e.message}"))
        }
    }

    override suspend fun getAllUsers(userId: Long): Response<List<UserDTO>> {
        val response: Response<List<UserDTO>> =
            eduHubApiService.getAllUsers(userId)
        return if (response.isSuccessful) {
            Response.success(response.body())
        } else {
            Response.error(response.code(), response.errorBody()!!)
        }
    }

//    @RequiresApi(Build.VERSION_CODES.O)
//    override fun parseWebSocketMessage(json: String): ChatMessage {
//        val jsonObject = JSONObject(json)
//        return ChatMessage(
//            sender = jsonObject.getString("sender"),
//            recipient = jsonObject.getString("recipient"),
//            content = jsonObject.getString("content"),
//            timestamp = LocalDateTime.now().toString()
//        )
//    }


    override suspend fun getPublicKey(useremail: String): Response<String> {
        val response = eduHubApiService.getPublicKey(useremail)
        return if(response.isSuccessful){
            Response.success(response.body())
        }else{
            Response.error(response.code(), response.errorBody())
        }
    }


    override suspend fun sendFriendRequest(senderId: Long, receiverId: Long): Response<String> {
        val res = eduHubApiService.sendFriendRequest(senderId, receiverId)
        return if(res.isSuccessful){
            Response.success(res.body())
        }else{
            Response.error(res.code(), res.errorBody())
        }
    }
}