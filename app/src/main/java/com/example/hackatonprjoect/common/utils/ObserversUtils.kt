package com.example.hackatonprjoect.common.utils

import com.example.hackatonprjoect.common.model.fids.FidsEntity
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppObserver @Inject constructor() {
//    private val _aLocation = MutableSharedFlow<CalculatedLocation?>(replay = 1)
//    val aLocation: SharedFlow<CalculatedLocation?> = _aLocation

    private val _refreshFavourite = MutableSharedFlow<Boolean>(replay = 1)
    val refreshFavourite: SharedFlow<Boolean> = _refreshFavourite


    private val _refreshFidsEntity = MutableSharedFlow<FidsEntity>(replay = 1)
    val refreshFidsEntity: SharedFlow<FidsEntity> = _refreshFidsEntity

    private val _refreshNotify = MutableSharedFlow<Boolean>(replay = 1)
    val refreshNotify: SharedFlow<Boolean> = _refreshNotify

    private val _searchRefresh = MutableSharedFlow<Boolean>(replay = 1)
    val searchRefresh: SharedFlow<Boolean> = _searchRefresh


    private val _refreshHome = MutableSharedFlow<Boolean>(replay = 1)
    val refreshHome: SharedFlow<Boolean> = _refreshHome

    private val _totalPoints = MutableSharedFlow<Int>(replay = 1)
    val totalPoints: SharedFlow<Int> = _totalPoints



//    suspend fun updateLocation(aLocation: CalculatedLocation?) {
//        _aLocation.emit(aLocation)
//    }

    suspend fun updateTotalPoints(updatePoints: Int) {
        _totalPoints.emit(updatePoints)
    }

    suspend fun updateSearch(updateSearch: Boolean = true) {
        _searchRefresh.emit(updateSearch)
    }

    suspend fun updateFidsEntity(refreshFidsEntity: FidsEntity) {
        _refreshFidsEntity.emit(refreshFidsEntity)
    }

    suspend fun refreshFavourite() {
        _refreshFavourite.emit(true)
    }


    suspend fun refreshHome() {
        _refreshHome.emit(true)
    }

    suspend fun refreshNotify() {
        _refreshNotify.emit(true)
    }
}