package com.streetsaarthi.nasvi.model

import com.google.gson.annotations.SerializedName


data class BaseResponseDC<T>(
    @SerializedName("data")
    val `data`: T? = null,
    @SerializedName("message")
    val message: String? = null,
    @SerializedName("status_code")
    val statusCode: Int? = null,
    @SerializedName("token")
    val token: String? = null,
    @SerializedName("success")
    val success: Boolean? = false,
    @SerializedName("vendor_id")
    val vendor_id: String? = null,


)