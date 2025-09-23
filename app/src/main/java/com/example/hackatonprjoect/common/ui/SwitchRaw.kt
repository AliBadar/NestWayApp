package com.example.hackatonprjoect.common.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SwitchRaw(
    title: String,
    checked: Boolean = false,
    enable: Boolean = true,
    onCheckedChange: (Boolean) -> Unit = {},
) {
    var swChecked by rememberSaveable { mutableStateOf(checked) }

    Row(
        modifier = Modifier.clickable(enabled = enable) {
            swChecked = !swChecked
            onCheckedChange(swChecked)
        },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(16.dp, 8.dp)
        )
        Spacer(Modifier.weight(1f))
        Switch(
            modifier = Modifier.padding(16.dp, 0.dp),
            checked = swChecked,
            onCheckedChange = {
                swChecked = it
                onCheckedChange(it)
            },
            enabled = enable
        )
    }
}