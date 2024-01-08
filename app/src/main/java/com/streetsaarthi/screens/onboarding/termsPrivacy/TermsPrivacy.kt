package com.streetsaarthi.screens.onboarding.termsPrivacy

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.streetsaarthi.databinding.WebpageBinding
import com.streetsaarthi.utils.navigateBack
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class TermsPrivacy : Fragment() {
    //    private val viewModel: LoginVM by viewModels()
    private var _binding: WebpageBinding? = null
    private val binding get() = _binding!!


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
                    //view?.
                }
            }


        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = WebpageBinding.inflate(inflater)
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val data = arguments?.getString("data")
        //val token = arguments?.getString("token")
//        val json = Gson().toJson(str)
//
//        Log.e("TAG", "cccccccc "+json)

//        val json = getJson()
//        val topic = gson.fromJson(json, Topic::class.java)


        binding.webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                view?.loadUrl("http://167.71.225.20:8080/")
//                view?.loadUrl(getString(R.string.url))
                return true
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
              //  view?.loadUrl(getString(R.string.url))
//                view?.loadUrl("javascript:docWrite('" + data + "')");
//                 view?.loadUrl("javascript:docWrite2('fsdfasfg')");
            }
        }
        binding.webView.loadUrl("http://167.71.225.20:8080/");
//        binding.webView.loadUrl(getString(R.string.url));
    }

    override fun onResume() {
        super.onResume()
        binding.webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        binding.webView.settings.setSupportZoom(false)
        val settings = binding.webView.settings
        // Enable java script in web view
        settings.javaScriptEnabled = true
        binding. webView.setPadding(0, 0, 0, 0)

        settings.cacheMode = WebSettings.LOAD_DEFAULT
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


    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}