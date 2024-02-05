package com.streetsaarthi.nasvi.screens.mainActivity

//import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage
//import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage
//import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions
//import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage
//import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage
//import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.common.IntentSenderForResultStarter
import com.google.android.play.core.install.model.ActivityResult.RESULT_IN_APP_UPDATE_FAILED
import com.google.android.play.core.install.model.AppUpdateType
import com.google.gson.Gson
import com.streetsaarthi.nasvi.R
import com.streetsaarthi.nasvi.databinding.MainActivityBinding
import com.streetsaarthi.nasvi.datastore.DataStoreKeys
import com.streetsaarthi.nasvi.datastore.DataStoreUtil
import com.streetsaarthi.nasvi.datastore.DataStoreUtil.readData
import com.streetsaarthi.nasvi.models.login.Login
import com.streetsaarthi.nasvi.networking.ConnectivityManager
import com.streetsaarthi.nasvi.screens.onboarding.networking.Main
import com.streetsaarthi.nasvi.screens.onboarding.networking.Screen
import com.streetsaarthi.nasvi.screens.onboarding.networking.Start
import com.streetsaarthi.nasvi.screens.onboarding.networking.USER_TYPE
import com.streetsaarthi.nasvi.utils.LocaleHelper
import com.streetsaarthi.nasvi.utils.autoScroll
import com.streetsaarthi.nasvi.utils.getToken
import com.streetsaarthi.nasvi.utils.imageZoom
import com.streetsaarthi.nasvi.utils.ioThread
import com.streetsaarthi.nasvi.utils.loadImage
import com.streetsaarthi.nasvi.utils.singleClick
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import okhttp3.MultipartBody
import java.lang.ref.WeakReference


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    companion object {

        @JvmStatic
        lateinit var context: WeakReference<Context>

        @JvmStatic
        lateinit var activity:  WeakReference<Activity>

        @JvmStatic
        lateinit var mainActivity: WeakReference<MainActivity>

        var logoutAlert : AlertDialog?= null
        var deleteAlert : AlertDialog?= null

        @SuppressLint("StaticFieldLeak")
        var navHostFragment : NavHostFragment? = null

        private var _binding: MainActivityBinding? = null
        val binding get() = _binding!!


        @JvmStatic
        lateinit var isOpen : WeakReference<Boolean>

    }

    private val viewModel: MainActivityVM by viewModels()

    private val connectivityManager by lazy { ConnectivityManager(this) }


    private val pushNotificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if(granted){
            Log.e("TAG", "AAAAgranted "+granted)

        }else{
            Log.e("TAG", "BBBBgranted "+granted)
        }
    }

    @SuppressLint("SdCardPath", "MutableImplicitPendingIntent", "SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        checkUpdate()

//        setIntent(intent)
//
//        if (Build.VERSION.SDK_INT >= 33) {
//            pushNotificationPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
//        } else {
//
//        }



        context = WeakReference(this)
        activity = WeakReference(this)
        mainActivity = WeakReference(this)

        navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        window.decorView.viewTreeObserver.addOnGlobalLayoutListener {
            val r = Rect()
            window.decorView.getWindowVisibleDisplayFrame(r)
            val height = window.decorView.height
            if (height - r.bottom > height * 0.1399) {
                isOpen = WeakReference(true)
            } else {
                isOpen = WeakReference(false)
            }
        }


        loadBanner()
        viewModel.itemAds.observe(this@MainActivity, Observer {
            if (it != null) {
                viewModel.itemAds.value?.let { it1 ->
                    viewModel.bannerAdapter.submitData(it1)
                    binding.banner.adapter = viewModel.bannerAdapter
                    binding.tabDots.setupWithViewPager(binding.banner, true)
                    binding.banner.autoScroll()
                    when(screenValue){
                        0 -> binding.layoutBanner.visibility = View.GONE
                        in 1..2 -> binding.layoutBanner.visibility = View.VISIBLE
                    }
                }
            }
        })


        observeConnectivityManager()

        getToken(){
            Log.e("TAG", "getToken "+this)
        }


        val bundle=intent?.extras

        if(bundle != null) {
            showData(bundle)
        }

//        Log.e("TAG", "bundleAA "+bundle?.getString("key"))

//        var zz = "9988397522"
//        var aa = zz.substring(0,2)
//        Log.e("TAG", "aaAA "+aa)
//
//        var bb = zz.substring(zz.length - 2,zz.length)
//        Log.e("TAG", "aaBB "+bb)
//
//        Log.e("TAG", "aaCC "+aa+"XXXXXX"+bb)

//        val activityRootView: View = findViewById(R.id.layoutRoot)
//        activityRootView.viewTreeObserver.addOnGlobalLayoutListener {
//            val r = Rect()
//            activityRootView.getWindowVisibleDisplayFrame(r)
//            val heightDiff: Int = activityRootView.rootView.height - (r.bottom - r.top)
//            if (heightDiff > 100) {
//                //enter your code here
//                Log.e("TAG", "isOpenA ")
//            } else {
//                Log.e("TAG", "isOpenB ")
//            }
//        }


//        navHostFragment!!.navController.addOnDestinationChangedListener(object : NavController.OnDestinationChangedListener {
//            override fun onDestinationChanged(
//                controller: NavController,
//                destination: NavDestination,
//                arguments: Bundle?
//            ) {
//                Log.e("TAG", "onDestinationChanged: " + destination.label)
//            }
//        })

//        imagePathCC /storage/emulated/0/Download/1704524739950.png
//       content://com.streetsaarthi.nasvi.provider/external/Download/1704524739950.png
//        var aaa = "/storage/emulated/0/Download/1704434016909.png"
//        var bbb = File(aaa)
//        var ccc = File(bbb.absolutePath)

        //val imagePath: File = File(File("/storage/emulated/0/Download/1704434016909.png").absolutePath)
       // var file = File("/storage/emulated/0/Download/1704559718392.png")
//       fileZZ /storage/emulated/0/Download/1704559718392.png


//        val intent = Intent(Intent.ACTION_VIEW)
//        val data =
//            FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", file)
//        intent.setDataAndType(data, "image/*")
//        Log.e("TAG", "dataCC "+data)
//         intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
//        startActivity(intent)

//        val intent = Intent(Intent.ACTION_VIEW)
////        val data =
////            FileProvider.getUriForFile(requireContext(), requireContext().getApplicationContext().getPackageName() + ".provider", imagePath)
//      //  intent.setDataAndType(data, "image/*")
//        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
//
//////            val pendingIntent=if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//////                PendingIntent.getActivity(requireContext(),0,intent,PendingIntent.FLAG_MUTABLE)
//////            } else {
//////                PendingIntent.getActivity(requireContext(),0,intent,
//////                    PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE)
//////            }
//
//        val pendingIntent= if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
//            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_ALLOW_UNSAFE_IMPLICIT_INTENT)
//        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//            PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_MUTABLE)
//        } else {
//            PendingIntent.getActivity(this,0,intent,
//                PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE)
//        }
//
//        val CHANNEL_ID="my_channel_01" // The id of the channel.
//        val name: CharSequence="Download" // The user-visible name of the channel.
//        val importance= NotificationManager.IMPORTANCE_HIGH
//        var mChannel: NotificationChannel?=null
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            mChannel= NotificationChannel(CHANNEL_ID,name,importance)
//            mChannel.enableLights(true)
//            mChannel.lightColor= Color.WHITE
//            mChannel.setShowBadge(true)
//            mChannel.lockscreenVisibility= Notification.VISIBILITY_PUBLIC
//        }
//
//        val notification= NotificationCompat.Builder(this,CHANNEL_ID)
//            .setSmallIcon(R.mipmap.ic_launcher) //.setLargeIcon(icon)
//            .setPriority(Notification.PRIORITY_HIGH)
//            .setContentTitle(getString(R.string.app_name))
//            .setAutoCancel(true)
//            .setChannelId(CHANNEL_ID)
//            .setContentIntent(pendingIntent)
//            .setContentText("file.name").build()
//        val notificationManager=
//            getSystemService(NOTIFICATION_SERVICE) as NotificationManager
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) notificationManager.createNotificationChannel(
//            mChannel!!
//        )
//        notificationManager.notify(0,notification)





        if (intent!!.hasExtra(Screen)){
            var screen = intent.getStringExtra(Screen)
            Log.e("TAG", "screenAA "+screen)
            callBack()
            val navOptions: NavOptions = NavOptions.Builder()
                .setPopUpTo(R.id.navigation_bar, true)
                .build()
//            Handler(Looper.getMainLooper()).postDelayed(Thread {
                    if(screen == Start){
                        navHostFragment?.navController?.navigate(R.id.start, null, navOptions)
                    } else if (screen == Main){
                        navHostFragment?.navController?.navigate(R.id.dashboard, null, navOptions)
                    }
//            }, 100)

        }




        val manager =  packageManager
        val info = manager?.getPackageInfo( packageName, 0)
        val versionName = info?.versionName
        binding.textVersion.text = getString(R.string.app_version_1_0, versionName)

        binding.btLogout.singleClick {
            callLogoutDialog()
        }


//        binding.btLogout.singleClick {
//            MainActivity.mainActivity.get()!!.callBack()
//
//            var logoutAlert = MaterialAlertDialogBuilder(this)
//                .setTitle(resources.getString(R.string.logout))
//                .setMessage(resources.getString(R.string.are_your_sure_want_to_logout))
//                .setPositiveButton(resources.getString(R.string.yes)) { dialog, i ->
//                    dialog.dismiss()
//
//                    CallDataStore.clearAllData()
//                    Toast.makeText(requireContext(), getString(R.string.logged_out_successfully), Toast.LENGTH_SHORT).show()
//                    view.navigateDirection(SettingsDirections.actionSettings2ToLogin())
//                }
//                .setNegativeButton(resources.getString(R.string.cancel)) { dialog, i ->
//                    dialog.dismiss()
//                }
//                .setCancelable(false)
//                .show()
//
//
//        }


        val mDrawerToggle: ActionBarDrawerToggle = object : ActionBarDrawerToggle(
            this, binding.drawerLayout,
            R.string.open, R.string.close
        ) {
            override fun onDrawerClosed(view: View) {
                super.onDrawerClosed(view)
                Log.e("TAG", "onDrawerClosed")
            }

            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)
                Log.e("TAG", "onDrawerOpened")
            }
        }
        binding.drawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        binding.apply {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)

            textTitleMain.singleClick {
                drawerLayout.close()
            }

            topLayout.ivMenu.singleClick {
                drawerLayout.open()
            }

            binding.recyclerView.setHasFixedSize(true)
            binding.recyclerView.adapter = viewModel.menuAdapter
            viewModel.menuAdapter.submitList(viewModel.itemMain)
            recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
        }



