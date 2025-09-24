package com.example.hackatonprjoect.presentation.map

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hackatonprjoect.R
import com.example.hackatonprjoect.common.utils.AppPreference
import com.example.hackatonprjoect.core.fomatters.JsonTransformerClass
import com.example.hackatonprjoect.data.repositories.MainRepository
import com.example.hackatonprjoect.presentation.treauser_hunt.ArtPiece
import com.example.hackatonprjoect.visioglobe.ui.model.Building
import com.example.hackatonprjoect.visioglobe.ui.model.Category
import com.example.hackatonprjoect.visioglobe.ui.model.Floor
import com.example.hackatonprjoect.visioglobe.ui.model.NavigationModel
import com.example.hackatonprjoect.visioglobe.ui.model.PlaceInfo
import com.example.hackatonprjoect.visioglobe.ui.model.PlaceType
import com.example.hackatonprjoect.visioglobe.ui.model.Poi
import com.example.hackatonprjoect.visioglobe.ui.model.SelectorData
import com.google.gson.JsonObject
import com.visioglobe.visiomoveessential.VMEMapController
import com.visioglobe.visiomoveessential.VMEMapControllerBuilder
import com.visioglobe.visiomoveessential.VMEMapView
import com.visioglobe.visiomoveessential.callbacks.VMEAnimationCallback
import com.visioglobe.visiomoveessential.callbacks.VMEComputeRouteCallback
import com.visioglobe.visiomoveessential.enums.VMERouteDestinationsOrder
import com.visioglobe.visiomoveessential.enums.VMERouteRequestType
import com.visioglobe.visiomoveessential.enums.VMEViewMode
import com.visioglobe.visiomoveessential.listeners.VMEBuildingListener
import com.visioglobe.visiomoveessential.listeners.VMECompassListener
import com.visioglobe.visiomoveessential.listeners.VMELifeCycleListener
import com.visioglobe.visiomoveessential.listeners.VMEMapListener
import com.visioglobe.visiomoveessential.listeners.VMENavigationListener
import com.visioglobe.visiomoveessential.listeners.VMEPoiListener
import com.visioglobe.visiomoveessential.models.VMECameraDistanceRange
import com.visioglobe.visiomoveessential.models.VMECameraHeading
import com.visioglobe.visiomoveessential.models.VMECameraPitch
import com.visioglobe.visiomoveessential.models.VMECameraUpdateBuilder
import com.visioglobe.visiomoveessential.models.VMECategory
import com.visioglobe.visiomoveessential.models.VMEInstruction
import com.visioglobe.visiomoveessential.models.VMELocation
import com.visioglobe.visiomoveessential.models.VMEPosition
import com.visioglobe.visiomoveessential.models.VMERouteRequest
import com.visioglobe.visiomoveessential.models.VMERouteResult
import com.visioglobe.visiomoveessential.models.VMESceneContext
import com.visioglobe.visiomoveessential.models.VMEVenueInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import javax.inject.Inject


@HiltViewModel
class MapViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : ViewModel() {

    var mContext: Context? = null
    var isLiveTraking : Boolean = false
    private var mMapController: VMEMapController? = null

    private val _poiList = MutableStateFlow<List<Poi>>(emptyList())
    val poiList: StateFlow<List<Poi>> = _poiList

    private val _mapDataLoaded = MutableStateFlow<Boolean>(false)
    val mapDataLoaded: StateFlow<Boolean> = _mapDataLoaded

    private val _isPlaceInfoDisplayed = MutableStateFlow<Boolean>(false)
    val isPlaceInfoDisplayed: StateFlow<Boolean> = _isPlaceInfoDisplayed

    private val _placeInfo = MutableStateFlow<PlaceInfo>(PlaceInfo())
    val placeInfo: StateFlow<PlaceInfo> = _placeInfo

    private val _navigationModel = MutableStateFlow<NavigationModel>(NavigationModel())
    val navigationModel: StateFlow<NavigationModel> = _navigationModel

    private val _selectorData: MutableStateFlow<SelectorData> = MutableStateFlow(SelectorData())
    val selectorData: StateFlow<SelectorData> = _selectorData

