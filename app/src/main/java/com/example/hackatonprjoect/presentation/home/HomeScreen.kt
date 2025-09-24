package com.example.hackatonprjoect.presentation.home

import android.content.Intent
import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.hackatonprjoect.MapActivity
import com.example.hackatonprjoect.NavigationRoutes
import com.example.hackatonprjoect.R
import com.example.hackatonprjoect.WebViewActivity
import com.example.hackatonprjoect.common.model.fids.FidsEntity
import com.example.hackatonprjoect.common.ui.BottomBarFlightData
import com.example.hackatonprjoect.common.ui.TopBar
import com.example.hackatonprjoect.common.utils.Constants.INFODESK
import com.example.hackatonprjoect.common.utils.Constants.LIVETRACKING
import com.example.hackatonprjoect.presentation.treauser_hunt.artpiece
import com.example.hackatonprjoect.ui.theme.Background
import com.example.hackatonprjoect.ui.theme.ThemeGreenColor
import com.example.hackatonprjoect.ui.theme.Typography
import com.example.hackatonprjoect.ui.theme.YellowColorDark
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp


@Composable
fun HomeScreen(navController: NavHostController,
               homeViewModel: HomeViewModel,
               modifier: Modifier = Modifier
) {


    val context = LocalContext.current

    val totalPointsData = homeViewModel.totalPoints.collectAsStateWithLifecycle(initialValue = 5000).value


    var totalPoints by rememberSaveable {
        mutableIntStateOf(5000)
    }

    LaunchedEffect(totalPointsData) {
        totalPoints = totalPointsData
    }


    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        homeViewModel.getTotalPoints()
    }


    Scaffold(
        topBar = {
            TopBar()
        },
        bottomBar = {
            BottomBarFlightData(FidsEntity())
        }

    ) { paddingValues ->
        Column(
            modifier
                .fillMaxSize()
                .background(Background)
                .padding(start = 8.sdp, end = 8.sdp, top = paddingValues.calculateTopPadding(), bottom = paddingValues.calculateBottomPadding())
                .verticalScroll(rememberScrollState())
        ) {


            Spacer(modifier.height(20.sdp))


            Box(
                modifier
                    .fillMaxWidth()
                    .height(160.sdp)
                    .clip(
                        RoundedCornerShape(16.sdp)
                    )
                    .background(ThemeGreenColor)
                    .padding(12.sdp)
            ) {
                Row(modifier.fillMaxWidth()) {

                    Column(modifier.fillMaxHeight()) {

                        Text(
                            "Hello John", color = Color.White,
                            style = Typography.bodyLarge.copy(
                                fontSize = 25.ssp,
                                fontWeight = FontWeight.Normal
                            )
                        )

                        Text(
                            "TOTAL REWARDS", color = Color.White,
                            style = Typography.bodyLarge.copy(
                                fontSize = 10.ssp,
                                fontWeight = FontWeight.Bold
                            )
                        )

                        Spacer(modifier.weight(1f))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                painter = painterResource(R.drawable.ic_coin),
                                contentDescription = null,
                                tint = Color.Unspecified,
                                modifier = modifier.size(40.sdp)
                            )

                            Spacer(modifier.width(4.sdp))

                            Text(
                                "$totalPoints pts", color = Color.White,
                                style = Typography.bodyLarge.copy(
                                    fontSize = 35.ssp,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }

                    Spacer(modifier.weight(1f))

                    Column(modifier.padding(top = 20.sdp)) {
                        Icon(
                            painter = painterResource(R.drawable.ic_find_gate),
                            contentDescription = null,
                            tint = Color.Unspecified,
                            modifier = modifier.height(80.sdp)
                        )
                    }
                }


            }

            Spacer(modifier.height(16.sdp))

            Text(
                "Lets make your journey adventures !!!", color = ThemeGreenColor,
                style = Typography.bodyLarge.copy(fontSize = 14.ssp, fontWeight = FontWeight.Bold)
            )

            Spacer(modifier.height(16.sdp))

            Row(modifier = modifier.fillMaxWidth()) {

                TrackingEventsCard(
                    R.drawable.ic_treasure_hunt, "Treasure\n" +
                            "Hunt"
                ) {

                    navController.navigate(NavigationRoutes.FIND_ART_PIECES)
                    
                }



                Spacer(modifier.weight(1f))

                TrackingEventsCard(
                    R.drawable.ic_live_tracking, "Live\n" +
                            "Tracking"
                ) {
                    val intent = Intent(context, MapActivity::class.java).apply {
                        putExtra("type", LIVETRACKING)
                    }
                    context.startActivity(intent)
                }

                Spacer(modifier.weight(1f))

                TrackingEventsCard(
                    R.drawable.ic_airport_guide, "Airport\n" +
                            "Guide"
                ) {


                    val intent = Intent(context, WebViewActivity::class.java).apply {
                        putExtra("url", "https://hackathonchatbot.azurewebsites.net/") // change link
                    }
                    context.startActivity(intent)

//                    val url = "https://hackathonchatbot.azurewebsites.net/" // change to your link
//                    val builder = CustomTabsIntent.Builder()
//                    val customTabsIntent = builder.build()
//                    customTabsIntent.launchUrl(context, Uri.parse(url))

                }

            }


            Spacer(modifier.height(16.sdp))

            Text(
                "Information Desks", color = ThemeGreenColor,
                style = Typography.bodyLarge.copy(fontSize = 14.ssp, fontWeight = FontWeight.Bold)
            )

            Spacer(modifier.height(16.sdp))

            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .wrapContentHeight()

                    .alpha(0.9f)
                    .background(YellowColorDark, RoundedCornerShape(12.sdp)),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ItemInformationDesk(R.drawable.ic_main_logo, "Muzn Lounge") {
                    val intent = Intent(context, MapActivity::class.java).apply {
                        putExtra("type", INFODESK)
                        putExtra("visioID", "B01-UL001-IDA0352")
                    }
                    context.startActivity(intent)
                }

                ItemInformationDesk(R.drawable.ic_main_logo, "Airport Information Desk") {
                    val intent = Intent(context, MapActivity::class.java).apply {
                        putExtra("type", INFODESK)
                        putExtra("visioID", "B01-UL001-IDA0352")
                    }
                    context.startActivity(intent)
                }

                ItemInformationDesk(R.drawable.ic_main_logo, "Qatar Airways Desk") {
                    val intent = Intent(context, MapActivity::class.java).apply {
                        putExtra("type", INFODESK)
                        putExtra("visioID", "B01-UL001-IDA0352")
                    }
                    context.startActivity(intent)
                }

                ItemInformationDesk(R.drawable.ic_main_logo, "QAS Information Desk") {
                    val intent = Intent(context, MapActivity::class.java).apply {
                        putExtra("type", INFODESK)
                        putExtra("visioID", "B01-UL001-IDA0352")
                    }
                    context.startActivity(intent)
                }

                ItemInformationDesk(R.drawable.ic_main_logo, "Discover Qatar for Transit Visa") {
                    val intent = Intent(context, MapActivity::class.java).apply {
                        putExtra("type", INFODESK)
                        putExtra("visioID", "B01-UL001-IDA0352")
                    }
                    context.startActivity(intent)
                }
            }

        }
    }


}

