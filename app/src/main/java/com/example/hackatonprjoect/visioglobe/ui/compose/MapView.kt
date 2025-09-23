package com.example.hackatonprjoect.visioglobe.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import com.visioglobe.visiomoveessential.VMEMapView

@Composable
fun MapView(
    modifier: Modifier = Modifier,
    onMapViewAttached: (mapView : VMEMapView) -> Unit = { }
) {
    AndroidView(
        modifier = modifier.fillMaxSize()
            .background(Color.Transparent)
            .clipToBounds(), // Occupy the max size in the Compose UI tree

        factory = { context ->
            VMEMapView(context,null).also {
                onMapViewAttached(it)
            }
        },

        update = {
            // View's been inflated or state read in this block has been updated
            // Add logic here if necessary

            // As selectedItem is read here, AndroidView will recompose
            // whenever the state changes
            // Example of Compose -> View communication
            //view.coordinator.selectedItem = selectedItem.value
            //mMapController!!.loadMapView(mapView!!)


            //mMapController.mComposeMapView = it
        }
    )
}

@Preview
@Composable
fun MapViewPreview() {
    MapView()
}

