package com.streetsaarthi.nasvi.models
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
@Parcelize
data class Items(
    val incomplete_results: Boolean,
    val items: @RawValue ArrayList<Item> ?= ArrayList(),
    val total_count: Int
): Parcelable