package com.connect.eduhubconnect

import android.app.Application
import android.util.Log
import com.connect.eduhubconnect.repository.UserRepository
import com.connect.eduhubconnect.utils.TokenManager
import com.connect.eduhubconnect.utils.WebSocketManager
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class EduHubConnect : Application() {

    @Inject
    lateinit var tokenManager: TokenManager

    override fun onCreate() {
        super.onCreate()
        // Initialize Firebase
        FirebaseApp.initializeApp(this)
//        WebSocketManager.initialize(applicationContext)
        tokenManager.getAccessToken()?.let { token ->
            WebSocketManager.connect(token, applicationContext)
        }
    }
}