//         onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
//            override fun handleOnBackPressed() {
////                Log.e("TAG", "onBackPressedDispatcher")
//                //view?.navigateBack()
////                if (binding.webView.canGoBack())
////                    binding.webView.goBack()
////                else
////                    view?.navigateBack()
////                    //view?.
//
//                var cc = navHostFragment!!.getChildFragmentManager().getFragments().get(0);
//                Log.e("TAG", "onBackPressedDispatcher "+cc)
//            }
//        })


//        if(intent != null){
//            var screen = intent.getStringExtra(Screen)
//            when(screen){
//                Start -> navHostFragment.navController.navigate(R.id.start)
//                else -> print("I don't know anything about it")
//            }
//        }

        //detectLanguage("i am Dinesh")

//        val llc = ConfigurationCompat.getLocales(Resources.getSystem().configuration)
//        for (i in 0 until llc.size()) {
//            println(llc[i]!!.displayLanguage)
//            Log.e("MainActivity", "displayLanguage "+llc[i]!!.displayLanguage)
//        }
//
//        val isLang = Locale.getDefault().language == "hi"
//        Log.e("MainActivity", "isLang "+isLang)
//
//        var local = Locale.getDefault().getLanguage()
//        Log.e("MainActivity", "locallocal "+local)


//        initLanguage(
//            FirebaseTranslateLanguage.EN,
//            FirebaseTranslateLanguage.HI,
//            "Hi i am dinesh"
//        )
    }



    @SuppressLint("SuspiciousIndentation")
    fun callLogoutDialog() {
        if(logoutAlert?.isShowing == true) {
            return
        }
        logoutAlert = MaterialAlertDialogBuilder(this, R.style.LogoutDialogTheme)
            .setTitle(resources.getString(R.string.app_name))
            .setMessage(resources.getString(R.string.are_your_sure_want_to_logout))
            .setPositiveButton(resources.getString(R.string.yes)) { dialog, _ ->
                dialog.dismiss()
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.drawerLayout.close()
                }, 500)
                readData(DataStoreKeys.LOGIN_DATA) { loginUser ->
                    if (loginUser != null) {
                        val requestBody: MultipartBody.Builder = MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                        requestBody.addFormDataPart("mobile_number", ""+Gson().fromJson(loginUser, Login::class.java).mobile_no)
                        viewModel.logoutAccount(requestBody.build())
                    }
                }
            }
            .setNegativeButton(resources.getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.drawerLayout.close()
                }, 500)
            }
            .setCancelable(false)
            .show()


        viewModel.itemLogoutResult.value = false
        viewModel.itemLogoutResult.observe(this@MainActivity, Observer {
            if (it) {
                clearData()
            }
        })
    }


    @SuppressLint("SuspiciousIndentation")
    fun callDeleteDialog() {
        if(deleteAlert?.isShowing == true) {
            return
        }
        deleteAlert = MaterialAlertDialogBuilder(this, R.style.LogoutDialogTheme)
            .setTitle(resources.getString(R.string.app_name))
            .setMessage(resources.getString(R.string.are_your_sure_want_to_delete))
            .setPositiveButton(resources.getString(R.string.yes)) { dialog, _ ->
                dialog.dismiss()
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.drawerLayout.close()
                }, 500)
                readData(DataStoreKeys.LOGIN_DATA) { loginUser ->
                    if (loginUser != null) {
                        val requestBody: MultipartBody.Builder = MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("user_type", USER_TYPE)
                        requestBody.addFormDataPart("user_id", ""+Gson().fromJson(loginUser, Login::class.java).id)
                        requestBody.addFormDataPart("delete_account", "Yes")
                        viewModel.deleteAccount(requestBody.build())
                    }
                }
            }
            .setNegativeButton(resources.getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.drawerLayout.close()
                }, 500)
            }
            .setCancelable(false)
            .show()

        viewModel.itemDeleteResult.value = false
        viewModel.itemDeleteResult.observe(this@MainActivity, Observer {
            if (it) {
                clearData()
            }
        })


    }



    fun clearData(){
        DataStoreUtil.removeKey(DataStoreKeys.LOGIN_DATA) {}
        DataStoreUtil.removeKey(DataStoreKeys.AUTH) {}
        DataStoreUtil.removeKey(DataStoreKeys.LIVE_SCHEME_DATA) {}
        DataStoreUtil.removeKey(DataStoreKeys.LIVE_NOTICE_DATA) {}
        DataStoreUtil.removeKey(DataStoreKeys.LIVE_TRAINING_DATA) {}
        DataStoreUtil.removeKey(DataStoreKeys.Complaint_Feedback_DATA) {}
        DataStoreUtil.removeKey(DataStoreKeys.Information_Center_DATA) {}
        DataStoreUtil.clearDataStore {  }
        callBack()
        val navOptions: NavOptions = NavOptions.Builder()
            .setPopUpTo(R.id.navigation_bar, true)
            .build()
        runOnUiThread {
            navHostFragment?.navController?.navigate(R.id.onboard, null, navOptions)
        }
    }


    //    private fun initLanguage(idSL: Any?, idTL: Any?, text: String?) {
