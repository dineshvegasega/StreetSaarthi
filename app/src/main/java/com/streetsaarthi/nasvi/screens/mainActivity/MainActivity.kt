package com.streetsaarthi.nasvi.screens.mainActivity

//import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage
//import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage
//import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions
//import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage
//import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage
//import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions
import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.app.ActivityManager.RunningAppProcessInfo
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Build
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
import com.streetsaarthi.nasvi.App
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
import com.streetsaarthi.nasvi.utils.imageZoom
import com.streetsaarthi.nasvi.utils.ioThread
import com.streetsaarthi.nasvi.utils.isAppIsInBackground
import com.streetsaarthi.nasvi.utils.loadImage
import com.streetsaarthi.nasvi.utils.showSnackBar
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
        lateinit var activity: WeakReference<Activity>

        @JvmStatic
        lateinit var mainActivity: WeakReference<MainActivity>

        var logoutAlert: AlertDialog? = null
        var deleteAlert: AlertDialog? = null

        @SuppressLint("StaticFieldLeak")
        var navHostFragment: NavHostFragment? = null

        private var _binding: MainActivityBinding? = null
        val binding get() = _binding!!

        @JvmStatic
        lateinit var isOpen: WeakReference<Boolean>

        @JvmStatic
        var scale10: Int = 0


    }

    private val viewModel: MainActivityVM by viewModels()

    private val connectivityManager by lazy { ConnectivityManager(this) }


    private val pushNotificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            Log.e("TAG", "AAAAgranted " + granted)

        } else {
            Log.e("TAG", "BBBBgranted " + granted)
        }
    }

    @SuppressLint("SdCardPath", "MutableImplicitPendingIntent", "SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)


        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        context = WeakReference(this)
        activity = WeakReference(this)
        mainActivity = WeakReference(this)


        var aa = 10
        var total = 11
        var totalPage = 0

        totalPage = total / aa
        val dd = total % aa
        if(dd != 0){
            totalPage += 1
        }

        Log.e("TAG", "totalPage "+totalPage)

        checkUpdate()

//
//        if (Build.VERSION.SDK_INT >= 33) {
//            pushNotificationPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
//        }



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
                    when (screenValue) {
                        0 -> binding.layoutBanner.visibility = View.GONE
                        in 1..2 -> binding.layoutBanner.visibility = View.VISIBLE
                    }
                }
            }
        })


        observeConnectivityManager()

//        getToken(){
//            Log.e("TAG", "getToken "+this)
//            saveData(DataStoreKeys.TOKEN, this)
//        }


        val bundle = intent?.extras

        if (bundle != null) {
            showData(bundle)
        }


        val manager = packageManager
        val info = manager?.getPackageInfo(packageName, 0)
        val versionName = info?.versionName
        binding.textVersion.text = getString(R.string.app_version_1_0, versionName)

        binding.btLogout.singleClick {
            callLogoutDialog()
        }


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

