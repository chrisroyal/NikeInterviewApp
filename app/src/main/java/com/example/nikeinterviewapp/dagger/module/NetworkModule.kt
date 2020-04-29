package com.example.nikeinterviewapp.dagger.module

import com.example.nikeinterviewapp.network.CommonHeaderInterceptor
import com.example.nikeinterviewapp.network.UrbanDictionaryAPI
import dagger.Module
import dagger.Provides
import dagger.Reusable
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Named

@Module
object NetworkModule {

    private const val BASE_URL = "https://mashape-community-urban-dictionary.p.rapidapi.com"
    private const val OK_HTTP_CLIENT = "BaseOkHttp3"


    @Provides
    @Reusable
    @JvmStatic
    internal fun provideDictionaryAPI(retrofit: Retrofit): UrbanDictionaryAPI {
        return retrofit.create(UrbanDictionaryAPI::class.java)
    }

    @Provides
    @Reusable
    @Named(OK_HTTP_CLIENT)
    internal fun provideOkHttClient(
        commonHeaderInterceptor: CommonHeaderInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addNetworkInterceptor(commonHeaderInterceptor)
            .build()
    }

    @Provides
    @Reusable
    @JvmStatic
    internal fun provideRetrofit(
        @Named(OK_HTTP_CLIENT) okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .build()
    }





}