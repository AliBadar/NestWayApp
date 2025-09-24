package com.example.hackatonprjoect.presentation.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.ResourceResolutionException
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hackatonprjoect.R
import com.example.hackatonprjoect.common.utils.AppObserver
import com.example.hackatonprjoect.ui.theme.Background
import com.example.hackatonprjoect.ui.theme.ThemeGreenColor
import com.example.hackatonprjoect.ui.theme.WhiteColor
import com.example.hackatonprjoect.ui.theme.YellowColor
import com.example.hackatonprjoect.ui.theme.Typography
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp

@Composable
fun MainScreen(
    appObserver: AppObserver,
    modifier: Modifier = Modifier,
    onEnterPlayZoneClick: () -> Unit
) {

    Box(
        modifier
            .fillMaxSize()
            .background(color = Background)
            .padding(bottom = 10.sdp),
    ) {

        Image(
            painter = painterResource(R.drawable.ic_background_dots),
            contentDescription = null,
            modifier = modifier
                .fillMaxWidth()
                .height(460.sdp)
        )


        Column(modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {

            Spacer(modifier.height(60.sdp))

            Icon(
                painter = painterResource(R.drawable.ic_main_logo),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = modifier.size(90.sdp)
            )

            Spacer(modifier.height(14.sdp))

            Text(
                "NestAway", color = ThemeGreenColor,
                style = Typography.bodyLarge.copy(fontSize = 30.ssp, fontWeight = FontWeight.Bold)
            )

            Text(
                "Lets make your journey adventures !!!", color = ThemeGreenColor,
                style = Typography.bodyMedium.copy(fontSize = 10.ssp, fontWeight = FontWeight.Bold)
            )

            Spacer(modifier.height(25.sdp))

            Icon(
                painter = painterResource(R.drawable.ic_find_gate),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = modifier.height(230.sdp)
            )

            Spacer(modifier.weight(1f))

            Box(
                modifier
                    .fillMaxWidth()
                    .height(250.sdp)
                    .padding(horizontal = 10.sdp)
                    .background(ThemeGreenColor, RoundedCornerShape(20.sdp)),
                contentAlignment = Alignment.Center
            ) {

                Column(
                    modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Welcome to Kids Zone", color = WhiteColor,
                        style = Typography.bodyMedium.copy(fontSize = 12.ssp)
                    )

                    Spacer(modifier.height(8.sdp))

                    Text(
                        "Scan Your Boarding Pass", color = WhiteColor,
                        style = Typography.bodyMedium.copy(
                            fontSize = 20.ssp,
                            fontWeight = FontWeight.Bold
                        )
                    )


                    Spacer(modifier.height(12.sdp))

                    Row(
                        modifier
                            .background(YellowColor, RoundedCornerShape(20.sdp))
                            .padding(9.sdp)
                            .clickable {
                                onEnterPlayZoneClick()
                            },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Enter Play Zone", color = ThemeGreenColor,
                            style = Typography.bodyMedium.copy(
                                fontSize = 12.ssp,
                                fontWeight = FontWeight.Bold
                            )
                        )

                        Spacer(modifier.width(5.sdp))

                        Icon(
                            painter = painterResource(R.drawable.ic_arrow_right),
                            contentDescription = null,
                            tint = Color.Unspecified
                        )
                    }
                }

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
fun GreetingPreview() {
    MainScreen(AppObserver()) {

    }
}