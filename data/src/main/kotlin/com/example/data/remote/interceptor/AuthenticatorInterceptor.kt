package com.example.data.remote.interceptor


import com.example.data.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthenticatorInterceptor @Inject constructor() :Interceptor{
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val requestBuilder = original.newBuilder()
       requestBuilder.addHeader("Authorization", "Bearer ${BuildConfig.API_KEY}")
        val request = requestBuilder.build()
        return chain.proceed(request)
    }
}