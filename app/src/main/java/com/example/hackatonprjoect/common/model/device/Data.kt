package com.example.example

import com.google.gson.annotations.SerializedName


data class Data (

  @SerializedName("id"          ) var id          : Int?    = null,
  @SerializedName("documentId"  ) var documentId  : String? = null,
  @SerializedName("name"        ) var name        : String? = null,
  @SerializedName("createdAt"   ) var createdAt   : String? = null,
  @SerializedName("updatedAt"   ) var updatedAt   : String? = null,
  @SerializedName("publishedAt" ) var publishedAt : String? = null,
  @SerializedName("deviceId"    ) var deviceId    : String? = null,
  @SerializedName("tokenId"     ) var tokenId     : String? = null,
  @SerializedName("channelId"   ) var channelId   : String? = null,
  @SerializedName("lat"         ) var lat         : String? = null,
  @SerializedName("lon"         ) var lon         : String? = null,
  @SerializedName("floor"       ) var floor       : String? = null

)