    private val _compassState = MutableStateFlow<Boolean>(false)
    val compassState: StateFlow<Boolean> = _compassState

    var currentInstructionIndex = 0

    var currentInstructionList: List<VMEInstruction>? = null

    var stopVisioIDs: ArrayList<String> = arrayListOf()

    private val buildingList: HashMap<String, String> = HashMap()
    private val floorList: HashMap<String, String> = HashMap()





    override fun onCleared() {
        mMapController?.unloadMapView()
        mMapController?.unloadMapData()
        super.onCleared()
    }

    private fun populateBuildingAndFloor(venueInfo: VMEVenueInfo) {
        venueInfo.buildings.forEach {
            buildingList[it.id] = it.name
            it.floors.forEach { floor ->
                floorList[floor.id] = floor.name
            }
        }
    }


    private fun populateCurrentBuilding(venueInfo: VMEVenueInfo) {
        // Map venueInfo into view object
        val buildings = venueInfo.buildings.map {
            Building(
                id = it.id,
                name = it.name,
                floors = it.floors.map { floor ->
                    Floor(id = floor.id, name = floor.name)
                }
            )
        }

        // update SelectorData
        _selectorData.value = _selectorData.value.copy(
            buildings = buildings,
            mapName = venueInfo.mapName
        )
    }

    fun navigateToNextIndex(visioID: String) {
        setDestination(visioID).also {
            computeRoute(PlaceType.Stop, visioID)
        }

    }



    private val mLifeCycleListener: VMELifeCycleListener = object : VMELifeCycleListener() {

        override fun mapDataDidLoad(venueInfo: VMEVenueInfo) {
            super.mapDataDidLoad(venueInfo)
            _mapDataLoaded.value = true

            populateBuildingAndFloor(venueInfo)
            populateCurrentBuilding(venueInfo)

            val transformer = JsonTransformerClass(mContext!!)
            val jsonString = transformer.getSimplifiedJsonString()
            Log.d("SimplifiedJSON", jsonString)
            mMapController!!.setPois(jsonString)
        }

        override fun mapViewDidLoad() {
            Log.v("DEBUG", "mapViewDidLoad")
            super.mapViewDidLoad()

            val stopId = _navigationModel.value.placeStopId
            if (stopId.isNotEmpty()) {
                computeRoute(type = PlaceType.Start, id = _navigationModel.value.placeStartId)
                computeRoute(type = PlaceType.Stop, id = stopId)
            }
        }

    }

    fun setDestination(destinationId: String) {
        Log.e("*********", "setDestination: called")
        _navigationModel.value = _navigationModel.value.copy(
            placeStopId = destinationId
        )


        stopVisioIDs.add(destinationId)
    }

    fun setOrigin(originId: String) {
        _navigationModel.value = _navigationModel.value.copy(
            placeStartId = originId
        )
    }

    private val mPoiListener: VMEPoiListener = object : VMEPoiListener() {

        override fun mapDidSelectPoi(
            poiID: String?,
            position: VMEPosition?
        ): Boolean {
            if (poiID == null) return true
            val lPoi = mMapController!!.getPoi(poiID)

            _placeInfo.value = _placeInfo.value.copy(
                id = poiID,
                name = lPoi?.name ?: poiID,
                description = lPoi?.htmlDescription ?: ""
            )

            if (lPoi != null) {
                val lIcon = mMapController!!.getDrawableFromMapBundle(lPoi.icon)
                if (lIcon != null) {
                    _placeInfo.value = _placeInfo.value.copy(
                        //icon = lIcon.toBitmap(),
                    )
                }
            }
            _isPlaceInfoDisplayed.value = true
            return true
        }
    }

    private val mCompassListener: VMECompassListener = object : VMECompassListener {
        override fun compassStateChanged(enabled: Boolean) {
            _compassState.value = enabled
        }
    }

