package com.example.hackatonprjoect.visioglobe.ui.model

data class SelectorData(
    val buildings: List<Building> = emptyList(),
    val mapName: String = "",
    val selectedBuildingId: String = "",
    val selectedFloorId: String = "",
) {
    fun getSelectorText(): String {
        return if (selectedFloorId.isNotEmpty() && selectedBuildingId.isNotEmpty()) {
            val building = buildings.firstOrNull { it.id == selectedBuildingId }
            val floor = building?.floors?.firstOrNull { it.id == selectedFloorId }
            if (floor != null) {
                "${building.name} - ${floor.name}"
            } else {
                building?.name ?: mapName
            }
        } else if (selectedFloorId.isEmpty() && selectedBuildingId.isNotEmpty()) {
            val building = buildings.firstOrNull { it.id == selectedBuildingId }
            building?.name ?: mapName
        } else {
            mapName
        }
    }

    companion object {
        fun preview(
            selectedBuildingId: String = "",
            selectedFloorId: String = ""
        ): SelectorData {
            return SelectorData(
                buildings = (1..10).map { Building.preview(it) },
                mapName = "Example Map",
                selectedFloorId = selectedFloorId,
                selectedBuildingId = selectedBuildingId
            )
        }
    }
}