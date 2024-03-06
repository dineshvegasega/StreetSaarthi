package com.streetsaarthi.nasvi.di


import okhttp3.Interceptor


/**
 * Status Code Interceptor
 * */
object NetworkInterceptor {


    /**
     * Status Code Handler
     * */
    val interceptor = Interceptor { chain ->
        var request = chain.request()
        request = request.newBuilder().apply {
//            header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
            header("Content-Type", "application/json;charset=utf-8")
            header("User-Agent","Mozilla/5.0")
            method(request.method, request.body)
        }.build()
        val response = chain.proceed(request)
        response
    }




}