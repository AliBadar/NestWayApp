package com.example.hackatonprjoect.presentation.flight_details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.hackatonprjoect.R
import com.example.hackatonprjoect.common.model.fids.FidsEntity
import com.example.hackatonprjoect.common.utils.Constants.ARRIVALS
import com.example.hackatonprjoect.presentation.flight_details.components.ImageComponent
import com.example.hackatonprjoect.ui.theme.AskAgentff
import com.example.hackatonprjoect.ui.theme.CheckInClosed7D
import com.example.hackatonprjoect.ui.theme.Delayed0F
import com.example.hackatonprjoect.ui.theme.GreenColor
import com.example.hackatonprjoect.ui.theme.StatusCancel55
import com.example.hackatonprjoect.ui.theme.StatusEarly1f
import com.example.hackatonprjoect.ui.theme.StatusEstimated7F
import com.example.hackatonprjoect.ui.theme.StatusFirstBag78
import com.example.hackatonprjoect.ui.theme.customFontFamily
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp
import kotlinx.coroutines.CoroutineScope

@Composable
fun FlightDetailScreen(
    fidsEntity: FidsEntity? = null,
    updateFidsEntity: (FidsEntity) -> Unit,
    onMapNavigation: (FidsEntity) -> Unit,
    modifier: Modifier = Modifier,
    scope: CoroutineScope = rememberCoroutineScope(),
    onCloseSheetClick: () -> Unit
) {

    Box() {
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .background(Color.White)

        ) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(if (fidsEntity?.viaNameEn.isNullOrEmpty()) 165.sdp else 150.sdp)
                ) {
                    ImageComponent(icon = fidsEntity?.destinationImage.toString())

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        Color(0x70000000), // Fully transparent black
                                        Color(0x90000000)  // 50% transparent black
                                    )
                                )
                            )
                            .padding(horizontal = 4.sdp)

                    ) {

                        Spacer(modifier = Modifier.height(42.sdp))

                        // Other header content (flight information, etc.)
                        Row(
                            modifier = Modifier
                                .padding(horizontal = 8.sdp)
                                .offset(y = (-5).sdp)
                        ) {
                            AsyncImage(
                                model = fidsEntity?.airlineLogo,
                                error = painterResource(id = R.drawable.ic_flight),
                                placeholder = painterResource(id = R.drawable.ic_flight),
                                contentDescription = "ic_flight",
                                modifier = modifier
                                    .width(50.sdp)
                                    .height(60.sdp)
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            val flightStatus = fidsEntity?.flightStatusEn
                            Text(
                                text = flightStatus ?: stringResource(id = R.string.check_in_open),
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontFamily = customFontFamily,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 11.sp,
                                    color = Color.White
                                ),
                                modifier = modifier
                                    .background(
                                        color = when (fidsEntity?.statusCode) {
//                                            "ES" -> Yellow01  // For DELAYED flights, use red background
//                                            "EA" -> Color.Blue  // For EARLY flights, use green background
//                                            "CX" -> Red4C  // For CANCELLED flights, use gray background
//                                            "LB" -> Red4C  // For CANCELLED flights, use gray background
//                                            "OT" -> Green43 // For ON_TIME flights, use blue background
//                                            "GC" -> GreyTextColor // For ON_TIME flights, use blue background
//                                            "LC" -> Red4C // For ON_TIME flights, use blue background
//                                            "LC" -> Red4C // For ON_TIME flights, use blue background
//                                            else -> Green43  // Default case, transparent background

                                            "CX" -> StatusCancel55
                                            "SH" -> GreenColor
                                            "ES" -> Delayed0F
                                            "LD" -> StatusEstimated7F
                                            "AR" -> StatusEstimated7F
                                            "OB" -> StatusEstimated7F
                                            "BB" -> StatusEstimated7F
                                            "EA" -> StatusEarly1f
                                            "FB" -> StatusFirstBag78
                                            "LB" -> StatusCancel55
                                            "BE" -> StatusEstimated7F
                                            "OT" -> StatusEstimated7F
                                            "DV" -> StatusEstimated7F
                                            "NI" -> StatusEstimated7F
                                            "BD" -> StatusFirstBag78
                                            "CC" -> CheckInClosed7D
                                            "CL" -> StatusCancel55
                                            "CO" -> GreenColor
                                            "GC" -> CheckInClosed7D
                                            "GO" -> StatusFirstBag78
                                            "LC" -> StatusCancel55
                                            "AB" -> StatusEstimated7F
                                            "DP" -> StatusEstimated7F
                                            "AB" -> StatusEstimated7F
                                            "ABD" -> StatusEstimated7F
                                            "ASK" -> AskAgentff
                                            else -> AskAgentff

                                        }, RoundedCornerShape(4.sdp)
                                    )
                                    .padding(horizontal = 10.sdp, vertical = 2.sdp)
                            )
                        }

                        //                    Spacer(modifier = Modifier.weight(1f))
                        val destOrOrigin = when {
                            fidsEntity?.type == ARRIVALS -> fidsEntity?.originNameEn
                            else -> fidsEntity?.destinationNameEn
                        }

                        if (destOrOrigin != null) {
                            Text(
                                text = destOrOrigin,
                                style = MaterialTheme.typography.headlineSmall.copy(
                                    fontWeight = FontWeight.Normal, fontFamily = customFontFamily
                                ),
                                color = Color.White,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = modifier
                                    .padding(start = 8.sdp)
                                    .offset(y = -5.sdp)
                            )
                        }
                        val viaName = fidsEntity?.viaNameEn ?: ""
                        if (!viaName.isNullOrEmpty()) {
                            Text(
                                text = "via ${(viaName)}",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontWeight = FontWeight.Normal,
                                    fontFamily = customFontFamily,
                                    fontSize = 15.ssp
                                ),
                                color = Color.White,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = modifier
                                    .padding(start = 8.sdp)
                                    .offset(y = (-10).sdp)
                            )
                        }

                        Text(
                            text = (fidsEntity?.flightNumber ?: ""),
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.Normal,
                                fontFamily = customFontFamily,
                                fontSize = 15.ssp
                            ),
                            color = Color.White,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = modifier
                                .padding(start = 8.sdp)
                                .offset(y = (-10).sdp)
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }

}