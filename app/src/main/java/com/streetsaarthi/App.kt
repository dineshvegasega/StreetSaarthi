package com.streetsaarthi

import android.app.Application
import com.mukesh.easydatastoremethods.calldatastore.CallDataStore
import dagger.hilt.android.HiltAndroidApp
//import lv.chi.photopicker.ChiliPhotoPicker
import com.streetsaarthi.R
import com.streetsaarthi.controller.CoilImageLoader


@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()
//        ChiliPhotoPicker.init(
//            loader = CoilImageLoader(), authority = "com.streetsaarthi.provider"
//        )
        CallDataStore.initializeDataStore(
            context = applicationContext,
            dataBaseName = applicationContext.getString(R.string.app_name).replace(" ", "_")
        )
    }
}