package com.connect.eduhubconnect.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.connect.eduhubconnect.dto.CommentDTO
import com.connect.eduhubconnect.dto.UserDTO
import com.connect.eduhubconnect.dto.UserProfileDTO
import com.connect.eduhubconnect.model.AuthRequest
import com.connect.eduhubconnect.model.AuthResponse
import com.connect.eduhubconnect.model.UserProfileResponse
import com.connect.eduhubconnect.repository.UserRepository
import com.connect.eduhubconnect.utils.ResultState
import com.connect.eduhubconnect.utils.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _loginState = MutableStateFlow<ResultState<AuthResponse>>(ResultState.Loading)
    val loginState: StateFlow<ResultState<AuthResponse>> = _loginState

    private val _dashboardState = MutableStateFlow<ResultState<UserProfileResponse>>(ResultState.Loading)
    val dashboardState: StateFlow<ResultState<UserProfileResponse>> = _dashboardState

    private val _uploadStatus = MutableStateFlow<ResultState<String>>(ResultState.Loading)
    val uploadStatus: StateFlow<ResultState<String>> get() = _uploadStatus

    private val _friendList = MutableStateFlow<ResultState<List<UserDTO>>>(ResultState.Loading)
    val friendList : StateFlow<ResultState<List<UserDTO>>>get() = _friendList

    private val _userProfile = MutableStateFlow<ResultState<UserProfileDTO>>(ResultState.Loading)
    val userProfile : StateFlow<ResultState<UserProfileDTO>>get() = _userProfile

    private val _likeState = MutableStateFlow<ResultState<String>>(ResultState.Loading)
    val likeState: StateFlow<ResultState<String>> = _likeState

    private val _likeCountState = MutableStateFlow<ResultState<Int>>(ResultState.Loading)
    val likeCountState: StateFlow<ResultState<Int>> = _likeCountState

    private val _comments = MutableStateFlow<ResultState<List<CommentDTO>>>(ResultState.Loading)
    val comments: StateFlow<ResultState<List<CommentDTO>>> get() = _comments

    private val _friends = MutableStateFlow<Set<Long>>(emptySet())
    val friends: StateFlow<Set<Long>> get() = _friends

    private val _userList = MutableStateFlow<ResultState<List<UserDTO>>>(ResultState.Loading)
    val userList : StateFlow<ResultState<List<UserDTO>>> get() = _userList


    private val _userPublicKey = MutableLiveData<ResultState<String>>(ResultState.Loading)
    val userPublicKey :LiveData<ResultState<String>> get() = _userPublicKey


    private val _sendFriendReqRes = MutableStateFlow<ResultState<String>>(ResultState.Loading)
    val sendFriendReqRes : StateFlow<ResultState<String>> get() = _sendFriendReqRes

    fun login(authRequest: AuthRequest) {
        viewModelScope.launch {
            _loginState.value = ResultState.Loading
            try {
                val response = userRepository.login(authRequest)
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        _loginState.value = ResultState.Success(body)
                    } else {
                        _loginState.value = ResultState.Error(Throwable("Empty response"))
                    }
                } else {
                    _loginState.value = ResultState.Error(Throwable("Login failed: ${response.message()}"))
                }
            } catch (e: Exception) {
                _loginState.value = ResultState.Error(e)
            }
        }
    }

    fun getDashboard(userId: Long) {
        viewModelScope.launch {
            _dashboardState.value = ResultState.Loading
            try {
                val response = userRepository.getDashboard(userId)
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        _dashboardState.value = ResultState.Success(body)
                    } else {
                        _dashboardState.value = ResultState.Error(Throwable("Empty dashboard response"))
                    }
                } else {
                    _dashboardState.value = ResultState.Error(Throwable("Failed: ${response.message()}"))
                }
            } catch (e: Exception) {
                _dashboardState.value = ResultState.Error(e)
            }
        }
    }


    fun uploadPost(userId: Long,  content: RequestBody, image: MultipartBody.Part){
        viewModelScope.launch {
            _uploadStatus.value = ResultState.Loading
            try {
                val response = userRepository.uploadPost(userId, content, image)
                if (response.isSuccessful) {
                    _uploadStatus.value = ResultState.Success("Post uploaded successfully!")
                } else {
                    _uploadStatus.value = ResultState.Error(Throwable("Upload failed: ${response.message()}"))
                }
            } catch (e: Exception) {
                _uploadStatus.value = ResultState.Error(Throwable("Upload failed: ${e.message}"))
            }
        }
    }

    fun seeAllUsersAvailable(userId: Long){
        viewModelScope.launch {
            _friendList.value = ResultState.Loading
            try {
                val response = userRepository.seeAllAvailable(userId)
                if (response.isSuccessful) {
                    _friendList.value = ResultState.Success(response.body()!!)
                } else {
                    _friendList.value = ResultState.Error(Throwable("Unable to fetch: ${response.message()}"))
                }
            }catch (e : Exception){
                _friendList.value = ResultState.Error(Throwable("Unable to fetch: ${e.message}"))
            }

        }
    }

    fun getUserProfile(userId: Long){
        viewModelScope.launch {
            _userProfile.value = ResultState.Loading
            try {
                val response = userRepository.seeUserProfile(userId)
                if (response.isSuccessful) {
                    _userProfile.value = ResultState.Success(response.body()!!)
                } else {
                    _userProfile.value = ResultState.Error(Throwable("Unable to fetch: ${response.message()}"))
                }
            }catch (e : Exception){
                _userProfile.value = ResultState.Error(Throwable("Unable to fetch: ${e.message}"))
            }
        }
    }


    fun toggleLike(userId: Long, postId: Long) {
        viewModelScope.launch {
            _likeState.value = ResultState.Loading
            try {
                // Call the repository to toggle like
                val response = userRepository.toggleLike(userId, postId)
                if (response.isSuccessful) {
                    _likeState.value = ResultState.Success("Like toggled successfully!")
                } else {
                    _likeState.value = ResultState.Error(Throwable("Failed to toggle like: ${response.message()}"))
                }
            } catch (e: Exception) {
                _likeState.value = ResultState.Error(Throwable(e.localizedMessage))
            }
        }
    }

    // Function to get like count for a post
    fun getLikeCount(postId: Long) {
        viewModelScope.launch {
            _likeCountState.value = ResultState.Loading
            try {
                // Call the repository to get like count
                val response = userRepository.getLikeCount(postId)
                if (response.isSuccessful) {
                    response.body()?.let {
                        _likeCountState.value = ResultState.Success(it)
                    } ?: run {
                        _likeCountState.value = ResultState.Error(Throwable("Failed to fetch like count: Empty response"))
                    }
                } else {
                    _likeCountState.value = ResultState.Error(Throwable("Failed to fetch like count: ${response.message()}"))

                }
            } catch (e: Exception) {
                _likeCountState.value = ResultState.Error(Throwable(e.localizedMessage))
            }
        }
    }

    fun addComment(userId: Long, postId: Long, content: String) {
        viewModelScope.launch {
            _comments.value = ResultState.Loading
            try {
                // Add the comment via the repository
                val comment = userRepository.addComment(userId, postId, content)

                // Handle success
                if (comment != null) {
                    _comments.value = ResultState.Success(listOf(comment.body()!!))  // Update with the new comment
                } else {
                    _comments.value = ResultState.Error(Exception("Failed to add comment"))
                }
            } catch (e: Exception) {
                _comments.value = ResultState.Error(e) // Handle error
            }
        }
    }

    // Function to fetch comments for a post
    fun getCommentsForPost(postId: Long) {
        viewModelScope.launch {
            _comments.value = ResultState.Loading
            try {
                // Fetch the comments via the repository
                val comments = userRepository.getCommentsForPost(postId)

                // Handle success
                if (comments?.body() != null && comments.isSuccessful) {
                    _comments.value = ResultState.Success(comments.body()!!) // Update with fetched comments
                } else {
                    _comments.value = ResultState.Error(Exception("No comments available"))
                }
            } catch (e: Exception) {
                _comments.value = ResultState.Error(e) // Handle error
            }
        }
    }


    fun getAllUsers() {
        viewModelScope.launch {
            _userList.value = ResultState.Loading
            try {
                val userId = tokenManager.getUserId()
                val response = userId?.let { userRepository.getAllUsers(it) }
                if (response?.isSuccessful == true && response?.body() != null) {
                    _userList.value = ResultState.Success(response.body()!!)
                } else {
                    _userList.value = ResultState.Error(Throwable("Failed to fetch users: ${response?.message()}"))
                }
            } catch (e: Exception) {
                _userList.value = ResultState.Error(Throwable("Exception occurred: ${e.localizedMessage}"))
            }
        }
    }


    fun getUserPublicKey(userEmail:String){
        viewModelScope.launch {
            _userPublicKey.value = ResultState.Loading
            try {
                val response = userRepository.getPublicKey(userEmail)

                if(response.isSuccessful && response.body()!=null){
                    _userPublicKey.value = ResultState.Success(response.body()!!)
                }else{
                    _userProfile.value = ResultState.Error(Throwable("KEY NOT FOUND"))
                }
            }catch (e:Exception){
                _userPublicKey.value =ResultState.Error(Throwable("KEY NOT FOUND ${e.message}"))
            }
        }
    }


    fun sendFriendRequest(senderId:Long,receiverId:Long){
        viewModelScope.launch {
            _sendFriendReqRes.value=ResultState.Loading
            try {
                val response = userRepository.sendFriendRequest(senderId, receiverId)

                if(response.isSuccessful && response.body()!=null){
                    _sendFriendReqRes.value = ResultState.Success(response.body()!!)
                }else{
                    _sendFriendReqRes.value = ResultState.Error(Throwable("Sorry Unable to send req:"))
                }
            }catch (e:Exception){
                _sendFriendReqRes.value = ResultState.Error(Throwable(e.message))
            }

        }
    }


}
