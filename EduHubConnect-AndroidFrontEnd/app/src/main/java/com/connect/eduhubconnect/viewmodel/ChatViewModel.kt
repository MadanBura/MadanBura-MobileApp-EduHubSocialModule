package com.connect.eduhubconnect.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.connect.eduhubconnect.dto.RecentChatRes
import com.connect.eduhubconnect.model.ChatMessage
import com.connect.eduhubconnect.repository.UserRepository
import com.connect.eduhubconnect.utils.ResultState
import com.connect.eduhubconnect.utils.WebSocketManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatRepository: UserRepository
) : ViewModel(), WebSocketManager.MessageListener {

    private val _recentChatsLiveData = MutableLiveData<ResultState<List<RecentChatRes>>>()
    val recentChatsLiveData: LiveData<ResultState<List<RecentChatRes>>> = _recentChatsLiveData

    private val _chatMessagesLiveData = MutableLiveData<ResultState<List<ChatMessage>>>()
    val chatMessagesLiveData: LiveData<ResultState<List<ChatMessage>>> = _chatMessagesLiveData

    private val _incoming = MutableLiveData<ChatMessage>()
    val newIncomingMessage: LiveData<ChatMessage> = _incoming

    fun registerSocketListener() {
        WebSocketManager.registerListener(this)
    }

    fun unregisterSocketListener() {
        WebSocketManager.unregisterListener(this)
    }

    fun fetchRecentChats(username: String) {
        _recentChatsLiveData.value = ResultState.Loading
        viewModelScope.launch {
            try {
                val result = chatRepository.getRecentChats(username)
                _recentChatsLiveData.value = result
            } catch (e: Exception) {
                _recentChatsLiveData.value = ResultState.Error(Throwable(e.message ?: "Unknown error"))
            }
        }
    }

    fun fetchMessagesBetweenUsers(user1: String, user2: String) {
        _chatMessagesLiveData.value = ResultState.Loading
        viewModelScope.launch {
            try {
                val result = chatRepository.getMessagesBetweenUsers(user1, user2)
                _chatMessagesLiveData.value = result
            } catch (e: Exception) {
                _chatMessagesLiveData.value = ResultState.Error(Throwable(e.message ?: "Unknown error"))
            }
        }
    }

    fun sendMessage(chatMessage: ChatMessage) {
        try {
            WebSocketManager.sendMessage(chatMessage)
        } catch (e: Exception) {
            Log.e("ChatViewModel", "Send message error", e)
        }
    }

    fun disconnectWebSocket() {
        unregisterSocketListener()
    }

    override fun onMessage(message: ChatMessage) {
        _incoming.postValue(message)
    }
}
