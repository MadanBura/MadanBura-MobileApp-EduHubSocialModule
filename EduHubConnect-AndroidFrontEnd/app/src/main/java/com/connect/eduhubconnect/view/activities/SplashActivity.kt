package com.connect.eduhubconnect.view.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.connect.eduhubconnect.LoginActivity
import com.connect.eduhubconnect.R
import com.connect.eduhubconnect.repository.UserRepository
import com.connect.eduhubconnect.utils.TokenManager
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    @Inject lateinit var userRepository: UserRepository
    @Inject lateinit var tokenManager: TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val userId = tokenManager.getUserId()
        if (userId != null && userId > 0L) {
            navigateToDashboard()
            sendTokenIfNeeded(userId)
        } else {
            navigateToLogin()
        }
    }

    private fun navigateToDashboard() {
        startActivity(Intent(this, DashboardActivity::class.java))
        finish()
    }

    private fun navigateToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    private fun sendTokenIfNeeded(userId: Long) {
        val storedToken = tokenManager.getFCMToken()
        if (!storedToken.isNullOrEmpty()) {
            sendFCMTokenToBackend(storedToken, userId)
        } else {
            FirebaseMessaging.getInstance().token.addOnSuccessListener { token ->
                tokenManager.storeFCMToken(token)
                sendFCMTokenToBackend(token, userId)
            }
        }
    }

    private fun sendFCMTokenToBackend(token: String, userId: Long) {
        CoroutineScope(Dispatchers.IO).launch {
            userRepository.saveFcmToken(userId, token)
        }
    }
}
