package com.example.hackatonprjoect.core.fcm

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun CombinedPermissionsHandler(
    onAllGranted: () -> Unit,
    onDenied: (denied: List<String>) -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val locationResult = remember { mutableStateOf<Boolean?>(null) }
    val backgroundResult = remember { mutableStateOf<Boolean?>(null) }
    val notificationResult = remember { mutableStateOf<Boolean?>(null) }
    val bluetoothResult = remember { mutableStateOf<Map<String, Boolean>?>(null) }

    val locationLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {
            locationResult.value = it
        }
    val backgroundLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {
            backgroundResult.value = it
        }
    val notificationLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {
            notificationResult.value = it
        }
    val bluetoothLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            bluetoothResult.value = it
        }

    fun requestAllPermissions() {
        coroutineScope.launch {
            val denied = mutableListOf<String>()

            locationLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            while (locationResult.value == null) delay(50)
            if (locationResult.value == false) denied += Manifest.permission.ACCESS_FINE_LOCATION

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                backgroundLauncher.launch(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                while (backgroundResult.value == null) delay(50)
                if (backgroundResult.value == false) denied += Manifest.permission.ACCESS_BACKGROUND_LOCATION
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                notificationLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                while (notificationResult.value == null) delay(50)
                if (notificationResult.value == false) denied += Manifest.permission.POST_NOTIFICATIONS
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                bluetoothLauncher.launch(
                    arrayOf(
                        Manifest.permission.BLUETOOTH_SCAN,
                        Manifest.permission.BLUETOOTH_CONNECT
                    )
                )
                while (bluetoothResult.value == null) delay(50)
                val deniedBluetooth = bluetoothResult.value!!.filterValues { !it }.keys
                denied += deniedBluetooth
            }

            if (denied.isEmpty()) onAllGranted() else onDenied(denied)
        }
    }

    LaunchedEffect(Unit) {
        requestAllPermissions()
    }
}



