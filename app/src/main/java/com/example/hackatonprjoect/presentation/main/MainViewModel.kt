package com.example.hackatonprjoect.presentation.main

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hackatonprjoect.common.model.fids.FidsEntity
import com.example.hackatonprjoect.common.model.fids.FidsReqVO
import com.example.hackatonprjoect.common.utils.AppPreference
import com.example.hackatonprjoect.data.repositories.MainRepository
import com.example.hackatonprjoect.data.repositories.NotificationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    private val notificationRepository: NotificationRepository,
) : ViewModel() {

    fun getFlightDetailsByFlightNo(reqVO: FidsReqVO, callback: (FidsEntity?) -> Unit) {
        viewModelScope.launch {
            val fids = mainRepository.fetchFlightDetails(reqVO)
            callback(fids)  // Call the callback with the result
        }
    }

    fun createUser(context: Context, deviceID: String, callback: (String) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
        val docID = AppPreference.getUser(context).first()
            Log.e("--------","docID: $docID")
            if (docID.isEmpty()) {
                mainRepository.createUser(deviceID, notificationRepository.getCurrentToken()).collect {fids ->
                    withContext(Dispatchers.Main) {
                        callback(fids)  // Call the callback with the result

                    }
                }

            }else {
                callback(docID)
            }
        }
    }


}