//        val option = FirebaseTranslatorOptions.Builder()
//            .setSourceLanguage(idSL as Int)
//            .setTargetLanguage(idTL as Int)
//            .build()
//        val textTranslator = FirebaseNaturalLanguage.getInstance().getTranslator(option)
//
//        // Download model for the first time
//
//        textTranslator.downloadModelIfNeeded()
//            .addOnSuccessListener {
//                Log.e("MainActivity", "Download Success")
//            }
//            .addOnFailureListener {
//                Log.e("MainActivity", "Download Failed: $it")
//            }
//
//
//        // Translate text from source language to target language related with model
//        textTranslator.translate(text.toString())
//            .addOnSuccessListener {
//                //tvResult.text = it
//                Log.e("MainActivity", "TranslateAA Success $it")
//            }.addOnFailureListener {
//                Log.e("MainActivity", "TranslateAA Failed: $it")
//            }
//    }
    private fun detectLanguage(string: String) {
//        val languageIdentifier = LanguageIdentification.getClient()
//        languageIdentifier.identifyLanguage("Hello World")
//            .addOnSuccessListener { languageCode ->
//                if (languageCode == "hi") {
//                    Log.e("TAG", "Can't identify language.")
//                } else {
//                    //Log.e("TAG", "Language: $languageCode "+)
////                    when (languageCode) {
////                        "en" -> detectedLanguage.text = "The Idenify Langauge is English"
////                        "hi" -> detectedLanguage.text = "The Idenify Langauge is Hindi"
////                        "ar" -> detectedLanguage.text = "The Idenify Langauge is Arabic"
////                    }
//                }
//            }
//            .addOnFailureListener {
//                Log.i("TAG", "Can't identify language.")
//            }

//        val options = FirebaseTranslatorOptions.Builder()
//            .setSourceLanguage(FirebaseTranslateLanguage.EN)
//            .setTargetLanguage(FirebaseTranslateLanguage.HI)
//            .build()
//        val englishGermanTranslator = FirebaseNaturalLanguage.getInstance().getTranslator(options)

//        englishGermanTranslator.downloadModelIfNeeded()
//            .addOnSuccessListener {
//                Log.e("TAG", " AAAAA "+it)
//            }
//            .addOnFailureListener { exception ->
//                Log.e("TAG", "Can't identify language.")
//            }

//        englishGermanTranslator.translate(string)
//            .addOnSuccessListener { translatedText ->
//                Log.e("TAG", " AAAAAtranslatedText "+translatedText)
//            }
//            .addOnFailureListener { exception ->
//                Log.e("TAG", "Can't identify language.")
//            }
    }

    public override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if(intent?.extras != null) {
            showData(intent?.extras!!)
        }
    }


    private fun showData(bundle: Bundle) {
        try {
            var key = bundle?.getString("key")
            var _id = bundle?.getString("_id")
//            Log.e("TAG", "showData "+res)
            when(key){
                "scheme" -> navHostFragment!!.navController.navigate(R.id.liveSchemes)
                "notice" -> navHostFragment!!.navController.navigate(R.id.liveNotices)
                "training" -> navHostFragment!!.navController.navigate(R.id.liveTraining)
                "profile" -> navHostFragment!!.navController.navigate(R.id.profiles)
                "Feedback" -> navHostFragment!!.navController.navigate(R.id.historyDetail, Bundle().apply {
                    putString("key", _id)
                })
            }
        }catch (e: Exception){}
    }


    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase,""))
    }






    fun callBack() {
        binding.apply {
            readData(DataStoreKeys.LOGIN_DATA) { loginUser ->
                if (loginUser == null) {
                    binding.topLayout.topToolbar.visibility = View.GONE
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                    binding.layoutBanner.visibility = View.GONE
                    binding.textHeaderTxt1.visibility = View.GONE
                } else {
                    binding.topLayout.topToolbar.visibility = View.VISIBLE
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)

                    var imageUrl = Gson().fromJson(loginUser, Login::class.java)?.profile_image_name?.url ?: ""
                    topLayout.ivImage.loadImage(url = { imageUrl })
                    topLayout.ivImage.singleClick {
                        arrayListOf(imageUrl).imageZoom(topLayout.ivImage)
                    }
                }
            }
            loadBanner()
        }
    }


