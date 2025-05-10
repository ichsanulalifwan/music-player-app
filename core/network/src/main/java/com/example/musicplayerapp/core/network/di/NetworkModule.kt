package com.example.musicplayerapp.core.network.di

import com.example.musicplayerapp.core.network.constant.NetworkConstant.DEFAULT_TIMEOUT
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    /**
     * this function for setup http client like connection, read, write timeout
     * and hit remote config
     *
     * @return OkHttpClient.Builder for setup to retrofit
     */
    @Provides
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .retryOnConnectionFailure(true)
        .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
        .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
        .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
        .addInterceptor(
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        )
        .cache(null)
        .build()

    /**
     * this function for setup generator json convert with moshi
     */
    @Provides
    fun provideMoshi(): Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    /**
     * this function for API Instance
     *
     * @param client [provideOkHttpClient] http client
     * @param moshi [provideMoshi] generator JSON
     *
     * @return [Retrofit]
     */
    @Provides
    @Singleton
    fun provideRetrofit(
        client: OkHttpClient,
        moshi: Moshi,
    ): Retrofit = Retrofit.Builder()
        .baseUrl("https://api.deezer.com/") // For maintainability and security put in local.properties
        .client(client)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
}