    private val mRouteCallback: VMEComputeRouteCallback = object :
        VMEComputeRouteCallback {

        override fun computeRouteDidFinish(
            routeRequest: VMERouteRequest,
            routeResult: VMERouteResult
        ): Boolean {
            _isPlaceInfoDisplayed.value = false
            _navigationModel.value = _navigationModel.value.copy(
                isNavigationDisplayed = true
            )
            return true
        }

        override fun computeRouteDidFail(routeRequest: VMERouteRequest, error: String?) {
            val lRouteDescription = String.format("computeRouteDidFail, Error: %s", error)
            Log.i("DEBUG", lRouteDescription)
            clearRoute()
        }
    }

    private val mMapListener: VMEMapListener = object : VMEMapListener() {

        override fun mapSceneDidUpdate(scene: VMESceneContext?, viewMode: VMEViewMode?) {
            if (scene == null) {
                return
            }
            if (viewMode == VMEViewMode.GLOBAL) {
                _selectorData.value = _selectorData.value.copy(
                    selectedBuildingId = "",
                    selectedFloorId = ""
                )
            } else {
                _selectorData.value = _selectorData.value.copy(
                    selectedFloorId = scene.floorID,
                    selectedBuildingId = scene.buildingID
                )
            }
        }

        override fun mapDidReceiveTapGesture(position: VMEPosition?) {
            position?.let {
                if(!isLiveTraking){
                    updateLocation(position)
                }
            }



        }
    }

    fun updateLocation(pos: VMEPosition) {
        viewModelScope.launch(Dispatchers.IO) {
            mainRepository.updateCurrentLocation(pos, AppPreference.getUser(mContext!!).first())
        }
    }

    fun getOtherDeviceLocation(docID: String) {
        viewModelScope.launch(Dispatchers.IO) {
            mainRepository.getOtherDeviceLocation(docID).collect {response ->
                if (response.isNotEmpty()) {
                    val json = JSONObject(response)
                    if (json != null) {
                        val data = json.getJSONObject("data")
                        if (data != null) {
                            val lat = data.getString("lat")
                            val lon = data.getString("lon")

                            withContext(Dispatchers.Main) {
                                setLocationOnMap(lat, lon, "1")
                            }

                        }
                    }
                }
            }
        }
    }

    private val mBuildingListener: VMEBuildingListener = object : VMEBuildingListener() {
        override fun mapDidSelectBuilding(buildingID: String, position: VMEPosition?): Boolean {
            //Log.v("DEBUG", "BuildingID: " + buildingID)
            return false
        }
    }

    private val mNavigationListener: VMENavigationListener = object : VMENavigationListener {

        override fun instructionsGenerated(instructions: List<VMEInstruction>) {
            currentInstructionList = instructions
            currentInstructionIndex = 0
            _navigationModel.value = _navigationModel.value.copy(
                currentInstruction = currentInstructionList!![currentInstructionIndex].mText,
                isPreviousArrowEnabled = false,
                isNextArrowEnabled = true,
                instructionIndex = 0
            )
            mMapController!!.getTexture(
                currentInstructionList!![currentInstructionIndex].mIconResource,
                ContextCompat.getColor(mContext!!, R.color.purple_500),
                ContextCompat.getColor(mContext!!, R.color.purple_200),
                ContextCompat.getColor(mContext!!, R.color.white)
            ) { bitmap ->
                _navigationModel.value = _navigationModel.value.copy(
                    currentInstructionBitmap = bitmap,
                )
            }
        }


        override fun instructionIndexUpdated(instructionIndex: Int) {
            currentInstructionIndex = instructionIndex

            _navigationModel.value = _navigationModel.value.copy(
                currentInstruction = currentInstructionList!![currentInstructionIndex].mText,
                isPreviousArrowEnabled = instructionIndex > 0,
                isNextArrowEnabled = instructionIndex < (currentInstructionList!!.count() - 1),
                instructionIndex = currentInstructionIndex
            )

            mMapController!!.getTexture(
                currentInstructionList!![currentInstructionIndex].mIconResource,
                ContextCompat.getColor(mContext!!, R.color.purple_500),
                ContextCompat.getColor(mContext!!, R.color.purple_200),
                ContextCompat.getColor(mContext!!, R.color.white)
            ) { bitmap ->
                _navigationModel.value = _navigationModel.value.copy(
                    currentInstructionBitmap = bitmap,
                )
            }
        }
    }

