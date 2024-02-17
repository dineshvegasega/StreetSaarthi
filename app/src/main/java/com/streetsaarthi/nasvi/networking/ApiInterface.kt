package com.streetsaarthi.nasvi

import com.google.common.collect.Multimap
import com.google.gson.JsonElement
import com.streetsaarthi.nasvi.model.BaseResponseDC
import com.streetsaarthi.nasvi.models.chat.ItemChat
import com.streetsaarthi.nasvi.models.mix.ItemAds
import com.streetsaarthi.nasvi.models.mix.ItemComplaintType
import com.streetsaarthi.nasvi.models.mix.ItemDistrict
import com.streetsaarthi.nasvi.models.mix.ItemMarketplace
import com.streetsaarthi.nasvi.models.mix.ItemOrganization
import com.streetsaarthi.nasvi.models.mix.ItemPanchayat
import com.streetsaarthi.nasvi.models.mix.ItemPincode
import com.streetsaarthi.nasvi.models.mix.ItemState
import com.streetsaarthi.nasvi.models.mix.ItemVending
import com.streetsaarthi.nasvi.screens.onboarding.networking.ADS_LIST
import com.streetsaarthi.nasvi.screens.onboarding.networking.AddFeedbackConversation
import com.streetsaarthi.nasvi.screens.onboarding.networking.AllNoticeHistory
import com.streetsaarthi.nasvi.screens.onboarding.networking.AllSchemeHistory
import com.streetsaarthi.nasvi.screens.onboarding.networking.AllTrainingHistory
import com.streetsaarthi.nasvi.screens.onboarding.networking.ComplaintFeedback
import com.streetsaarthi.nasvi.screens.onboarding.networking.ComplaintFeedbackHistory
import com.streetsaarthi.nasvi.screens.onboarding.networking.Complaint_Type
import com.streetsaarthi.nasvi.screens.onboarding.networking.DISTRICT
import com.streetsaarthi.nasvi.screens.onboarding.networking.DeleteNotification
import com.streetsaarthi.nasvi.screens.onboarding.networking.FeedbackConversationDetails
import com.streetsaarthi.nasvi.screens.onboarding.networking.InformationCenter
import com.streetsaarthi.nasvi.screens.onboarding.networking.InformationDetail
import com.streetsaarthi.nasvi.screens.onboarding.networking.LOCAL_ORGANISATION
import com.streetsaarthi.nasvi.screens.onboarding.networking.LOGIN
import com.streetsaarthi.nasvi.screens.onboarding.networking.LOGOUT
import com.streetsaarthi.nasvi.screens.onboarding.networking.LiveScheme
import com.streetsaarthi.nasvi.screens.onboarding.networking.LiveTraining
import com.streetsaarthi.nasvi.screens.onboarding.networking.MOBILE_TOKEN
import com.streetsaarthi.nasvi.screens.onboarding.networking.Marketplace
import com.streetsaarthi.nasvi.screens.onboarding.networking.NewFeedback
import com.streetsaarthi.nasvi.screens.onboarding.networking.NomineeDetails
import com.streetsaarthi.nasvi.screens.onboarding.networking.NoticeDetail
import com.streetsaarthi.nasvi.screens.onboarding.networking.NoticeLiveList
import com.streetsaarthi.nasvi.screens.onboarding.networking.Notifications
import com.streetsaarthi.nasvi.screens.onboarding.networking.PANCHAYAT
import com.streetsaarthi.nasvi.screens.onboarding.networking.PASSWORD_UPDATE
import com.streetsaarthi.nasvi.screens.onboarding.networking.PINCODE
import com.streetsaarthi.nasvi.screens.onboarding.networking.PasswordUpdate
import com.streetsaarthi.nasvi.screens.onboarding.networking.RESEND_OTP
import com.streetsaarthi.nasvi.screens.onboarding.networking.SEND_OTP
import com.streetsaarthi.nasvi.screens.onboarding.networking.SIGN_UP
import com.streetsaarthi.nasvi.screens.onboarding.networking.STATE
import com.streetsaarthi.nasvi.screens.onboarding.networking.SaveSettings
import com.streetsaarthi.nasvi.screens.onboarding.networking.SchemeApply
import com.streetsaarthi.nasvi.screens.onboarding.networking.SchemeDetail
import com.streetsaarthi.nasvi.screens.onboarding.networking.SchemeHistoryList
import com.streetsaarthi.nasvi.screens.onboarding.networking.TrainingDetail
import com.streetsaarthi.nasvi.screens.onboarding.networking.UpdateNomineeDetails
import com.streetsaarthi.nasvi.screens.onboarding.networking.UpdateNotification
import com.streetsaarthi.nasvi.screens.onboarding.networking.VENDER_PROFILE
import com.streetsaarthi.nasvi.screens.onboarding.networking.VENDER_PROFILE_UPDATE
import com.streetsaarthi.nasvi.screens.onboarding.networking.VERIFY_OTP
import com.streetsaarthi.nasvi.screens.onboarding.networking.Vending
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ApiInterface {

    @POST(MOBILE_TOKEN)
    suspend fun mobileToken(
        @Body requestBody: RequestBody
    ): Response<BaseResponseDC<JsonElement>>


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
    @POST(NewFeedback)
    suspend fun newFeedback(
        @Body hashMap: RequestBody
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

    @POST(SchemeApply)
    suspend fun applyLink(
        @Body requestBody: RequestBody
    ): Response<BaseResponseDC<JsonElement>>

    @GET(SchemeDetail+ "/{id}")
    suspend fun schemeDetail(
        @Path("id") id: String,
    ): Response<BaseResponseDC<JsonElement>>

    @GET(TrainingDetail+ "/{id}")
    suspend fun trainingDetail(
        @Path("id") id: String,
    ): Response<BaseResponseDC<JsonElement>>

    @GET(NoticeDetail+ "/{id}")
    suspend fun noticeDetail(
        @Path("id") id: String,
    ): Response<BaseResponseDC<JsonElement>>


    @POST(NoticeLiveList)
    suspend fun liveNotice(
        @Body requestBody: RequestBody
    ): Response<BaseResponseDC<JsonElement>>


    @GET(InformationDetail+ "/{id}")
    suspend fun informationDetail(
        @Path("id") id: String,
    ): Response<BaseResponseDC<JsonElement>>


    @POST(LiveTraining)
    suspend fun liveTraining(
        @Body requestBody: RequestBody
    ): Response<BaseResponseDC<JsonElement>>



    @POST(AllSchemeHistory)
    suspend fun allSchemeList(
        @Body requestBody: RequestBody
    ): Response<BaseResponseDC<JsonElement>>


    @POST(AllTrainingHistory)
    suspend fun allTrainingList(
        @Body requestBody: RequestBody
    ): Response<BaseResponseDC<JsonElement>>


    @POST(AllNoticeHistory)
    suspend fun allNoticeList(
        @Body requestBody: RequestBody
    ): Response<BaseResponseDC<JsonElement>>



    @GET(Notifications)
    suspend fun notifications(
        @Query("page")  page: Int,
        @Query("is_read")  is_read: Boolean,
        @Query("user_id")  user_id: String
    ): Response<BaseResponseDC<JsonElement>>


    @POST(DeleteNotification)
    suspend fun deleteNotification(
        @Body requestBody: RequestBody
    ): Response<BaseResponseDC<JsonElement>>



    @POST(UpdateNotification)
    suspend fun updateNotification(
        @Body requestBody: RequestBody
    ): Response<BaseResponseDC<JsonElement>>


    @POST(ComplaintFeedback)
    suspend fun complaintFeedback(
        @Body requestBody: RequestBody
    ): Response<BaseResponseDC<JsonElement>>



    @POST(ComplaintFeedbackHistory)
    suspend fun complaintFeedbackHistory(
        @Body requestBody: RequestBody
    ): Response<BaseResponseDC<JsonElement>>


    @POST(InformationCenter)
    suspend fun informationCenter(
        @Body requestBody: RequestBody
    ): Response<BaseResponseDC<JsonElement>>


    @GET(ADS_LIST)
    suspend fun adsList(): Response<BaseResponseDC<List<ItemAds>>>


    @GET(Complaint_Type)
    suspend fun complaintType(): Response<BaseResponseDC<List<ItemComplaintType>>>



    @GET(FeedbackConversationDetails+ "/{id}")
    suspend fun feedbackConversationDetails(
        @Path("id") id: String,
        @Query("page") page: String,
    ): Response<ItemChat>



    @Headers("Accept: application/json")
    @POST(AddFeedbackConversation)
    suspend fun addFeedbackConversation(
        @Body hashMap: RequestBody
    ): Response<BaseResponseDC<Any>>


    @POST(SaveSettings)
    suspend fun saveSettings(
        @Body requestBody: RequestBody
    ): Response<BaseResponseDC<JsonElement>>


    @POST(LOGOUT)
    suspend fun logout(
        @Body requestBody: RequestBody
    ): Response<BaseResponseDC<JsonElement>>


    @POST(PasswordUpdate)
    suspend fun passwordUpdate(
        @Body requestBody: RequestBody
    ): Response<BaseResponseDC<JsonElement>>


    @Headers("Accept: application/json")
    @POST(UpdateNomineeDetails)
    suspend fun updateNomineeDetails(
        @Body requestBody: RequestBody
    ): Response<BaseResponseDC<JsonElement>>



    @POST(NomineeDetails)
    suspend fun nomineeDetails(
        @Body requestBody: RequestBody
    ): Response<BaseResponseDC<JsonElement>>

}