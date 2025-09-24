package com.example.hackatonprjoect.presentation.treauser_hunt

import android.content.Intent
import android.location.Location
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.hackatonprjoect.MapActivity
import com.example.hackatonprjoect.R
import com.example.hackatonprjoect.common.model.fids.FidsEntity
import com.example.hackatonprjoect.common.ui.BottomBarFlightData
import com.example.hackatonprjoect.common.ui.CommonButton
import com.example.hackatonprjoect.common.ui.TopBar
import com.example.hackatonprjoect.common.utils.AppObserver
import com.example.hackatonprjoect.ui.theme.Background
import com.example.hackatonprjoect.ui.theme.ThemeGreenColor
import com.example.hackatonprjoect.ui.theme.ThemeLightGreenColor
import com.example.hackatonprjoect.ui.theme.Typography
import com.example.hackatonprjoect.ui.theme.WhiteColor
import com.example.hackatonprjoect.ui.theme.YellowColorDark
import com.hia.android.model.menu_model.MenuModel
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp
import java.io.Serializable


val artpiece = listOf<ArtPiece>(
    ArtPiece(title = "Bottega Veneta", visioID = "B01-UL001-IDB0384", location = "Concourse B", 1, 2),
    ArtPiece(title = "Swarovski", visioID = "B01-UL001-IDA0419", location = "Concourse A", 1, 3),
    ArtPiece(title = "kids play area", visioID = "B01-UL001-IDC0395", location = "Concourse C", 1, 4),
    ArtPiece(title = "Illy Cafe", visioID = "B01-UL001-IDC0085", location = "Concourse C", 1, 5),
    ArtPiece(title = "big lies", visioID = "B01-UL001-IDC0368", location = "Concourse C", 1, 6),
    ArtPiece(title = "Gate C23", visioID = "B01-UL001-IDC1119", location = "Concourse C", 1, 7),
    )


data class ArtPiece(
    val title: String,
    val visioID: String,
    val location: String,
    val major: Int,
    val minor: Int,


) : Serializable

@Composable
fun FindArtPlaces(
    appObserver: AppObserver, modifier: Modifier = Modifier) {

    val totalPoints =
        appObserver.refreshFidsEntity.collectAsState(appObserver.totalPoints).value

    val context = LocalContext.current

    Scaffold(modifier = modifier
        .fillMaxSize()
        .background(Background),
        bottomBar = {

            Column {
               CommonButton("Start Treasure Hunt", modifier = modifier.fillMaxWidth().padding(8.sdp)) {
                   val intent = Intent(context, MapActivity::class.java).apply {
                       putExtra("type", "")
                       putExtra("visioID", ArrayList(artpiece))
                   }
                   context.startActivity(intent)
               }
                
                BottomBarFlightData(FidsEntity())

            }

        },
        topBar = {
        TopBar()
    }) { paddingValues ->
        Column(modifier
            .fillMaxSize()
            .background(Background)
            .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally) {

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
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Icon(
                        painter = painterResource(R.drawable.ic_find_gate),
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = modifier.height(30.sdp)
                    )

                    Text(
                        "Hello John", color = WhiteColor,
                        modifier = modifier.padding(start = 5.sdp),
                        style = Typography.bodyMedium.copy(fontSize = 12.ssp, fontWeight = FontWeight.Bold)
                    )

                    Spacer(modifier.weight(1f))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(painter = painterResource(R.drawable.ic_coin), contentDescription = null, tint = Color.Unspecified,
                            modifier = modifier.size(20.sdp))

                        Spacer(modifier.width(4.sdp))

                        Text(
                            "5000 pts", color = Color.White,
                            style = Typography.bodyLarge.copy(fontSize = 8.ssp, fontWeight = FontWeight.Bold)
                        )
                    }

                }
            }


            Spacer(modifier.height(10.sdp))

            Text(
                "Find Art Pieces\n" +
                        "Around Airport", color = ThemeGreenColor,
                style = Typography.bodyLarge.copy(fontSize = 30.ssp, fontWeight = FontWeight.Normal, lineHeight = 30.ssp)
            )

            Spacer(modifier.height(10.sdp))

            LazyColumn(
                modifier.padding(8.sdp)
            ) {

                items(artpiece) {menuModel ->
                    ItemDirectionArtPiece(menuModel) {

                    }

                    Spacer(modifier.height(8.sdp))
                }

            }

        }
    }

}

@Composable
fun ItemDirectionArtPiece(menuModel: ArtPiece, modifier: Modifier = Modifier, onItemClick: (ArtPiece) -> Unit) {

    Row(modifier
        .fillMaxWidth()
        .background(YellowColorDark, RoundedCornerShape(12.sdp))
        .padding(horizontal = 12.sdp, vertical = 5.sdp,)
        .clickable {
            onItemClick(menuModel)
        },
        verticalAlignment = Alignment.CenterVertically) {


        Column(verticalArrangement = Arrangement.Center) {
            Text(
                menuModel.title.toString(), color = ThemeGreenColor,
                modifier = modifier,
                style = Typography.bodyMedium.copy(fontSize = 12.ssp, fontWeight = FontWeight.Bold)
            )

            Spacer(modifier.height(2.sdp))

            Text(
                menuModel.location.toString(), color = Color.White,
                modifier = modifier.clip(RoundedCornerShape(5.sdp)).background(ThemeLightGreenColor).padding(3.sdp),
                style = Typography.bodyMedium.copy(fontSize = 8.ssp, fontWeight = FontWeight.Bold)
            )
        }

        Spacer(modifier.weight(1f))

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(painter = painterResource(R.drawable.ic_coin), contentDescription = null, tint = Color.Unspecified,
                    modifier = modifier.size(20.sdp))

                Spacer(modifier.width(4.sdp))

                Text(
                    "5000 pts", color = ThemeGreenColor,
                    style = Typography.bodyLarge.copy(fontSize = 8.ssp, fontWeight = FontWeight.Bold)
                )

            }

            Icon(painter = painterResource(R.drawable.ic_checked), contentDescription = null, tint = Color.Unspecified,
                modifier = modifier.size(16.sdp))


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
fun FindArtPlacesPreview() {
    FindArtPlaces(AppObserver())
}