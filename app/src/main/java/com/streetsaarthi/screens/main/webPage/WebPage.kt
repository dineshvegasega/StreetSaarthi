package com.streetsaarthi.screens.main.webPage

import android.Manifest
import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.DownloadListener
import android.webkit.JavascriptInterface
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.NotificationCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.streetsaarthi.R
import com.streetsaarthi.databinding.WebpageBinding
import com.streetsaarthi.screens.mainActivity.MainActivity
import com.streetsaarthi.screens.onboarding.networking.LoginOtp
import com.streetsaarthi.screens.onboarding.networking.LoginPassword
import com.streetsaarthi.screens.onboarding.networking.Screen
import com.streetsaarthi.screens.onboarding.networking.WEB_URL
import com.streetsaarthi.utils.navigateBack
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import kotlin.io.encoding.ExperimentalEncodingApi


@AndroidEntryPoint
class WebPage : Fragment() {

    companion object{
        @SuppressLint("StaticFieldLeak")
        private var _binding: WebpageBinding? = null
        private val binding get() = _binding!!
        @SuppressLint("StaticFieldLeak")
        lateinit var navController: NavController
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (binding.webView.canGoBack()){
                    binding.webView.goBack()
                    Log.e("TAG", "onBackPressedDispatcher1")
                }
                else {
                    Log.e("TAG", "onBackPressedDispatcher2")
                    view?.navigateBack()
                }
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = WebpageBinding.inflate(inflater)
        return binding.root
    }


    private val pushNotificationPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        Log.e("TAG", "grantedAAA")
    }
    @SuppressLint("SuspiciousIndentation")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = view.findNavController()


        pushNotificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)

//        myView = view
      //  val data = arguments?.getString("data")
        //val token = arguments?.getString("token")
//        val json = Gson().toJson(str)
//
//        val data = "Hello"
//        Log.e("TAG", "cccccccc "+data)

//        val json = getJson()
//        val topic = gson.fromJson(json, Topic::class.java)


//        binding.buttonPanel.setOnClickListener {
//            view.findNavController().navigate(R.id.action_webPage_to_QuickRegister)
//        }

//        binding.webView.setBackgroundTintList(ColorStateList.valueOf(ResourcesCompat.getColor(
//            getResources(), R.color._F02A2A, null)));


//        binding.webView.setDownloadListener(DownloadListener { url, userAgent, contentDisposition, mimetype, contentLength ->
//            val i = Intent(Intent.ACTION_VIEW)
//            i.setData(Uri.parse(url))
//            startActivity(i)
//        })


        binding.webView.webViewClient = object : WebViewClient() {
            override fun shouldInterceptRequest(
                view: WebView?,
                request: WebResourceRequest?
            ): WebResourceResponse? {
                return super.shouldInterceptRequest(view, request)
            }
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                binding.progressBar.visibility = View.GONE
              //  view?.loadUrl(getString(R.string.url))
//                view?.loadUrl("javascript:docWrite('" + data + "')")
//                 view?.loadUrl("javascript:docWrite2('fsdfasfg')");
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                binding.progressBar.visibility = View.VISIBLE
            }

            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                super.onReceivedError(view, request, error)
                binding.progressBar.visibility = View.GONE
            }
        }



//https://streetsaarthi.in/#/mobile-login
        var screen = arguments?.getString(Screen)
            if (screen == LoginPassword){
                binding.webView.loadUrl(WEB_URL+"#/mobile-login")
//                binding.webView.loadUrl("https://amritmahotsav.nic.in/downloads.htm")

            } else if (screen == LoginOtp){
                binding.webView.loadUrl(WEB_URL+"#/mobile-otp-login")
            }


