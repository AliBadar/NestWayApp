package com.example.hackatonprjoect.presentation.flight_details.components

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import coil.compose.AsyncImage
import com.example.hackatonprjoect.R
import ir.kaaveh.sdpcompose.sdp

@Composable
fun ImageComponent(height: Dp = 265.sdp) {
    Image(
        modifier = Modifier
            .fillMaxWidth()
            .height(height),
        painter = painterResource(id = R.drawable.flight_details),
        contentDescription = "flight_details",
        contentScale = ContentScale.FillBounds
    )
}

@Composable
fun ImageComponent(icon: String, height: Dp = 265.sdp) {
    AsyncImage(
        model = icon, // Replace with your image loading method
        placeholder = painterResource(id = R.drawable.ic_placeholder_logo),
        error = painterResource(id = R.drawable.ic_placeholder_logo),
        contentDescription = "flight_details",
        contentScale = ContentScale.FillBounds,
        modifier = Modifier
            .fillMaxWidth()
            .height(height),
    )
}

@Composable
fun ImageComponent(icon: Uri, height: Dp = 265.sdp) {
    AsyncImage(
        model = icon, // Replace with your image loading method
        placeholder = painterResource(id = R.drawable.flight_details),
        error = painterResource(id = R.drawable.flight_details),
        contentDescription = "flight_details",
        contentScale = ContentScale.FillBounds,
        modifier = Modifier
            .fillMaxWidth()
            .height(height),
    )
}