//        onBackPressedDispatcher.addCallback(this , object : OnBackPressedCallback(true) {
//            override fun handleOnBackPressed() {
//                val backStackEntryCount = navHostFragment?.childFragmentManager?.backStackEntryCount
//                Log.e("TAG", "backStackEntryCount "+backStackEntryCount)
//                if(backStackEntryCount == 0){
//                    this@MainActivity.finish()
//                }else if(backStackEntryCount == 1){
//                    navHostFragment!!.navController.navigate(R.id.dashboard)
//                }
//            }
//        })
    }


    @SuppressLint("SuspiciousIndentation")
    fun callLogoutDialog() {
        if (logoutAlert?.isShowing == true) {
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
                        requestBody.addFormDataPart(
                            "mobile_number",
                            "" + Gson().fromJson(loginUser, Login::class.java).mobile_no
                        )
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
        if (deleteAlert?.isShowing == true) {
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
                        requestBody.addFormDataPart(
                            "user_id",
                            "" + Gson().fromJson(loginUser, Login::class.java).id
                        )
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
                showSnackBar(getString(R.string.request_delete))
                clearData()
            }
        })


    }


    fun clearData() {
        DataStoreUtil.removeKey(DataStoreKeys.LOGIN_DATA) {}
        DataStoreUtil.removeKey(DataStoreKeys.AUTH) {}
        DataStoreUtil.removeKey(DataStoreKeys.LIVE_SCHEME_DATA) {}
        DataStoreUtil.removeKey(DataStoreKeys.LIVE_NOTICE_DATA) {}
        DataStoreUtil.removeKey(DataStoreKeys.LIVE_TRAINING_DATA) {}
        DataStoreUtil.removeKey(DataStoreKeys.Complaint_Feedback_DATA) {}
        DataStoreUtil.removeKey(DataStoreKeys.Information_Center_DATA) {}
        DataStoreUtil.clearDataStore { }
        callBack()
        val navOptions: NavOptions = NavOptions.Builder()
            .setPopUpTo(R.id.navigation_bar, true)
            .build()
        runOnUiThread {
            navHostFragment?.navController?.navigate(R.id.onboard, null, navOptions)
        }
    }


    public override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (intent?.extras != null) {
            showData(intent?.extras!!)
        }
    }


    private fun showData(bundle: Bundle) {
        try {
            val navOptions: NavOptions = NavOptions.Builder()
                .setPopUpTo(R.id.navigation_bar, true)
                .build()
            if (intent!!.hasExtra(Screen)) {
                var screen = intent.getStringExtra(Screen)
                Log.e("TAG", "screenAA " + screen)
                if (screen == Main) {
                    binding.topLayout.topToolbar.visibility = View.VISIBLE
                }
                callBack()
                if (screen == Start) {
                    navHostFragment?.navController?.navigate(R.id.start, null, navOptions)
                } else if (screen == Main) {
                    if (bundle?.getString("key") != null) {
                        callRedirect(bundle)
                    } else {
                        Log.e("key", "showDataBB ")
                        Log.e("_id", "showDataBB ")
                        navHostFragment?.navController?.navigate(R.id.dashboard, null, navOptions)
                    }
                }
            } else {
                Log.e("TAG", "screenBB ")
                if (bundle?.getString("key") != null) {
                    callRedirect(bundle)
                }
            }

        } catch (e: Exception) {
        }
    }

    @SuppressLint("SuspiciousIndentation")
    private fun callRedirect(bundle: Bundle) {
        var key = bundle?.getString("key")
        var _id = bundle?.getString("_id")
        Log.e("key", "showDataAA " + key)
        Log.e("_id", "showDataAA " + _id)
        val navOptions: NavOptions = NavOptions.Builder()
            .setPopUpTo(R.id.dashboard, true)
            .build()

//        val myProcess = RunningAppProcessInfo()
//        ActivityManager.getMyMemoryState(myProcess)
//        val isInBackground = myProcess.importance != RunningAppProcessInfo.IMPORTANCE_FOREGROUND
//
//        Log.e("TAG", "isActivityBackgound$isInBackground")

//        if(bundle.getBoolean("isActivityBackgound")){
//            Log.e("TAG", "isAppIsInBackgroundAA ")
//            when (key) {
//                "scheme" -> navHostFragment!!.navController.navigate(R.id.liveSchemes, null, navOptions)
//                "notice" -> navHostFragment!!.navController.navigate(R.id.liveNotices, null, navOptions)
//                "training" -> navHostFragment!!.navController.navigate(R.id.liveTraining, null, navOptions)
//                "profile" -> navHostFragment!!.navController.navigate(R.id.profiles, null, navOptions)
//                "Feedback" -> navHostFragment!!.navController.navigate(
//                    R.id.historyDetail,
//                    Bundle().apply {
//                        putString("key", _id)
//                    }, navOptions)
//            }
//        }else{
//            Log.e("TAG", "isAppIsInBackgroundBB ")
            when (key) {
                "scheme" -> navHostFragment!!.navController.navigate(R.id.liveSchemes)
                "notice" ->  navHostFragment!!.navController.navigate(R.id.liveNotices)
                "training" -> navHostFragment!!.navController.navigate(R.id.liveTraining)
                "profile" -> navHostFragment!!.navController.navigate(R.id.profiles)
                "Feedback" -> navHostFragment!!.navController.navigate(
                    R.id.historyDetail,
                    Bundle().apply {
                        putString("key", _id)
                    })
            }
//        }

    }


    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase, ""))
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

                    var imageUrl = ""
                    try {
                        val imageUrlLogin = Gson().fromJson(loginUser, Login::class.java)
                        if (imageUrlLogin != null) {
                            val imageUrlName = imageUrlLogin.profile_image_name
                            imageUrl = imageUrlName.url ?: ""
                        }
                    } catch (_: Exception) {
                    }
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
    fun callFragment(screen: Int) {
        screenValue = screen
        binding.apply {
            when (screen) {
                0 -> {
                    binding.textHeaderTxt1.visibility = View.GONE
                    binding.layoutBanner.visibility = View.GONE
                }

                1 -> {
                    binding.textHeaderTxt1.visibility = View.VISIBLE
                    binding.mainLayout.setBackgroundResource(R.color.white)
                    viewModel.itemAds.value?.let { it1 ->
                        if (it1.size > 0) {
                            if (screen == 1) {
                                binding.layoutBanner.visibility = View.VISIBLE
                            } else {
                                binding.layoutBanner.visibility = View.GONE
                            }
                        } else {
                            binding.layoutBanner.visibility = View.GONE
                        }
                    }
                }

                2 -> {
                    binding.textHeaderTxt1.visibility = View.VISIBLE
                    binding.mainLayout.setBackgroundResource(R.color._FFF3E4)
                    viewModel.itemAds.value?.let { it1 ->
                        if (it1.size > 0) {
                            if (screen == 2) {
                                binding.layoutBanner.visibility = View.VISIBLE
                            } else {
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


    var resultUpdate =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            Log.e("TAG", "result.resultCode " + result.resultCode)
            if (result.resultCode == RESULT_OK) {
                // Handle successful app update
            } else if (result.resultCode == RESULT_CANCELED) {
//                finish()
            } else if (result.resultCode == RESULT_IN_APP_UPDATE_FAILED) {
//                finish()
            } else {
//                finish()
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
    }


    fun loadBanner() {
        if (viewModel.itemAds.value == null) {
            readData(DataStoreKeys.LOGIN_DATA) { loginUser ->
                if (loginUser != null) {
                    viewModel.adsList()
                }
            }
        }
    }


    fun reloadActivity(language: String, screen: String) {
        LocaleHelper.setLocale(this, language)
        val refresh = Intent(Intent(this, MainActivity::class.java))
        refresh.putExtra(Screen, screen)
        refresh.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        finish()
        finishAffinity()
        startActivity(refresh)
    }


    override fun onStart() {
        super.onStart()
        val fontScale = resources.configuration.fontScale
        Log.e("TAG", "App.scale xxxhdpi "+fontScale)
        scale10 = when(fontScale){
            0.8f ->  14
            0.9f ->  13
            1.0f ->  12
            1.1f ->  11
            1.2f ->  10
            1.3f ->  9
            1.5f ->  8
            1.7f ->  7
            2.0f ->  6
            else -> {5}
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
