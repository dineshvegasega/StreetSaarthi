package com.streetsaarthi.nasvi.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.res.Configuration
import android.database.Cursor
import android.net.Uri
import android.os.SystemClock
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.DisplayMetrics
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.DimenRes
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
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.File
import java.io.FileOutputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
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
        Snackbar.make(
            (context as Activity).findViewById(android.R.id.content),
            message,
            Snackbar.LENGTH_LONG
        ).apply {
            setBackgroundTint(ContextCompat.getColor(context, R.color._000000))
            animationMode = Snackbar.ANIMATION_MODE_SLIDE
            setTextColor(ContextCompat.getColor(context, R.color._ffffff))
            view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text).maxLines = 5
            if (message.contains("Unable to resolve host")) {
                message = context.getString(R.string.no_internet_connection)
                show()
            } else if (message.contains("DOCTYPE html")) {
                message = context.getString(R.string.something_went_wrong)
                show()
            } else if (message.contains("Failed to connect")) {
            } else  {
                show()
            }
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
    }.addOnCanceledListener {
        callBack("result")
    }
}


fun String.firstCharIfItIsLowercase() = replaceFirstChar {
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
    .placeholder(R.drawable.main_logo_land)
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
    .placeholder(R.drawable.main_logo)
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



 fun Context.isAppIsInBackground(): Boolean {
    var isInBackground = true
    try {
        val am = this.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runningProcesses = am.runningAppProcesses
        for (processInfo in runningProcesses) {
            if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                for (activeProcess in processInfo.pkgList) {
                    if (activeProcess == this.packageName) {
                        isInBackground = false
                    }
                }
            }
        }
    } catch (ex: Exception) {
        ex.printStackTrace()
    }
    return isInBackground
}


fun Context.px(@DimenRes dimen: Int): Int = resources.getDimension(dimen).toInt()

fun Context.dp(@DimenRes dimen: Int): Float = px(dimen) / resources.displayMetrics.density


fun Context.dpToPx(dp: Int): Int {
//    val displayMetrics: DisplayMetrics = resources.getDisplayMetrics()
//    return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT))
    val displayMetrics: DisplayMetrics = resources.getDisplayMetrics();
    return ((dp * displayMetrics.density) + 0.5).toInt()
}

fun Context.pxToDp(px: Int): Int {
//    val displayMetrics: DisplayMetrics = resources.getDisplayMetrics()
//    return Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT))
    val displayMetrics: DisplayMetrics = resources.getDisplayMetrics();
    return ((px/displayMetrics.density)+0.5).toInt()
}
fun Context.calculateDpToPixel(dp: Int): Int {
//    val metrics: DisplayMetrics = resources.getDisplayMetrics()
    val d: Float = resources.getDisplayMetrics().density
    return (dp * d).toInt()
   // return dp * (metrics.density / 160)
}


fun String.relationType(array: Array<String>): String {
    ""+this.let{
        return when(it){
            "father" -> {
                array[0]
            }
            "mother" -> {
                array[1]
            }
            "son" -> {
                array[2]
            }
            "daughter" -> {
                array[3]
            }
            "sister" -> {
                array[4]
            }
            "brother" -> {
                array[5]
            }
            "husband" -> {
                array[6]
            }
            "wife" -> {
                array[7]
            }

            else -> {""}
        }
    }
}





//suspend fun String.callUrlAndParseResult(langTo: String): String {
//    val url = "https://translate.googleapis.com/translate_a/single?" +
//            "client=gtx&" +
//            "sl=" + "en" +
//            "&tl=" + langTo +
//            "&dt=t&q=" + URLEncoder.encode(this, "UTF-8")
//    val obj = URL(url)
//    val con = obj.openConnection() as HttpURLConnection
//    con.setRequestProperty("User-Agent", "Mozilla/5.0")
//    val `in` = BufferedReader(
//        InputStreamReader(con.inputStream)
//    )
//    var inputLine: String?
//    val response = StringBuffer()
//    while (`in`.readLine().also { inputLine = it } != null) {
//        response.append(inputLine)
//    }
//    `in`.close()
//    return response.toString().parseResult()
//}




@Throws(Exception::class)
fun String.callUrlAndParseResult(
    langTo: String
): String {
    var myResponse = ""
    val url = "https://translate.googleapis.com/translate_a/single?" +
            "client=gtx&" +
            "sl=" + "en" +
            "&tl=" + langTo +
            "&dt=t&q=" + URLEncoder.encode(this, "UTF-8")
    val httpURLConnection = URL(url).openConnection() as HttpURLConnection
    httpURLConnection.setRequestProperty("Accept", "application/json")
    httpURLConnection.setRequestProperty("User-Agent", "Mozilla/5.0")
    httpURLConnection.requestMethod = "GET"
    httpURLConnection.doInput = true
    httpURLConnection.doOutput = false
    httpURLConnection.connectTimeout = 10000
    httpURLConnection.readTimeout = 10000
    val responseCode = httpURLConnection.responseCode
    if (responseCode == HttpURLConnection.HTTP_OK) {
        val response = httpURLConnection.inputStream.bufferedReader().use {it.readText() }
        myResponse = response.toString().parseResult()
    }

    httpURLConnection.inputStream.bufferedReader().use {
        it.close()
    }
    return myResponse
}


@Throws(Exception::class)
fun String.parseResult(): String {
    var words = ""
    val jsonArray = JSONArray(this)
    val jsonArray2 = jsonArray[0] as JSONArray
    for (i in 0..jsonArray2.length() - 1) {
        val jsonArray3 = jsonArray2[i] as JSONArray
        words += jsonArray3[0].toString()
    }
    return words.toString()
}