package com.example.hackatonprjoect.presentation.success_dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
fun SuccessDialog(modifier: Modifier = Modifier, onCloseClick: () -> Unit) {
    Box(modifier.fillMaxWidth().wrapContentWidth()
        .background(Color.Transparent)
        .padding(start = 10.sdp, end = 10.sdp, bottom = 10.sdp),
        contentAlignment = Alignment.TopCenter
        ) {

        Column(
            modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
                .background(
                    Background, RoundedCornerShape(20.dp)
                )
                .padding(horizontal = 10.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier.height(40.sdp))

            Icon(
                painter = painterResource(R.drawable.ic_star), contentDescription = "",
                tint = Color.Unspecified
            )

            Spacer(modifier.height(20.sdp))

            Text(
                "Congratulations !!!", color = ThemeGreenColor,
                style = Typography.bodyMedium.copy(fontSize = 15.ssp, fontWeight = FontWeight.Bold)
            )

            Spacer(modifier.height(20.sdp))

            Text(
                "You have successfully reach your destination", color = ThemeGreenColor,
                textAlign = TextAlign.Center,
                style = Typography.bodyMedium.copy(fontSize = 15.ssp, fontWeight = FontWeight.Bold)
            )


            Spacer(modifier.height(40.sdp))

        }


        Icon(
            painter = painterResource(R.drawable.ic_close), contentDescription = "",
            tint = Color.Unspecified,
            modifier = modifier.align(Alignment.TopEnd).size(30.sdp).clickable {
                onCloseClick()
            }
        )

    }
}

@Preview(
    device = Devices.PIXEL_7_PRO,
    backgroundColor = 0xFFFFFFFF,
    showBackground = true,
    showSystemUi = true,
)
@Composable
fun SuccessDialogPreview() {
    SuccessDialog() {}

}