//        binding.webView.loadUrl("http://167.71.225.20:8080/#/login")
//        binding.webView.loadUrl(getString(R.string.url));
//        binding.webView.loadUrl("http://167.71.225.20:8080/")

        Handler(Looper.getMainLooper()).postDelayed({
            binding.webView.addJavascriptInterface(WebAppInterface(requireContext(), binding), "Android")
        }, 200)



        binding.webView.setDownloadListener(DownloadListener { url, userAgent, contentDisposition, mimetype, contentLength ->

            if (url.startsWith("data:")) {  //when url is base64 encoded data
                val path: String = createAndSaveFileFromBase64Url(url)
            }

//            var data = android.util.Base64.decode(url, android.util.Base64.DEFAULT);

//            val request = DownloadManager.Request(Uri.parse(url))
//            request.setMimeType(mimeType.toString())
//         //   val cookies: String = CookieManager.getInstance().getCookie(url)
////            request.addRequestHeader("cookie", cookies)
////            request.addRequestHeader("User-Agent", userAgent)
//            request.setDescription(resources.getString(R.string.enterOtp))
//            val filename = URLUtil.guessFileName(url, contentDisposition, mimeType.toString())
//            request.setTitle(filename)
////            request.allowScanningByMediaScanner()
//            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
//            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, filename)
//            val dm =  requireContext().getSystemService(DOWNLOAD_SERVICE) as DownloadManager?
//            dm!!.enqueue(request)
//            Toast.makeText( requireContext().getApplicationContext(), R.string.COVText, Toast.LENGTH_LONG)
//                .show()
////            val request = DownloadManager.Request(
////                Uri.parse(url)
////            )
////            request.allowScanningByMediaScanner()

            Log.e("TAG", "urlAA "+url)


//            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
//            request.setDestinationInExternalPublicDir(
//                Environment.DIRECTORY_DOWNLOADS,
//                "StreetSaarthi_${Calendar.getInstance().timeInMillis}_${url.substring(url.lastIndexOf("/") + 1)}"
//            )
//
//            val dm = requireContext().getSystemService(DOWNLOAD_SERVICE) as DownloadManager?
//            dm!!.enqueue(request)
//            Toast.makeText(
//                requireContext(),
//                "File Downloading...",
//                Toast.LENGTH_LONG
//            ).show()
        })


    }


    @SuppressLint("MutableImplicitPendingIntent")
    @OptIn(ExperimentalEncodingApi::class)
    fun createAndSaveFileFromBase64Url(url: String): String {
        val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val filetype = url.substring(url.indexOf("/") + 1, url.indexOf(";"))
        val filename = System.currentTimeMillis().toString() + "." + filetype
        val file = File(path, filename)
        try {
            if (!path.exists()) path.mkdirs()
            if (!file.exists()) file.createNewFile()
            val base64EncodedString = url.substring(url.indexOf(",") + 1)
//            val decodedBytes = Base64.decode(base64EncodedString, Base64.DEFAULT)
            var decodedBytes = android.util.Base64.decode(base64EncodedString, android.util.Base64.DEFAULT);
//            android.util.Base64.decode(url, android.util.Base64.DEFAULT)
            val os: OutputStream = FileOutputStream(file)
            os.write(decodedBytes)
            os.close()
            val imagePath: File = File(file.absolutePath)
            val intent = Intent(Intent.ACTION_VIEW)
            val data =
                FileProvider.getUriForFile(requireContext(), requireContext().getApplicationContext().getPackageName() + ".provider", imagePath)
            intent.setDataAndType(data, "image/*")
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
           // startActivity(intent)

            val pendingIntent=if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                PendingIntent.getActivity(requireContext(),0,intent,PendingIntent.FLAG_MUTABLE)
            } else {
                PendingIntent.getActivity(requireContext(),0,intent,
                    PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE)
            }


            val CHANNEL_ID="my_channel_01" // The id of the channel.
            val name: CharSequence="test" // The user-visible name of the channel.
            val importance=NotificationManager.IMPORTANCE_HIGH
            var mChannel: NotificationChannel?=null
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mChannel=NotificationChannel(CHANNEL_ID,name,importance)
                mChannel.enableLights(true)
                mChannel.lightColor= Color.WHITE
                mChannel.setShowBadge(true)
                mChannel.lockscreenVisibility=Notification.VISIBILITY_PUBLIC
            }

            val notification=NotificationCompat.Builder(requireContext(),CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher) //.setLargeIcon(icon)
