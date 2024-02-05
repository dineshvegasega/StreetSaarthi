package com.streetsaarthi.nasvi.models.mix

data class ItemLiveScheme(
    val description: String,
    val start_at: String,
    val end_at: String,
    val name: String,
    val scheme_id: Int,
    val scheme_image: SchemeImage,
    val status: String,
    var user_scheme_status: String,
//    var isRead : Boolean = false
)
