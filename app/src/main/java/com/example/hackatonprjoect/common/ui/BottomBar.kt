package com.example.hackatonprjoect.common.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hackatonprjoect.R
import com.example.hackatonprjoect.common.model.fids.FidsEntity
import com.example.hackatonprjoect.presentation.flight_details.FlightDetailsCard
import com.example.hackatonprjoect.ui.theme.Background
import com.example.hackatonprjoect.ui.theme.ThemeGreenColor
import com.example.hackatonprjoect.ui.theme.Typography
import com.example.hackatonprjoect.ui.theme.WhiteColor
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp

@Composable
fun BottomBarFlightData(fidsEntity: FidsEntity, modifier: Modifier = Modifier) {
    Column(
        modifier
            .fillMaxWidth()
            .padding(8.sdp)
            .background(
                ThemeGreenColor, RoundedCornerShape(10.dp)
            )
            .padding(8.sdp)
            .padding(horizontal = 10.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier
                .fillMaxWidth()
                .padding(),
            horizontalArrangement = Arrangement.Center
        ) {

            Column {
                Text(
                    fidsEntity?.gateNoGeneral ?: "NOT ASSIGNED", color = WhiteColor,
                    style = Typography.bodyMedium.copy(fontSize = 12.ssp, fontWeight = FontWeight.Bold)
                )

                Spacer(modifier.height(3.dp))

                Text(
                    "Flight Number", color = WhiteColor,
                    style = Typography.bodyMedium.copy(fontSize = 10.ssp)
                )
            }

            Spacer(modifier.weight(1f))

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    "C23", color = WhiteColor,
                    style = Typography.bodyMedium.copy(fontSize = 12.ssp, fontWeight = FontWeight.Bold)
                )

                Spacer(modifier.height(3.dp))

                Text(
                    "Flight Number", color = WhiteColor,
                    style = Typography.bodyMedium.copy(fontSize = 10.ssp)
                )
            }
            Spacer(modifier.weight(1f))
            Column {
                Text(
                    fidsEntity?.flightStatusEn ?: "", color = WhiteColor,
                    style = Typography.bodyMedium.copy(fontSize = 12.ssp, fontWeight = FontWeight.Bold)
                )

                Spacer(modifier.height(3.dp))

                Text(
                    "Status", color = WhiteColor,
                    modifier = modifier.align(Alignment.End),
                    style = Typography.bodyMedium.copy(fontSize = 10.ssp)
                )
            }
        }
    }
}

@Preview(
    device = Devices.PIXEL_7_PRO,
    backgroundColor = 0xFFFFFFFF,
    showBackground = true,
    showSystemUi = true,
)
@Composable
fun BottomBarFlightDataPreview() {
    BottomBarFlightData(FidsEntity())
}