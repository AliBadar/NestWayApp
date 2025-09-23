package com.example.hackatonprjoect.common.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hackatonprjoect.R
import com.example.hackatonprjoect.ui.theme.ThemeGreenColor
import com.example.hackatonprjoect.ui.theme.Typography
import com.example.hackatonprjoect.ui.theme.YellowColor

@Composable
fun CommonButton(title: String, modifier: Modifier = Modifier, onButtonClick: () -> Unit) {

    Row(
        modifier
            .widthIn(min = 150.dp)
            .background(YellowColor, RoundedCornerShape(20.dp))
            .padding(9.dp)
            .clickable {
                onButtonClick()

            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            title, color = ThemeGreenColor,
            style = Typography.bodyMedium.copy(
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        )

        Spacer(modifier.weight(1f))

        Icon(
            painter = painterResource(R.drawable.ic_arrow_right), contentDescription = null,
            tint = Color.Unspecified
        )
    }
}