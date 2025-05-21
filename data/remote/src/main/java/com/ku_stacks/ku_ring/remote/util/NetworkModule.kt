package com.ku_stacks.ku_ring.remote.util

import android.content.Context
import com.ku_stacks.ku_ring.remote.BuildConfig
import com.ku_stacks.ku_ring.remote.kuringbot.KuringBotClient
import com.ku_stacks.ku_ring.remote.kuringbot.KuringBotSSEClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.sse.SSE
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    @Named("Default")
    fun provideOkHttpClient(@ApplicationContext context: Context): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = if (BuildConfig.DEBUG) {
                    HttpLoggingInterceptor.Level.BODY
                } else {
                    HttpLoggingInterceptor.Level.NONE
                }
            })
            .addInterceptor(HeaderInterceptor(context))
            .build()
    }

    @Provides
    @Singleton
    @Named("Default")
    fun provideRetrofit(@Named("Default") okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BuildConfig.API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val kotlinxJson = Json { ignoreUnknownKeys = true }

    @Provides
    @Singleton
    @Named("KotlinxSerialization")
    fun provideKotlinxSerializationRetrofit(@Named("Default") okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BuildConfig.API_BASE_URL)
            .addConverterFactory(kotlinxJson.asConverterFactory("application/json; charset=UTF8".toMediaType()))
            .build()
    }

    @Provides
    @Singleton
    @Named("KuringSpace")
    fun provideKuringSpaceRetrofit(@Named("Default") okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl("https://raw.githubusercontent.com/ku-ring/space/main/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideKuringBotClient(): KuringBotClient {
        val client = HttpClient(CIO) {
            install(SSE) {
                showCommentEvents()
                showRetryEvents()
            }
        }
        return KuringBotSSEClient(client)
    }

    @Provides
    @Singleton
    @Named("Library")
    fun provideLibraryRetrofit(@Named("Default") okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl("https://library.konkuk.ac.kr/pyxis-api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}