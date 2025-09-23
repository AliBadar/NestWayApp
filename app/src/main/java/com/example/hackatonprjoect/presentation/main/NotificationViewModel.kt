package com.example.hackatonprjoect.presentation.main

import android.app.Application
import android.provider.Settings
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hackatonprjoect.data.repositories.NotificationRepository
import com.hia.common.model.notifications.RegistrationReqVO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val repository: NotificationRepository, private val application: Application
) : ViewModel() {

    private val _notificationState = MutableStateFlow<NotificationState>(NotificationState.Idle)
    val notificationState = _notificationState.asStateFlow()
    val deviceIdentifier = Settings.Secure.getString(
        application.contentResolver, Settings.Secure.ANDROID_ID
    )

    private fun sendTokenToServer(token: String, onResult: () -> Unit = {}) {
        // Your API call implementation
        val registrationReqVO =
            RegistrationReqVO(deviceIdentifier = deviceIdentifier, deviceToken = token)
        viewModelScope.launch {
            try {
                repository.registerDeviceForPushNotification(registrationReqVO)
                    .collect { sessionToken ->
                        withContext(Dispatchers.Main) {
                            onResult()
                        }
                    }
            } catch (e: Exception) {
                e.printStackTrace()

            }
        }
    }

    fun getFcmToken(onResult: () -> Unit = {}) {
        viewModelScope.launch {
            _notificationState.value = NotificationState.Loading
            try {
                val token = repository.getCurrentToken()
                repository.saveFcmToken(application, token)
                _notificationState.value = NotificationState.TokenReceived(token)
                Log.d("NotificationViewModel", "Token: " + token)
                sendTokenToServer(token, onResult)
            } catch (e: Exception) {
                _notificationState.value = NotificationState.Error(e.message ?: "Unknown error")
            }
        }
    }
}

sealed class NotificationState {
    object Idle : NotificationState()
    object Loading : NotificationState()
    data class TokenReceived(val token: String) : NotificationState()
    data class Error(val message: String) : NotificationState()
}