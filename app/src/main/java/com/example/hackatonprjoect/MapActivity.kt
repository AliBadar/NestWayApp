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
import android.os.Handler
import android.os.Looper
import android.os.RemoteException
import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.example.hackatonprjoect.NavigationRoutes.HOME
import com.example.hackatonprjoect.common.utils.AppObserver
import com.example.hackatonprjoect.common.utils.AppPreference
import com.example.hackatonprjoect.common.utils.Constants.INFODESK
import com.example.hackatonprjoect.common.utils.Constants.LIVETRACKING
import com.example.hackatonprjoect.common.utils.Constants.docIDHuwaei
import com.example.hackatonprjoect.common.utils.Constants.docIDSamsung
import com.example.hackatonprjoect.core.fomatters.JsonTransformerClass
import com.example.hackatonprjoect.presentation.flight_details.FlightDetailsCard
import com.example.hackatonprjoect.presentation.map.MapViewModel
import com.example.hackatonprjoect.presentation.success_dialog.SuccessDialog
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
import dagger.hilt.android.AndroidEntryPoint
import nl.dionsegijn.konfetti.compose.KonfettiView
import nl.dionsegijn.konfetti.compose.OnParticleSystemUpdateListener
import nl.dionsegijn.konfetti.core.PartySystem
import nl.dionsegijn.konfetti.xml.image.ImageUtil
import org.altbeacon.beacon.Beacon
import org.altbeacon.beacon.BeaconConsumer
import org.altbeacon.beacon.BeaconManager
import org.altbeacon.beacon.BeaconParser
import org.altbeacon.beacon.Identifier
import org.altbeacon.beacon.RangeNotifier
import org.altbeacon.beacon.Region
import javax.inject.Inject


@AndroidEntryPoint
class MapActivity : ComponentActivity(), BeaconConsumer {


    private var typeMap: String = ""
    private val handler = Handler(Looper.getMainLooper())

    private var liveTracking = false


    var visioIDs: ArrayList<ArtPiece>? = arrayListOf()
    private val viewModel: MapViewModel by viewModels()
    private val konfettiViewModel: KonfettiViewModel by viewModels()

    private var tts: TextToSpeech? = null
    private var ttsActivated: Boolean = false

    private var currentRouteIndex = 0


    private lateinit var beaconManager: BeaconManager
    private var isBeaconFound by mutableStateOf(false)



    @Volatile var BEACON_MAJOR = 1
    @Volatile var BEACON_MINOR = 2

    private var currentRegion: Region? = null


    private var totalPoints = 5000

    @Inject
    lateinit var appObservers: AppObserver



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

                val distance = b.distance // AltBeacon gives meters directly
                majorOk && minorOk && distance <= 0.5


//                majorOk && minorOk
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
        handler.removeCallbacks(delayedTask)
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


