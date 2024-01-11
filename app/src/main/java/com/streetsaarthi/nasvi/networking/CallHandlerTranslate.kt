package com.streetsaarthi.nasvi.networking

import com.demo.networking.ApiInterface
import com.streetsaarthi.nasvi.utils.showSnackBar

fun interface CallHandlerTranslate <T> {


    suspend fun sendRequest(apiInterface: ApiTranslateInterface): T


    fun loading(){
    }

    fun success(response: T){
    }


    fun error(message: String){
        showSnackBar(message)
    }

}