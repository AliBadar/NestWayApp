package com.hia.common.model.content

import androidx.room.Embedded
import com.google.gson.annotations.SerializedName


data class MobileContent(
    @Embedded val content: ContentEntity,
    @SerializedName("mcn_content") val mcnContent: List<ContentDetailsEntity>,
)

data class Category(val id: String, val title: String)