var screenValue = 0
    fun callFragment(screen : Int) {
        screenValue = screen
        binding.apply {
            when(screen){
                0-> {
                    binding.textHeaderTxt1.visibility = View.GONE
                    binding.layoutBanner.visibility = View.GONE
                }
                1-> {
                    binding.textHeaderTxt1.visibility = View.VISIBLE
                    binding.mainLayout.setBackgroundResource(R.color.white)
                    viewModel.itemAds.value?.let { it1 ->
                        if (it1.size > 0){
                            if(screen == 1){
                                binding.layoutBanner.visibility = View.VISIBLE
                            }else{
                                binding.layoutBanner.visibility = View.GONE
                            }
                        } else {
                            binding.layoutBanner.visibility = View.GONE
                        }
                    }
                }
                2-> {
                    binding.textHeaderTxt1.visibility = View.VISIBLE
                    binding.mainLayout.setBackgroundResource(R.color._FFF3E4)
                    viewModel.itemAds.value?.let { it1 ->
                        if (it1.size > 0){
                            if(screen == 2){
                                binding.layoutBanner.visibility = View.VISIBLE
                            }else{
                                binding.layoutBanner.visibility = View.GONE
                            }
                        } else {
                            binding.layoutBanner.visibility = View.GONE
                        }
                    }
                }
            }
        }
    }


    private fun observeConnectivityManager() = try {
        connectivityManager.observe(this) {
            binding.tvInternet.isVisible = !it
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }



    var resultUpdate = registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()){ result->
            Log.e("TAG", "result.resultCode "+result.resultCode)
        if (result.resultCode == RESULT_OK) {
                    // Handle successful app update
            } else if (result.resultCode == RESULT_CANCELED) {
                finish()
            } else if (result.resultCode == RESULT_IN_APP_UPDATE_FAILED) {
                finish()
            } else {
                finish()
            }
    }

    private fun checkUpdate() {
        val appUpdateManager: AppUpdateManager = AppUpdateManagerFactory.create(this)
        appUpdateManager
            .appUpdateInfo
            .addOnSuccessListener { appUpdateInfo ->
                val starter =
                    IntentSenderForResultStarter { intent, _, fillInIntent, flagsMask, flagsValues, _, _ ->
                        val request = IntentSenderRequest.Builder(intent)
                            .setFillInIntent(fillInIntent)
                            .setFlags(flagsValues, flagsMask)
                            .build()
                        resultUpdate.launch(request)
                    }

                appUpdateManager.startUpdateFlowForResult(
                    appUpdateInfo,
                    AppUpdateType.FLEXIBLE,
                    starter,
                    123,
                )
            }

//        val appUpdateManager: AppUpdateManager = AppUpdateManagerFactory.create(this)
//        appUpdateManager
//            .appUpdateInfo
//            .addOnSuccessListener { appUpdateInfo ->
//                if (appUpdateInfo.updateAvailability()
//                    == UpdateAvailability.UPDATE_AVAILABLE
//                ) {
//                    // If an in-app update is already running, resume the update.
//                    appUpdateManager.startUpdateFlowForResult(
//                        appUpdateInfo,
//                        AppUpdateType.IMMEDIATE,
//                        this,
//                        1234
//                    )
//                }
//            }
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        when (requestCode) {
//            1234 -> {
//                when (resultCode) {
//                    Activity.RESULT_OK -> {
//                        Log.e("MainActivity", "onActivityResult" + "Result Ok")
//                        //  handle user's approval }
//                    }
//                    Activity.RESULT_CANCELED -> {
//                        Log.e("MainActivity", "onActivityResult" + "Result Cancelled")
//                        //  handle user's rejection  }
//                        finish()
//                    }
//                    ActivityResult.RESULT_IN_APP_UPDATE_FAILED -> {
//                        //if you want to request the update again just call checkUpdate()
//                        Log.e("MainActivity", "onActivityResult" + "Update Failure")
//                        //  handle update failure
//                        finish()
//                    }
//                }
//            }
//        }
//    }




    fun loadBanner() {
        if(viewModel.itemAds.value == null){
            readData(DataStoreKeys.LOGIN_DATA) { loginUser ->
                if (loginUser != null) {
                    viewModel.adsList()
                }
            }
        }
    }


    override fun onResume() {
        super.onResume()
        ioThread {
            delay(2500)
            callBack()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        logoutAlert?.let {
            logoutAlert!!.cancel()
        }
        deleteAlert?.let {
            deleteAlert!!.cancel()
        }
    }
}
