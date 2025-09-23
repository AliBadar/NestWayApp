package com.example.hackatonprjoect

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.RemoteException
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import org.altbeacon.beacon.*
import org.altbeacon.beacon.AltBeaconParser

class BeaconScannerActivity : ComponentActivity(), BeaconConsumer {

    private lateinit var beaconManager: BeaconManager
    private var isBeaconFound by mutableStateOf(false)
    //
    private val BEACON_UUID = "2C6D5E30-A872-43E2-81F0-5592D66EDA62"
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val allGranted = permissions.entries.all { it.value }
            if (allGranted) {
                // Permissions are granted, start your beacon scanning
                startScanning()
            } else {
                // Permissions are denied, show a message to the user
                Toast.makeText(this, "Permissions are required to scan for beacons.", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        beaconManager = BeaconManager.getInstanceForApplication(this)

        // To use AltBeacon format (recommended)
        beaconManager.beaconParsers.add(BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"))

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    BeaconScannerScreen(isBeaconFound = isBeaconFound)
                    if (isBeaconFound) {
                        // Pass a lambda that updates the state
                        ShowBeaconDetectedAlert(onDismiss = { isBeaconFound = false })
                    }
                }
            }
        }

        checkPermissionsAndStart()
    }

    private fun checkPermissionsAndStart() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            startScanning()
        } else {
            val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                arrayOf(
                    Manifest.permission.BLUETOOTH_SCAN,
                    Manifest.permission.BLUETOOTH_CONNECT,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            } else {
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
            }
            requestPermissionLauncher.launch(permissions)
        }
    }

    @SuppressLint("MissingPermission")
    private fun startScanning() {
        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        val bluetoothAdapter = bluetoothManager.adapter
        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivity(enableBtIntent)
        }
        beaconManager.bind(this)
    }

    override fun onBeaconServiceConnect() {
        // Define the specific Major and Minor values you want to track
        val BEACON_MAJOR = 1
        val BEACON_MINOR = 2

    // Create a Region with a null UUID to act as a wildcard
        val region = Region("my-major-minor-region",
            null, // Pass null for the UUID to match any UUID
            Identifier.fromInt(BEACON_MAJOR),
            Identifier.fromInt(BEACON_MINOR))

    // Start ranging for this region
//        val region = Region("my-beacon-region", Identifier.parse(BEACON_UUID), null, null)
        beaconManager.addRangeNotifier(object : RangeNotifier {
            override fun didRangeBeaconsInRegion(beacons: MutableCollection<Beacon>, region: Region) {
                if (beacons.isNotEmpty()) {
                    isBeaconFound = true
                } else {
                    isBeaconFound = false
                }
            }
        })

        try {
            beaconManager.startRangingBeaconsInRegion(region)
        } catch (e: RemoteException) {
            e.printStackTrace()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        beaconManager.unbind(this)
    }
}

// Composable functions for UI
@Composable
fun BeaconScannerScreen(isBeaconFound: Boolean) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = if (isBeaconFound) "Beacon Found! 🎉" else "Scanning for beacon..."
            )
            Spacer(modifier = Modifier.height(16.dp))
            CircularProgressIndicator(
                modifier = Modifier.size(64.dp)
            )
        }
    }
}

@Composable
fun ShowBeaconDetectedAlert(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = { /* No action on dismiss outside of button click */ },
        title = { Text(text = "Beacon Detected") },
        text = { Text(text = "You are in proximity of the target beacon!") },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("OK")
            }
        }
    )
}