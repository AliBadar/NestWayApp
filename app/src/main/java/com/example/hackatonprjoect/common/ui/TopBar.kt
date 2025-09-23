package com.example.hackatonprjoect.common.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import com.example.hackatonprjoect.R
import com.example.hackatonprjoect.common.model.fids.FidsEntity
import com.example.hackatonprjoect.presentation.flight_details.FlightDetailsCard
import com.example.hackatonprjoect.ui.theme.Background
import com.example.hackatonprjoect.ui.theme.ThemeGreenColor
import com.example.hackatonprjoect.ui.theme.Typography
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp

@Composable
fun TopBar(showPoints: Boolean = false, modifier: Modifier = Modifier) {
    Row(modifier = modifier.fillMaxWidth().background(Background).padding(8.sdp),
        verticalAlignment = Alignment.CenterVertically) {

        Icon(painter = painterResource(R.drawable.ic_main_logo), contentDescription = null, tint = Color.Unspecified,
            modifier = modifier.size(30.sdp))

        Spacer(modifier.width(8.sdp))

        Text(
            "NestAway", color = ThemeGreenColor,
            style = Typography.bodyLarge.copy(fontSize = 10.ssp, fontWeight = FontWeight.Bold)
        )

        Spacer(modifier.weight(1f))

        if (showPoints) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(painter = painterResource(R.drawable.ic_coin), contentDescription = null, tint = Color.Unspecified,
                    modifier = modifier.size(20.sdp))

                Spacer(modifier.width(4.sdp))

                Text(
                    "500 pts", color = ThemeGreenColor,
                    style = Typography.bodyLarge.copy(fontSize = 8.ssp, fontWeight = FontWeight.Bold)
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
fun TopBarPreview() {
    TopBar()
}