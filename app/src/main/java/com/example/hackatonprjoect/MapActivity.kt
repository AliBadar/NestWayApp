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
import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.hackatonprjoect.core.fomatters.JsonTransformerClass
import com.example.hackatonprjoect.presentation.map.MapViewModel
import com.example.hackatonprjoect.presentation.treauser_hunt.ArtPiece
import com.example.hackatonprjoect.ui.theme.HackatonPrjoectTheme
import com.example.hackatonprjoect.visioglobe.ui.compose.ContentView
import com.example.hackatonprjoect.visioglobe.ui.model.NavigationModel
import com.example.hackatonprjoect.visioglobe.ui.model.PlaceInfo
import com.example.hackatonprjoect.visioglobe.ui.model.PlaceType
import kotlinx.coroutines.launch
import java.util.Locale
import com.example.hackatonprjoect.visioglobe.ui.model.Poi
import com.example.hackatonprjoect.visioglobe.ui.model.SelectorData
import org.altbeacon.beacon.Beacon
import org.altbeacon.beacon.BeaconConsumer
import org.altbeacon.beacon.BeaconManager
import org.altbeacon.beacon.BeaconParser
import org.altbeacon.beacon.Identifier
import org.altbeacon.beacon.RangeNotifier
import org.altbeacon.beacon.Region


class MapActivity : ComponentActivity(), BeaconConsumer {

    var visioIDs: ArrayList<ArtPiece>? = arrayListOf()
    private val viewModel: MapViewModel by viewModels()

    private var tts: TextToSpeech? = null
    private var ttsActivated: Boolean = false

    private var currentRouteIndex = 0


    private lateinit var beaconManager: BeaconManager
    private var isBeaconFound by mutableStateOf(false)

    @Volatile var BEACON_MAJOR = 1
    @Volatile var BEACON_MINOR = 2

    private var currentRegion: Region? = null



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

//    override fun onBeaconServiceConnect() {
//        Log.e("***********", "connection generating")
//
//        val region = Region(
//            "my-major-minor-region",
//            null,
//            Identifier.fromInt(BEACON_MAJOR),
//            Identifier.fromInt(BEACON_MINOR)
//        )
//        currentRegion = region
//
//        beaconManager.removeAllRangeNotifiers()
//        beaconManager.addRangeNotifier { beacons, _ ->
//            Log.e("***********", "didRangeBeaconsInRegion beacons: ${beacons.size}")
//            Log.e("***********", "values = $BEACON_MAJOR , $BEACON_MINOR")
//            isBeaconFound = beacons.isNotEmpty()
//        }
//
//        try {
//            beaconManager.startRangingBeaconsInRegion(region)
//        } catch (e: RemoteException) {
//            e.printStackTrace()
//        }
//    }

    override fun onBeaconServiceConnect() {
        Log.e("***********", "Beacon service connected")

        val region = Region("wildcard-region", null, null, null)
        currentRegion = region

        beaconManager.removeAllRangeNotifiers()
        beaconManager.addRangeNotifier { beacons, _ ->
            val filtered = beacons.filter { b ->
                val majorOk = b.id2.toInt() == BEACON_MAJOR
                val minorOk = b.id3.toInt() == BEACON_MINOR
                majorOk && minorOk
            }
            Log.e("***********", "ranged=${beacons.size}, matched=${filtered.size}, target=$BEACON_MAJOR,$BEACON_MINOR")
            isBeaconFound = filtered.isNotEmpty()
        }

        try {
            beaconManager.startRangingBeaconsInRegion(region)
        } catch (e: RemoteException) {
            e.printStackTrace()
        }
    }

    override fun onDestroy() {
        tts?.shutdown()
        super.onDestroy()
        beaconManager.unbind(this)
    }

    private fun initTTS(text: String) {
        tts = TextToSpeech(applicationContext) {
            when (it) {
                TextToSpeech.SUCCESS -> {
                    tts?.language = Locale.FRANCE
                    tts?.setPitch(0.5f)
                    tts?.setSpeechRate(0.8f)
                    speak(text)
                }
                else -> {
                    Log.d("TTS", "TTS ERROR")
                }
            }
        }
    }


