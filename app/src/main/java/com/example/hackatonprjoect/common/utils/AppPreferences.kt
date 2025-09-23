package com.example.hackatonprjoect.common.utils

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Locale

private val Context.dataStore by preferencesDataStore(name = "settings")

object AppPreference {
    private val LANGUAGE_KEY = stringPreferencesKey("app_language")
    private val APP_USER = stringPreferencesKey("app_user")

    fun getLanguage(context: Context): Flow<String> {
        return context.dataStore.data.map { preferences ->
            preferences[LANGUAGE_KEY] ?: getLocalLang()
        }
    }

    fun getLocalLang(): String {
        return if (Locale.getDefault().language.contains("ar")) "ar" else "en"
    }

    suspend fun saveLanguage(context: Context, language: String) {
        context.dataStore.edit { preferences ->
            preferences[LANGUAGE_KEY] = language
        }
    }

    suspend fun saveUser(context: Context, user: String) {
        context.dataStore.edit { preferences ->
            preferences[APP_USER] = user
        }
    }

    fun getUser(context: Context): Flow<String> {
        return context.dataStore.data.map { preferences ->
            preferences[APP_USER] ?: getLocalLang()
        }
    }
}