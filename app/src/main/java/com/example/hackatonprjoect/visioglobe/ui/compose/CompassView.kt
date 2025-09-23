package com.example.hackatonprjoect.visioglobe.ui.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.R
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.hackatonprjoect.ui.theme.HackatonPrjoectTheme


@Composable
fun CompassView(
    modifier: Modifier = Modifier,
    compassEnabled: Boolean = false,
    onCompassClick: () -> Unit = {},
) {
    Button(
        onClick = { onCompassClick() },
        contentPadding = PaddingValues(all = 8.dp),
        modifier = modifier
            .padding(all = 10.dp)
            .size(64.dp)
            .alpha(
                if (compassEnabled) 1.0f else 0.5f
            )
    ) {
//        Image(
//            painterResource(id = R.drawable.baseline_navigation_24),
//            "Compass",
//            colorFilter = ColorFilter.tint(Color.White)
//        )
    }
}

@Preview
@Composable
fun CompassViewEnabledPreview() {
    HackatonPrjoectTheme {
        CompassView(
            compassEnabled = true
        )
    }
}

@Preview
@Composable
fun CompassViewDisabledPreview() {
    HackatonPrjoectTheme {
        CompassView(
            compassEnabled = false
        )
    }
}
