package com.streetsaarthi.networking

import com.google.gson.JsonElement
import com.streetsaarthi.model.BaseResponseDC
import com.streetsaarthi.models.translate.ItemTranslate
import com.streetsaarthi.screens.onboarding.networking.TRANSLATE
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiTranslateInterface {
    @GET(TRANSLATE)
    suspend fun translate(
    ): Response<ItemTranslate>
}