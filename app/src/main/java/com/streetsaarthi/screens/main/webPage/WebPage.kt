package com.streetsaarthi.screens.main.webPage

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.demo.networking.LoginOtp
import com.demo.networking.LoginPassword
import com.demo.networking.Screen
import com.streetsaarthi.MainActivity
import com.streetsaarthi.R
import com.streetsaarthi.databinding.WebpageBinding
import com.streetsaarthi.utils.navigateBack
import dagger.hilt.android.AndroidEntryPoint


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



    @SuppressLint("SuspiciousIndentation")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = view.findNavController()
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
                binding.webView.loadUrl("https://nasvi.in/#/mobile-login")
            } else if (screen == LoginOtp){
                binding.webView.loadUrl("https://nasvi.in/#/mobile-otp-login")
            }


//        binding.webView.loadUrl("http://167.71.225.20:8080/#/login")
//        binding.webView.loadUrl(getString(R.string.url));
//        binding.webView.loadUrl("http://167.71.225.20:8080/")

        Handler(Looper.getMainLooper()).postDelayed({
            binding.webView.addJavascriptInterface(WebAppInterface(requireContext(), binding), "Android")
        }, 200)
    }


//    Request OTP Again in

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