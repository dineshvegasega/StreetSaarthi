package com.streetsaarthi.nasvi

//import com.google.android.datatransport.runtime.scheduling.SchedulingConfigModule_ConfigFactory.config
//import com.google.firebase.FirebaseApp
//import com.google.firebase.remoteconfig.BuildConfig
//import com.google.firebase.remoteconfig.FirebaseRemoteConfig
//import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import android.app.Application
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import com.streetsaarthi.nasvi.datastore.DataStoreUtil
import com.streetsaarthi.nasvi.utils.pxToDp
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class App : Application() {
    companion object{
       // var scale10: Int = 0
    }
//   lateinit var mFirebaseRemoteConfig: FirebaseRemoteConfig
    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

//    val fontScale = resources.configuration.fontScale
//    Log.e("TAG", "App.scale xxxhdpi "+fontScale)
//    if (fontScale == 0.8f) {
//        scale10 = 14
//    } else if (fontScale == 0.9f) {
//        scale10 = 13
//    } else if (fontScale == 1.0f) {
//        scale10 = 12
//    } else if (fontScale == 1.1f) {
//        scale10 = 11
//    } else if (fontScale == 1.2f) {
//        scale10 = 10
//    } else if (fontScale == 1.3f) {
//        scale10 = 9
//    } else if (fontScale == 1.5f) {
//        scale10 = 8
//    } else if (fontScale == 1.7f) {
//        scale10 = 7
//    } else if (fontScale == 2.0f) {
//        scale10 = 6
//    }

//
//    if (scale >= 4.0) {
//        //Log.e("TAG", "App.scale xxxhdpi")
//        scale10 = 12
//    } else if (scale >= 3.0) {
//        //Log.e("TAG", "App.scale xxhdpi")
//        scale10 = 11
//    }else if (scale >= 2.0) {
//       // Log.e("TAG", "App.scale xhdpi")
//        //scale10 = pxToDp(ratio) / 34
////        Log.e("TAG", "App.scale xhdpi"+scale10)
//       // var dd = Math.round((10 * (resources.displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT)).toDouble())
////        var dd = calculateDpToPixel(8)
////        var dp = 10
//
////        Log.e("TAG", "App.scale xhdpi "+dd)
////        var px: Int = 0
////        if (ratio.toInt() == 0) {
////            px = dp
////        } else {
////            px = Math.round(dp * ratio).toInt()
////        }
////        Log.e("TAG", "App.scale xhdpi "+px)
//        scale10 = 11
//    }else if (scale >= 1.5) {
//       // Log.e("TAG", "App.scale hdpi")
//        scale10 = 11
//    }else if (scale >= 1.0) {
//        Log.e("TAG", "App.scale mdpi")
//        scale10 = 10
//    } else {
//        //Log.e("TAG", "App.scale ldpi")
//        scale10 = 9
//    }

//    Log.e("TAG", "App.scale "+App.scale)
//    Log.e("TAG", "App.ratio "+App.ratio)
//    if (scale >= 4.0) {
//        Log.e("TAG", "App.scale xxxhdpi")
//    } else if (scale >= 3.0) {
//        Log.e("TAG", "App.scale xxhdpi")
//    }else if (scale >= 2.0) {
//        Log.e("TAG", "App.scale xhdpi")
//    }else if (scale >= 1.5) {
//        Log.e("TAG", "App.scale hdpi")
//    }else if (scale >= 1.0) {
//        Log.e("TAG", "App.scale mdpi")
//    } else {
//        Log.e("TAG", "App.scale ldpi")
//    }


//    FirebaseApp.initializeApp(applicationContext);
//        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()

//        val configSettings: FirebaseRemoteConfigSettings = FirebaseRemoteConfigSettings.Builder()
//            .setMinimumFetchIntervalInSeconds(3600)
//            .build()
//        mFirebaseRemoteConfig.fetchAndActivate()


//        ChiliPhotoPicker.init(
//            loader = CoilImageLoader(), authority = "com.streetsaarthi.nasvi.provider"
//        )
//        CallDataStore.initializeDataStore(
//            context = applicationContext,
//            dataBaseName = applicationContext.getString(R.string.app_name).replace(" ", "_")
//        )

//        var cacheExpiration = 43200L
//        if (BuildConfig.DEBUG) {
//            cacheExpiration = 0
//        } else {
//            cacheExpiration = 43200L // 12 hours same as the default value
//        }
//
//        val configSettings = FirebaseRemoteConfigSettings.Builder()
//            .setMinimumFetchIntervalInSeconds(cacheExpiration)
//            .build()
//
//        var config = FirebaseRemoteConfig.getInstance()
//        config.setConfigSettingsAsync(configSettings)
//        config.fetch(cacheExpiration).addOnCompleteListener {
//            Log.e("TAG", "addOnCompleteListener "+it)
//        }


        DataStoreUtil.initDataStore(applicationContext)
    }
}