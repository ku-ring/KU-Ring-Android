package com.ku_stacks.ku_ring.di

import com.ku_stacks.ku_ring.BuildConfig
import com.ku_stacks.ku_ring.BuildConfig.API_BASE_URL
import com.ku_stacks.ku_ring.data.api.FeedbackClient
import com.ku_stacks.ku_ring.data.api.FeedbackService
import com.ku_stacks.ku_ring.data.api.NoticeClient
import com.ku_stacks.ku_ring.data.api.NoticeService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
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
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideNoticeService(retrofit: Retrofit): NoticeService {
        return retrofit.create(NoticeService::class.java)
    }

    @Provides
    @Singleton
    fun provideNoticeClient(noticeService: NoticeService): NoticeClient {
        return NoticeClient(noticeService)
    }

    @Provides
    @Singleton
    fun provideFeedbackService(retrofit: Retrofit): FeedbackService {
        return retrofit.create(FeedbackService::class.java)
    }

    @Provides
    @Singleton
    fun provideFeedbackClient(feedbackService: FeedbackService): FeedbackClient {
        return FeedbackClient(feedbackService)
    }

}