//                .setStyle("bigText")
                .setPriority(Notification.PRIORITY_HIGH)
                .setContentTitle(getString(R.string.app_name))
                .setAutoCancel(true)
                .setChannelId("my_channel_01")
                .setContentIntent(pendingIntent)
                .setContentText(file.name).build()
            val notificationManager=
                requireContext().getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) notificationManager.createNotificationChannel(
                mChannel!!
            )
            notificationManager.notify(0,notification)

        } catch (e: IOException) {
            Log.w("ExternalStorage", "Error writing $file", e)
        }
        return file.toString()
    }





    class WebAppInterface(private val mContext: Context, var bind: WebpageBinding) {
        /** Show a toast from the web page.  */
        @JavascriptInterface
        fun showToast(toast: String) {
            Toast.makeText(mContext, "4"+toast, Toast.LENGTH_SHORT).show()
        }

        @JavascriptInterface
        fun redirectToForgetPage(toast: String) {
            Toast.makeText(mContext, "5"+toast, Toast.LENGTH_SHORT).show()
        }

        @JavascriptInterface
        fun redirectToMainPage(toast: String) {
           // Toast.makeText(mContext, "1"+toast, Toast.LENGTH_SHORT).show()
            Handler(Looper.getMainLooper()).post(Thread {
                MainActivity.activity.get()?.runOnUiThread {
                    //navController.navigate(R.id.action_webPage_to_onboard)
                    navController.navigateUp()
                }
            })
        }

        @JavascriptInterface
        fun redirectToRegisterPage(toast: String) {
//            Toast.makeText(mContext, "2"+toast, Toast.LENGTH_SHORT).show()
            Handler(Looper.getMainLooper()).post(Thread {
                MainActivity.activity.get()?.runOnUiThread {
                    navController.navigate(R.id.action_webPage_to_register)
                }
            })

        }

        @JavascriptInterface
        fun redirectToQucikRegisterPage(toast: String) {
//            Toast.makeText(mContext, "3"+toast, Toast.LENGTH_SHORT).show()
            Handler(Looper.getMainLooper()).post(Thread {
                MainActivity.activity.get()?.runOnUiThread {
                    navController.navigate(R.id.action_webPage_to_QuickRegister)
                }
            })

        }

        @JavascriptInterface
        fun redirectToOTPLogin(toast: String) {
            Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show()
        }

    }

    override fun onResume() {
        super.onResume()
        binding.webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY)

        binding.webView.settings.setSupportZoom(false)
        val settings = binding.webView.settings
        // Enable java script in web view
        settings.javaScriptEnabled = true
        binding. webView.setPadding(0, 0, 0, 0)

        settings.cacheMode = WebSettings.LOAD_NO_CACHE
        binding.webView.setVerticalScrollBarEnabled(false);
        binding.webView.setHorizontalScrollBarEnabled(false);

        // Enable zooming in web view
        settings.setSupportZoom(false)
        settings.builtInZoomControls = false
        settings.displayZoomControls = false

        // Zoom web view text
//        settings.textZoom = 100

        // Enable disable images in web view
        settings.blockNetworkImage = false
        // Whether the WebView should load image resources
        settings.loadsImagesAutomatically = true


        // More web view settings
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            settings.safeBrowsingEnabled = true  // api 26
        }

        settings.pluginState = WebSettings.PluginState.ON
        settings.useWideViewPort = true
        settings.loadWithOverviewMode = true
        settings.javaScriptCanOpenWindowsAutomatically = true
        settings.mediaPlaybackRequiresUserGesture = false

        settings.domStorageEnabled = true
        settings.setSupportMultipleWindows(true)
        settings.loadWithOverviewMode = true
        settings.allowContentAccess = true
        settings.setGeolocationEnabled(true)
        settings.allowFileAccess = true



        binding.webView.fitsSystemWindows = true

        binding.webView.setLayerType(View.LAYER_TYPE_HARDWARE, null)
//        webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null)

        binding.webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
//        webView.setInitialScale(getScale())

        binding.webView.settings.databaseEnabled = true
        binding.webView.settings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
        binding.webView.setInitialScale(30);
    }

}