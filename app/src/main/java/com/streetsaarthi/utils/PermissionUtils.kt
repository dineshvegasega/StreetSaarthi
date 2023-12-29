package com.demo.utils

import android.Manifest
import android.Manifest.permission.CAMERA
import android.Manifest.permission.READ_MEDIA_IMAGES
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat


object PermissionUtils {

    /**
     * Function to check if the location permissions are granted or not
     */
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun isAccessREAD_MEDIA_IMAGESGranted(context: Context): Boolean {
        return ContextCompat
            .checkSelfPermission(
                context,
                READ_MEDIA_IMAGES
            ) == PackageManager.PERMISSION_GRANTED
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun isAccessCAMERAGranted(context: Context): Boolean {
        return ContextCompat
            .checkSelfPermission(
                context,
                CAMERA
            ) == PackageManager.PERMISSION_GRANTED
    }


    /**
     * Function to check if location of the device is enabled or not
     */
    fun isEnabled(context: Context): Boolean {
        val locationManager: LocationManager =
            context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(READ_MEDIA_IMAGES)
                || locationManager.isProviderEnabled(CAMERA)
    }

}