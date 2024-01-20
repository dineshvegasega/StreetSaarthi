package com.demo.networking

import com.google.gson.JsonElement
import com.streetsaarthi.nasvi.model.BaseResponseDC
import com.streetsaarthi.nasvi.models.mix.ItemAds
import com.streetsaarthi.nasvi.models.mix.ItemDistrict
import com.streetsaarthi.nasvi.models.mix.ItemMarketplace
import com.streetsaarthi.nasvi.models.mix.ItemOrganization
import com.streetsaarthi.nasvi.models.mix.ItemPanchayat
import com.streetsaarthi.nasvi.models.mix.ItemPincode
import com.streetsaarthi.nasvi.models.mix.ItemState
import com.streetsaarthi.nasvi.models.mix.ItemVending
import com.streetsaarthi.nasvi.screens.onboarding.networking.ADS_LIST
import com.streetsaarthi.nasvi.screens.onboarding.networking.ComplaintFeedback
import com.streetsaarthi.nasvi.screens.onboarding.networking.DISTRICT
import com.streetsaarthi.nasvi.screens.onboarding.networking.InformationCenter
import com.streetsaarthi.nasvi.screens.onboarding.networking.LOCAL_ORGANISATION
import com.streetsaarthi.nasvi.screens.onboarding.networking.LOGIN
import com.streetsaarthi.nasvi.screens.onboarding.networking.LiveScheme
import com.streetsaarthi.nasvi.screens.onboarding.networking.LiveTraining
import com.streetsaarthi.nasvi.screens.onboarding.networking.Marketplace
import com.streetsaarthi.nasvi.screens.onboarding.networking.NoticeLiveList
import com.streetsaarthi.nasvi.screens.onboarding.networking.PANCHAYAT
import com.streetsaarthi.nasvi.screens.onboarding.networking.PASSWORD_UPDATE
import com.streetsaarthi.nasvi.screens.onboarding.networking.PINCODE
import com.streetsaarthi.nasvi.screens.onboarding.networking.RESEND_OTP
import com.streetsaarthi.nasvi.screens.onboarding.networking.SEND_OTP
import com.streetsaarthi.nasvi.screens.onboarding.networking.SIGN_UP
import com.streetsaarthi.nasvi.screens.onboarding.networking.STATE
import com.streetsaarthi.nasvi.screens.onboarding.networking.SchemeHistoryList
import com.streetsaarthi.nasvi.screens.onboarding.networking.VENDER_PROFILE
import com.streetsaarthi.nasvi.screens.onboarding.networking.VENDER_PROFILE_UPDATE
import com.streetsaarthi.nasvi.screens.onboarding.networking.VERIFY_OTP
import com.streetsaarthi.nasvi.screens.onboarding.networking.Vending
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ApiInterface {

    @POST(LOGIN)
    suspend fun login(
        @Body requestBody: RequestBody
    ): Response<BaseResponseDC<JsonElement>>


    @GET(VENDER_PROFILE+ "/{id}")
    suspend fun profile(
        @Path("id") id: String,
    ): Response<BaseResponseDC<JsonElement>>


    @Headers("Accept: application/json")
    @POST(VENDER_PROFILE_UPDATE+ "/{id}")
    suspend fun profileUpdate(
        @Path("id") id: String,
        @Body hashMap: RequestBody
    ): Response<BaseResponseDC<JsonElement>>


    @POST(VERIFY_OTP)
    suspend fun verifyOTPData(
        @Body requestBody: RequestBody
    ): Response<BaseResponseDC<JsonElement>>


    @POST(SEND_OTP)
    suspend fun sendOTP(
        @Body requestBody: RequestBody
    ): Response<BaseResponseDC<Any>>

    @POST(VERIFY_OTP)
    suspend fun verifyOTP(
        @Body requestBody: RequestBody
    ): Response<BaseResponseDC<Any>>

    @POST(RESEND_OTP)
    suspend fun reSendOTP(
        @Body requestBody: RequestBody
    ): Response<BaseResponseDC<Any>>


    @POST(SIGN_UP)
    suspend fun register(
        @Body requestBody: RequestBody
    ): Response<BaseResponseDC<Any>>

    @Headers("Accept: application/json")
    @POST(SIGN_UP)
    suspend fun registerWithFiles(
        @Body hashMap: RequestBody
    ): Response<BaseResponseDC<Any>>

    @POST(PASSWORD_UPDATE)
    suspend fun passwordupdate(
        @Body requestBody: RequestBody
    ): Response<BaseResponseDC<Any>>



    @GET(Vending)
    suspend fun vending(): Response<BaseResponseDC<List<ItemVending>>>

    @GET(Marketplace)
    suspend fun marketplace(): Response<BaseResponseDC<List<ItemMarketplace>>>

    @GET(STATE)
    suspend fun state(): Response<BaseResponseDC<List<ItemState>>>

    @POST(DISTRICT)
    suspend fun district(
        @Body requestBody: RequestBody
    ): Response<BaseResponseDC<List<ItemDistrict>>>

    @POST(PANCHAYAT)
    suspend fun panchayat(
        @Body requestBody: RequestBody
    ): Response<BaseResponseDC<List<ItemPanchayat>>>

    @POST(PINCODE)
    suspend fun pincode(
        @Body requestBody: RequestBody
    ): Response<BaseResponseDC<List<ItemPincode>>>

    @POST(LOCAL_ORGANISATION)
    suspend fun localOrganisation(
        @Body requestBody: RequestBody
    ): Response<BaseResponseDC<List<ItemOrganization>>>



    @POST(SchemeHistoryList)
    suspend fun schemeHistoryList(
        @Body requestBody: RequestBody
    ): Response<BaseResponseDC<JsonElement>>



    @POST(LiveScheme)
    suspend fun liveScheme(
        @Body requestBody: RequestBody
    ): Response<BaseResponseDC<JsonElement>>


    @POST(NoticeLiveList)
    suspend fun liveNotice(
        @Body requestBody: RequestBody
    ): Response<BaseResponseDC<JsonElement>>


    @POST(LiveTraining)
    suspend fun liveTraining(
        @Body requestBody: RequestBody
    ): Response<BaseResponseDC<JsonElement>>


    @POST(ComplaintFeedback)
    suspend fun complaintFeedback(
        @Body requestBody: RequestBody
    ): Response<BaseResponseDC<JsonElement>>


    @POST(InformationCenter)
    suspend fun informationCenter(
        @Body requestBody: RequestBody
    ): Response<BaseResponseDC<JsonElement>>


    @GET(ADS_LIST)
    suspend fun adsList(): Response<BaseResponseDC<List<ItemAds>>>
}