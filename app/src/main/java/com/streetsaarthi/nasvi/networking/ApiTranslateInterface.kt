package com.streetsaarthi.nasvi.networking

import com.google.gson.JsonElement
import com.streetsaarthi.nasvi.screens.onboarding.networking.TRANSLATE
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface ApiTranslateInterface {

    @Headers("Accept: application/json")
    @GET(TRANSLATE)
    fun translate(
        @Query("tl") lang: String,
        @Query("q") q: String
    ): Call<JsonElement>
}