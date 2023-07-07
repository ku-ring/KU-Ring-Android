package com.ku_stacks.ku_ring.di

import com.ku_stacks.ku_ring.BuildConfig
import com.ku_stacks.ku_ring.BuildConfig.API_BASE_URL
import com.ku_stacks.ku_ring.data.api.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    @Named("Default")
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = if (BuildConfig.DEBUG) {
                    HttpLoggingInterceptor.Level.BODY
                } else {
                    HttpLoggingInterceptor.Level.NONE
                }
            })
            .addInterceptor(HeaderInterceptor())
            .build()
    }

    @Provides
    @Singleton
    @Named("Default")
    fun provideRetrofit(@Named("Default") okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    @Named("Sendbird")
    fun provideSendbirdOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                val newRequest = chain
                    .request()
                    .newBuilder()
                    .addHeader("Content-Type", "application/json; charset=utf-8")
                    .addHeader("Api-Token", BuildConfig.SENDBIRD_API_TOKEN)
                    .build()
                chain.proceed(newRequest)
            }
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = if (BuildConfig.DEBUG) {
                    HttpLoggingInterceptor.Level.BODY
                } else {
                    HttpLoggingInterceptor.Level.NONE
                }
            })
            .build()
    }

    @Provides
    @Singleton
    @Named("Sendbird")
    fun provideSendbirdRetrofit(@Named("Sendbird") okHttpClient: OkHttpClient): Retrofit {
        val url = "https://api-${BuildConfig.SENDBIRD_APP_ID}.sendbird.com/v3/"
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideNoticeService(@Named("Default") retrofit: Retrofit): NoticeService {
        return retrofit.create(NoticeService::class.java)
    }

    @Provides
    @Singleton
    fun provideSearchService(@Named("Default") retrofit: Retrofit): SearchService {
        return retrofit.create(SearchService::class.java)
    }

    @Provides
    @Singleton
    fun provideNoticeClient(noticeService: NoticeService): NoticeClient {
        return NoticeClient(noticeService)
    }

    @Provides
    @Singleton
    fun provideFeedbackService(@Named("Default") retrofit: Retrofit): FeedbackService {
        return retrofit.create(FeedbackService::class.java)
    }

    @Provides
    @Singleton
    fun provideFeedbackClient(feedbackService: FeedbackService): FeedbackClient {
        return FeedbackClient(feedbackService)
    }

    @Provides
    @Singleton
    fun provideSendbirdService(@Named("Sendbird") retrofit: Retrofit): SendbirdService {
        return retrofit.create(SendbirdService::class.java)
    }

    @Provides
    @Singleton
    fun provideSendbirdClient(sendbirdService: SendbirdService): SendbirdClient {
        return SendbirdClient(sendbirdService)
    }

    @Provides
    @Singleton
    fun provideDepartmentService(@Named("Default") retrofit: Retrofit): DepartmentService {
        return retrofit.create(DepartmentService::class.java)
    }
}