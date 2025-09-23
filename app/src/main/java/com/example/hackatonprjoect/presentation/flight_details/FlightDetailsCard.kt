package com.example.hackatonprjoect.presentation.flight_details

import android.content.Context
import android.widget.Toast
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hackatonprjoect.R
import com.example.hackatonprjoect.common.model.fids.FidsEntity
import com.example.hackatonprjoect.common.ui.CommonButton
import com.example.hackatonprjoect.ui.theme.ThemeGreenColor
import com.example.hackatonprjoect.ui.theme.WhiteColor
import com.example.hackatonprjoect.ui.theme.YellowColor
import com.example.hackatonprjoect.ui.theme.Typography
import com.hia.android.ui.theme.Black5E
import ir.kaaveh.sdpcompose.sdp

@Composable
fun FlightDetailsCard(
    fidsEntity: FidsEntity? = null,
    updateFidsEntity: (FidsEntity) -> Unit,
    onCloseClick: () -> Unit,
    modifier: Modifier = Modifier,
    context: Context = LocalContext.current,
    onContinueClick: (String) -> Unit
) {

    var nickName by rememberSaveable {
        mutableStateOf("")
    }


    Box(
        modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(Color.Transparent)
            .padding(start = 10.sdp, end = 10.sdp, bottom = 10.sdp),
        contentAlignment = Alignment.TopCenter
    ) {

        Column(
            modifier
                .fillMaxWidth()
                .padding(top = 50.dp)
                .background(
                    ThemeGreenColor, RoundedCornerShape(20.dp)
                )
                .padding(horizontal = 10.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier.height(80.sdp))

            Text(
                "Your Flight Details", color = WhiteColor,
                style = Typography.bodyMedium.copy(fontSize = 14.sp)
            )

            Spacer(modifier.height(16.dp))

            Row(
                modifier
                    .fillMaxWidth()
                    .padding(),
                horizontalArrangement = Arrangement.Center
            ) {

                Column {
                    Text(
                        fidsEntity?.gateNoGeneral ?: "NOT ASSIGNED", color = WhiteColor,
                        style = Typography.bodyMedium.copy(fontSize = 18.sp)
                    )

                    Spacer(modifier.height(3.dp))

                    Text(
                        "Flight Number", color = WhiteColor,
                        style = Typography.bodyMedium.copy(fontSize = 14.sp)
                    )
                }

                Spacer(modifier.weight(1f))

                Column {
                    Text(
                        "C23", color = WhiteColor,
                        style = Typography.bodyMedium.copy(fontSize = 18.sp)
                    )

                    Spacer(modifier.height(3.dp))

                    Text(
                        "Flight Number", color = WhiteColor,
                        style = Typography.bodyMedium.copy(fontSize = 14.sp)
                    )
                }
                Spacer(modifier.weight(1f))
                Column {
                    Text(
                        fidsEntity?.flightStatusEn ?: "", color = WhiteColor,
                        style = Typography.bodyMedium.copy(fontSize = 18.sp)
                    )

                    Spacer(modifier.height(3.dp))

                    Text(
                        "Status", color = WhiteColor,
                        modifier = modifier.align(Alignment.End),
                        style = Typography.bodyMedium.copy(fontSize = 14.sp)
                    )
                }
            }

            Spacer(modifier.height(8.dp))
            HorizontalDivider(color = YellowColor)

            Spacer(modifier.height(16.dp))

            Row(
                modifier
                    .fillMaxWidth()
                    .padding(),
                horizontalArrangement = Arrangement.Center
            ) {

                Column {
                    Text(
                        fidsEntity?.airlineNameEn ?: "QATAR AIRWAYS", color = WhiteColor,
                        style = Typography.bodyMedium.copy(fontSize = 18.sp)
                    )

                    Spacer(modifier.height(3.dp))

                    Text(
                        "Airline Name", color = WhiteColor,
                        style = Typography.bodyMedium.copy(fontSize = 14.sp)
                    )
                }

                Spacer(modifier.weight(1f))


                Column {
                    Text(
                        "${fidsEntity?.originCode}-${fidsEntity?.destinationCode}",
                        color = WhiteColor,
                        style = Typography.bodyMedium.copy(fontSize = 18.sp)
                    )

                    Spacer(modifier.height(3.dp))

                    Text(
                        "Place", color = WhiteColor,
                        modifier = modifier.align(Alignment.End),
                        style = Typography.bodyMedium.copy(fontSize = 14.sp)
                    )
                }
            }

            Spacer(modifier.height(8.dp))
            HorizontalDivider(color = YellowColor)

            Spacer(modifier.height(16.dp))
            Text(
                "Enter Nickname", color = WhiteColor,
                modifier = modifier.align(Alignment.Start),
                style = Typography.bodyMedium.copy(fontSize = 14.sp)
            )

            Spacer(modifier.height(8.dp))

            TextField(
                shape = RoundedCornerShape(8.dp),
                modifier = modifier
                    .fillMaxWidth()
                    .height(50.dp),
                value = nickName,
                onValueChange = {
                    nickName = it
                },
                maxLines = 1,
                placeholder = {
                    Text(
                        "Enter Nickname", color = Black5E,
                        modifier = modifier.align(Alignment.End),
                        style = Typography.bodyMedium.copy(fontSize = 14.sp)
                    )
                })

            Spacer(modifier.height(20.dp))

            CommonButton("Continue") {
                onContinueClick(nickName)
//                if (nickName.isNullOrEmpty()) {
//                    Toast.makeText(
//                        context,
//                        "Please enter your Nick Name",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                } else {
//                    onContinueClick(nickName)
//                }
            }
            Spacer(modifier.height(20.dp))
        }

        Icon(
            painter = painterResource(R.drawable.ic_star), contentDescription = "",
            tint = Color.Unspecified
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
fun FlightDetailsCardPreview() {
    FlightDetailsCard(FidsEntity(), {}, {}) {}
}