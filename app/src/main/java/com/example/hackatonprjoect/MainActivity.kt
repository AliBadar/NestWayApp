package com.example.hackatonprjoect

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.example.hackatonprjoect.NavigationRoutes.HOME
import com.example.hackatonprjoect.common.model.fids.FidsEntity
import com.example.hackatonprjoect.common.utils.AppObserver
import com.example.hackatonprjoect.common.utils.AppPreference
import com.example.hackatonprjoect.common.utils.Constants.docIDHuwaei
import com.example.hackatonprjoect.common.utils.Constants.docIDSamsung
import com.example.hackatonprjoect.common.utils.Utility
import com.example.hackatonprjoect.common.utils.Utility.parseBP
import com.example.hackatonprjoect.core.fcm.CombinedPermissionsHandler
import com.example.hackatonprjoect.data.repositories.NotificationRepository
import com.example.hackatonprjoect.presentation.flight_details.FlightDetailScreen
import com.example.hackatonprjoect.presentation.flight_details.FlightDetailsCard
import com.example.hackatonprjoect.presentation.home.HomeViewModel
import com.example.hackatonprjoect.presentation.main.MainScreen
import com.example.hackatonprjoect.presentation.main.MainViewModel
import com.example.hackatonprjoect.presentation.main.NotificationViewModel
import com.example.hackatonprjoect.ui.theme.HackatonPrjoectTheme
import com.google.gson.Gson
import com.google.zxing.integration.android.IntentIntegrator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.getValue

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    val mainViewModel: MainViewModel by viewModels()
    val homeViewModel: HomeViewModel by viewModels()

    @Inject
    lateinit var appObservers: AppObserver

    @Inject
    lateinit var notificationRepo: NotificationRepository

    @SuppressLint("ViewModelConstructorInComposable")
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        intent?.data?.let { handleDeepLink(it) }

        enableEdgeToEdge()
        setContent {

            val navController = rememberNavController()
            val notificationViewModel = NotificationViewModel(notificationRepo, application)

            var scanningState by remember { mutableStateOf(false) }
            var scanRequestedState by remember { mutableStateOf(false) }
            val context = LocalContext.current
            var fidsEntity by remember { mutableStateOf(FidsEntity("")) }
            var openBottomSheet by rememberSaveable { mutableStateOf(false) }

            val calculatedLocation =
                appObservers.refreshFidsEntity.collectAsState(initial = FidsEntity()).value

            val totalPoints =
                appObservers.refreshFidsEntity.collectAsState(initial = 5000).value

            var showLoader by rememberSaveable { mutableStateOf(false) }

            val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)


            var errorMessage by remember {
                mutableStateOf("")
            }

            var showDialog by remember {
                mutableStateOf(false)
            }

            LaunchedEffect(fidsEntity) {
                if (fidsEntity.uid.isNotEmpty()) {
                    openBottomSheet = true
                }
            }

            LaunchedEffect(Unit) {
                appObservers.updateTotalPoints(5000)
            }

            var permissionsGranted by rememberSaveable { mutableStateOf(false) }

            if (!permissionsGranted) {
                CombinedPermissionsHandler(onAllGranted = {
                    permissionsGranted = true
                    notificationViewModel.getFcmToken()
                }, onDenied = { deniedPermissions ->
                    Log.e("Permissions", "Denied: $deniedPermissions")
                    // Show UI message or retry logic if needed
                })
            }

            val cameraPermissionState = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestPermission()
            ) { isGranted ->
                if (isGranted) {
                    scanningState = true
                } else {
                    Toast.makeText(
                        context,
                        context.getString(R.string.cam_perm_required),
                        Toast.LENGTH_SHORT
                    ).show()
                    //shouldCloseScreen = true
                }
            }

            val scannerResult = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.StartActivityForResult()
            ) { result ->

                when (result.resultCode) {
                    Activity.RESULT_CANCELED -> {
//                        showLoader = false
                        // User pressed back in scanner - close our screen too
                        //shouldCloseScreen = true
                    }

                    Activity.RESULT_OK -> {
//                        showLoader = true
                        val scanResult =
                            IntentIntegrator.parseActivityResult(result.resultCode, result.data)
                        scanResult?.contents?.let { barcode ->
                            try {

                                mainViewModel.createUser(this@MainActivity ,Utility.getDeviceId(this@MainActivity)) {
                                    lifecycleScope.launch {
                                        if (Build.BRAND.lowercase().contains("samsung")) {
                                            AppPreference.saveUser(this@MainActivity, docIDSamsung)
                                        }else {
                                            AppPreference.saveUser(this@MainActivity, docIDHuwaei)
                                        }

                                        navController.navigate(HOME)
                                    }

                                }

//                                val fidsReqVo = parseBP(barcode, context)
//                                if (fidsReqVo != null) {
//
//
//
//                                    mainViewModel.getFlightDetailsByFlightNo(fidsReqVo) { fids ->
//                                        if (fids != null) {
//                                            lifecycleScope.launch {
//                                                appObservers.updateFidsEntity(fids)
//                                            }
//                                            fidsEntity = fids
//                                            openBottomSheet = true
//                                        } else {
//                                            errorMessage =
//                                                context.getString(R.string.please_scan_boarding_pass)
//                                            showDialog = true
//                                        }
//                                        showLoader = false
//                                    }
//                                } else {
//                                    showLoader = false
//                                    //shouldCloseScreen = true
//
//                                    errorMessage =
//                                        context.getString(R.string.please_scan_boarding_pass)
//                                    showDialog = true
//                                }
                            } catch (e: Exception) {
                                showLoader = false
                                errorMessage = context.getString(R.string.please_scan_boarding_pass)
                                showDialog = true
                                //shouldCloseScreen = true
//                        Toast.makeText(context, "Scan error: ${e.message}", Toast.LENGTH_SHORT)
//                            .show()
                            } /*finally {
                        shouldCloseScreen = true
                    }*/
                        }
                    }
                }
                scanningState = false
                scanRequestedState = false
            }

            LaunchedEffect(scanningState) {
                if (scanningState && scanRequestedState) {
//                    activity.requestedOrientation =
//                        ActivityInfo.SCREEN_ORIENTATION_PORTRAIT // Force portrait temporarily

                    val integrator = IntentIntegrator(this@MainActivity).apply {
                        setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES)
                        setOrientationLocked(false) // Allow device to manage orientation but prevent forced landscape
                        setPrompt(context.getString(R.string.scan_boarding_pass))
                        setBeepEnabled(false)
                        setBarcodeImageEnabled(true)
                    }
                    scannerResult.launch(integrator.createScanIntent())

//                    // Reset orientation once scanning is done
//                    activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
                }
            }



            HackatonPrjoectTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    Column(
                        modifier = Modifier.padding(innerPadding),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

//                        MainScreen() {
//                            scanRequestedState = true
//                            cameraPermissionState.launch(Manifest.permission.CAMERA)
//                        }

                        Navigation(navController, appObservers, homeViewModel, innerPadding) {

//                            navController.navigate(HOME)
//                            openBottomSheet = false

                            mainViewModel.createUser(this@MainActivity ,Utility.getDeviceId(this@MainActivity)) {
                                lifecycleScope.launch {
                                    if (Build.BRAND.lowercase().contains("samsung")) {
                                        AppPreference.saveUser(this@MainActivity, docIDSamsung)
                                    }else {
                                        AppPreference.saveUser(this@MainActivity, docIDHuwaei)
                                    }

                                    navController.navigate(HOME)
                                }

                            }

//                            scanRequestedState = true
//                            cameraPermissionState.launch(Manifest.permission.CAMERA)
                        }

//                        Button(onClick = {
//                            startActivity(
//                                Intent(
//                                    this@MainActivity,
//                                    LiveStreamingActivity::class.java
//                                )
//                            )
//                        }) {
//                            Text("Call Now")
//                        }
//
//                        Spacer(Modifier.height(5.dp))
//
//                        Button(onClick = {
//                            scanRequestedState = true
//                            cameraPermissionState.launch(Manifest.permission.CAMERA)
//                        }) {
//                            Text("Scanning")
//                        }
//
//                        Spacer(Modifier.height(5.dp))
//
//                        Button(onClick = {
//                            startActivity(
//                                Intent(
//                                    this@MainActivity,
//                                    LiveStreamingActivity::class.java
//                                )
//                            )
//                        }) {
//                            Text("Call Now")
//                        }
                    }

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


                        FlightDetailsCard(fidsEntity, {}, {
                            openBottomSheet = false
                        }) { name ->

                            lifecycleScope.launch {
                                AppPreference.saveUser(this@MainActivity, name)
                            }
                            navController.navigate(HOME)
                            openBottomSheet = false
                        }
                    }
                }
            }
        }
    }
//    override fun onNewIntent(intent: Intent) {
//        super.onNewIntent(intent)
//        setIntent(intent) // Update the current intent
//
//        intent?.data?.let { uri ->
//            handleDeepLink(uri)
//        }
//    }`
//    private fun handleDeepLink(uri: Uri) {
//        val type = uri.getQueryParameter("type")
//        val destination = uri.getQueryParameter("destination")
//
//        if (type != null && destination != null) {
//            val mapIntent = Intent(this, MapActivity::class.java).apply {
//                putExtra("destination", destination)
//            }
//            startActivity(mapIntent)
//        }
//    }
}