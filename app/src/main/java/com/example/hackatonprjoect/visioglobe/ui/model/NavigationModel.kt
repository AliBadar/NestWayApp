package com.example.hackatonprjoect.visioglobe.ui.model

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory


data class NavigationModel(
    val isNavigationDisplayed: Boolean = false,
    val currentInstruction: String = "",
    val currentInstructionBitmap: Bitmap? = null,
    val isPreviousArrowEnabled: Boolean = false,
    val isNextArrowEnabled: Boolean = false,
    val placeStartId: String = "",
    val placeStopId: String = "",
    val instructionIndex: Int = 0,
){
    fun canComputeRoute(): Boolean {
        return !(placeStartId.isEmpty()
                || placeStopId.isEmpty()
                || placeStartId == placeStopId)
    }

    companion object {
        fun preview(context: Context): NavigationModel = NavigationModel(
            currentInstruction = "Turn left",
            currentInstructionBitmap = BitmapFactory.decodeResource(
                context.resources,
                android.R.drawable.ic_media_play
            ),
            isNavigationDisplayed = true,
            isPreviousArrowEnabled = true,
            isNextArrowEnabled = true,
            placeStartId = "StartID",
            placeStopId = "StopID",
            instructionIndex = 0
        )
    }
}
