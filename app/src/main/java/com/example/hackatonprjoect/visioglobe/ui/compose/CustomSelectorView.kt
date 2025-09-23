package com.example.hackatonprjoect.visioglobe.ui.compose

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.hackatonprjoect.visioglobe.ui.model.Floor
import com.example.hackatonprjoect.visioglobe.ui.model.SelectorData
import com.hia.android.ui.theme.TabPinkColorGradient2

@Composable
fun CustomSelectorView(
    modifier: Modifier = Modifier,
    selectorData: SelectorData = SelectorData(),
    onBuildingSelected: (buildingId: String) -> Unit = { _ -> },
    onFloorSelected: (floorId: String, buildingId: String) -> Unit = { _, _ -> }
) {
    Box(
        modifier = modifier.padding(10.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // Get all floors and sort them in descending order
            selectorData.buildings.forEach { building ->
                building.floors
                    .sortedByDescending { it.name } // Sort floors in descending order
                    .forEach { floor ->
                        FloorButton(
                            floor = floor,
                            isSelected = building.id == selectorData.selectedBuildingId
                                    && floor.id == selectorData.selectedFloorId,
                            onClick = {
                                onFloorSelected(floor.id, building.id)
                            }
                        )
                    }
            }
        }
    }
}

@Composable
private fun FloorButton(
    floor: Floor,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .width(36.dp)
            .height(36.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) {
//                MaterialTheme.colorScheme.primary
                TabPinkColorGradient2
            } else {
                Color.White
            }
        ),
        border = BorderStroke(
            width = 1.dp,
            color = if (isSelected) {
//                MaterialTheme.colorScheme.primary
                TabPinkColorGradient2
            } else {
                MaterialTheme.colorScheme.outline
            }
        ),
        contentPadding = PaddingValues(4.dp),
        shape = MaterialTheme.shapes.small
    ) {
        Text(
            text = floor.name,
            style = MaterialTheme.typography.bodyMedium,
            color = if (isSelected) {
                Color.White
            } else {
//                MaterialTheme.colorScheme.primary
                TabPinkColorGradient2
            },
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}