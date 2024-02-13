package com.streetsaarthi.nasvi.models.chat

data class ItemChat(
    val complaint_type: String,
    val `data`: Data,
    val feedback_id: String,
    val media: MediaX,
    val message: String,
    val name: String,
    val registration_date: String,
    val status: String,
    val status_code: Int,
    val subject: String,
    val success: Boolean,
    val type: String,
    val user_id: Int,
    val user_type: String
)