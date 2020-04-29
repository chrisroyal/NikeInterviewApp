package com.example.nikeinterviewapp.network

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class CommonHeaderInterceptor @Inject constructor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response = chain.run {
        proceed(
            request()
                .newBuilder()
                .addHeader(MarkedHeader.HEADER_AUTHORIZATION_HOST_KEY, MarkedHeader.HEADER_AUTHORIZATION_HOST_VALUE)
                .addHeader(MarkedHeader.HEADER_AUTHORIZATION_TOKEN_KEY, MarkedHeader.HEADER_AUTHORIZATION_TOKEN_VALUE)
                .build()
        )
    }
}