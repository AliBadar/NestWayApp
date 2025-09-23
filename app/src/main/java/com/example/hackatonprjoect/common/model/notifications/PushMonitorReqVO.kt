package com.example.hackatonprjoect.common.model.notifications

data class PushMonitorReqVO(
    val device_identifier: String,
    val device_token: String,
    val type: String,
    val session_token: String,
    val flight_identifier: String
)
