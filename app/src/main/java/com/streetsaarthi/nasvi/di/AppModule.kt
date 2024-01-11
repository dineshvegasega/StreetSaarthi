package com.streetsaarthi.nasvi.di

import android.content.Context
import com.demo.networking.ApiInterface
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.streetsaarthi.nasvi.networking.ApiTranslateInterface
import com.streetsaarthi.nasvi.screens.onboarding.networking.TRANSLATE_URL
import com.streetsaarthi.nasvi.screens.onboarding.networking.URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun cache(@ApplicationContext context: Context): Cache =
        Cache(context.cacheDir, (5 * 1024 * 1024).toLong())
    @Provides
    @Singleton
    fun provideOkHttpClient(
        cache: Cache
    ): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient.Builder()
            .connectTimeout(2, TimeUnit.MINUTES)
            .readTimeout(2, TimeUnit.MINUTES)
            .writeTimeout(2, TimeUnit.MINUTES)
            .addInterceptor(loggingInterceptor)
            .retryOnConnectionFailure(true)
            .cache(cache)
            .build()
    }


    @Provides
    @Singleton
    fun gson(): Gson = GsonBuilder().setLenient().create()

    @Provides
    @Singleton
    @Qualifiers.Normal
    fun providesRetrofit(
        gson: Gson,
        okHttpClient: OkHttpClient
    ): Retrofit = Retrofit.Builder()
        .baseUrl(URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(okHttpClient)
        .build()


    @Provides
    @Singleton
    @Qualifiers.Translate
    fun providesRetrofitTranslate(
        gson: Gson,
        okHttpClient: OkHttpClient
    ): Retrofit = Retrofit.Builder()
        .baseUrl(TRANSLATE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(okHttpClient)
        .build()

    @Provides
    @Singleton
    fun provideApiService(@Qualifiers.Normal retrofit: Retrofit): ApiInterface =
        retrofit.create(ApiInterface::class.java)



    @Provides
    @Singleton
    fun provideTranslateApiService(@Qualifiers.Translate retrofit: Retrofit): ApiTranslateInterface =
        retrofit.create(ApiTranslateInterface::class.java)
}