@Composable
fun ItemInformationDesk(@DrawableRes drawable: Int, title: String, modifier: Modifier = Modifier, onItemClick: () -> Unit) {
    Row(modifier
        .fillMaxWidth()
        .padding(8.sdp)
        .clickable { onItemClick() },
        verticalAlignment = Alignment.CenterVertically) {

        Icon(painter = painterResource(drawable), contentDescription = null, modifier = modifier.size(20.sdp))

        Spacer(modifier.width(8.sdp))

        Text(
            title, color = ThemeGreenColor,
            style = Typography.bodyLarge.copy(fontSize = 12.ssp)
        )

        Spacer(modifier.weight(1f))

        Icon(
            painter = painterResource(R.drawable.ic_arrow_right),
            contentDescription = null,
            tint = Color.Unspecified
        )
    }
}

@Composable
fun TrackingEventsCard(@DrawableRes drawable: Int, title: String, modifier: Modifier = Modifier, onItemClick: () -> Unit) {
    Column (
        modifier = modifier
            .width(90.sdp)
            .height(130.sdp)
            .fillMaxWidth()
            .alpha(0.9f)
            .background(YellowColorDark, RoundedCornerShape(15.sdp))
            .clickable{
                onItemClick()
            }
        ,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier.height(12.sdp))

        Icon(
            painter = painterResource(drawable),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = modifier.size(70.sdp)
        )

        Spacer(modifier.height(5.sdp))

        Text(
            title, color = ThemeGreenColor,
            style = Typography.bodyMedium.copy(fontSize = 13.ssp, fontWeight = FontWeight.Bold),
            textAlign = TextAlign.Center
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
fun HomeScreenPreview() {
    HomeScreen(rememberNavController(), hiltViewModel())
}