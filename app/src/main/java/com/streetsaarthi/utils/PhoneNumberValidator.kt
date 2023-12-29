package com.blacqhorse.customClasses.phoneValidation

import android.content.Context
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil

/**
 * Phone Number Validation
 * */
class PhoneNumberValidator(context: Context) {

    /**
     * Phone Number Validation
     * */
    private var phoneUtilInstance: PhoneNumberUtil? = null


    /**
     * Initializer
     * */
    init {
        phoneUtilInstance = PhoneNumberUtil.createInstance(context)
    }


    /**
     * Check Valid Phone Number
     * */
    fun checkValidPhoneNumber(countryCode: String, phoneNumber: String, iosCode: String): Boolean {
        val number = phoneUtilInstance?.parse("$countryCode$phoneNumber", iosCode)
        return phoneUtilInstance?.isValidNumber(number) ?: false
    }

}