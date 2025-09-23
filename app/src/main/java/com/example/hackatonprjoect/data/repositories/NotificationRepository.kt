package com.example.hackatonprjoect.data.repositories

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.hackatonprjoect.common.model.notifications.PushMonitorReqVO
import com.example.hackatonprjoect.common.model.notifications.RegistartionResponse
import com.example.hackatonprjoect.core.network.ApiService
import com.google.firebase.messaging.FirebaseMessaging
import com.hia.common.model.notifications.RegistrationReqVO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


// Top-level DataStore declaration
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "notification_prefs")

class NotificationRepository @Inject constructor(
    private val context: Context,
    private val apiService: ApiService
) {
    // Moved inside the class (not in companion object)
    private val FCM_TOKEN_KEY = stringPreferencesKey("fcm_token")
    private val SESSION_TOKEN = stringPreferencesKey("session_token")

    suspend fun saveFcmToken(context: Context, token: String) {
        context.dataStore.edit { preferences ->
            preferences[FCM_TOKEN_KEY] = token
        }
    }

    suspend fun getFcmToken(context: Context): String? {
        return context.dataStore.data.first()[FCM_TOKEN_KEY]
    }


    suspend fun getCurrentToken(): String {
        return try {
            FirebaseMessaging.getInstance().token.await()
        } catch (e: Exception) {
            throw NotificationException("Failed to get FCM token", e)
        }
    }

    suspend fun deleteToken() {
        try {
            FirebaseMessaging.getInstance().deleteToken().await()
            context.dataStore.edit { it.remove(FCM_TOKEN_KEY) }
        } catch (e: Exception) {
            throw NotificationException("Failed to delete FCM token", e)
        }
    }

    suspend fun saveSessionToken(context: Context, token: String) {
        context.dataStore.edit { preferences ->
            preferences[SESSION_TOKEN] = token
        }
    }

    suspend fun getSessionToken(context: Context): String? {
        return context.dataStore.data.first()[SESSION_TOKEN]
    }

    suspend fun deleteSessionToken() {
        try {
            FirebaseMessaging.getInstance().deleteToken().await()
            context.dataStore.edit { it.remove(SESSION_TOKEN) }
        } catch (e: Exception) {
            throw NotificationException("Failed to delete Session token", e)
        }
    }

    suspend fun registerDeviceForPushNotification(registrationReqVO: RegistrationReqVO): Flow<Boolean> {

        return flow {
            val regResponse = apiService.registerDeviceForNotification(registrationReqVO)
            regResponse?.let {
                saveRegistrationResponse(regResponse).apply {
                    emit(true)
                }
            }
        }
    }

    private suspend fun saveRegistrationResponse(regResponse: RegistartionResponse) {
        regResponse?.session_token?.let {
            Log.d("HIA", "saveRegistrationResponse: $it")
            saveSessionToken(context, it)
        }
    }
}

class NotificationException(message: String, cause: Throwable) : Exception(message, cause)