    override fun onPause() {
        tts?.stop()
        super.onPause()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        beaconManager = BeaconManager.getInstanceForApplication(this)

        // To use AltBeacon format (recommended)
        beaconManager.beaconParsers.add(BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"))
        beaconManager.foregroundScanPeriod = 1100L
        beaconManager.foregroundBetweenScanPeriod = 0L
        beaconManager.updateScanPeriods()

        var destinationId: String? = intent.getStringExtra("destination")
        viewModel.setOrigin( "B01-UL001-IDA0394")
        if (destinationId.isNullOrEmpty()) {
            visioIDs = intent.getSerializableExtra("visioID") as? ArrayList<ArtPiece>
            if (visioIDs?.isNotEmpty() == true) {
                viewModel.setDestination(visioIDs?.get(0)?.visioID.toString())
            }
        } else {
            destinationId?.let { id ->

                val transformer = JsonTransformerClass(this)
                val visioIds: List<String> = transformer.getVisioGlobeIdsForMcnNid(id.replace(Regex("[^0-9]"), ""))

                Log.d("VisioGlobeIDs", visioIds.joinToString(", "))
//            viewModel.setDestination("B01-UL001-IDA0355")
                viewModel.setDestination(visioIds[0])
            }
        }

        sendInstructionToTTS()



        setContent {
            HackatonPrjoectTheme {
                val poiList: State<List<Poi>> =
                    viewModel.poiList.collectAsState()
                val mapDataLoaded: State<Boolean> = viewModel.mapDataLoaded.collectAsState()
                val isPlaceInfoDisplayed: State<Boolean> =
                    viewModel.isPlaceInfoDisplayed.collectAsState()
                val placeInfo: State<PlaceInfo> =
                    viewModel.placeInfo.collectAsState()
                val navigationModel: State<NavigationModel> =
                    viewModel.navigationModel.collectAsState()
                val compassState: State<Boolean> = viewModel.compassState.collectAsState()

                val selectorData: State<SelectorData> = viewModel.selectorData.collectAsState()


                Box(modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center) {

                    ContentView(
                        pois = poiList.value,
                        mapDataLoaded = mapDataLoaded.value,
                        isPlaceInfoDisplayed = isPlaceInfoDisplayed.value,
                        placeInfo = placeInfo.value,
                        navigationModel = navigationModel.value,
                        compassState = compassState.value,
                        selectorData = selectorData.value,
                        onSearchStart = { viewModel.loadCategoriesAndPlaces(it) },
                        cancelSearch = { viewModel.cancelSearch() },
                        goToPoi = { viewModel.goToPoi(it) },
                        onMapViewCreated = { viewModel.loadMapView(it) },
                        loadMapDataRequested = { viewModel.viewDidLoad(this@MapActivity) },
                        computeRoute = { type, id ->
                            viewModel.computeRoute(type, id)
                        },
                        hidePlaceInfo = { viewModel.hidePlaceInfo() },
                        onCompassClick = { viewModel.compassClicked() },
                        onPreviousNavigationClick = { viewModel.previousInstruction() },
                        onNextNavigationClick = { viewModel.nextInstruction() },
                        onClearRouteClick = { viewModel.clearRoute() },
                        onBuildingSelected = { buildingId ->
                            viewModel.selectBuilding(buildingId)
                        },
                        onFloorSelected = { floorId, buildingId ->
                            viewModel.selectFloor(floorId, buildingId)
                        },
                        onTextToSpeechStateChange = {
                            ttsActivated = it
                            if (it) {
                                initTTS(navigationModel.value.currentInstruction)
                            } else {
                                tts?.stop()
                            }
                        }
                    )

                    Button(onClick = {
                        if (visioIDs?.isNotEmpty() == true) {
                            if (currentRouteIndex < visioIDs!!.size - 1) {
                                currentRouteIndex++
                                viewModel.navigateToNextIndex(visioIDs!![currentRouteIndex].visioID)
                                ++BEACON_MINOR
                                Log.e("***********", "values = $BEACON_MAJOR , $BEACON_MINOR")

//                                // Stop old region
//                                currentRegion?.let { region ->
//                                    try {
//                                        beaconManager.stopRangingBeaconsInRegion(region)
//                                    } catch (e: Exception) {
//                                        e.printStackTrace()
//                                    }
//                                }
//
//                                // Start new region
//                                val newRegion = Region(
//                                    "my-major-minor-region",
//                                    null,
//                                    Identifier.fromInt(BEACON_MAJOR),
//                                    Identifier.fromInt(BEACON_MINOR)
//                                )
//                                currentRegion = newRegion
//
//                                try {
//                                    beaconManager.startRangingBeaconsInRegion(newRegion)
//                                } catch (e: RemoteException) {
//                                    e.printStackTrace()
//                                }
                            }
                        }
                    }) {
                        Text("Click for the next route")
                    }

                }





            }

            checkPermissionsAndStart()

            if (isBeaconFound) {
                // Pass a lambda that updates the state
                Toast.makeText(this, "Beacon Found", Toast.LENGTH_SHORT).show()

                if (visioIDs?.isNotEmpty() == true) {
                    if (currentRouteIndex < visioIDs!!.size - 1) {
                        currentRouteIndex++
                        viewModel.navigateToNextIndex(visioIDs!![currentRouteIndex].visioID)
                        ++BEACON_MINOR
                        Log.e("***********", "values = $BEACON_MAJOR , $BEACON_MINOR")

//                                // Stop old region
//                                currentRegion?.let { region ->
//                                    try {
//                                        beaconManager.stopRangingBeaconsInRegion(region)
//                                    } catch (e: Exception) {
//                                        e.printStackTrace()
//                                    }
//                                }
//
//                                // Start new region
//                                val newRegion = Region(
//                                    "my-major-minor-region",
//                                    null,
//                                    Identifier.fromInt(BEACON_MAJOR),
//                                    Identifier.fromInt(BEACON_MINOR)
//                                )
//                                currentRegion = newRegion
//
//                                try {
//                                    beaconManager.startRangingBeaconsInRegion(newRegion)
//                                } catch (e: RemoteException) {
//                                    e.printStackTrace()
//                                }
                    }
                }
            }
        }

    }

    private fun sendInstructionToTTS() {
        lifecycleScope.launch {
            viewModel.navigationModel.collect {
                    speak(it.currentInstruction)
            }
        }
    }

    private fun speak(text: String = "POOOOUUUUUUHETTE") {
        if (ttsActivated) {
            tts?.speak(
                text,
                TextToSpeech.QUEUE_FLUSH, /*Bundle()*/
                null,
                TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID
            )
        } else {
            Log.d("TTS", "You should activate TTS before")
        }
    }
}
