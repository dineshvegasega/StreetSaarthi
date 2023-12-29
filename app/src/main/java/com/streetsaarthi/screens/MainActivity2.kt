package com.streetsaarthi

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.webkit.JavascriptInterface
import android.webkit.WebSettings
import android.webkit.WebSettings.LOAD_CACHE_ELSE_NETWORK
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


//import org.json.JSONObject


class MainActivity2 : AppCompatActivity() {

    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.webpage)
        webView = findViewById(R.id.webView)

//        webView.webViewClient = WebViewClient()
//        webView.setWebChromeClient(WebChromeClient())

//        if (Build.VERSION.SDK_INT >= 23 && (ContextCompat.checkSelfPermission(
//                this,
//                Manifest.permission.WRITE_EXTERNAL_STORAGE
//            ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
//                this,
//                Manifest.permission.CAMERA
//            ) != PackageManager.PERMISSION_GRANTED)
//        ) {
//            ActivityCompat.requestPermissions(
//                this@MainActivity,
//                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA),
//                1
//            )
//        }
        val str = "xxxaaa"
//        webView.webViewClient = object : WebViewClient() {
//            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
//                view?.loadUrl(getString(R.string.url))
//                return true
//            }
//
//            override fun onPageFinished(view: WebView?, url: String?) {
//                super.onPageFinished(view, url)
//                view?.loadUrl("javascript:init('" + str + "')");
//            }
//        }
//        webView.loadUrl(getString(R.string.url));


//        val html = "<!DOCTYPE html>" +
//                "<html>" +
//                "<body onload='document.frm1.submit()'>" +
//                "<form action='http://www.yoursite.com/postreceiver' method='post' name='frm1'>" +
//                "  <input type='hidden' name='foo' value='12345'><br>" +
//                "  <input type='hidden' name='bar' value='23456'><br>" +
//                "</form>" +
//                "</body>" +
//                "</html>"
//        webView.loadData(html, "text/html", "UTF-8")
//        webView.addJavascriptInterface(WebAppInterface(), "Android")

//        val json: String = Gson().toJson("jobj")

//        val json = JsonObject().apply {
//            addProperty("age","28")
//            addProperty("name","john")
//            addProperty("contents","test")
//        }

//        val json = JSONObject().apply {
//            addProperty("age","28")
//            addProperty("name","john")
//            addProperty("contents","test")
//        }

//        webView.webViewClient = object : WebViewClient() {
//            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
////                view?.loadUrl("http://167.71.225.20:8080/")
//                return true
//            }
//
//            override fun onPageFinished(view: WebView?, url: String?) {
//                super.onPageFinished(view, url)
////                view?.loadUrl("javascript:docWrite('" + str + "')");
//               // view?.loadUrl("javascript:docWrite2('fsdfasfg')");
//            }
//        }
//        webView.loadUrl("http://167.71.225.20:8080/");
//        val url = "file:///android_asset/local.html"
//        webView.loadUrl(url)
//        webView.addJavascriptInterface(JavaScriptInterface(this), "Android");

//        GetHTML()?.let { webView.loadData(it, "text/html", "utf-8") };

//        Handler(Looper.getMainLooper()).postDelayed({
//            webView.addJavascriptInterface(WebAppInterface(this), "Android")
//
//        }, 200)


        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
               // view?.loadUrl(getString(R.string.url))
                return true
            }

//            override fun onPageFinished(view: WebView?, url: String?) {
//                super.onPageFinished(view, url)
////                view?.loadUrl("javascript:(function() { " +
////                        "document.getElementById(':1.container').style.display='none'; " +
////                        "})()");
//
//                view?.loadUrl("javascript:(function() { " +
//                        "document.getElementsByClassName(':1.container')[0].style.display='none'; })()");
//            }
        }
       // webView.loadUrl(getString(R.string.url));


    }


//    class JavaScriptInterface
//    /** Instantiate the interface and set the context  */ internal constructor(var mContext: Context) {
//        /** Show a toast from the web page  */
//        fun showToast(toast: String?) {
//            Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show()
//        }
//    }

    class WebAppInterface(private val mContext: Context) {

        /** Show a toast from the web page.  */
        @JavascriptInterface
        fun showToast(toast: String) {
            Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    class JavaScriptInterface
   internal constructor(var mContext: Context) {
        fun showToast(toast: String?) {
            Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show()
        }
    }


//    @SuppressLint("SetJavaScriptEnabled")
//    class JavaScriptInterface {
//
//        @JavascriptInterface
//        fun showToast(status: String) {
//            //show toast here or handle status
//            Log.e("TAG", "hhhhhhhhhh "+status)
//        }
//    }




    fun getValue(str: String): String? {
        webView.loadUrl("javascript:function1(colors)")
        Toast.makeText(this, "Under getValue $str", Toast.LENGTH_SHORT).show()
        return str
    }

    override fun onResume() {
        super.onResume()
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        webView.settings.setSupportZoom(false)
        val settings = webView.settings
        // Enable java script in web view
        settings.javaScriptEnabled = true
        webView.setPadding(0, 0, 0, 0)

        settings.cacheMode = WebSettings.LOAD_DEFAULT
        webView.setVerticalScrollBarEnabled(false);
        webView.setHorizontalScrollBarEnabled(false);

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

        webView.fitsSystemWindows = true

        webView.setLayerType(View.LAYER_TYPE_HARDWARE, null)
//        webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null)

        webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
//        webView.setInitialScale(getScale())

        webView.settings.databaseEnabled = true
        webView.settings.cacheMode = LOAD_CACHE_ELSE_NETWORK
        webView.setInitialScale(30);
    }

    private fun getScale(): Int {
        val metrics: DisplayMetrics = this.getResources().getDisplayMetrics()
        val width = metrics.widthPixels
        var `val`: Double = width / metrics.widthPixels.toDouble()
        `val` *= 100.0
        return `val`.toInt()


    }


override fun onBackPressed() {
    if (webView.canGoBack())
        webView.goBack()
    else
        super.onBackPressed()
}

}