package com.streetsaarthi.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
//import com.mukesh.easydatastoremethods.calldatastore.CallDataStore
//import com.mukesh.easydatastoremethods.getpreferencekey.getDataPreferenceKey
import com.streetsaarthi.models.login.Login
import com.streetsaarthi.networking.convertStringIntoClass
import kotlinx.coroutines.flow.first

//
//val AUTH_TOKEN = String.getDataPreferenceKey("authToken")
//val USER_DATA = String.getDataPreferenceKey("userData")


///** Get Auth Token */
//fun getAuthToken(authData: String.() -> Unit) {
//    try {
//        CallDataStore.getPreferenceData(AUTH_TOKEN) { authToken ->
//            authData(authToken.orEmpty())
//        }
//    } catch (e: Exception) {
//        e.printStackTrace()
//    }
//}


/**
 * Get User Data
 * */
fun getUserData(userData: String.() -> Unit) {
//    var login : Login ?= null
//    try {
//        CallDataStore.getPreferenceData(USER_DATA) {
//            if (it.isNullOrEmpty()) userData(String())
//            else userData(it.convertStringIntoClass())
//        }
//    } catch (e: Exception) {
//        e.printStackTrace()
//    }


}

val Context.dataStore: DataStore<Preferences> by preferencesDataStore("APP_DATASTORE")

suspend fun Context.getData(key: String): String {
    return dataStore.data.first()[stringPreferencesKey(key)] ?: ""
}

suspend fun Context.putData(key: String, `object`: String) {
    dataStore.edit {
        it[stringPreferencesKey(key)] = `object`
    }
}

//private const val USER_PREFERENCES_NAME = "user_preferences"
//private val Context.dataStore by preferencesDataStore(
//    name = USER_PREFERENCES_NAME
//)