package com.streetsaarthi.screens.onboarding.start

import androidx.lifecycle.ViewModel
import com.demo.networking.Repository
import com.streetsaarthi.screens.mainActivity.MainActivity
import com.streetsaarthi.R
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class StartVM @Inject constructor(private val repository: Repository): ViewModel() {

    var itemMain : ArrayList<Item> ?= ArrayList()
    val locale: Locale = MainActivity.context.get()!!.resources.configuration.locales[0]

    init {
//        locale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            MainActivity.context.get()!!.resources.configuration.locales[0]
//        } else {
//            MainActivity.context.get()!!.resources.configuration.locale
//        }
//        Log.e("TAG", "getLanguage "+ locale)
        if (MainActivity.context.get()!!.getString(R.string.englishVal) == ""+locale){
            itemMain?.add(Item(MainActivity.context.get()!!.getString(R.string.english), MainActivity.context.get()!!.getString(R.string.englishVal),true))
        }else{
            itemMain?.add(Item(MainActivity.context.get()!!.getString(R.string.english), MainActivity.context.get()!!.getString(R.string.englishVal),false))
        }

        if (MainActivity.context.get()!!.getString(R.string.bengaliVal) == ""+locale){
            itemMain?.add(Item(MainActivity.context.get()!!.getString(R.string.bengali), MainActivity.context.get()!!.getString(R.string.bengaliVal),true))
        }else{
            itemMain?.add(Item(MainActivity.context.get()!!.getString(R.string.bengali), MainActivity.context.get()!!.getString(R.string.bengaliVal),false))
        }

        if (MainActivity.context.get()!!.getString(R.string.gujaratiVal) == ""+locale){
            itemMain?.add(Item(MainActivity.context.get()!!.getString(R.string.gujarati), MainActivity.context.get()!!.getString(R.string.gujaratiVal),true))
        }else{
            itemMain?.add(Item(MainActivity.context.get()!!.getString(R.string.gujarati), MainActivity.context.get()!!.getString(R.string.gujaratiVal),false))
        }

        if (MainActivity.context.get()!!.getString(R.string.hindiVal) == ""+locale){
            itemMain?.add(Item(MainActivity.context.get()!!.getString(R.string.hindi), MainActivity.context.get()!!.getString(R.string.hindiVal),true))
        }else{
            itemMain?.add(Item(MainActivity.context.get()!!.getString(R.string.hindi), MainActivity.context.get()!!.getString(R.string.hindiVal),false))
        }

        if (MainActivity.context.get()!!.getString(R.string.kannadaVal) == ""+locale){
            itemMain?.add(Item(MainActivity.context.get()!!.getString(R.string.kannada), MainActivity.context.get()!!.getString(R.string.kannadaVal),true))
        }else{
            itemMain?.add(Item(MainActivity.context.get()!!.getString(R.string.kannada), MainActivity.context.get()!!.getString(R.string.kannadaVal),false))
        }

        if (MainActivity.context.get()!!.getString(R.string.malayalamVal) == ""+locale){
            itemMain?.add(Item(MainActivity.context.get()!!.getString(R.string.malayalam), MainActivity.context.get()!!.getString(R.string.malayalamVal),true))
        }else{
            itemMain?.add(Item(MainActivity.context.get()!!.getString(R.string.malayalam), MainActivity.context.get()!!.getString(R.string.malayalamVal),false))
        }

        if (MainActivity.context.get()!!.getString(R.string.marathiVal) == ""+locale){
            itemMain?.add(Item(MainActivity.context.get()!!.getString(R.string.marathi), MainActivity.context.get()!!.getString(R.string.marathiVal),true))
        }else{
            itemMain?.add(Item(MainActivity.context.get()!!.getString(R.string.marathi), MainActivity.context.get()!!.getString(R.string.marathiVal),false))
        }

        if (MainActivity.context.get()!!.getString(R.string.punjabiVal) == ""+locale){
            itemMain?.add(Item(MainActivity.context.get()!!.getString(R.string.punjabi), MainActivity.context.get()!!.getString(R.string.punjabiVal),true))
        }else{
            itemMain?.add(Item(MainActivity.context.get()!!.getString(R.string.punjabi), MainActivity.context.get()!!.getString(R.string.punjabiVal),false))
        }

        if (MainActivity.context.get()!!.getString(R.string.tamilVal) == ""+locale){
            itemMain?.add(Item(MainActivity.context.get()!!.getString(R.string.tamil), MainActivity.context.get()!!.getString(R.string.tamilVal),true))
        }else{
            itemMain?.add(Item(MainActivity.context.get()!!.getString(R.string.tamil), MainActivity.context.get()!!.getString(R.string.tamilVal),false))
        }

        if (MainActivity.context.get()!!.getString(R.string.teluguVal) == ""+locale){
            itemMain?.add(Item(MainActivity.context.get()!!.getString(R.string.telugu), MainActivity.context.get()!!.getString(R.string.teluguVal),true))
        }else{
            itemMain?.add(Item(MainActivity.context.get()!!.getString(R.string.telugu), MainActivity.context.get()!!.getString(R.string.teluguVal),false))
        }
    }

    data class Item (
        var name: String = "",
        var locale: String = "",
        var isSelected: Boolean? = false
    )


}