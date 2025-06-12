package com.connect.eduhubconnect.utils


import okhttp3.Interceptor
import okhttp3.Response

class AuthIntercepter(private val tokenManager: TokenManager) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        // Skip adding token for login or register
        val skipAuth = originalRequest.url.encodedPath.contains("auth/login") ||
                originalRequest.url.encodedPath.contains("auth/register")

        return if (skipAuth) {
            chain.proceed(originalRequest)
        } else {
            val token = tokenManager.getAccessToken()

            val newRequest = if (!token.isNullOrEmpty()) {
                originalRequest.newBuilder()
                    .addHeader("Authorization", "Bearer $token")
                    .build()
            } else {
                originalRequest
            }

            chain.proceed(newRequest)
        }
    }

}
