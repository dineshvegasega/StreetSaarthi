package com.streetsaarthi.nasvi.fcm

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class SmsListener : BroadcastReceiver() {
    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.e("Test","testaa");
    }
}