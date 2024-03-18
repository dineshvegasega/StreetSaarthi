package com.streetsaarthi.nasvi.networking

import com.google.gson.JsonElement
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiTranslateInterface {
//    @FormUrlEncoded
//    @Headers("Content-Type: application/x-www-form-urlencoded")
//    @POST(TRANSLATE)
//    fun translate(
//        @Field("client") client: String = "gtx",
//        @Field("sl") sl: String = "en",
//        @Field("dt") dt: String = "t",
//        @Field("tl") tl: String,
//        @Field("q") q: String
//    ): Call<JsonElement>

    @Headers("Accept: application/json")
    @GET(TRANSLATE)
    fun translate(
        @Query("tl") lang: String,
        @Query("q") q: String
    ): Call<JsonElement>
}