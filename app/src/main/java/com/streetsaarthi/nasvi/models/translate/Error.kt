package com.streetsaarthi.nasvi.models.translate

data class Error(
    val code: Int,
    val errors: List<ErrorX>,
    val message: String,
    val status: String
)