package com.connect.eduhubconnect.datasource.remote

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
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query


interface EduHubApiService {

    @POST("auth/login")
    suspend fun login(
        @Body authRequest: AuthRequest
    ): Response<AuthResponse>


    @GET("dashboard/{userid}")
    suspend fun getUserDashboard(
        @Path("userid") id: Long
    ): Response<UserProfileResponse>

    @Multipart
    @POST("posts/user/{userId}")
    suspend fun createPost(
        @Path("userId") userId: Long,
        @Part("content") content: RequestBody,
        @Part image: MultipartBody.Part // The name "image" is already included in the part
    ): Response<PostResponse>


    @GET("friends/available/{userId}")
    suspend fun seeAllAvailable(
        @Path("userId") userId: Long
    ): Response<List<UserDTO>>


    @GET("profile/seeUserProfile/{userId}")
    suspend fun seeUserProfile(
        @Path("userId") userId: Long
    ): Response<UserProfileDTO>

    @POST("/api/likes/toggle")
    suspend fun toggleLike(
        @Query("userId") userId: Long,
        @Query("postId") postId: Long
    ): Response<String>

    // Endpoint to get the like count for a post
    @GET("/api/likes/count/{postId}")
    suspend fun getLikeCount(
        @Path("postId") postId: Long
    ): Response<Int>


    @POST("comments/add")
    suspend fun addComment(
        @Query("userId") userId: Long,
        @Query("postId") postId: Long,
        @Body content: String
    ): Response<CommentDTO>

    @GET("comments/post/{postId}")
    suspend fun getCommentsForPost(
        @Path("postId") postId: Long
    ): Response<List<CommentDTO>>


    @GET("notifications/{userId}")
    suspend fun getNotifications(
        @Path("userId") userId: Long
    ): Response<List<NotificationResponseItem>>

    @POST("notifications/saveFcmToken")
    suspend fun saveFcmToken(
        @Query("userId") userId: Long,
        @Query("fcmToken") fcmToken: String
    ): Response<ResponseBody>

    @GET("chat/recent/{email}")
    suspend fun getRecentChats(
        @Path("email") email: String
    ): Response<List<RecentChatRes>>

    // Get chat messages between two users
    @GET("chat/messages/{user1}/{user2}")
    suspend fun getMessagesBetweenUsers(
        @Path("user1") user1: String,
        @Path("user2") user2: String
    ): Response<List<ChatMessage>>

    @GET("users/getAlluser")
    suspend fun getAllUsers(@Query("userId") userId: Long): Response<List<UserDTO>>

    @GET("/api/keys/{username}")
    suspend fun getPublicKey(@Path("username") username: String): Response<String>


    @POST("friends/send")
    suspend fun sendFriendRequest(
        @Field("senderId") senderId: Long?,
        @Field("receiverId") receiverId: Long?
    ):Response<String>



}