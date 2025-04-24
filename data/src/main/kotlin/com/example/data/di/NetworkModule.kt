package com.example.data.di

import android.content.Context
import com.example.data.BuildConfig
import com.example.data.remote.interceptor.AuthenticatorInterceptor
import com.example.data.remote.MovieAPI
import com.example.data.utils.BASE_URL
import com.example.data.utils.BatteryCheckerImp
import com.example.data.utils.NetworkCheckerImp
import com.example.domain.util.BatteryChecker
import com.example.domain.util.NetworkChecker
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {
    private const val timeoutConnect = 30
    private const val timeoutRead = 30

    @Provides
    fun provideOkHttpClient(headerInterceptor: AuthenticatorInterceptor): OkHttpClient {
        val okHttpBuilder = OkHttpClient.Builder()
        if (BuildConfig.DEBUG){
            okHttpBuilder.addInterceptor(HttpLoggingInterceptor().apply {
                setLevel(
                    HttpLoggingInterceptor.Level.BODY
                )
            })
        }
        okHttpBuilder.connectTimeout(timeoutConnect.toLong(), TimeUnit.SECONDS)
        okHttpBuilder.readTimeout(timeoutRead.toLong(), TimeUnit.SECONDS)
        okHttpBuilder.addInterceptor(headerInterceptor)
        return okHttpBuilder.build()
    }

    @Singleton
    @Provides
    fun provideGson(): Gson {
        return GsonBuilder()
            .setLenient()
            .create()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideIdentityApi(retrofit: Retrofit): MovieAPI {
        return retrofit.create(MovieAPI::class.java)
    }

    @Provides
    @Singleton
    fun provideNetworkChecker(@ApplicationContext context: Context):NetworkChecker{
        return NetworkCheckerImp(context = context)
    }

    @Provides
    @Singleton
    fun provideCheckBattery(@ApplicationContext context: Context):BatteryChecker{
        return BatteryCheckerImp(context = context)
    }

}