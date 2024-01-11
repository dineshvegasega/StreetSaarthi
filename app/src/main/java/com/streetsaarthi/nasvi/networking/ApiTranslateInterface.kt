package com.streetsaarthi.nasvi.networking

import com.google.gson.JsonElement
import com.streetsaarthi.nasvi.model.BaseResponseDC
import com.streetsaarthi.nasvi.models.translate.ItemTranslate
import com.streetsaarthi.nasvi.screens.onboarding.networking.TRANSLATE
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