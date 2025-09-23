package com.example.hackatonprjoect.visioglobe.ui.model

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory

data class PlaceInfo(
    val id : String = "",
    val name : String= "",
    val description : String= "",
    val icon : Bitmap? = null
){
    companion object {
        fun preview(context: Context): PlaceInfo {
            return PlaceInfo(
                id = "placeInfoID",
                name = "placeInfoName",
                description = "placeInfoDescription",
                icon = BitmapFactory.decodeResource(
                    context.resources,
                    android.R.drawable.ic_media_play
                )
            )
        }
    }
}