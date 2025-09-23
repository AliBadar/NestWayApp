package com.example.hackatonprjoect.visioglobe.ui.compose


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.hackatonprjoect.R
import com.example.hackatonprjoect.ui.theme.HackatonPrjoectTheme
import com.example.hackatonprjoect.visioglobe.ui.model.NavigationModel
import com.example.hackatonprjoect.visioglobe.ui.model.PlaceInfo
import com.example.hackatonprjoect.visioglobe.ui.model.PlaceType

@Composable
fun PlaceInfoView(
    modifier: Modifier = Modifier,
    placeInfo: PlaceInfo = PlaceInfo(),
    navigationModel: NavigationModel = NavigationModel(),
    hidePlaceInfo: () -> Unit = {},
    computeRoute: (type: PlaceType, id: String) -> Unit = { _, _ -> },
) {
    Card(
        modifier = modifier
            .alpha(0.9f),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
        shape = RoundedCornerShape(16.dp, 16.dp, 0.dp,0.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Spacer(modifier = Modifier.width(5.dp))
            Button(
                onClick = { hidePlaceInfo() },
                // Uses ButtonDefaults.ContentPadding by default
                contentPadding = PaddingValues(all = 2.dp),
                modifier = Modifier
                    .padding(8.dp)
                    .width(32.dp)
                    .height(32.dp)
            ) {
                // Inner content including an icon and a text label
                Icon(
                    Icons.Rounded.Close,
                    contentDescription = "Close place info"
                )
            }
            Spacer(Modifier.weight(0.5f))
            placeInfo.icon?.let {
                Image(
                    bitmap = it.asImageBitmap(),
                    modifier = Modifier
                        .height(40.dp)
                        .width(40.dp),
                    contentDescription = ""
                )
            }
            Text(
                modifier = Modifier.padding(8.dp),
                text = placeInfo.name
            )
            Spacer(Modifier.weight(1f))
        }

        WebView(
            modifier = Modifier
                .padding(8.dp)
                .background(Color.Transparent)
                .fillMaxHeight(0.8f),
            placeInfo = placeInfo
        )

        Row(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.inversePrimary)
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(8.dp),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painterResource(id = R.drawable.track_start),
                contentDescription = "Start",
                modifier = Modifier
                    .clickable {
                        if (placeInfo.id != navigationModel.placeStopId) {
                            computeRoute(PlaceType.Start, placeInfo.id)
                        }
                    }
                    .size(32.dp)
                    .alpha(
                        if (navigationModel.placeStartId != ""
                            || placeInfo.id == navigationModel.placeStopId
                        ) 0.5f else 1.0f
                    ),
                contentScale = ContentScale.Fit

            )
            Spacer(modifier = Modifier.width(16.dp))
            Image(
                painterResource(id = R.drawable.track_end),
                contentDescription = "End",
                modifier = Modifier
                    .clickable {
                        if (placeInfo.id != navigationModel.placeStartId) {
                            computeRoute(PlaceType.Stop, placeInfo.id)
                        }
                    }
                    .size(32.dp)
                    .alpha(
                        if (navigationModel.placeStopId != "" ||
                            placeInfo.id == navigationModel.placeStartId
                        ) 0.5f else 1.0f
                    ),
                contentScale = ContentScale.Fit
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFE91E63)
@Composable
fun PlaceInfoViewPreview() {
    HackatonPrjoectTheme {
        PlaceInfoView(
            placeInfo = PlaceInfo.preview(LocalContext.current),
            navigationModel = NavigationModel.preview(LocalContext.current)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFE91E63)
@Composable
fun PlaceInfoViewPositioningPreview() {
    HackatonPrjoectTheme {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            PlaceInfoView(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth(0.9f)
                    .fillMaxHeight(0.4f),
                placeInfo = PlaceInfo.preview(LocalContext.current),
                navigationModel = NavigationModel.preview(LocalContext.current)
            )
        }
    }
}

