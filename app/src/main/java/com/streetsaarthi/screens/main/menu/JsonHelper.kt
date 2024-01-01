package com.streetsaarthi.screens.main.menu

import android.content.Context
import android.content.res.ColorStateList
import androidx.core.content.res.ResourcesCompat
import org.json.JSONObject
import com.google.gson.Gson
import com.streetsaarthi.R
import com.streetsaarthi.screens.main.MainActivity
import java.util.Locale


open class JsonHelper(private var context: Context) {
    private var newspaperList: MutableList<ItemMenuModel>? = null

    open fun getMenuData(locale: Locale): List<ItemMenuModel>? {
        if (newspaperList == null)
            newspaperList = ArrayList()

        try {
            var jsonObject : JSONObject = JSONObject()
            if (MainActivity.context.get()!!.getString(R.string.englishVal) == ""){
                jsonObject = JSONObject(getJSONFromAssets("menu_data_english.json")!!)
            } else if (MainActivity.context.get()!!.getString(R.string.englishVal) == ""+locale){
                jsonObject = JSONObject(getJSONFromAssets("menu_data_english.json")!!)
            } else if (MainActivity.context.get()!!.getString(R.string.bengaliVal) == ""+locale){
                jsonObject = JSONObject(getJSONFromAssets("menu_data_bengali.json")!!)
            } else if (MainActivity.context.get()!!.getString(R.string.gujaratiVal) == ""+locale){
                jsonObject = JSONObject(getJSONFromAssets("menu_data_gujarati.json")!!)
            } else if (MainActivity.context.get()!!.getString(R.string.hindiVal) == ""+locale){
                jsonObject = JSONObject(getJSONFromAssets("menu_data_hindi.json")!!)
            } else if (MainActivity.context.get()!!.getString(R.string.kannadaVal) == ""+locale){
                jsonObject = JSONObject(getJSONFromAssets("menu_data_kannada.json")!!)
            } else if (MainActivity.context.get()!!.getString(R.string.malayalamVal) == ""+locale){
                jsonObject = JSONObject(getJSONFromAssets("menu_data_malayalam.json")!!)
            } else if (MainActivity.context.get()!!.getString(R.string.marathiVal) == ""+locale){
                jsonObject = JSONObject(getJSONFromAssets("menu_data_marathi.json")!!)
            } else if (MainActivity.context.get()!!.getString(R.string.punjabiVal) == ""+locale){
                jsonObject = JSONObject(getJSONFromAssets("menu_data_punjabi.json")!!)
            } else if (MainActivity.context.get()!!.getString(R.string.tamilVal) == ""+locale){
                jsonObject = JSONObject(getJSONFromAssets("menu_data_tamil.json")!!)
            }

            val jsonArray = jsonObject.getJSONArray("menu")
            val k = jsonArray.length()

            for (i in 0 until k) {
                val tempJsonObject = jsonArray.getJSONObject(i).toString()
                val gson = Gson()
                val newsPaper = gson.fromJson<ItemMenuModel>(tempJsonObject, ItemMenuModel::class.java)
                newspaperList?.add(newsPaper)
            }
            return newspaperList
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    private fun getJSONFromAssets(fileName: String): String? {
        val json: String
        try {
            val inputStream = context.assets.open(fileName)
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            json = String(buffer)
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
        return json
    }
}