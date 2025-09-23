package com.example.hackatonprjoect.visioglobe.ui.model

data class Building(
    val id: String,
    val name: String,
    val floors: List<Floor>
){
    companion object {
        fun preview(id: Int): Building {
            return Building(
                id = "B$id",
                name = "building $id",
                floors = (1..10).map { Floor.preview(it) }
            )
        }
    }
}