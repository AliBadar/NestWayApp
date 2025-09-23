package com.example.hackatonprjoect.visioglobe.ui.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.hackatonprjoect.visioglobe.ui.model.SelectorData


@Composable
fun SelectorView(
    modifier: Modifier = Modifier,
    selectorData: SelectorData = SelectorData(),
    onBuildingSelected: (buildingId: String) -> Unit = { _ -> },
    onFloorSelected: (floorId: String, buildingId: String) -> Unit = { _, _ -> }
) {
    var expanded by remember { mutableStateOf(false) }
    var expandedBuildingId by remember { mutableStateOf("") }

    Box(
        modifier = modifier.padding(16.dp)
    ) {
        Button(onClick = { expanded = !expanded }) {
            Text(selectorData.getSelectorText())
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
                expandedBuildingId = ""
            },
        ) {
            selectorData.buildings.forEach { building ->
                DropdownMenuItem(
                    text = {
                        Text(
                            building.name,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    },
                    onClick = {
                        if (building.floors.isEmpty()){
                            expanded = false
                            onBuildingSelected(building.id)
                        }else if (building.id == expandedBuildingId) {
                            expandedBuildingId = ""
                        } else {
                            expandedBuildingId = building.id
                        }
                    },
                    leadingIcon = {
                        if (building.id == selectorData.selectedBuildingId) {
                            Icon(
                                imageVector = Icons.Filled.Check,
                                contentDescription = "Checked"
                            )
                        }
                    },
                    trailingIcon = {
                        if (building.floors.isNotEmpty()){
                            Icon(
                                imageVector = if (building.id == expandedBuildingId) {
                                    Icons.Filled.ArrowDropDown
                                }else {
                                    Icons.Filled.ArrowDropUp
                                }
                                ,
                                contentDescription = "Floors of ${building.name}"
                            )
                        }
                    },
                )
                if (building.id == expandedBuildingId) {
                    building.floors.forEach { floor ->
                        DropdownMenuItem(
                            text = { Text(floor.name) },
                            onClick = {
                                expanded = false
                                onFloorSelected(floor.id, building.id)
                            },
                            leadingIcon = {
                                if (building.id == selectorData.selectedBuildingId
                                    && floor.id == selectorData.selectedFloorId
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Check,
                                        contentDescription = "Checked"
                                    )
                                }
                            }
                        )

                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun SelectorViewPreview() {
    SelectorView(
        selectorData = SelectorData.preview()
    )
}

@Preview
@Composable
fun SelectorViewExpandedBuildingPreview() {
    SelectorView(
        selectorData = SelectorData.preview(selectedBuildingId = "B1")
    )
}
@Preview
@Composable
fun SelectorViewExpandedFloorPreview() {
    SelectorView(
        selectorData = SelectorData.preview(selectedBuildingId = "B1", selectedFloorId = "F1")
    )
}
