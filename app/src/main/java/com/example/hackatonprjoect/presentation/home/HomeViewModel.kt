package com.example.hackatonprjoect.presentation.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hackatonprjoect.common.utils.AppPreference
import com.example.hackatonprjoect.presentation.main.NotificationState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val application: Application)  : AndroidViewModel(application) {

    private val _totalPoints = MutableSharedFlow<Int>()
    val totalPoints = _totalPoints.asSharedFlow()


    init {
        getTotalPoints()
    }

    fun getTotalPoints() {

        viewModelScope.launch {
            _totalPoints.emit(AppPreference.getUserPoints(application).first().toInt())
            }

        }


}