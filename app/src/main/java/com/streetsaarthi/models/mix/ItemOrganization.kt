package com.streetsaarthi.models.mix

data class ItemOrganization(
    val district_id: Int,
    val id: Int,
    val leader_name: String,
    val local_organization_name: String,
    val mobile_no: Long,
    val state_id: Int,
    val status: String
)