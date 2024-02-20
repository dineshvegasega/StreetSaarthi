package com.streetsaarthi.nasvi.di

import android.util.Log
import com.google.gson.JsonElement
import com.streetsaarthi.nasvi.networking.ApiTranslateInterface
import com.streetsaarthi.nasvi.networking.new.ApiClient
import com.streetsaarthi.nasvi.utils.parseResult
import kotlinx.coroutines.runBlocking
import okhttp3.internal.notify
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TotalEr : Thread() {
    var myResponse = ""
    override fun run() {
        super.run()
        synchronized(this){
            val apiInterface: ApiTranslateInterface = ApiClient.getClient()!!.create(
                ApiTranslateInterface::class.java)
            val hero: Call<JsonElement> = apiInterface.translate("hi", "Hello")
            hero.enqueue(object : Callback<JsonElement> {
                override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                    if (response != null && response.isSuccessful && response.body() != null) {
                        Log.e("Error:::","onResponse "+response.body()!!)
                        myResponse = response.body().toString().parseResult()
                        this.notify()
                    }
                }

                override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                    Log.e("Error:::","onFailure "+t.message)
                    this.notify()
                }
            })
        }
    }
}