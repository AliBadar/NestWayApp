package com.hia.android.ui.compose

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.hackatonprjoect.R
import com.example.hackatonprjoect.presentation.flight_details.components.ImageComponent
import com.example.hackatonprjoect.ui.theme.customFontFamily
import com.example.hackatonprjoect.visioglobe.ui.model.NavigationModel
import com.example.hackatonprjoect.visioglobe.ui.model.PlaceInfo
import com.example.hackatonprjoect.visioglobe.ui.model.PlaceType
import com.hia.android.ui.theme.TabPinkColorGradient2
import ir.kaaveh.sdpcompose.sdp

@Composable
fun CustomPlaceInfoView(
    modifier: Modifier = Modifier,
    placeInfo: PlaceInfo = PlaceInfo(),
    navigationModel: NavigationModel = NavigationModel(),
    onCloseClick: () -> Unit = {},
    onStartNavigation: (type: PlaceType, placeInfo: PlaceInfo, id: String) -> Unit = { _, _, _ -> },
) {

    val layoutDirection = LocalLayoutDirection.current
    val isRtl = layoutDirection == LayoutDirection.Rtl

    Box(
        modifier = modifier
            .fillMaxWidth()

    ) {
        // Main Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .alpha(0.9f),
            elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
            shape = RoundedCornerShape(16.dp, 16.dp, 0.dp, 0.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(230.sdp),
                contentAlignment = Alignment.TopEnd
            ) {
                // Background Image
                ImageComponent(icon = placeInfo.icon.toString(), height = 230.sdp)
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color(0x70000000), // Fully transparent black
                                    Color(0xFF000000)  // 100% transparent black
                                )
                            )
                        )
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // First Row: 3 TextFields on the left and a Flight Logo on the right
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier.weight(1f),
                        ) {
                            Text(
                                text = placeInfo.name,
                                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                                fontFamily = customFontFamily,
                                color = Color.White,
                                modifier = Modifier.fillMaxWidth(),
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )

                            Spacer(modifier = Modifier.height(5.sdp))

                            Text(
                                text = placeInfo.description.toString(),
                                style = MaterialTheme.typography.bodyMedium,
                                fontFamily = customFontFamily,
                                color = Color.White,
                                maxLines = if (isRtl) 4 else 6,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))
                        AsyncImage(
                            model = placeInfo.icon,
//                            error = painterResource(id = R.drawable.ic_flight),
//                            placeholder = painterResource(id = R.drawable.ic_flight),
                            contentDescription = "ic_flight",
                            modifier = modifier
                                .width(80.sdp)
                                .height(60.sdp)
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    Button(
                        onClick = { onStartNavigation(PlaceType.Stop, placeInfo, placeInfo.id) },
                        shape = RoundedCornerShape(7.sdp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = TabPinkColorGradient2,
                            contentColor = Color.White
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(stringResource(id = R.string.start_navigation))
                    }
                }
            }
        }

        // Close Button Image Component
        Image(
            painter = painterResource(id = R.drawable.ic_close_circle),
            contentDescription = "close",
            modifier = Modifier
                .size(30.sdp)
                .offset(y = (-15).dp) // Move the image half outside the card (adjust as needed)
                .align(Alignment.TopEnd) // Align the button at top right
                .clip(CircleShape) // Apply circle clipping
                .background(Color.White) // Optionally add background color to make it stand out
                .border(1.dp, Color.Gray, CircleShape)
                .clickable {
                    onCloseClick()
                }, // Optionally add border
            contentScale = ContentScale.Crop
        )
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Preview(
    device = Devices.PIXEL_7_PRO,
    backgroundColor = 0xFFFFFFFF,
    showBackground = true,
    showSystemUi = true,
)
@Composable
fun CustomPlaceInfoViewPreview() {
    CustomPlaceInfoView()
}