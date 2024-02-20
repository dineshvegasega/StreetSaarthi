package com.streetsaarthi.nasvi.networking.new

import com.streetsaarthi.nasvi.di.NetworkInterceptor
import com.streetsaarthi.nasvi.screens.onboarding.networking.TRANSLATE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiClient {
    companion object {
//        val BASE_URL = "https://translate.googleapis.com/translate_a/"
        var retrofit: Retrofit? = null
        fun getClient(): Retrofit? {
            if (retrofit == null) {
                val interceptor = HttpLoggingInterceptor()
                interceptor.level = HttpLoggingInterceptor.Level.BODY
                val client = OkHttpClient.Builder().apply {
                    readTimeout(20, TimeUnit.SECONDS)
                    writeTimeout(20, TimeUnit.SECONDS)
                    connectTimeout(20, TimeUnit.SECONDS)
                    addInterceptor(interceptor)
                    addInterceptor { chain ->
                        var request = chain.request()
                        request = request.newBuilder()
                            .build()
                        val response = chain.proceed(request)
                        response
                    }
                   .addInterceptor(NetworkInterceptor.interceptor)
                }
                retrofit = Retrofit.Builder()
                    .baseUrl(TRANSLATE_URL)
                    .client(client.build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

            }

            return retrofit
        }
    }
}