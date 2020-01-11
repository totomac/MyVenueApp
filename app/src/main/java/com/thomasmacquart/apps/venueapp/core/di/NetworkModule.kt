package com.thomasmacquart.apps.venueapp.core.di

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.thomasmacquart.apps.venueapp.BuildConfig
import com.thomasmacquart.apps.venueapp.venues.data.api.VenueDetailsApi
import com.thomasmacquart.apps.venueapp.venues.data.api.VenuesSearchApi
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
class NetworkModule {

    private val CONNECTION_TIMEOUT_SECONDS = 10L
    private val BASE_URL = "https://api.foursquare.com/"

    @Provides
    @Singleton
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    @Provides
    @Singleton
    fun provideHttpClient(): OkHttpClient {

        val builder = OkHttpClient.Builder().apply {
            connectTimeout(CONNECTION_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            readTimeout(CONNECTION_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            writeTimeout(CONNECTION_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            addInterceptor(CredentialsInterceptor())
            addInterceptor(createHttpLoggingInterceptor())
        }
        return builder.build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        moshi: Moshi
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(EnvelopingConverter())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

    @Provides
    @Singleton
    fun provideVenuesSearchApi(retrofit: Retrofit) : VenuesSearchApi =
        retrofit.create(VenuesSearchApi::class.java)

    @Provides
    @Singleton
    fun provideVenueDetailsApi(retrofit: Retrofit) : VenueDetailsApi =
        retrofit.create(VenueDetailsApi::class.java)

    private fun createHttpLoggingInterceptor(): Interceptor {
        val level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
        return HttpLoggingInterceptor().apply {
            this.level = level
        }
    }
}