package com.streetsaarthi

import android.app.Application
import com.streetsaarthi.datastore.DataStoreUtil
import dagger.hilt.android.HiltAndroidApp



@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()
//        ChiliPhotoPicker.init(
//            loader = CoilImageLoader(), authority = "com.streetsaarthi.provider"
//        )
//        CallDataStore.initializeDataStore(
//            context = applicationContext,
//            dataBaseName = applicationContext.getString(R.string.app_name).replace(" ", "_")
//        )

        DataStoreUtil.initDataStore(applicationContext)
    }
}