package com.demo.networking

import com.streetsaarthi.model.BaseResponseDC
import com.streetsaarthi.models.ItemContributors
import com.streetsaarthi.models.Items
import com.streetsaarthi.models.login.Login
import com.streetsaarthi.models.login.LoginResponse
import com.streetsaarthi.models.mix.ItemDistrict
import com.streetsaarthi.models.mix.ItemMarketplace
import com.streetsaarthi.models.mix.ItemPanchayat
import com.streetsaarthi.models.mix.ItemPincode
import com.streetsaarthi.models.mix.ItemState
import com.streetsaarthi.models.mix.ItemVending
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ApiInterface {


    @POST(LOGIN)
    suspend fun login(
        @Body requestBody: RequestBody
    ): Response<BaseResponseDC<Login>>


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


}