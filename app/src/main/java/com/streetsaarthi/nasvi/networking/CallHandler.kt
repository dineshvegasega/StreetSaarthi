package com.streetsaarthi.nasvi

import com.streetsaarthi.nasvi.screens.mainActivity.MainActivity
import com.streetsaarthi.nasvi.utils.getErrorMessage
import com.streetsaarthi.nasvi.utils.showSnackBar


fun interface CallHandler<T> {


    suspend fun sendRequest(apiInterface: ApiInterface): T


    fun loading(){
    }

    fun success(response: T){
    }


    fun error(message: String){
//        if(message.contains("DOCTYPE html")){
//            MainActivity.context?.get()?.resources?.getString(R.string.something_went_wrong)
//                ?.let { showSnackBar(it) }
//        }else{
            showSnackBar(message)
//        }
    }

}