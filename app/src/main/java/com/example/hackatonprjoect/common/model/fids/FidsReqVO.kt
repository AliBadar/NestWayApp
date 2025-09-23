package com.example.hackatonprjoect.common.model.fids

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class FidsReqVO(
    @SerializedName("uid")
    @Expose val uid: String? = null,
    @SerializedName("startTime") val startTime: String? = null,
    @SerializedName("endTime") val endTime: String? = null,
    @SerializedName("globalSearch") val globalSearch: String? = null,
    @SerializedName("type") val type: String? = null,
    @SerializedName("limit") val limit: String? = null,
    @SerializedName("BaggageBelt") val BaggageBelt: String? = null,
    @SerializedName("gateNoGeneral") val gateNoGeneral: String? = null,
    val order: String? = null,
)