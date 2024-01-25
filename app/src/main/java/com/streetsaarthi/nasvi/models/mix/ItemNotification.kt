package com.streetsaarthi.nasvi.models.mix

data class ItemNotification(
    val notification_id: Int,
    val user_id: Int,
    val title: String,
    val status: String,
    val type: String,
    val type_id: Int,
    val sent_at: String,
    val is_read: Boolean,
)
