/*
 * Copyright (c) Visioglobe SAS. All rights reserved.
 *
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
package com.hia.android.ui.helpers

import com.visioglobe.visiomoveessential.VMEMapController
import com.visioglobe.visiomoveessential.callbacks.VMEAnimationCallback
import com.visioglobe.visiomoveessential.enums.VMEViewMode
import com.visioglobe.visiomoveessential.models.VMECameraDistanceRange
import com.visioglobe.visiomoveessential.models.VMECameraDistanceRange.Companion.newAltitudeRange
import com.visioglobe.visiomoveessential.models.VMECameraHeading
import com.visioglobe.visiomoveessential.models.VMECameraHeading.Companion.newCurrent
import com.visioglobe.visiomoveessential.models.VMECameraHeading.Companion.newHeading
import com.visioglobe.visiomoveessential.models.VMECameraPitch
import com.visioglobe.visiomoveessential.models.VMECameraPitch.Companion.newPitch
import com.visioglobe.visiomoveessential.models.VMECameraUpdateBuilder
import com.visioglobe.visiomoveessential.models.VMELocation


object CameraHelper {

    fun startCameraPosition(mapController: VMEMapController) {
        val lUpdate = VMECameraUpdateBuilder().setTargets(listOf("outside"))
            .setHeading(VMECameraHeading.newPoiID("outside")).setPitch(newPitch(-30.0))
            .setViewMode(VMEViewMode.GLOBAL).build()
        mapController?.updateCamera(lUpdate)
    }

    fun firstCameraAnimation(mapController: VMEMapController) {
        val lUpdate = VMECameraUpdateBuilder().setTargets(listOf("B01"))
            .setHeading(VMECameraHeading.newPoiID("B01"))
            .setDistanceRange(VMECameraDistanceRange.newDefaultAltitudeRange())
            .setViewMode(viewMode = VMEViewMode.FLOOR).setPitch(VMECameraPitch.newCurrent()).build()
        mapController.animateCamera(lUpdate)
    }

    fun gotoTeddyBear(mapController: VMEMapController) {
        val lUpdate = VMECameraUpdateBuilder().setTargets(listOf("B01-UL001-IDA0394"))
            .setHeading(VMECameraHeading.newPoiID("B01-UL001-IDA0394"))
            .setPitch(newPitch(-20.0)).setViewMode(viewMode = VMEViewMode.FLOOR)
            .setDistanceRange(newAltitudeRange(5.0, 20.0)).build()
        mapController.animateCamera(lUpdate, 1.0f, null)

    }

    fun gotoPointOfInterest(
        mMapController: VMEMapController, lDests: List<Any>, value: Boolean
    ) {
        val lUpdate = VMECameraUpdateBuilder().setTargets(lDests).setViewMode(VMEViewMode.FLOOR)
            .setHeading(heading = VMECameraHeading.newPoiID(lDests[0].toString()))
            .setPitch(newPitch(-20.0)).setDistanceRange(newAltitudeRange(5.0, 20.0)).build()
        val lAnimationCallback = object : VMEAnimationCallback {
            override fun didFinish() {
                mMapController?.showPoiInfo(lDests[0].toString())
            }
        }
        if (value) {
            mMapController?.animateCamera(lUpdate, 1.0f, lAnimationCallback)
        }
    }

    fun goToLocation(mapController: VMEMapController, mLocation: VMELocation?) {
        val lCameraHeading: VMECameraHeading
        if (null != mLocation) {
            lCameraHeading = if (mLocation.bearing < 0) {
                newCurrent()
            } else {
                newHeading(mLocation.bearing)
            }
            val lCameraUpdate = VMECameraUpdateBuilder().setTargets(listOf(mLocation.position))
                .setViewMode(VMEViewMode.FLOOR)
                .setHeading(lCameraHeading).setPitch(newPitch(-20.0))
                .setDistanceRange(newAltitudeRange(5.0, 20.0)).build()
            mapController.animateCamera(lCameraUpdate, 1.0f, null)
        }
    }

    public fun zoomToPoi(mapController: VMEMapController, visioID: String) {
        val lCameraUpdate = VMECameraUpdateBuilder().setTargets(listOf(visioID))
            .setHeading(VMECameraHeading.newPoiID(visioID))
            .setDistanceRange(VMECameraDistanceRange.newDefaultAltitudeRange())
            .setPitch(newPitch(-30.0)).setViewMode(VMEViewMode.FLOOR).build()
        mapController?.animateCamera(lCameraUpdate)
    }

    public fun zoomToPois(mapController: VMEMapController, visioIDs: List<String>) {
        val lCameraUpdate = VMECameraUpdateBuilder().setTargets(visioIDs)
            .setHeading(VMECameraHeading.newPoiID(visioIDs.get(0)))
            .setDistanceRange(newAltitudeRange(5.0, 20.0)).setPitch(newPitch(-30.0))
            .setViewMode(VMEViewMode.FLOOR).build()
        mapController?.animateCamera(lCameraUpdate)
    }

    public fun recenterLocation(mapController: VMEMapController, mLocation: VMELocation?) {
        val currentPosition = mLocation?.position
        currentPosition?.let {
            val lTargets: List<Any> = listOf(currentPosition)
            val lMargin = 50
            val lCameraUpdate = VMECameraUpdateBuilder().setTargets(lTargets).setPaddingTop(lMargin)
                .setPaddingBottom(lMargin).setPaddingLeft(lMargin).setPaddingRight(lMargin)
                .setPitch(newPitch(-30.0)).build()
            mapController?.animateCamera(lCameraUpdate)
        }
    }


}