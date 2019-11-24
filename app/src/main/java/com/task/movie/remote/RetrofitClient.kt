package com.task.movie.remote

import com.task.movie.BuildConfig
import com.task.movie.remote.repository.RetrofitInterface
import dagger.Module
import dagger.Provides
import dagger.Reusable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/*
 * Project Name : MovieApp
 * Created by : SATHISH KUMAR R
 * Created on :17-11-2019 12:28
 * File Name : RetrofitClient.kt
 * ClassName : RetrofitClient
 * Module Name : app
 * Desc : 
 */
@Module
object RetrofitClient {

    @Provides
    @Reusable
    @JvmStatic
    internal fun getClient(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okhttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    }

    @Provides
    @Reusable
    @JvmStatic
    internal fun okhttpClient(): OkHttpClient {
        val interceptor: HttpLoggingInterceptor? = HttpLoggingInterceptor()
        interceptor!!.level = HttpLoggingInterceptor.Level.BODY
        return if (!BuildConfig.RETROFIT_LOG_INTERCEPTOR) {
            OkHttpClient.Builder().build()
        } else {
            try {
                OkHttpClient.Builder().addInterceptor(interceptor).build()
            } finally {
            }
        }
    }

    /**
     * Provides the Post service implementation.
     * @param retrofit the Retrofit object used to instantiate the service
     * @return the Post service implementation.
     */
    @Provides
    @Reusable
    @JvmStatic
    internal fun providePostApi(retrofit: Retrofit): RetrofitInterface {
        return retrofit.create(RetrofitInterface::class.java)
    }
}