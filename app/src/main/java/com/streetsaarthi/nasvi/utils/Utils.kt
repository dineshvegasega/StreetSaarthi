package com.streetsaarthi.nasvi.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.Signature
import android.content.res.Configuration
import android.database.Cursor
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Parcelable
import android.os.SystemClock
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.provider.Settings
import android.util.DisplayMetrics
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.DimenRes
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import coil.load
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.messaging.FirebaseMessaging
import com.stfalcon.imageviewer.StfalconImageViewer
import com.streetsaarthi.nasvi.R
import com.streetsaarthi.nasvi.models.ItemReturn
import com.streetsaarthi.nasvi.screens.mainActivity.MainActivity
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.math.RoundingMode
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.text.DecimalFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.Period
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


fun Activity.hideKeyboard() {
    val inputMethodManager =
        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    // Check if no view has focus
    val currentFocusedView = currentFocus
    currentFocusedView?.let {
        inputMethodManager.hideSoftInputFromWindow(
            currentFocusedView.windowToken, InputMethodManager.HIDE_NOT_ALWAYS
        )
    }
}

/**
 * Show Snack Bar
 * */
@SuppressLint("CutPasteId")
fun showSnackBar(string: String) = try {
    MainActivity.activity.get()?.hideKeyboard()
    MainActivity.context.get()?.let { context ->
        val message = if (string.contains("Unable to resolve host")) {
            context.getString(R.string.no_internet_connection)
        } else if (string.contains("DOCTYPE html")) {
            context.getString(R.string.something_went_wrong)
        } else if (string.contains("<script>")) {
            context.getString(R.string.something_went_wrong)
        } else if (string.contains("SQLSTATE")) {
            context.getString(R.string.something_went_wrong)
        } else {
            "" + string
        }
//        val message = string
        Snackbar.make(
            (context as Activity).findViewById(android.R.id.content),
            message,
            Snackbar.LENGTH_LONG
        ).apply {
            setBackgroundTint(ContextCompat.getColor(context, R.color._000000))
            animationMode = Snackbar.ANIMATION_MODE_SLIDE
            setTextColor(ContextCompat.getColor(context, R.color._ffffff))
            //var sdpSize = MainActivity.activity.get()?.resources?.getDimension(com.intuit.sdp.R.dimen._5sdp) ?: 5
            view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text).textSize =
                (MainActivity.scale10 + 1).toFloat()
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
    var cursor: Cursor? = null
    return try {
        val proj = arrayOf<kotlin.String>(MediaStore.Images.Media.DATA)
        //val proj: Array<String>= arrayOf<kotlin.String>(MediaStore.Images.Media.DATA)
        cursor = contentResolver.query(contentUri!!, proj, null, null, null)
        assert(cursor != null)
        val column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
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
//        val name = cursor.getString(nameIndex)
        File(filesDir, getImageName()).run {
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


fun getGalleryImage(requireActivity: FragmentActivity, callBack: Uri.() -> Unit) {
    requireActivity.registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
//            Log.e("PhotoPicker", "Selected URI: $uri")
            // binding.imageUploadpassportsizeImage.loadImage(url = {  requireContext().getMediaFilePathFor(uri) })
            // callBack(requireActivity.getMediaFilePathFor(uri))
        } else {
//            Log.e("PhotoPicker", "No media selected")
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
    type: Int,
    url: () -> String,
    errorPlaceHolder: () -> Int = { if (type == 1) R.drawable.user_icon else R.drawable.no_image_banner }
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


//@SuppressLint("CheckResult")
//fun ImageView.loadImageBanner(
//    url: () -> String,
//    errorPlaceHolder: () -> Int = { R.drawable.no_image_banner }
//) = try {
//    val circularProgressDrawable = CircularProgressDrawable(this.context).apply {
//        strokeWidth = 5f
//        centerRadius = 30f
//        start()
//    }
//    load(if (url().startsWith("http")) url() else File(url())) {
//        placeholder(circularProgressDrawable)
//        crossfade(true)
//        error(errorPlaceHolder())
//    }
//} catch (e: Exception) {
//    e.printStackTrace()
//}


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
var runnable: Runnable? = null
fun ViewPager.autoScroll() {
    autoScrollStop()
    var scrollPosition = 0
    runnable = object : Runnable {
        override fun run() {
            val count = adapter?.count ?: 0
            setCurrentItem(scrollPosition++ % count, true)
            if (handler != null) {
                handler?.let {
                    postDelayed(this, 3000)
                }
            }
        }
    }
    if (handler != null) {
        if (runnable != null) {
            handler?.let {
                post(runnable as Runnable)
            }
        }
    }
}


fun ViewPager.autoScrollStop() {
    if (handler != null) {
        if (runnable != null) {
            runnable?.let { handler?.removeCallbacks(it) }
        }
    }
}


@SuppressLint("SimpleDateFormat")
fun String.changeDateFormat(inDate: String, outDate: String): String? {
    var str: String? = ""
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
    } catch (e: Exception) {
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


fun ArrayList<String>.imageZoom(ivImage: ImageView, type: Int) {
    StfalconImageViewer.Builder<String>(MainActivity.mainActivity.get()!!, this) { view, image ->
        Glide.with(MainActivity.mainActivity.get()!!)
            .load(image)
            .apply(if (type == 1) myOptionsGlide else if (type == 2) myOptionsGlideUser else myOptionsGlide)
            .into(view)
    }
        .withTransitionFrom(ivImage)
        .withBackgroundColor(
            ContextCompat.getColor(
                MainActivity.mainActivity.get()!!,
                R.color._D9000000
            )
        )
        .show()
}


fun getToken(callBack: String.() -> Unit) {
    FirebaseMessaging.getInstance().token.addOnSuccessListener { result ->
        if (result != null) {
            callBack(result)
        }
    }.addOnCanceledListener {
        callBack("")
    }
}


fun String.firstCharIfItIsLowercase() = replaceFirstChar {
    if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
}


fun RecyclerView.smoothSnapToPosition(
    position: Int,
    snapMode: Int = LinearSmoothScroller.SNAP_TO_END
) {
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


fun ViewPager2.setIsFocusableInTouchModeKtx(isFocusableInTouchMode: Boolean) {
    if (childCount > 0) {
        val rl = this.getChildAt(0)
        if (rl is RecyclerView) {
            rl.isFocusableInTouchMode = isFocusableInTouchMode
        }
    }
}


fun ViewPager2.setDescendantFocusabilityKtx(focusability: Int) {
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

val myOptionsGlideUser: RequestOptions = RequestOptions()
    .placeholder(R.drawable.user_icon)
    .diskCacheStrategy(DiskCacheStrategy.ALL)
    .dontAnimate()
    //  .apply( RequestOptions().centerCrop().circleCrop().placeholder(R.drawable.no_image_2))
    .skipMemoryCache(false)

fun String.glideImage(context: Context, ivMap: ShapeableImageView) {
    Glide.with(context)
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

fun String.glideImagePortrait(context: Context, ivMap: ShapeableImageView) {
    Glide.with(context)
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
    return ((px / displayMetrics.density) + 0.5).toInt()
}

fun Context.calculateDpToPixel(dp: Int): Int {
//    val metrics: DisplayMetrics = resources.getDisplayMetrics()
    val d: Float = resources.getDisplayMetrics().density
    return (dp * d).toInt()
    // return dp * (metrics.density / 160)
}


fun String.relationType(array: Array<String>): String {
    "" + this.let {
        return when (it) {
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

            else -> {
                ""
            }
        }
    }
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


var networkAlert: AlertDialog? = null

@SuppressLint("SuspiciousIndentation")
fun Context.callNetworkDialog() {
    if (networkAlert?.isShowing == true) {
        return
    }
    networkAlert = MaterialAlertDialogBuilder(this, R.style.LogoutDialogTheme)
        .setTitle(resources.getString(R.string.app_name))
        .setMessage(resources.getString(R.string.no_internet_connection))
        .setPositiveButton(resources.getString(R.string.ok)) { dialog, _ ->
            dialog.dismiss()
        }
//        .setCancelable(false)
        .show()
}


fun Context.isNetworkAvailable() =
    (getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).run {
        getNetworkCapabilities(activeNetwork)?.run {
            hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                    || hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                    || hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
        } ?: false
    }


fun Context.getTitle(_type: String, _title: String): String {
    val locale = LocaleHelper.getLanguage(applicationContext)
    var title = ""
    if (_type == "scheme") {
        title = if (applicationContext.resources.getString(R.string.englishVal) == "" + locale) {
            applicationContext.resources.getString(R.string.scheme_title)
        } else if (applicationContext.resources.getString(R.string.bengaliVal) == "" + locale) {
            applicationContext.resources.getString(R.string.scheme_title_bn)
        } else if (applicationContext.resources.getString(R.string.gujaratiVal) == "" + locale) {
            applicationContext.resources.getString(R.string.scheme_title_gu)
        } else if (applicationContext.resources.getString(R.string.hindiVal) == "" + locale) {
            applicationContext.resources.getString(R.string.scheme_title_hi)
        } else if (applicationContext.resources.getString(R.string.kannadaVal) == "" + locale) {
            applicationContext.resources.getString(R.string.scheme_title_kn)
        } else if (applicationContext.resources.getString(R.string.malayalamVal) == "" + locale) {
            applicationContext.resources.getString(R.string.scheme_title_ml)
        } else if (applicationContext.resources.getString(R.string.marathiVal) == "" + locale) {
            applicationContext.resources.getString(R.string.scheme_title_mr)
        } else if (applicationContext.resources.getString(R.string.punjabiVal) == "" + locale) {
            applicationContext.resources.getString(R.string.scheme_title_pa)
        } else if (applicationContext.resources.getString(R.string.tamilVal) == "" + locale) {
            applicationContext.resources.getString(R.string.scheme_title_ta)
        } else if (applicationContext.resources.getString(R.string.teluguVal) == "" + locale) {
            applicationContext.resources.getString(R.string.scheme_title_te)
        } else if (applicationContext.resources.getString(R.string.urduVal) == "" + locale) {
            applicationContext.resources.getString(R.string.scheme_title_ur)
        } else {
            applicationContext.resources.getString(R.string.scheme_title)
        }
    } else if (_type == "notice") {
        title = if (applicationContext.resources.getString(R.string.englishVal) == "" + locale) {
            applicationContext.resources.getString(R.string.notice_title)
        } else if (applicationContext.resources.getString(R.string.bengaliVal) == "" + locale) {
            applicationContext.resources.getString(R.string.notice_title_bn)
        } else if (applicationContext.resources.getString(R.string.gujaratiVal) == "" + locale) {
            applicationContext.resources.getString(R.string.notice_title_gu)
        } else if (applicationContext.resources.getString(R.string.hindiVal) == "" + locale) {
            applicationContext.resources.getString(R.string.notice_title_hi)
        } else if (applicationContext.resources.getString(R.string.kannadaVal) == "" + locale) {
            applicationContext.resources.getString(R.string.notice_title_kn)
        } else if (applicationContext.resources.getString(R.string.malayalamVal) == "" + locale) {
            applicationContext.resources.getString(R.string.notice_title_ml)
        } else if (applicationContext.resources.getString(R.string.marathiVal) == "" + locale) {
            applicationContext.resources.getString(R.string.notice_title_mr)
        } else if (applicationContext.resources.getString(R.string.punjabiVal) == "" + locale) {
            applicationContext.resources.getString(R.string.notice_title_pa)
        } else if (applicationContext.resources.getString(R.string.tamilVal) == "" + locale) {
            applicationContext.resources.getString(R.string.notice_title_ta)
        } else if (applicationContext.resources.getString(R.string.teluguVal) == "" + locale) {
            applicationContext.resources.getString(R.string.notice_title_te)
        } else if (applicationContext.resources.getString(R.string.urduVal) == "" + locale) {
            applicationContext.resources.getString(R.string.notice_title_ur)
        } else {
            applicationContext.resources.getString(R.string.notice_title)
        }
    } else if (_type == "training") {
        title = if (applicationContext.resources.getString(R.string.englishVal) == "" + locale) {
            applicationContext.resources.getString(R.string.training_title)
        } else if (applicationContext.resources.getString(R.string.bengaliVal) == "" + locale) {
            applicationContext.resources.getString(R.string.training_title_bn)
        } else if (applicationContext.resources.getString(R.string.gujaratiVal) == "" + locale) {
            applicationContext.resources.getString(R.string.training_title_gu)
        } else if (applicationContext.resources.getString(R.string.hindiVal) == "" + locale) {
            applicationContext.resources.getString(R.string.training_title_hi)
        } else if (applicationContext.resources.getString(R.string.kannadaVal) == "" + locale) {
            applicationContext.resources.getString(R.string.training_title_kn)
        } else if (applicationContext.resources.getString(R.string.malayalamVal) == "" + locale) {
            applicationContext.resources.getString(R.string.training_title_ml)
        } else if (applicationContext.resources.getString(R.string.marathiVal) == "" + locale) {
            applicationContext.resources.getString(R.string.training_title_mr)
        } else if (applicationContext.resources.getString(R.string.punjabiVal) == "" + locale) {
            applicationContext.resources.getString(R.string.training_title_pa)
        } else if (applicationContext.resources.getString(R.string.tamilVal) == "" + locale) {
            applicationContext.resources.getString(R.string.training_title_ta)
        } else if (applicationContext.resources.getString(R.string.teluguVal) == "" + locale) {
            applicationContext.resources.getString(R.string.training_title_te)
        } else if (applicationContext.resources.getString(R.string.urduVal) == "" + locale) {
            applicationContext.resources.getString(R.string.training_title_ur)
        } else {
            applicationContext.resources.getString(R.string.training_title)
        }
    } else if (_type == "Vendor Details" || _type == "VendorDetails") {
        if (_title.contains("approved")) {
            title =
                if (applicationContext.resources.getString(R.string.englishVal) == "" + locale) {
                    applicationContext.resources.getString(R.string.vendor_details_title)
                } else if (applicationContext.resources.getString(R.string.bengaliVal) == "" + locale) {
                    applicationContext.resources.getString(R.string.vendor_details_title_bn)
                } else if (applicationContext.resources.getString(R.string.gujaratiVal) == "" + locale) {
                    applicationContext.resources.getString(R.string.vendor_details_title_gu)
                } else if (applicationContext.resources.getString(R.string.hindiVal) == "" + locale) {
                    applicationContext.resources.getString(R.string.vendor_details_title_hi)
                } else if (applicationContext.resources.getString(R.string.kannadaVal) == "" + locale) {
                    applicationContext.resources.getString(R.string.vendor_details_title_kn)
                } else if (applicationContext.resources.getString(R.string.malayalamVal) == "" + locale) {
                    applicationContext.resources.getString(R.string.vendor_details_title_ml)
                } else if (applicationContext.resources.getString(R.string.marathiVal) == "" + locale) {
                    applicationContext.resources.getString(R.string.vendor_details_title_mr)
                } else if (applicationContext.resources.getString(R.string.punjabiVal) == "" + locale) {
                    applicationContext.resources.getString(R.string.vendor_details_title_pa)
                } else if (applicationContext.resources.getString(R.string.tamilVal) == "" + locale) {
                    applicationContext.resources.getString(R.string.vendor_details_title_ta)
                } else if (applicationContext.resources.getString(R.string.teluguVal) == "" + locale) {
                    applicationContext.resources.getString(R.string.vendor_details_title_te)
                } else if (applicationContext.resources.getString(R.string.urduVal) == "" + locale) {
                    applicationContext.resources.getString(R.string.vendor_details_title_ur)
                } else {
                    applicationContext.resources.getString(R.string.vendor_details_title)
                }
        } else if (_title.contains("rejected")) {
            title =
                if (applicationContext.resources.getString(R.string.englishVal) == "" + locale) {
                    applicationContext.resources.getString(R.string.vendor_details_title_reject)
                } else if (applicationContext.resources.getString(R.string.bengaliVal) == "" + locale) {
                    applicationContext.resources.getString(R.string.vendor_details_title_reject_bn)
                } else if (applicationContext.resources.getString(R.string.gujaratiVal) == "" + locale) {
                    applicationContext.resources.getString(R.string.vendor_details_title_reject_gu)
                } else if (applicationContext.resources.getString(R.string.hindiVal) == "" + locale) {
                    applicationContext.resources.getString(R.string.vendor_details_title_reject_hi)
                } else if (applicationContext.resources.getString(R.string.kannadaVal) == "" + locale) {
                    applicationContext.resources.getString(R.string.vendor_details_title_reject_kn)
                } else if (applicationContext.resources.getString(R.string.malayalamVal) == "" + locale) {
                    applicationContext.resources.getString(R.string.vendor_details_title_reject_ml)
                } else if (applicationContext.resources.getString(R.string.marathiVal) == "" + locale) {
                    applicationContext.resources.getString(R.string.vendor_details_title_reject_mr)
                } else if (applicationContext.resources.getString(R.string.punjabiVal) == "" + locale) {
                    applicationContext.resources.getString(R.string.vendor_details_title_reject_pa)
                } else if (applicationContext.resources.getString(R.string.tamilVal) == "" + locale) {
                    applicationContext.resources.getString(R.string.vendor_details_title_reject_ta)
                } else if (applicationContext.resources.getString(R.string.teluguVal) == "" + locale) {
                    applicationContext.resources.getString(R.string.vendor_details_title_reject_te)
                } else if (applicationContext.resources.getString(R.string.urduVal) == "" + locale) {
                    applicationContext.resources.getString(R.string.vendor_details_title_reject_ur)
                } else {
                    applicationContext.resources.getString(R.string.vendor_details_title_reject)
                }
        } else if (_title.contains("pending")) {
            title =
                if (applicationContext.resources.getString(R.string.englishVal) == "" + locale) {
                    applicationContext.resources.getString(R.string.pending_title_en)
                } else if (applicationContext.resources.getString(R.string.bengaliVal) == "" + locale) {
                    applicationContext.resources.getString(R.string.pending_title_bn)
                } else if (applicationContext.resources.getString(R.string.gujaratiVal) == "" + locale) {
                    applicationContext.resources.getString(R.string.pending_title_gu)
                } else if (applicationContext.resources.getString(R.string.hindiVal) == "" + locale) {
                    applicationContext.resources.getString(R.string.pending_title_hi)
                } else if (applicationContext.resources.getString(R.string.kannadaVal) == "" + locale) {
                    applicationContext.resources.getString(R.string.pending_title_kn)
                } else if (applicationContext.resources.getString(R.string.malayalamVal) == "" + locale) {
                    applicationContext.resources.getString(R.string.pending_title_ml)
                } else if (applicationContext.resources.getString(R.string.marathiVal) == "" + locale) {
                    applicationContext.resources.getString(R.string.pending_title_mr)
                } else if (applicationContext.resources.getString(R.string.punjabiVal) == "" + locale) {
                    applicationContext.resources.getString(R.string.pending_title_pa)
                } else if (applicationContext.resources.getString(R.string.tamilVal) == "" + locale) {
                    applicationContext.resources.getString(R.string.pending_title_ta)
                } else if (applicationContext.resources.getString(R.string.teluguVal) == "" + locale) {
                    applicationContext.resources.getString(R.string.pending_title_te)
                } else if (applicationContext.resources.getString(R.string.urduVal) == "" + locale) {
                    applicationContext.resources.getString(R.string.pending_title_ur)
                } else {
                    applicationContext.resources.getString(R.string.pending_title_en)
                }
        }
    } else if (_type == "information") {
        title = if (applicationContext.resources.getString(R.string.englishVal) == "" + locale) {
            applicationContext.resources.getString(R.string.information_title)
        } else if (applicationContext.resources.getString(R.string.bengaliVal) == "" + locale) {
            applicationContext.resources.getString(R.string.information_title_bn)
        } else if (applicationContext.resources.getString(R.string.gujaratiVal) == "" + locale) {
            applicationContext.resources.getString(R.string.information_title_gu)
        } else if (applicationContext.resources.getString(R.string.hindiVal) == "" + locale) {
            applicationContext.resources.getString(R.string.information_title_hi)
        } else if (applicationContext.resources.getString(R.string.kannadaVal) == "" + locale) {
            applicationContext.resources.getString(R.string.information_title_kn)
        } else if (applicationContext.resources.getString(R.string.malayalamVal) == "" + locale) {
            applicationContext.resources.getString(R.string.information_title_ml)
        } else if (applicationContext.resources.getString(R.string.marathiVal) == "" + locale) {
            applicationContext.resources.getString(R.string.information_title_mr)
        } else if (applicationContext.resources.getString(R.string.punjabiVal) == "" + locale) {
            applicationContext.resources.getString(R.string.information_title_pa)
        } else if (applicationContext.resources.getString(R.string.tamilVal) == "" + locale) {
            applicationContext.resources.getString(R.string.information_title_ta)
        } else if (applicationContext.resources.getString(R.string.teluguVal) == "" + locale) {
            applicationContext.resources.getString(R.string.information_title_te)
        } else if (applicationContext.resources.getString(R.string.urduVal) == "" + locale) {
            applicationContext.resources.getString(R.string.information_title_ur)
        } else {
            applicationContext.resources.getString(R.string.information_title)
        }
    } else if (_title.contains("Feedback")) {
        title = if (applicationContext.resources.getString(R.string.englishVal) == "" + locale) {
            applicationContext.resources.getString(R.string.feedback_title)
        } else if (applicationContext.resources.getString(R.string.bengaliVal) == "" + locale) {
            applicationContext.resources.getString(R.string.feedback_title_bn)
        } else if (applicationContext.resources.getString(R.string.gujaratiVal) == "" + locale) {
            applicationContext.resources.getString(R.string.feedback_title_gu)
        } else if (applicationContext.resources.getString(R.string.hindiVal) == "" + locale) {
            applicationContext.resources.getString(R.string.feedback_title_hi)
        } else if (applicationContext.resources.getString(R.string.kannadaVal) == "" + locale) {
            applicationContext.resources.getString(R.string.feedback_title_kn)
        } else if (applicationContext.resources.getString(R.string.malayalamVal) == "" + locale) {
            applicationContext.resources.getString(R.string.feedback_title_ml)
        } else if (applicationContext.resources.getString(R.string.marathiVal) == "" + locale) {
            applicationContext.resources.getString(R.string.feedback_title_mr)
        } else if (applicationContext.resources.getString(R.string.punjabiVal) == "" + locale) {
            applicationContext.resources.getString(R.string.feedback_title_pa)
        } else if (applicationContext.resources.getString(R.string.tamilVal) == "" + locale) {
            applicationContext.resources.getString(R.string.feedback_title_ta)
        } else if (applicationContext.resources.getString(R.string.teluguVal) == "" + locale) {
            applicationContext.resources.getString(R.string.feedback_title_te)
        } else if (applicationContext.resources.getString(R.string.urduVal) == "" + locale) {
            applicationContext.resources.getString(R.string.feedback_title_ur)
        } else {
            applicationContext.resources.getString(R.string.feedback_title)
        }
    } else if (_title.contains("Complaint")) {
        title = if (applicationContext.resources.getString(R.string.englishVal) == "" + locale) {
            applicationContext.resources.getString(R.string.complaint_title)
        } else if (applicationContext.resources.getString(R.string.bengaliVal) == "" + locale) {
            applicationContext.resources.getString(R.string.complaint_title_bn)
        } else if (applicationContext.resources.getString(R.string.gujaratiVal) == "" + locale) {
            applicationContext.resources.getString(R.string.complaint_title_gu)
        } else if (applicationContext.resources.getString(R.string.hindiVal) == "" + locale) {
            applicationContext.resources.getString(R.string.complaint_title_hi)
        } else if (applicationContext.resources.getString(R.string.kannadaVal) == "" + locale) {
            applicationContext.resources.getString(R.string.complaint_title_kn)
        } else if (applicationContext.resources.getString(R.string.malayalamVal) == "" + locale) {
            applicationContext.resources.getString(R.string.complaint_title_ml)
        } else if (applicationContext.resources.getString(R.string.marathiVal) == "" + locale) {
            applicationContext.resources.getString(R.string.complaint_title_mr)
        } else if (applicationContext.resources.getString(R.string.punjabiVal) == "" + locale) {
            applicationContext.resources.getString(R.string.complaint_title_pa)
        } else if (applicationContext.resources.getString(R.string.tamilVal) == "" + locale) {
            applicationContext.resources.getString(R.string.complaint_title_ta)
        } else if (applicationContext.resources.getString(R.string.teluguVal) == "" + locale) {
            applicationContext.resources.getString(R.string.complaint_title_te)
        } else if (applicationContext.resources.getString(R.string.urduVal) == "" + locale) {
            applicationContext.resources.getString(R.string.complaint_title_ur)
        } else {
            applicationContext.resources.getString(R.string.complaint_title)
        }
    } else {
        ""
    }
    return title
}


fun String.getChannelName(): String {
    return if (this == "scheme") {
        "Scheme"
    } else if (this == "notice") {
        "Notice"
    } else if (this == "training") {
        "Training"
    } else if (this == "Vendor Details" || this == "VendorDetails") {
        "Vendor Details"
    } else if (this == "information") {
        "Information Center"
    } else if (this == "Feedback") {
        "Complaints/Feedback"
    } else {
        "Others"
    }
}


fun String.getNotificationId(): Int {
    return if (this == "scheme") {
        1
    } else if (this == "notice") {
        2
    } else if (this == "training") {
        3
    } else if (this == "Vendor Details" || this == "VendorDetails") {
        4
    } else if (this == "information") {
        5
    } else if (this == "Feedback") {
        6
    } else {
        7
    }
}


//@RequiresApi(Build.VERSION_CODES.TIRAMISU)
//inline fun <reified T : Parcelable> Intent.parcelable(key: String): String? = when {
//    SDK_INT >= 33 -> getParcelableExtra(key, T::class.java)
//    else -> @Suppress("DEPRECATION") getParcelableExtra(key) as? T
//}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
inline fun <reified T : Parcelable> Bundle.parcelable(key: String): T? = when {
    SDK_INT >= 33 -> getParcelable(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelable(key) as? T
}


fun String.parseOneTimeCode(): String {
    return if (this != null && this.length >= 6) {
        this.trim { it <= ' ' }.substring(0, 6)
    } else ""
}


@Suppress("DEPRECATION")
inline fun <reified P : Parcelable> Intent.getParcelable(key: String): P? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getParcelableExtra(key, P::class.java)
    } else {
        getParcelableExtra(key)
    }
}

fun Context.determineScreenDensityCode(): String {
    return when (resources.displayMetrics.densityDpi) {
        DisplayMetrics.DENSITY_LOW -> "ldpi"
        DisplayMetrics.DENSITY_MEDIUM -> "mdpi"
        DisplayMetrics.DENSITY_HIGH -> "hdpi"
        DisplayMetrics.DENSITY_XHIGH, DisplayMetrics.DENSITY_280 -> "xhdpi"
        DisplayMetrics.DENSITY_XXHIGH, DisplayMetrics.DENSITY_360, DisplayMetrics.DENSITY_400, DisplayMetrics.DENSITY_420 -> "xxhdpi"
        DisplayMetrics.DENSITY_XXXHIGH, DisplayMetrics.DENSITY_560 -> "xxxhdpi"
        else -> "Unknown code ${resources.displayMetrics.densityDpi}"
    }
}


fun Context.getScreenDensity(): String {
    val density: String
    when (resources.displayMetrics.densityDpi) {
        DisplayMetrics.DENSITY_LOW -> density = "LDPI"
        DisplayMetrics.DENSITY_140 -> density = "LDPI - MDPI"
        DisplayMetrics.DENSITY_MEDIUM -> density = "MDPI"
        DisplayMetrics.DENSITY_180, DisplayMetrics.DENSITY_200, DisplayMetrics.DENSITY_220 -> density =
            "MDPI - HDPI"

        DisplayMetrics.DENSITY_HIGH -> density = "HDPI"
        DisplayMetrics.DENSITY_260, DisplayMetrics.DENSITY_280, DisplayMetrics.DENSITY_300 -> density =
            "HDPI - XHDPI"

        DisplayMetrics.DENSITY_XHIGH -> density = "XHDPI"
        DisplayMetrics.DENSITY_340, DisplayMetrics.DENSITY_360, DisplayMetrics.DENSITY_400, DisplayMetrics.DENSITY_420, DisplayMetrics.DENSITY_440 -> density =
            "XHDPI - XXHDPI"

        DisplayMetrics.DENSITY_XXHIGH -> density = "XXHDPI"
        DisplayMetrics.DENSITY_560, DisplayMetrics.DENSITY_600 -> density = "XXHDPI - XXXHDPI"
        DisplayMetrics.DENSITY_XXXHIGH -> density = "XXXHDPI"
        DisplayMetrics.DENSITY_TV -> density = "TVDPI"
        else -> density = "UNKNOWN"
    }
    return density
}


fun Context.getDensityName(): String {
    val density = resources.displayMetrics.density
    if (density >= 4.0) {
        return "xxxhdpi"
    }
    if (density >= 3.0) {
        return "xxhdpi"
    }
    if (density >= 2.0) {
        return "xhdpi"
    }
    if (density >= 1.5) {
        return "hdpi"
    }
    return if (density >= 1.0) {
        "mdpi"
    } else "ldpi"
}


@RequiresApi(Build.VERSION_CODES.P)
fun Context.getSignature(): String {
    var info: PackageInfo? = null
    try {
        info = packageManager.getPackageInfo(
            packageName,
            PackageManager.GET_SIGNING_CERTIFICATES
        )
        val sigHistory: Array<Signature> = info.signingInfo.signingCertificateHistory
        val signature: ByteArray = sigHistory[0].toByteArray()
        val md = MessageDigest.getInstance("SHA1")
        val digest = md.digest(signature)
        val sha1Builder = StringBuilder()
        for (b in digest) sha1Builder.append(String.format("%02x", b))
        return sha1Builder.toString()
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
    } catch (e: NoSuchAlgorithmException) {
        e.printStackTrace()
    }
    return ""
}


fun Double.decimal2Digits(): String {
    return String.format("%.2f", this)
}

fun Double.roundOffDecimal(): Double { //here, 1.45678 = 1.46
    val df = DecimalFormat("#.##")
    df.roundingMode = RoundingMode.CEILING
    return df.format(this).toDouble()
}

//fun roundOffDecimal(number: Double): Double? { //here, 1.45678 = 1.45
//    val df = DecimalFormat("#.##")
//    df.roundingMode = RoundingMode.FLOOR
//    return df.format(number).toDouble()
//}

fun Activity.getCameraPath(callBack: Uri.() -> Unit) {
    runOnUiThread() {
        val directory = File(filesDir, "camera_images")
        if (!directory.exists()) {
            directory.mkdirs()
        }
        val uriReal = FileProvider.getUriForFile(
            this,
            packageName + ".provider",
            File(directory, "${Calendar.getInstance().timeInMillis}.png")
        )
        callBack(uriReal)
    }
}


fun getImageName(): String {
    return "${"StreetSaarthi_" + SimpleDateFormat("HHmmss").format(Date())}.png"
}


fun Activity.showOptions(callBack: Int.() -> Unit) = try {
    val dialogView = layoutInflater.inflate(R.layout.dialog_choose_image_option, null)
    val btnCancel = dialogView.findViewById<AppCompatButton>(R.id.btnCancel)
    val tvPhotos = dialogView.findViewById<AppCompatTextView>(R.id.tvPhotos)
    val tvPhotosDesc = dialogView.findViewById<AppCompatTextView>(R.id.tvPhotosDesc)
    val tvCamera = dialogView.findViewById<AppCompatTextView>(R.id.tvCamera)
    val tvCameraDesc = dialogView.findViewById<AppCompatTextView>(R.id.tvCameraDesc)
    val dialog = BottomSheetDialog(this, R.style.TransparentDialog)
    dialog.setContentView(dialogView)
    dialog.show()

    btnCancel.singleClick {
        dialog.dismiss()
    }
    tvCamera.singleClick {
        dialog.dismiss()
        callBack(1)
    }
    tvCameraDesc.singleClick {
        dialog.dismiss()
        callBack(1)
    }

    tvPhotos.singleClick {
        dialog.dismiss()
        callBack(2)
    }
    tvPhotosDesc.singleClick {
        dialog.dismiss()
        callBack(2)
    }
} catch (e: Exception) {
    e.printStackTrace()
}


@SuppressLint("SuspiciousIndentation")
fun Activity.callPermissionDialog(callBack: Intent.() -> Unit) {

    MaterialAlertDialogBuilder(this, R.style.LogoutDialogTheme)
        .setTitle(resources.getString(R.string.app_name))
        .setMessage(resources.getString(R.string.required_permissions))
        .setPositiveButton(resources.getString(R.string.yes)) { dialog, _ ->
            dialog.dismiss()
            val i = Intent()
            i.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            i.addCategory(Intent.CATEGORY_DEFAULT)
            i.data = Uri.parse("package:" + packageName)
            callBack(i)
        }
        .setNegativeButton(resources.getString(R.string.cancel)) { dialog, _ ->
            dialog.dismiss()
            Handler(Looper.getMainLooper()).postDelayed({
                MainActivity.binding.drawerLayout.close()
            }, 500)
        }
        .setCancelable(false)
        .show()
}


fun Activity.showDropDownDialog(
    type: Int = 0,
    arrayList: Array<String?> = emptyArray(),
    callBack: ItemReturn.() -> Unit
) {
    hideKeyboard()

    when (type) {
        1 -> {
            val list = resources.getStringArray(R.array.gender_array)
            MaterialAlertDialogBuilder(this, R.style.DropdownDialogTheme)
                .setTitle(resources.getString(R.string.gender))
                .setItems(list) { _, which ->
                    callBack(ItemReturn(which, list[which]))
                }.show()
        }

        2 -> {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)
            val dpd = DatePickerDialog(
                this,
                R.style.CalendarDatePickerDialog,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    val today = LocalDate.now()
                    val birthday: LocalDate = LocalDate.of(year, (monthOfYear + 1), dayOfMonth)
                    val p: Period = Period.between(birthday, today)

                    var mm: String = (monthOfYear + 1).toString()
                    if (mm.length == 1) {
                        mm = "0" + mm
                    }

                    var dd: String = "" + dayOfMonth
                    if (dd.length == 1) {
                        dd = "0" + dd
                    }

                    if (p.getYears() > 13) {
                        callBack(ItemReturn(name = "" + year + "-" + mm + "-" + dd))
                    } else {
                        showSnackBar(getString(R.string.age_minimum))
                        callBack(ItemReturn(name = ""))
                    }
                },
                year,
                month,
                day
            )
            dpd.show()
        }

        3 -> {
            val list = resources.getStringArray(R.array.socialCategory_array)
            MaterialAlertDialogBuilder(this, R.style.DropdownDialogTheme)
                .setTitle(resources.getString(R.string.social_category))
                .setItems(list) { _, which ->
                    callBack(ItemReturn(which, list[which]))
                }.show()
        }

        4 -> {
            val list = resources.getStringArray(R.array.socialEducation_array)
            MaterialAlertDialogBuilder(this, R.style.DropdownDialogTheme)
                .setTitle(resources.getString(R.string.education_qualifacation))
                .setItems(list) { _, which ->
                    callBack(ItemReturn(which, list[which]))
                }.show()
        }

        5 -> {
            val list = resources.getStringArray(R.array.maritalStatus_array)
            MaterialAlertDialogBuilder(this, R.style.DropdownDialogTheme)
                .setTitle(resources.getString(R.string.marital_status))
                .setItems(list) { _, which ->
                    callBack(ItemReturn(which, list[which]))
                }.show()
        }

        6 -> {
            MaterialAlertDialogBuilder(this, R.style.DropdownDialogTheme)
                .setTitle(resources.getString(R.string.select_state))
                .setItems(arrayList) { _, which ->
                    callBack(ItemReturn(which, arrayList[which]!!))
                }.show()
        }

        7 -> {
            MaterialAlertDialogBuilder(this, R.style.DropdownDialogTheme)
                .setTitle(resources.getString(R.string.select_district))
                .setItems(arrayList) { _, which ->
                    callBack(ItemReturn(which, arrayList[which]!!))
                }.show()
        }

        8 -> {
            MaterialAlertDialogBuilder(this, R.style.DropdownDialogTheme)
                .setTitle(resources.getString(R.string.municipality_panchayat))
                .setItems(arrayList) { _, which ->
                    callBack(ItemReturn(which, arrayList[which]!!))
                }.show()
        }

        9 -> {
            MaterialAlertDialogBuilder(this, R.style.DropdownDialogTheme)
                .setTitle(resources.getString(R.string.select_pincode))
                .setItems(arrayList) { _, which ->
                    callBack(ItemReturn(which, arrayList[which]!!))
                }.show()
        }

        10 -> {
            MaterialAlertDialogBuilder(this, R.style.DropdownDialogTheme)
                .setTitle(resources.getString(R.string.type_of_market_place))
                .setItems(arrayList) { _, which ->
                    callBack(ItemReturn(which, arrayList[which]!!))
                }.show()
        }

        11 -> {
            MaterialAlertDialogBuilder(this, R.style.DropdownDialogTheme)
                .setTitle(resources.getString(R.string.type_of_vending))
                .setItems(arrayList) { _, which ->
                    callBack(ItemReturn(which, arrayList[which]!!))
                }.show()
        }

        12 -> {
            val list = resources.getStringArray(R.array.years_array)
            MaterialAlertDialogBuilder(this, R.style.DropdownDialogTheme)
                .setTitle(resources.getString(R.string.total_years_of_vending))
                .setItems(list) { _, which ->
                    callBack(ItemReturn(which, list[which]))
                }.show()
        }

        13 -> {
            MaterialAlertDialogBuilder(this, R.style.DropdownDialogTheme)
                .setTitle(resources.getString(R.string.localOrganisation))
                .setItems(arrayList) { _, which ->
                    callBack(ItemReturn(which, arrayList[which]!!))
                }.show()
        }
        14 -> {
            val mTimePicker: TimePickerDialog
            val mcurrentTime = Calendar.getInstance()
            val hour = mcurrentTime.get(Calendar.HOUR_OF_DAY)
            val minute = mcurrentTime.get(Calendar.MINUTE)
            mTimePicker = TimePickerDialog(
                this,
                R.style.TimeDialogTheme,
                object : TimePickerDialog.OnTimeSetListener {
                    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
                        val datetime = Calendar.getInstance()
                        datetime[Calendar.HOUR_OF_DAY] = hourOfDay
                        datetime[Calendar.MINUTE] = minute
                        val strHrsToShow =
                            if (datetime.get(Calendar.HOUR) === 0) "12" else Integer.toString(
                                datetime.get(Calendar.HOUR)
                            )
                        var am_pm = ""
                        if (datetime.get(Calendar.AM_PM) == Calendar.AM)
                            am_pm = "AM";
                        else if (datetime.get(Calendar.AM_PM) == Calendar.PM)
                            am_pm = "PM";
                        val time =  strHrsToShow + ":" + (if (minute.toString().length == 1) "0"+datetime.get(Calendar.MINUTE)  else datetime.get(Calendar.MINUTE)) + " " + am_pm
                        val time00 =  "" + hourOfDay + ":" + (if (minute.toString().length == 1) "0"+minute else minute) + ":00"
                        callBack(ItemReturn(0, time, time00))
                    }
                },
                hour,
                minute,
                false
            )
            mTimePicker.show()
        }
        15 -> {
            val list=resources.getStringArray(R.array.relation_array)
            MaterialAlertDialogBuilder(this, R.style.DropdownDialogTheme)
                .setTitle(resources.getString(R.string.relationship_TypeStar))
                .setItems(list) {_,which->
                    callBack(ItemReturn(which, list[which]))
                }.show()
        }
        16 -> {
            val list=resources.getStringArray(R.array.type_array)
            MaterialAlertDialogBuilder(this, R.style.DropdownDialogTheme)
                .setTitle(resources.getString(R.string.select_your_choice))
                .setItems(list) {_,which->
                    callBack(ItemReturn(which, list[which]))
                }.show()
        }
        17 -> {
            MaterialAlertDialogBuilder(this, R.style.DropdownDialogTheme)
                .setTitle(resources.getString(R.string.select_complaint_type))
                .setItems(arrayList) {_,which->
                    callBack(ItemReturn(which, arrayList[which]!!))
                }.show()
        }
        18 -> {
            val list=resources.getStringArray(R.array.month_year_array)
            MaterialAlertDialogBuilder(this, R.style.DropdownDialogTheme)
                .setTitle(resources.getString(R.string.select_month_year))
                .setItems(list) {_,which->
                    callBack(ItemReturn(which, list[which]))
                }.show()
        }
        19 -> {
            val list=resources.getStringArray(R.array.numbers_array)
            MaterialAlertDialogBuilder(this, R.style.DropdownDialogTheme)
                .setTitle(resources.getString(R.string.choose_number))
                .setItems(list) {_,which->
                    callBack(ItemReturn(which, list[which]))
                }.show()
        }
    }

}

