package com.streetsaarthi.nasvi


import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import com.streetsaarthi.nasvi.screens.mainActivity.MainActivity
import com.streetsaarthi.nasvi.databinding.LoaderBinding
import com.streetsaarthi.nasvi.networking.ApiTranslateInterface
import com.streetsaarthi.nasvi.networking.CallHandlerTranslate
import com.streetsaarthi.nasvi.utils.getErrorMessage
import com.streetsaarthi.nasvi.utils.hideSoftKeyBoard
import com.streetsaarthi.nasvi.utils.ioDispatcher
import com.streetsaarthi.nasvi.utils.mainThread
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
class Repository @Inject constructor(
    private val apiInterface: ApiInterface,
    private val apiTranslateInterface: ApiTranslateInterface
) {

    private val mainDispatcher by lazy { Dispatchers.Main }
    var alertDialog: AlertDialog? = null


    /**
     * Call Api
     * */
    suspend fun <T> callApi(
        loader: Boolean = true,
        callHandler: CallHandler<T>
    ) {

        /**
         * Hide Soft Keyboard
         * */
        hideSoftKeyBoard()


        /**
         * Coroutine Exception Handler
         * */
        val coRoutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
            mainThread {
                throwable.message.let {
                    hideLoader()
                    callHandler.error(it.getErrorMessage())
                }
            }
        }

        /**
         * Call Api
         * */
        CoroutineScope(Dispatchers.IO + coRoutineExceptionHandler + Job()).launch {
            flow {
                emit(callHandler.sendRequest(apiInterface = apiInterface) as Response<*>)
            }.flowOn(ioDispatcher)
                .retryWhen { cause, attempt ->
                    (attempt < HttpStatusCode.RETRY_COUNT) && (cause is IOException)
                }.onStart {
                    callHandler.loading()
                    withContext(mainDispatcher) {
                        if (loader) MainActivity.context?.get()?.showLoader()
                    }
                }.catch { error ->
                        withContext(mainDispatcher) {
                            hideLoader()
                            callHandler.error(error.getErrorMessage())
                        }
                }.collect { response ->
                    withContext(mainDispatcher) {
                        hideLoader()
                        if (response.isSuccessful)
                            callHandler.success(response as T)
                        else
                            response.errorBody()?.string()
                                ?.let { callHandler.error(it.getErrorMessage()) }
                    }
                }
        }
    }



    /**
     * Call Api
     * */
    suspend fun <T> callApiTranslate(
        loader: Boolean = true,
        callHandler: CallHandlerTranslate<T>
    ) {

        /**
         * Hide Soft Keyboard
         * */
        hideSoftKeyBoard()


        /**
         * Coroutine Exception Handler
         * */
        val coRoutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
            mainThread {
                throwable.message.let {
                    hideLoader()
                    callHandler.error(it.getErrorMessage())
                }
            }
        }

        /**
         * Call Api
         * */
        CoroutineScope(Dispatchers.IO + coRoutineExceptionHandler + Job()).launch {
            flow {
                emit(callHandler.sendRequest(apiInterface = apiTranslateInterface) as Response<*>)
            }.flowOn(ioDispatcher)
                .retryWhen { cause, attempt ->
                    (attempt < HttpStatusCode.RETRY_COUNT) && (cause is IOException)
                }.onStart {
                    callHandler.loading()
                    withContext(mainDispatcher) {
                        if (loader) MainActivity.context?.get()?.showLoader()
                    }
                }.catch { error ->
                    withContext(mainDispatcher) {
                        hideLoader()
                        callHandler.error(error.getErrorMessage())
                    }
                }.collect { response ->
                    withContext(mainDispatcher) {
                        hideLoader()
                        if (response.isSuccessful)
                            callHandler.success(response as T)
                        else
                            response.errorBody()?.string()
                                ?.let { callHandler.error(it.getErrorMessage()) }
                    }
                }
        }
    }




    /**
     * Call Api
     * */
    suspend fun <T> callApiWithoutLoader(
        loader: Boolean = true,
        callHandler: CallHandler<T>
    ) {

        /**
         * Hide Soft Keyboard
         * */
        hideSoftKeyBoard()


        /**
         * Coroutine Exception Handler
         * */
        val coRoutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
            mainThread {
                throwable.message.let {
//                    hideLoader()
                    callHandler.error(it.getErrorMessage())
                }
            }
        }

        /**
         * Call Api
         * */
        CoroutineScope(Dispatchers.IO + coRoutineExceptionHandler + Job()).launch {
            flow {
                emit(callHandler.sendRequest(apiInterface = apiInterface) as Response<*>)
            }.flowOn(ioDispatcher)
                .retryWhen { cause, attempt ->
                    (attempt < HttpStatusCode.RETRY_COUNT) && (cause is IOException)
                }.onStart {
                    callHandler.loading()
                    withContext(mainDispatcher) {
                       // if (loader) MainActivity.context?.get()?.showLoader()
                    }
                }.catch { error ->
                    withContext(mainDispatcher) {
                      //  hideLoader()
                        callHandler.error(error.getErrorMessage())
                    }
                }.collect { response ->
                    withContext(mainDispatcher) {
                       // hideLoader()
                        if (response.isSuccessful)
                            callHandler.success(response as T)
                        else
                            response.errorBody()?.string()
                                ?.let { callHandler.error(it.getErrorMessage()) }
                    }
                }
        }
    }



    /**
     * Show Loader
     * */
    private fun Context.showLoader() {
        if (alertDialog == null) {
            val alert = AlertDialog.Builder(this)
            val binding = LoaderBinding.inflate(LayoutInflater.from(this), null, false)
            alert.setView(binding.root)
            alert.setCancelable(false)
            alertDialog = alert.create()
            alertDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            alertDialog?.show()
        }
    }



    fun showLoaderSecond() {
        if (alertDialog == null) {
            val alert = AlertDialog.Builder(MainActivity.activity.get())
            val binding = LoaderBinding.inflate(LayoutInflater.from(MainActivity.activity.get()), null, false)
            alert.setView(binding.root)
            alert.setCancelable(false)
            alertDialog = alert.create()
            alertDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            alertDialog?.show()
        }
    }


    /**
     * Hide Loader
     * */
    fun hideLoader() {
        alertDialog?.dismiss()
        alertDialog = null
    }



}