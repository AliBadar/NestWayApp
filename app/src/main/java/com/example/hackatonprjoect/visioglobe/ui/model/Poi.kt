package com.example.hackatonprjoect.visioglobe.ui.model

data class Poi(
    val id : String,
    val name: String,
    val building: String,
    val floor: String,
    val categoriesId: Set<String> = HashSet(),
    val category: Category,
) {
    companion object {
        fun preview(i: Int) = Poi(
            id = "poi id = $i",
            name = "name = $i",
            building = "building = $i",
            floor = "floor = $i",
            category = Category.preview(i)
        )
    }
}