    private val delayedTask = object : Runnable {
        override fun run() {
            if (liveTracking) {
                var docID = ""
                if (Build.BRAND.lowercase().contains("samsung")) {
                    docID = docIDHuwaei
                } else {
                    docID = docIDSamsung
                }

                viewModel.getOtherDeviceLocation(docID)

                // Re-post itself after 2 seconds
                handler.postDelayed(this, 2000)
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        handler.postDelayed(delayedTask, 5000)
        beaconManager = BeaconManager.getInstanceForApplication(this)

        // To use AltBeacon format (recommended)
        beaconManager.beaconParsers.add(BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"))
        beaconManager.foregroundScanPeriod = 1100L
        beaconManager.foregroundBetweenScanPeriod = 0L
        beaconManager.updateScanPeriods()

        typeMap = intent.getStringExtra("type") ?: ""

        if (typeMap == LIVETRACKING) {
            liveTracking = true
            viewModel.isLiveTraking = true
        } else if (typeMap == INFODESK) {
            val visioID = intent.getStringExtra("visioID")
            viewModel.setOrigin( "B01-UL001-IDA0394")
            viewModel.setDestination(visioID.toString())
        }
        else {
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
        }



        //sendInstructionToTTS()



        setContent {

            totalPoints = konfettiViewModel.totalPoints.collectAsStateWithLifecycle(5000).value

            var openBottomSheet by rememberSaveable { mutableStateOf(false) }

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

                val state: KonfettiViewModel.State by konfettiViewModel.state.observeAsState(
                    KonfettiViewModel.State.Idle,
                )

                val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)


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

//                    Button(onClick = {
//                        if (visioIDs?.isNotEmpty() == true) {
//                            if (currentRouteIndex < visioIDs!!.size - 1) {
//                                totalPoints += 500
//                                konfettiViewModel.updateTotalPoints(totalPoints)
//                                konfettiViewModel.festive()
//                                currentRouteIndex++
//                                viewModel.navigateToNextIndex(visioIDs!![currentRouteIndex].visioID)
//                                ++BEACON_MINOR
//                                Log.e("***********", "values = $BEACON_MAJOR , $BEACON_MINOR , total points =  $totalPoints")
//
////                                // Stop old region
////                                currentRegion?.let { region ->
////                                    try {
////                                        beaconManager.stopRangingBeaconsInRegion(region)
////                                    } catch (e: Exception) {
////                                        e.printStackTrace()
////                                    }
////                                }
////
////                                // Start new region
////                                val newRegion = Region(
////                                    "my-major-minor-region",
////                                    null,
////                                    Identifier.fromInt(BEACON_MAJOR),
////                                    Identifier.fromInt(BEACON_MINOR)
////                                )
////                                currentRegion = newRegion
////
////                                try {
////                                    beaconManager.startRangingBeaconsInRegion(newRegion)
////                                } catch (e: RemoteException) {
////                                    e.printStackTrace()
////                                }
//                            } else {
//                                konfettiViewModel.rain()
//                                totalPoints += 500
//                                konfettiViewModel.updateTotalPoints(totalPoints)
//                                lifecycleScope.launch {
//                                    appObservers.updateTotalPoints(totalPoints)
//                                }
//                                Log.e("***********", "values = $BEACON_MAJOR , $BEACON_MINOR , total points =  $totalPoints")
//                            }
//                        }
//                    }) {
//                        Text("Click for the next route")
//                    }

                }

                AnimatedVisibility(
                    visible = openBottomSheet,
                    enter = slideInVertically(
                        initialOffsetY = { it }, // Start from bottom (height of screen)
                        animationSpec = tween(durationMillis = 500) // Adjust speed as needed
                    ),
                    exit = slideOutVertically(
                        targetOffsetY = { it }, // Slide out to the top (negative screen height)
                        animationSpec = tween(durationMillis = 500) // Adjust speed as needed
                    )
                ) {

                    val lazyListState = rememberLazyListState()
                    val isAtTopBottomSheet =
                        lazyListState.firstVisibleItemIndex == 0 && lazyListState.firstVisibleItemScrollOffset == 0



                    ModalBottomSheet(
                        containerColor = Color.Transparent,
                        onDismissRequest = { openBottomSheet = false },
                        sheetState = bottomSheetState,

                        ) {
                        SuccessDialog() {
                            openBottomSheet = false
                        }




                    }
                }

                when (val newState = state) {
                    KonfettiViewModel.State.Idle -> {
                    }
                    is KonfettiViewModel.State.Started -> KonfettiView(
                        modifier = Modifier.fillMaxSize(),
                        parties = newState.party,
                        updateListener =
                            object : OnParticleSystemUpdateListener {
                                override fun onParticleSystemEnded(
                                    system: PartySystem,
                                    activeSystems: Int,
                                ) {
                                    if (activeSystems == 0) {
                                        konfettiViewModel.ended()
                                        openBottomSheet = true
                                    }
                                }
                            },
                    )

                }






            }

            checkPermissionsAndStart()

            if (isBeaconFound) {
                // Pass a lambda that updates the state
                Toast.makeText(this, "Beacon Found", Toast.LENGTH_SHORT).show()

                if (visioIDs?.isNotEmpty() == true) {
                    if (currentRouteIndex < visioIDs!!.size - 1) {
                        totalPoints += 500
                        konfettiViewModel.updateTotalPoints(totalPoints)
                        konfettiViewModel.festive()
                        currentRouteIndex++
                        viewModel.navigateToNextIndex(visioIDs!![currentRouteIndex].visioID)
                        ++BEACON_MINOR
                        Log.e("***********", "values = $BEACON_MAJOR , $BEACON_MINOR , total points =  $totalPoints")

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
                    } else {
                        totalPoints += 500
                        konfettiViewModel.updateTotalPoints(totalPoints)
                        konfettiViewModel.rain()
                        Log.e("***********", "values = $BEACON_MAJOR , $BEACON_MINOR , total points =  $totalPoints")
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