    fun cancelSearch() {
        _poiList.value = emptyList()
    }

    fun loadCategoriesAndPlaces(searchedText: String) {
        viewModelScope.launch {
            val categories: List<Category> = getCategories()
            val pois: List<Poi> = getPois(searchedText, buildingList, floorList, categories)

            _poiList.value = pois
        }
    }

    private fun getCategories(): List<Category> {
        return mMapController!!.queryAllCategoryIDs()
            .mapNotNull { mMapController?.getCategory(it) }
            .map { vmeCategory: VMECategory ->
                Category(
                    id = vmeCategory.id,
                    name = vmeCategory.name,
                    image = mMapController?.getDrawableFromMapBundle(vmeCategory.icon)
                        ?.toBitmap(),
                )
            }
    }

    fun loadMapView(mapView: VMEMapView) {
        mMapController!!.loadMapView(mapView)
    }

    fun viewDidLoad(context: Activity) {
        if (_mapDataLoaded.value) {
            Log.e("MapViewController", "Map Data should be loaded")
            return
        }
        val builder = VMEMapControllerBuilder()
//        builder.mapHash = "mb59995ef2012ff00e4e1ca8e93ebb0955685b269"
//        builder.mapHash = "mcd6c02932a027a03006e98721b9865085ed603a6"
        builder.mapPath = "asset://Data.move.zip"
        mContext = context
        mMapController = VMEMapController(context, builder)

        mMapController!!.loadMapData()

        mMapController!!.selectorViewVisible = false
        mMapController!!.compassHeadingMarkerVisible = false
        mMapController!!.navigationHeaderViewVisible = false

        mMapController!!.setLifeCycleListener(mLifeCycleListener)
        mMapController!!.setPoiListener(mPoiListener)
        mMapController!!.setMapListener(mMapListener)
        mMapController!!.setBuildingListener(mBuildingListener)
        mMapController!!.setNavigationListener(mNavigationListener)
        mMapController!!.setCompassListener(mCompassListener)
    }

    fun computeRoute(type: PlaceType, id: String) {
        when (type) {
            PlaceType.Start -> {
                _navigationModel.value = _navigationModel.value.copy(
                    placeStartId = id
                )
            }

            PlaceType.Stop -> {
                _navigationModel.value = _navigationModel.value.copy(
                    placeStopId = id
                )
            }
        }

        if (!_navigationModel.value.canComputeRoute()) {
            Log.d(
                "MapViewModel",
                "Can't compute route -- > start : ${_navigationModel.value.placeStartId} | stop : ${_navigationModel.value.placeStopId}"
            )
            return
        }

        val lDests: List<Any> = ArrayList<Any>(if (stopVisioIDs.isNullOrEmpty()) listOf(_navigationModel.value.placeStopId) else stopVisioIDs)

        var destinationOrder = if (stopVisioIDs.isNullOrEmpty()) VMERouteDestinationsOrder.CLOSEST else VMERouteDestinationsOrder.IN_ORDER

        val lRouteRequest = VMERouteRequest(
            requestType = VMERouteRequestType.SHORTEST,
            routeDestinationOrder = destinationOrder,
            isAccessible = false
        )
        lRouteRequest.setOrigin(_navigationModel.value.placeStartId)
        lRouteRequest.addDestinations(lDests)
        mMapController!!.computeRoute(lRouteRequest, mRouteCallback)
    }

    fun clearRoute() {
        _navigationModel.value = _navigationModel.value.copy(
            isNavigationDisplayed = false,
            placeStartId = "",
            placeStopId = ""
        )
        mMapController?.setFocusOnMap()
    }

    fun previousInstruction() {
        mMapController?.setNavigationIndex(currentInstructionIndex - 1)
    }

    fun nextInstruction() {
        mMapController?.setNavigationIndex(currentInstructionIndex + 1)
    }

