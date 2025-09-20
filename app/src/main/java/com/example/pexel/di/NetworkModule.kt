package com.example.pexel.di

import android.content.Context
import com.example.pexel.R
import com.example.pexel.data.remote.Api.ApiServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton
import java.io.File

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    @Named("PexelsApiKey")
    fun provideApiKey(@ApplicationContext context: Context): String {
        return context.getString(R.string.pexels_api_key)
    }

    @Provides
    @Singleton
    fun provideAuthInterceptor(
        @Named("PexelsApiKey") apiKey: String
    ): Interceptor {
        return Interceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("Authorization", apiKey)
                .build()
            chain.proceed(request)
        }
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
        @ApplicationContext context: Context,
        authInterceptor: Interceptor,
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .cache(Cache(File(context.cacheDir, "http_cache"), 10L * 1024 * 1024)) // 10MB cache
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.pexels.com/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideApiServices(retrofit: Retrofit): ApiServices {
        return retrofit.create(ApiServices::class.java)
    }
}
