package com.example.hackatonprjoect.visioglobe.ui.compose

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.hackatonprjoect.ui.theme.HackatonPrjoectTheme
import com.example.hackatonprjoect.visioglobe.ui.model.NavigationModel
import com.example.hackatonprjoect.visioglobe.ui.model.PlaceInfo
import com.example.hackatonprjoect.visioglobe.ui.model.PlaceType
import com.example.hackatonprjoect.visioglobe.ui.model.Poi
import com.example.hackatonprjoect.visioglobe.ui.model.SelectorData
import com.visioglobe.visiomoveessential.VMEMapView


@Composable
fun ContentView(
    pois: List<Poi> = emptyList(),
    mapDataLoaded: Boolean = false,
    isPlaceInfoDisplayed: Boolean = false,
    placeInfo: PlaceInfo = PlaceInfo(),
    navigationModel: NavigationModel = NavigationModel(),
    compassState: Boolean = false,
    selectorData: SelectorData = SelectorData(),
    onSearchStart: (text: String) -> Unit = {},
    cancelSearch: () -> Unit = {},
    goToPoi: (poiId: String) -> Unit = {},
    onMapViewCreated: (VMEMapView) -> Unit = {},
    loadMapDataRequested: () -> Unit = {},
    computeRoute: (type: PlaceType, id: String) -> Unit = { _, _ -> },
    hidePlaceInfo: () -> Unit = {},
    onCompassClick: () -> Unit = {},
    onPreviousNavigationClick: () -> Unit = {},
    onNextNavigationClick: () -> Unit = {},
    onClearRouteClick: () -> Unit = {},
    onBuildingSelected: (buildingId: String) -> Unit = { _ -> },
    onFloorSelected: (floorId: String, buildingId: String) -> Unit = { _, _ -> },
    onTextToSpeechStateChange: (state : Boolean) -> Unit = { _ -> },
) {

    var isSearching by remember { mutableStateOf(false) }

    SideEffect {
        loadMapDataRequested()
    }
    Box(
        // FUN FACT : Adding Modifier.fillMaxSize() make the map failing to load data.  (╯°□°）╯︵ ┻━┻
        //modifier = Modifier.fillMaxSize()
        modifier = Modifier,
    ) {
        if (mapDataLoaded) {
            MapView(
                onMapViewAttached = { mapView ->
                    onMapViewCreated(mapView)
                }
            )
        }

        // TOP SCREEN

        if (!navigationModel.isNavigationDisplayed) {
            SearchView(
                modifier = Modifier.align(alignment = Alignment.TopCenter),
                isSearching = isSearching,
                pois = pois,
                onSearchStateChange = { isSearching = it },
                onQueryChange = { onSearchStart(it) },
                cancelSearch = { cancelSearch() },
                goToPoi = { goToPoi(it) }
            )
        }

        AnimatedVisibility(
            visible = navigationModel.isNavigationDisplayed,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth(1.0f),
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            NavigationView(
                navigationModel = navigationModel,
                onNextInstructionClick = { onNextNavigationClick() },
                onPreviousInstructionClick = { onPreviousNavigationClick() },
                onClearRouteClick = { onClearRouteClick() },
                onTextToSpeechStateChange = { onTextToSpeechStateChange(it) },
            )
        }

        // BOTTOM SCREEN

        AnimatedVisibility(
            visible = isPlaceInfoDisplayed,
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .fillMaxHeight(0.4f)
                .align(Alignment.BottomCenter)
        ) {
            PlaceInfoView(
                placeInfo = placeInfo,
                navigationModel = navigationModel,
                hidePlaceInfo = { hidePlaceInfo() },
                computeRoute = { type, id ->
                    computeRoute(type, id)
                },
            )
        }

        if (!isPlaceInfoDisplayed && !isSearching) {

            if (navigationModel.isNavigationDisplayed) {
                CompassView(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(bottom = 50.dp, end = 20.dp),
                    compassEnabled = compassState,
                    onCompassClick = { onCompassClick() },
                )
            } else {
                SelectorView(
                    modifier = Modifier.align(Alignment.BottomCenter),
                    selectorData = selectorData,
                    onBuildingSelected = { buildingId ->
                        onBuildingSelected(buildingId)
                    },
                    onFloorSelected = { floorId, buildingId ->
                        onFloorSelected(floorId, buildingId)
                    }
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ContentViewPreview() {
    HackatonPrjoectTheme {
        ContentView(
            mapDataLoaded = true,
            isPlaceInfoDisplayed = false,
            navigationModel = NavigationModel.preview(LocalContext.current),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ContentViewPlaceInfoPreview() {
    HackatonPrjoectTheme {
        ContentView(
            mapDataLoaded = true,
            isPlaceInfoDisplayed = true,
            navigationModel = NavigationModel.preview(LocalContext.current),
        )
    }
}