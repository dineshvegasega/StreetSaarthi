package com.demo.networking

object HttpStatusCode {

    /**
     * Http Status Code
     * */

    const val UN_AUTHORIZE = 401


    /**
     * Accept Headers
     * */
    const val AUTHORIZATION = "Authorization"

    const val CACHE_CONTROL = "Cache-Control"

    const val ACCEPT = "Accept"

    const val CONTENT_TYPE = "Content-Type"

    const val APPLICATION_JSON = "application/json"


    /**
     * Retry Count
     * */
    const val RETRY_COUNT = 1


    /**
     * Regex
     * */
    const val PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@*#$%^&+=!])(?=\\S+$).{8,50}$"
}