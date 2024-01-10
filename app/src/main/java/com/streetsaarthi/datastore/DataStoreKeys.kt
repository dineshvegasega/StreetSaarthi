package com.streetsaarthi.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

object DataStoreKeys {
    const val DATA_STORE_NAME = "ApplicationTemplate"
    const val FOREVER_DATA_STORE_NAME = "ForeverApplicationTemplate"
    val THEME_KEY by lazy { stringPreferencesKey("theme_key") }
    val BOOLEAN_DATA by lazy { booleanPreferencesKey("BOOLEAN") }
    val LOGIN_DATA by lazy { stringPreferencesKey("LOGIN_DATA") }
    val AUTH by lazy { stringPreferencesKey("AUTH") }
    val REMEMBER by lazy { booleanPreferencesKey("REMEMBER") }
    val LOGIN_USER by lazy { stringPreferencesKey("LOGIN_USER") }
    val LOGIN_PASSWORD by lazy { stringPreferencesKey("LOGIN_PASSWORD") }
    val LANGUAGE by lazy { stringPreferencesKey("LANGUAGE") }
    val DEFAULT_ADDRESS by lazy { stringPreferencesKey("DEFAULT_ADDRESS") }
    val LIVE_SCHEME_DATA by lazy { stringPreferencesKey("LIVE_SCHEME_DATA") }
    val LIVE_NOTICE_DATA by lazy { stringPreferencesKey("LIVE_NOTICE_DATA") }
    val LIVE_TRAINING_DATA by lazy { stringPreferencesKey("LIVE_TRAINING_DATA") }

    val Context.dataStore by preferencesDataStore(DATA_STORE_NAME)
}
