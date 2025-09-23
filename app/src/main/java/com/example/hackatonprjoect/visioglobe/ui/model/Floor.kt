package com.example.hackatonprjoect.visioglobe.ui.model

data class Floor(
    val id: String,
    val name: String,
) {
    companion object {
        fun preview(id: Int): Floor {
            return Floor(
                id = "F$id",
                name = "floor $id"
            )
        }
    }
}