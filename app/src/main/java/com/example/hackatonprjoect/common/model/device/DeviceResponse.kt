package com.example.example

import com.google.gson.annotations.SerializedName


data class DeviceResponse (

  @SerializedName("data" ) var data : Data? = Data(),

)