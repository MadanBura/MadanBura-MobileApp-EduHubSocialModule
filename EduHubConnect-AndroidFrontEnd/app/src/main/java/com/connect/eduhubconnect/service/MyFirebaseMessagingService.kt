package com.connect.eduhubconnect.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.connect.eduhubconnect.R
import com.connect.eduhubconnect.repository.UserRepository
import com.connect.eduhubconnect.utils.TokenManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MyFirebaseMessagingService : FirebaseMessagingService() {

    private val CHANNEL_ID = "default_channel"

    @Inject lateinit var userRepository: UserRepository
    @Inject lateinit var tokenManager: TokenManager

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        remoteMessage.data["message"]?.let {
            showNotification(it)
        }
    }

    private fun showNotification(notificationMessage: String) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // For Android 8.0 and higher, create a notification channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Default Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Notifications for messages"
            }
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("New Activity")
            .setContentText(notificationMessage)
            .setSmallIcon(R.drawable.ic_notification)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(0, notification)
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM", "New FCM Token: $token")

        tokenManager.storeFCMToken(token)
        CoroutineScope(Dispatchers.IO).launch {
            sendTokenToBackend(token)

        }
    }

    private suspend fun sendTokenToBackend(token: String) {
        val userId = tokenManager.getUserId()
        if (userId != null && userId > 0L) {
            userRepository.saveFcmToken(userId, token)
            Log.d("FCM", "Token sent to backend")
        } else {
            Log.d("FCM", "User not logged in, token stored for later.")
        }
    }
}
