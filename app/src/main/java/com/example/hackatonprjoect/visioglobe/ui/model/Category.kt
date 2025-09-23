package com.example.hackatonprjoect.visioglobe.ui.model

import android.graphics.Bitmap

data class Category(
    val id: String,
    val name: String,
    val image : Bitmap? = null,
){
    companion object {
        fun preview(i:Int)= Category(
            id = "Cat id $i",
            name = "category name $i",
        )
    }
}