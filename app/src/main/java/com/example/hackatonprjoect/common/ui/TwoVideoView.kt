package com.example.hackatonprjoect.common.ui

import android.content.Context
import android.view.View
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.hackatonprjoect.common.TwoVideoViewType
import com.example.hackatonprjoect.common.VideoCell
import com.example.hackatonprjoect.common.VideoStatsInfo

@Composable
fun TwoVideoView(
    modifier: Modifier = Modifier,
    localUid: Int,
    remoteUid: Int,
    localStats: VideoStatsInfo = VideoStatsInfo(),
    remoteStats: VideoStatsInfo = VideoStatsInfo(),
    type: TwoVideoViewType = TwoVideoViewType.LargeSmall,
    localPrimary: Boolean = true,
    secondClickable: Boolean = false,
    onSecondClick: () -> Unit = {},
    localCreate: ((context: Context) -> View)? = null,
    remoteCreate: ((context: Context) -> View)? = null,
    localRender: (View, Int, Boolean) -> Unit,
    remoteRender: (View, Int, Boolean) -> Unit,
) {
    val primary: @Composable (Modifier) -> Unit = {
        VideoCell(
            modifier = it,
            id = if (localPrimary) localUid else remoteUid,
            isLocal = localPrimary,
            createView = if (localPrimary) localCreate else remoteCreate,
            setupVideo = if (localPrimary) localRender else remoteRender,
            statsInfo = if (localPrimary) localStats else remoteStats
        )
    }
    val second: @Composable (Modifier) -> Unit = {
        VideoCell(
            modifier = if (secondClickable) it.clickable {
                onSecondClick()
            } else it,
            id = if (!localPrimary) localUid else remoteUid,
            isLocal = !localPrimary,
            createView = if (localPrimary) remoteCreate else localCreate,
            setupVideo = if (!localPrimary) localRender else remoteRender,
            statsInfo = if (!localPrimary) localStats else remoteStats
        )
    }
    when (type) {
        TwoVideoViewType.LargeSmall -> {
            Box(modifier = modifier) {
                primary(Modifier.fillMaxSize())
                second(
                    Modifier
                        .fillMaxSize(0.5f)
                        .align(Alignment.TopEnd)
                        .padding(16.dp)
                )
            }
        }

        TwoVideoViewType.Row -> {
            Row(modifier = modifier) {
                primary(Modifier.weight(1.0f))
                second(Modifier.weight(1.0f))
            }
        }

        TwoVideoViewType.Column -> {
            Column(modifier = modifier) {
                primary(Modifier.weight(1.0f))
                second(Modifier.weight(1.0f))
            }
        }
    }
}