package com.example.hackatonprjoect.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hackatonprjoect.common.model.fids.FidsEntity
import com.example.hackatonprjoect.common.model.fids.FidsReqVO
import com.example.hackatonprjoect.data.repositories.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : ViewModel() {

    fun getFlightDetailsByFlightNo(reqVO: FidsReqVO, callback: (FidsEntity?) -> Unit) {
        viewModelScope.launch {
            val fids = mainRepository.fetchFlightDetails(reqVO)
            callback(fids)  // Call the callback with the result
        }
    }


}