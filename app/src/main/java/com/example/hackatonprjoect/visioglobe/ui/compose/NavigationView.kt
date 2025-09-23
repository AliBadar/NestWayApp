package com.example.hackatonprjoect.visioglobe.ui.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.VoiceOverOff
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.hackatonprjoect.ui.theme.HackatonPrjoectTheme
import com.example.hackatonprjoect.visioglobe.ui.model.NavigationModel


@Composable
fun NavigationView(
    modifier: Modifier = Modifier,
    navigationModel: NavigationModel = NavigationModel(),
    onNextInstructionClick: () -> Unit = {},
    onPreviousInstructionClick: () -> Unit = {},
    onClearRouteClick: () -> Unit = {},
    onTextToSpeechStateChange: (Boolean) -> Unit = {},
) {
    var voiceActivated: Boolean by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .alpha(0.9f),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
    )
    {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .wrapContentHeight(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            if (navigationModel.currentInstructionBitmap != null) {
                Image(
                    modifier = Modifier.padding(8.dp),
                    bitmap = navigationModel.currentInstructionBitmap.asImageBitmap(),
                    contentDescription = navigationModel.currentInstruction,
                )
            }
            Text(
                modifier = Modifier.weight(1F),
                text = navigationModel.currentInstruction)
            Icon(
                imageVector = if(voiceActivated) { Icons.Filled.VoiceOverOff } else { Icons.Filled.RecordVoiceOver },
                contentDescription = "Activate voice",
                modifier = Modifier
                    .padding(8.dp)
                    .clickable {
                        voiceActivated = !voiceActivated
                        onTextToSpeechStateChange(voiceActivated)
                    }
            )
        }
        Row(
            modifier = Modifier
                .padding(16.dp)
                .wrapContentHeight()
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            ArrowDirection(
                imageVector = Icons.Default.ArrowBack,
                description = "Back",
                enabled = navigationModel.isPreviousArrowEnabled,
                onClick = { onPreviousInstructionClick() }
            )
            Button(
                onClick = { onClearRouteClick() },
            ) {
                Text("Clear route")
            }
            ArrowDirection(
                imageVector = Icons.Default.ArrowForward,
                description = "Next",
                enabled = navigationModel.isNextArrowEnabled,
                onClick = { onNextInstructionClick() }
            )
        }
    }
}

@Composable
fun ArrowDirection(
    imageVector: ImageVector,
    description: String = "",
    enabled: Boolean = false,
    onClick: () -> Unit = {}
) {
    Icon(
        imageVector = imageVector,
        contentDescription = description,
        modifier = Modifier
            .clickable {
                if (enabled) {
                    onClick()
                }
            }
            .alpha(
                if (enabled) 1.0f else 0.5f
            )
            .size(size = 40.dp),
        tint = MaterialTheme.colorScheme.primary
    )
}

@Preview(showSystemUi = false)
@Composable
fun NavigationViewPreview() {
    HackatonPrjoectTheme {
        NavigationView(
            navigationModel = NavigationModel.preview(LocalContext.current)
        )
    }
}
