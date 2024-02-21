package com.streetsaarthi.nasvi.networking

import com.google.gson.JsonElement
import com.streetsaarthi.nasvi.model.BaseResponseDC
import com.streetsaarthi.nasvi.models.test.ItemT
import com.streetsaarthi.nasvi.models.translate.ItemTranslate
import com.streetsaarthi.nasvi.screens.onboarding.networking.TRANSLATE
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query
import java.net.URLEncoder

interface ApiTranslateInterface {
    @Headers("Accept: application/json")
    @GET(TRANSLATE)
    fun translate(
        @Query("tl") lang: String,
        @Query("q") q: String
    ): Call<JsonElement>
}