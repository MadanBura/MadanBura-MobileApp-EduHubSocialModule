package com.connect.eduhubconnect.utils

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenManager @Inject constructor(@ApplicationContext context: Context) {
    private val prefs = context.getSharedPreferences("eduhubConnectv2.2", Context.MODE_PRIVATE)

    fun saveUserId(userId:Long){
        prefs.edit().putLong("USER_ID", userId)
            .apply()
    }

    fun saveTokens(accessToken: String) {
        prefs.edit()
            .putString("ACCESS_TOKEN", accessToken)
            .apply()
    }

    fun getUserId():Long? = prefs.getLong("USER_ID", 0)
    fun getAccessToken(): String? = prefs.getString("ACCESS_TOKEN", null)
    fun getAppFlagDetails(): Boolean = prefs.getBoolean("IS_FIRST_TIME", true)

    fun setFirstTimeFlag(isFirstTime: Boolean) {
        prefs.edit().putBoolean("IS_FIRST_TIME", isFirstTime).apply()
    }

    fun clearTokens() {
        prefs.edit().clear().apply()
    }

    fun storeFCMToken(token: String) {
        prefs.edit().putString("FCM_TOKEN", token).apply()
    }

    // Get the stored FCM Token
    fun getFCMToken(): String? {
        return prefs.getString("FCM_TOKEN", null)
    }
}
