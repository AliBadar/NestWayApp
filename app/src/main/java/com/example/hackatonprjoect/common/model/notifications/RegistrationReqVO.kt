package com.hia.common.model.notifications

import com.google.gson.annotations.SerializedName

class RegistrationReqVO(
    @SerializedName("device_identifier") val deviceIdentifier: String? = null,
    @SerializedName("device_token") val deviceToken: String? = null,
    @SerializedName("type") val type: String? = "Android",
    @SerializedName("username") val userName: String? = "push_notifications_sebservice_user",
    @SerializedName("password") val password: String? = "hiasite@123",
    @SerializedName("language") val language: String? = "en"
)