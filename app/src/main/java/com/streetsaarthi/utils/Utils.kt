package com.streetsaarthi.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.streetsaarthi.screens.mainActivity.MainActivity

import com.streetsaarthi.R
import com.google.android.material.snackbar.Snackbar
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.util.*
import coil.load



/** -------- HIDE KEYBOARD -------- */
@SuppressLint("ServiceCast")
fun hideSoftKeyBoard() = try {
    (MainActivity.context?.get() as Activity).apply {
        val inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        val view = currentFocus
        if (view != null) {
            inputMethodManager.hideSoftInputFromWindow(
                view.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        }
    }
} catch (e: Exception) {
    e.printStackTrace()
}


/**
 * Open Soft Keyboard
 * */
fun View.openKeyboard() = try {
    (MainActivity.context?.get() as Activity).apply {
        postDelayed({
            val inputMethodManager: InputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            this@openKeyboard.requestFocus()
            inputMethodManager.showSoftInput(currentFocus, InputMethodManager.SHOW_IMPLICIT)
        }, 200)
    }
} catch (e: Exception) {
    e.printStackTrace()
}


/**
 * Show Snack Bar
 * */
fun showSnackBar(string: String) = try {
    MainActivity.context?.get()?.let { context ->
        var message = string
        if (message.contains("Unable to resolve host"))
            message = context.getString(R.string.no_internet_connection)
        Snackbar.make(
            (context as Activity).findViewById(android.R.id.content),
            message,
            Snackbar.LENGTH_LONG
        ).apply {
            setBackgroundTint(ContextCompat.getColor(context, R.color._000000))
            animationMode = Snackbar.ANIMATION_MODE_SLIDE
            setTextColor(ContextCompat.getColor(context, R.color._ffffff))
            view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text).maxLines = 5
            show()
        }
    }
} catch (e: Exception) {
    e.printStackTrace()
}







/**
 * Handle Error Messages
 * */
fun Any?.getErrorMessage(): String = when (this) {
    is Throwable -> this.message.getResponseError()
    else -> this.toString().getResponseError()
}

/**
 * Get Response error
 * */
fun String?.getResponseError(): String {
    if (this.isNullOrEmpty()) return ""
    return try {
        val jsonObject = JSONObject(this)
        if (jsonObject.has("message")) {
            jsonObject.getString("message")
        } else if (jsonObject.has("errors")) {
            val array = jsonObject.getJSONArray("errors")
            if (array.length() > 0) {
                array.getJSONObject(0)?.let {
                    if (it.has("message"))
                        return it.getString("message")
                }
            }
            this
        } else this
    } catch (e: Exception) {
        this
    }
}


fun Context.getMediaFilePathFor(uri: Uri): String {
    return contentResolver.query(uri, null, null, null, null)?.use { cursor ->
        val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        cursor.moveToFirst()
        val name = cursor.getString(nameIndex)
        File(filesDir, name).run {
            kotlin.runCatching {
                contentResolver.openInputStream(uri).use { inputStream ->
                    val outputStream = FileOutputStream(this)
                    var read: Int
                    val buffers = ByteArray(inputStream!!.available())
                    while (inputStream.read(buffers).also { read = it } != -1) {
                        outputStream.use {
                            it.write(buffers, 0, read)
                        }
                    }
                }
            }.onFailure {
                ///Logger.error("File Size %s", it.message.orEmpty())
            }
            return path
        }
    } ?: ""
}



fun getGalleryImage(requireActivity: FragmentActivity, callBack: Uri.() -> Unit){
    requireActivity.registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            Log.e("PhotoPicker", "Selected URI: $uri")
           // binding.imageUploadpassportsizeImage.loadImage(url = {  requireContext().getMediaFilePathFor(uri) })
           // callBack(requireActivity.getMediaFilePathFor(uri))
        } else {
            Log.e("PhotoPicker", "No media selected")
        }
    }
}



//fun registerForActivityResultGallery(requireActivity: FragmentActivity): String {
//    requireActivity.registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
//        if (uri != null) {
//            Log.e("PhotoPicker", "Selected URI: $uri")
//           // binding.imageUploadpassportsizeImage.loadImage(url = {  requireContext().getMediaFilePathFor(uri) })
//            return@registerForActivityResult
//        } else {
//            Log.e("PhotoPicker", "No media selected")
//        }
//    }
//}


@SuppressLint("CheckResult")
fun ImageView.loadImage(
    url: () -> String,
    errorPlaceHolder: () -> Int = { R.drawable.ic_place_holder }
) = try {
    val circularProgressDrawable = CircularProgressDrawable(this.context).apply {
        strokeWidth = 5f
        centerRadius = 30f
        start()
    }
    load(if (url().startsWith("http")) url() else File(url())) {
        placeholder(circularProgressDrawable)
        crossfade(true)
        error(errorPlaceHolder())
    }
} catch (e: Exception) {
    e.printStackTrace()
}



fun Context.isTablet(): Boolean {
    return this.resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK >= Configuration.SCREENLAYOUT_SIZE_LARGE
}



