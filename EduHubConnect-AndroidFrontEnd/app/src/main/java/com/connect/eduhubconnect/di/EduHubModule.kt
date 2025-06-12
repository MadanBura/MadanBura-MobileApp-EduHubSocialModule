package com.connect.eduhubconnect.di;


import android.content.Context
import com.connect.eduhubconnect.datasource.remote.EduHubApiService
import com.connect.eduhubconnect.repository.UserRepository
import com.connect.eduhubconnect.repositoryImpl.UserRepoImpl
import com.connect.eduhubconnect.utils.AuthIntercepter
import com.connect.eduhubconnect.utils.CONSTANTS
import com.connect.eduhubconnect.utils.TokenManager
import com.google.gson.GsonBuilder
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent;
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
class EduHubModule {

    @Provides
    @Singleton
    fun provideTokenManager(@ApplicationContext context: Context): TokenManager {
        return TokenManager(context)
    }

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        tokenManager: TokenManager,
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(AuthIntercepter(tokenManager))
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {

        val gson = GsonBuilder()
            .setLenient()
            .create()
        return Retrofit.Builder()
//            .baseUrl("http://10.0.102.134:8080/api/")
//            .baseUrl("http://10.0.102.40:8080/api/")
            .baseUrl("http://${CONSTANTS.BASEURL}/api/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): EduHubApiService {
        return retrofit.create(EduHubApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideUserRepository(quizApiService: EduHubApiService, tokenManager: TokenManager) : UserRepository{
        return UserRepoImpl(quizApiService, tokenManager)
    }


}
