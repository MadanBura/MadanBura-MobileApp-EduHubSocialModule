package com.connect.eduhubconnect.utils

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.AppGlideModule
import okhttp3.OkHttpClient
import java.io.InputStream

@GlideModule
class CustomGlideModule : AppGlideModule() {

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(AuthIntercepter(TokenManager(context))) // Your Bearer token interceptor
            .build()

            val factory = OkHttpUrlLoader.Factory(okHttpClient)
        registry.replace(GlideUrl::class.java, InputStream::class.java, factory)
    }
}
