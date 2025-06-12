package com.connect.eduhubconnect.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.connect.eduhubconnect.model.NotificationResponseItem
import com.connect.eduhubconnect.repository.UserRepository
import com.connect.eduhubconnect.utils.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _notifications = MutableStateFlow<ResultState<List<NotificationResponseItem>>>(ResultState.Loading)
    val notifications: StateFlow<ResultState<List<NotificationResponseItem>>> get() = _notifications

    private val _saveFcmTokenStatus = MutableStateFlow<ResultState<String>>(ResultState.Loading)
    val saveFcmTokenStatus: StateFlow<ResultState<String>> get() = _saveFcmTokenStatus

    // Fetch notifications for a user
    fun getNotifications(userId: Long) {
        viewModelScope.launch {
            _notifications.value = ResultState.Loading
            try {
                val response = userRepository.getNotifications(userId)

                if (response.isSuccessful && response.body() != null) {
                    _notifications.value = ResultState.Success(response.body()!!)
                } else {
                    _notifications.value = ResultState.Error(Throwable(response.message()))
                }
            } catch (e: Exception) {
                _notifications.value = ResultState.Error(Throwable(e.message))
            }
        }
    }

    // Save FCM Token for the user
    fun saveFcmToken(userId: Long, fcmToken: String) {
        viewModelScope.launch {
            _saveFcmTokenStatus.value = ResultState.Loading
            try {
                val response = userRepository.saveFcmToken(userId, fcmToken)

                if (response.isSuccessful) {
                    _saveFcmTokenStatus.value = ResultState.Success("FCM Token saved successfully!")
                } else {
                    _saveFcmTokenStatus.value = ResultState.Error(Throwable(response.message()))
                }
            } catch (e: Exception) {
                _saveFcmTokenStatus.value = ResultState.Error(Throwable(e.message))
            }
        }
    }
}
