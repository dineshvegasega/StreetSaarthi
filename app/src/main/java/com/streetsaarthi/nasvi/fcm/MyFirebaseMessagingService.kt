package com.streetsaarthi.nasvi.fcm

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.streetsaarthi.nasvi.R
import com.streetsaarthi.nasvi.datastore.DataStoreKeys
import com.streetsaarthi.nasvi.datastore.DataStoreUtil.readData
import com.streetsaarthi.nasvi.screens.mainActivity.MainActivity
import com.streetsaarthi.nasvi.screens.onboarding.networking.Main
import com.streetsaarthi.nasvi.screens.onboarding.networking.Screen
import com.streetsaarthi.nasvi.utils.isAppIsInBackground
import org.json.JSONObject

class MyFirebaseMessagingService : FirebaseMessagingService(){

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }


    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        Log.e("TAG", "onMessageReceived: " + remoteMessage.getFrom());
        Log.e("TAG", "onMessageReceived: Noti" + remoteMessage.getNotification());
        Log.e("TAG", "onMessageReceived: Data" + remoteMessage.getData());
      //  Log.e("TAG", "isAppIsInBackground()" +  isAppIsInBackground());


        readData(DataStoreKeys.LOGIN_DATA) { loginUser ->
            if (loginUser != null) {
                val json : JSONObject = JSONObject((remoteMessage.data as Map<*, *>?)!!)
                Log.e("TAG", "onMessageReceived: Data" + json.toString());
                noti(json)
            }
        }
    }



    @SuppressLint("MutableImplicitPendingIntent")
    fun noti(json : JSONObject) {
        val intent = Intent(this, MainActivity::class.java).putExtras(Bundle().apply {
            putString("key" , json.getString("type"))
            putString("_id" , json.getString("type_id"))
         //   putBoolean("isActivityBackgound" , isAppIsInBackground())

            putString(Screen , Main)
        })
        val pendingIntent= if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_ALLOW_UNSAFE_IMPLICIT_INTENT)
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
        } else {
            PendingIntent.getActivity(this,0,intent,
                PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE)
        }


        val CHANNEL_ID="my_channel_01" // The id of the channel.
        val name: CharSequence="Notification" // The user-visible name of the channel.
        val importance= NotificationManager.IMPORTANCE_HIGH
        var mChannel: NotificationChannel?=null
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mChannel= NotificationChannel(CHANNEL_ID,name,importance)
            mChannel.enableLights(true)
            mChannel.lightColor= Color.WHITE
            mChannel.setShowBadge(true)
            mChannel.lockscreenVisibility= Notification.VISIBILITY_PUBLIC
        }

        val notification= NotificationCompat.Builder(this,CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher) //.setLargeIcon(icon)
            .setPriority(Notification.PRIORITY_HIGH)
            .setContentTitle(getString(R.string.app_name))
            .setAutoCancel(true)
            .setChannelId(CHANNEL_ID)
            .setContentIntent(pendingIntent)
            .setContentText(json.getString("title")).build()
        val notificationManager=
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) notificationManager.createNotificationChannel(
            mChannel!!
        )
        notificationManager.notify(0,notification)
    }

}