    @SuppressLint("DefaultLocale")
    private fun setLocationOnMap(lat: String, lon: String, floor: String) {

        try {

            val lSceneContext = VMESceneContext("B0", floor)

//            val latitude = String.format("%.7f", lat).toDouble()
//            val longitude = String.format("%.7f", lon).toDouble()

            val lCurrentPosition = VMEPosition(
                lat.toDouble(), lon.toDouble(), 0.0, lSceneContext
            )
            val lCurrentLocation = VMELocation(
                lCurrentPosition, 0.0, 10.0
            )

            val lUpdate = VMECameraUpdateBuilder()
                .setTargets(listOf(lCurrentPosition))
                .setViewMode(VMEViewMode.FLOOR)
                .setPaddingTop(10)
                .setPitch(VMECameraPitch.newPitch(-30.0))
                .build()
            val lAnimationCallback = object : VMEAnimationCallback {
                override fun didFinish() {

                }
            }
            mMapController!!.animateCamera(lUpdate, 0.7f, lAnimationCallback)

            mMapController!!.updateLocation(lCurrentLocation)

        } catch (ex: Exception) {
            ex.printStackTrace()
        }

    }

    fun goToPoi(id: String) {

        val lUpdate = VMECameraUpdateBuilder()
            .setTargets(listOf(id))
            .setPaddingTop(10)
            .setPitch(VMECameraPitch.newDefaultPitch())
            .build()
        val lAnimationCallback = object : VMEAnimationCallback {
            override fun didFinish() {

            }
        }
        mMapController!!.animateCamera(lUpdate, 0.7f, lAnimationCallback)
        mMapController!!.showPoiInfo(id)
    }

    fun selectBuilding(buildingId: String) {
        _selectorData.value = _selectorData.value.copy(
            selectedBuildingId = buildingId,
            selectedFloorId = ""
        )
        goToFloorOrBuilding(buildingId)
    }

    fun selectFloor(floorId: String, buildingId: String) {
        _selectorData.value = _selectorData.value.copy(
            selectedBuildingId = buildingId,
            selectedFloorId = floorId
        )
        goToFloorOrBuilding(floorId)
    }

    fun hidePlaceInfo() {
        _isPlaceInfoDisplayed.value = false
    }

    fun compassClicked() {
        mMapController?.setCompass(!_compassState.value)
    }

    private fun goToFloorOrBuilding(id: String) {
        val lUpdate = VMECameraUpdateBuilder()
            .setTargets(listOf(id))
            .setViewMode(VMEViewMode.FLOOR)
            .setHeading(heading = VMECameraHeading.newPoiID(id))
            .setPitch(VMECameraPitch.newPitch(-30.0))
            .setDistanceRange(VMECameraDistanceRange.newDefaultAltitudeRange())
            .build()
        val lAnimationCallback = object : VMEAnimationCallback {
            override fun didFinish() {

            }
        }
        mMapController!!.animateCamera(lUpdate, 1.0f, lAnimationCallback)
    }

    private fun getPois(
        searchedText: String,
        buildingList: HashMap<String, String>,
        floorList: HashMap<String, String>,
        category: List<Category>,
    ): List<Poi> {
        return mMapController!!.queryAllPoiIDs()
            .mapNotNull { poiId ->
                mMapController?.getPoi(poiId)
            }.filter { vmePoi ->
                vmePoi.name.lowercase().contains(searchedText.lowercase()) || searchedText == ""
            }.map { vmePoi ->
                val lCenterPosition: VMEPosition? = mMapController!!.getPoiPosition(vmePoi.id)
                val buildingName =
                    lCenterPosition?.let { buildingList[it.scene.buildingID] ?: "" } ?: ""
                val floorName = lCenterPosition?.let { floorList[it.scene.floorID] ?: "" } ?: ""
                vmePoi.categories.map { categoryId ->
                    Poi(
                        name = vmePoi.name,
                        id = vmePoi.id,
                        building = buildingName,
                        floor = floorName,
                        category = category.first { it.id == categoryId },
                    )
                }
            }
            .flatten()
    }

}
