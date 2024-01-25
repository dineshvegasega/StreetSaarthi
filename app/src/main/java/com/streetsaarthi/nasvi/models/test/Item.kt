package com.streetsaarthi.nasvi.models.test

data class Item(
    val `data`: List<Data>,
    val message: String,
    val meta: Meta,
    val status_code: Int,
    val success: Boolean
)