package com.streetsaarthi.nasvi.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.database.Cursor
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import coil.load
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.messaging.FirebaseMessaging
import com.kochia.customer.utils.hideKeyboard
import com.streetsaarthi.nasvi.R
import com.streetsaarthi.nasvi.screens.mainActivity.MainActivity
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern


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
    MainActivity.activity.get()?.hideKeyboard()
    MainActivity.context.get()?.let { context ->
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



fun Context.getRealPathFromUri(contentUri: Uri?): String? {
    var cursor: Cursor?=null
    return try {
        var proj=arrayOf<kotlin.String>(MediaStore.Images.Media.DATA)
        //val proj: Array<String>= arrayOf<kotlin.String>(MediaStore.Images.Media.DATA)
        cursor=contentResolver.query(contentUri!!,proj,null,null,null)
        assert(cursor != null)
        val column_index=cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        cursor.getString(column_index)
    } finally {
        cursor?.close()
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
    errorPlaceHolder: () -> Int = { R.drawable.user_icon }
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


@SuppressLint("CheckResult")
fun ImageView.loadImageBanner(
    url: () -> String,
    errorPlaceHolder: () -> Int = { R.drawable.no_image_banner }
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






fun isValidPassword(password: String): Boolean {
    val pattern: Pattern
    val matcher: Matcher
    val specialCharacters = "-@%\\[\\}+'!/#$^?:;,\\(\"\\)~`.*=&\\{>\\]<_"
    val PASSWORD_REGEX =
        "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[$specialCharacters])(?=\\S+$).{8,20}$"
    pattern = Pattern.compile(PASSWORD_REGEX)
    matcher = pattern.matcher(password)
    return matcher.matches()
}


fun AppCompatEditText.focus() {
//    text?.let { setSelection(it.length) }
//    postDelayed({
//        requestFocus()
//        val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
//        imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
//    }, 100)
}


//var handlerMain = Handler(Looper.getMainLooper())
var runnable : Runnable ?= null
fun ViewPager.autoScroll() {
    autoScrollStop()
        var scrollPosition = 0
        runnable = object : Runnable {
            override fun run() {
                val count = adapter?.count ?: 0
                setCurrentItem(scrollPosition++ % count, true)
                if (handler != null){
                    handler?.let {
                        postDelayed(this, 3000)
                    }
                }
            }
        }
    if (handler != null){
        if (runnable != null){
            handler?.let {
                post(runnable as Runnable)
            }
        }
    }
}


fun ViewPager.autoScrollStop() {
    if (handler != null){
        if (runnable != null){
            runnable?.let { handler?.removeCallbacks(it) }
        }
    }
}


@SuppressLint("SimpleDateFormat")
fun String.changeDateFormat(inDate : String, outDate: String) : String? {
    var str : String ?= ""
    try {
        val inputFormat = SimpleDateFormat(inDate)
        val outputFormat = SimpleDateFormat(outDate)
        var date: Date? = null
        try {
            date = inputFormat.parse(this)
            str = date?.let { outputFormat.format(it) }
        } catch (e: ParseException) {
            e.printStackTrace()
        }
    }catch (e: Exception){
        str = ""
    }
    return str
}



@SuppressLint("ClickableViewAccessibility")
fun AppCompatEditText.onRightDrawableClicked(onClicked: (view: EditText) -> Unit) {
    this.setOnTouchListener { v, event ->
        var hasConsumed = false
        if (v is EditText) {
            if (event.x >= v.width - v.totalPaddingRight) {
                if (event.action == MotionEvent.ACTION_UP) {
                    onClicked(this)
                }
                hasConsumed = true
            }
        }
        hasConsumed
    }
}


fun ArrayList<String>.imageZoom(ivImage: ImageView) {
//    StfalconImageViewer.Builder<String>(MainActivity.binding.root.context, this) { view, image ->
//        Picasso.get().load(image).into(view)
//    }
//        .withTransitionFrom(ivImage)
//        .withBackgroundColor(ContextCompat.getColor(MainActivity.mainActivity.get()!!, R.color._D9000000))
//        .show()
}


fun getToken(callBack: String.() -> Unit){
    FirebaseMessaging.getInstance().token.addOnSuccessListener { result ->
        if(result != null){
            callBack(result)
        }
    }
}


fun String.titlecaseFirstCharIfItIsLowercase() = replaceFirstChar {
    if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
}



fun RecyclerView.smoothSnapToPosition(position: Int, snapMode: Int = LinearSmoothScroller.SNAP_TO_END) {
    val smoothScroller = object : LinearSmoothScroller(this.context) {
        override fun getVerticalSnapPreference(): Int = snapMode
        override fun getHorizontalSnapPreference(): Int = snapMode
    }
    smoothScroller.targetPosition = position
    layoutManager?.startSmoothScroll(smoothScroller)
}

fun Context.isTablet(): Boolean {
    return this.resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK >= Configuration.SCREENLAYOUT_SIZE_LARGE
}


fun ViewPager2.setIsFocusableInTouchModeKtx(isFocusableInTouchMode:Boolean){
    if (childCount > 0) {
        val rl = this.getChildAt(0)
        if (rl is RecyclerView) {
            rl.isFocusableInTouchMode = isFocusableInTouchMode
        }
    }
}


fun ViewPager2.setDescendantFocusabilityKtx(focusability:Int) {
    if (childCount > 0) {
        val rl = this.getChildAt(0)
        if (rl is RecyclerView) {
            rl.descendantFocusability = focusability
        }
    }
}



fun ViewPager2.updatePagerHeightForChild(view: View) {
    view.post {
        val wMeasureSpec = View.MeasureSpec.makeMeasureSpec(view.width, View.MeasureSpec.EXACTLY)
        val hMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        view.measure(wMeasureSpec, hMeasureSpec)
        layoutParams = (layoutParams).also { lp -> lp.height = height }
        invalidate()
    }
}


//fun Context.glideImage(ivMap: ShapeableImageView, url: String = "") {
//    GlideApp.with(this)
//        .load("url")
//        .apply(myOptionsGlide)
//        .into(ivMap)
//}



val myOptionsGlide: RequestOptions = RequestOptions()
    .placeholder(R.drawable.no_image)
    .diskCacheStrategy(DiskCacheStrategy.ALL)
    .dontAnimate()
    //  .apply( RequestOptions().centerCrop().circleCrop().placeholder(R.drawable.no_image_2))
    .skipMemoryCache(false)

fun String.glideImage(context : Context, ivMap: ShapeableImageView) {
    GlideApp.with(context)
        .load(this)
        .apply(myOptionsGlide)
        .into(ivMap)
}


val myOptionsGlidePortrait: RequestOptions = RequestOptions()
    .placeholder(R.drawable.image_port)
    .diskCacheStrategy(DiskCacheStrategy.ALL)
    .dontAnimate()
    //  .apply( RequestOptions().centerCrop().circleCrop().placeholder(R.drawable.no_image_2))
    .skipMemoryCache(false)

fun String.glideImagePortrait(context : Context, ivMap: ShapeableImageView) {
    GlideApp.with(context)
        .load(this)
        .apply(myOptionsGlidePortrait)
        .into(ivMap)
}



fun View.singleClick(throttleTime: Long = 600L, action: () -> Unit) {
    this.setOnClickListener(object : View.OnClickListener {
        private var lastClickTime: Long = 0
        override fun onClick(v: View) {
            if (SystemClock.elapsedRealtime() - lastClickTime < throttleTime) return
            else action()

            lastClickTime = SystemClock.elapsedRealtime()
        }
    })
}