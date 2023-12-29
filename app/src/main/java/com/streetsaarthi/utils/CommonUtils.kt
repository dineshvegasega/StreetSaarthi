package com.kochia.customer.utils

import android.app.Activity
import android.content.Context
import android.text.InputType
import android.text.TextUtils
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import java.text.SimpleDateFormat
import java.util.concurrent.TimeUnit

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

fun getFormattedCountDownTimer(millisUntilFinished: Long): String {
    return String.format(
        "%02d:%02d",
        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
            TimeUnit.MILLISECONDS.toHours(
                millisUntilFinished
            )
        ),
        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
            TimeUnit.MILLISECONDS.toMinutes(
                millisUntilFinished
            )
        )
    )
}


//fun togglePasswordVisibility(ctc:Context,editText: EditText,image:ImageView) {
//    if (editText.transformationMethod == PasswordTransformationMethod.getInstance())
//    {
//        editText.transformationMethod = HideReturnsTransformationMethod.getInstance()
//        image.setImageDrawable(ContextCompat.getDrawable(ctc,R.drawable.ic_password_visible))
//
//    } else {
//        editText.transformationMethod= PasswordTransformationMethod.getInstance()
//        image.setImageDrawable(ContextCompat.getDrawable(ctc,R.drawable.ic_unvisible_pass))
//
//    }
//}
 fun formatDate(first: Long?):String {
    val formatter = SimpleDateFormat("dd/MM/yyyy")
   return formatter.format(first)
}


fun setMarqueText(textView: TextView) {
    textView.ellipsize = TextUtils.TruncateAt.MARQUEE
    textView.isSingleLine = true
    textView.marqueeRepeatLimit = 10
    textView.isSelected = true
}