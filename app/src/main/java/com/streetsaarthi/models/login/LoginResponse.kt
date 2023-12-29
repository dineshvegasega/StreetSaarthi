package com.streetsaarthi.models.login

data class LoginResponse(
    val `data`: Login,
    val message: String,
    val status_code: Int,
    val success: Boolean,
    val token: String ?= null
)