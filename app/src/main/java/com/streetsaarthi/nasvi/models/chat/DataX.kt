package com.streetsaarthi.nasvi.models.chat

data class DataX(
    val media: MediaX?,
    val reply: String,
    val reply_date: String,
    val status: String,
    val user_id: Int,
    val user_type: String,
    var dateShow: Boolean = false,
)