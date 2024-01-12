package com.streetsaarthi.nasvi.screens.mainActivity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.ConfigurationCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.util.Util
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
//import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage
//import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage
//import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions
//import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage
//import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage
//import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions
import com.squareup.picasso.Picasso
import com.streetsaarthi.nasvi.R
import com.streetsaarthi.nasvi.databinding.MainActivityBinding
import com.streetsaarthi.nasvi.datastore.DataStoreKeys
import com.streetsaarthi.nasvi.datastore.DataStoreUtil
import com.streetsaarthi.nasvi.datastore.DataStoreUtil.readData
import com.streetsaarthi.nasvi.models.login.Login
import com.streetsaarthi.nasvi.screens.onboarding.networking.Main
import com.streetsaarthi.nasvi.screens.onboarding.networking.Screen
import com.streetsaarthi.nasvi.screens.onboarding.networking.Start
import com.streetsaarthi.nasvi.utils.LocaleHelper
import com.streetsaarthi.nasvi.utils.myOptionsGlide
import dagger.hilt.android.AndroidEntryPoint
import java.lang.ref.WeakReference
import java.util.Locale
import com.streetsaarthi.nasvi.utils.GlideApp
import com.streetsaarthi.nasvi.utils.loadImage


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    companion object {

        @JvmStatic
        var hideValue: Boolean = false

        @JvmStatic
        lateinit var context: WeakReference<Context>

        @JvmStatic
        lateinit var activity:  WeakReference<Activity>

        @JvmStatic
        lateinit var mainActivity: WeakReference<MainActivity>

        var logoutAlert : AlertDialog?= null

        @SuppressLint("StaticFieldLeak")
        var navHostFragment : NavHostFragment? = null

        private var _binding: MainActivityBinding? = null
        val binding get() = _binding!!


        @JvmStatic
        lateinit var isOpen : WeakReference<Boolean>

    }



    private val viewModel: MainActivityVM by viewModels()

    private val pushNotificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if(granted){
            Log.e("TAG", "AAAAgranted "+granted)

        }else{
            Log.e("TAG", "BBBBgranted "+granted)
        }
    }

    @SuppressLint("SdCardPath", "MutableImplicitPendingIntent")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
////            val pendingIntent=if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
////                PendingIntent.getActivity(requireContext(),0,intent,PendingIntent.FLAG_MUTABLE)
////            } else {
////                PendingIntent.getActivity(requireContext(),0,intent,
////                    PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE)
////            }
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
            if(screen == Start){
                navHostFragment?.navController?.navigate(R.id.action_splash_to_start)
            } else if (screen == Main){
                navHostFragment?.navController?.navigate(R.id.action_splash_to_dashboard)
            }
        }




        val manager =  packageManager
        val info = manager?.getPackageInfo( packageName, 0)
        val versionName = info?.versionName
        binding.textVersion.text = getString(R.string.app_version_1_0, versionName)

        binding.btLogout.setOnClickListener {
            // MainActivity.mainActivity.get()!!.callBack()
            if(logoutAlert?.isShowing == true) {
                return@setOnClickListener
            }
            logoutAlert = MaterialAlertDialogBuilder(this, R.style.LogoutDialogTheme)
                .setTitle(resources.getString(R.string.logout))
                .setMessage(resources.getString(R.string.are_your_sure_want_to_logout))
                .setPositiveButton(resources.getString(R.string.yes)) { dialog, _ ->
                    dialog.dismiss()
                    DataStoreUtil.removeKey(DataStoreKeys.LOGIN_DATA) {}
                    DataStoreUtil.removeKey(DataStoreKeys.AUTH) {}
                    DataStoreUtil.removeKey(DataStoreKeys.LIVE_SCHEME_DATA) {}
                    DataStoreUtil.removeKey(DataStoreKeys.LIVE_NOTICE_DATA) {}
                    DataStoreUtil.removeKey(DataStoreKeys.LIVE_TRAINING_DATA) {}
                    DataStoreUtil.clearDataStore {  }
                    Handler(Looper.getMainLooper()).postDelayed({
                        binding.drawerLayout.close()
                    }, 500)

                    callBack()
                    val navOptions: NavOptions = NavOptions.Builder()
                        .setPopUpTo(R.id.navigation_bar, true)
                        .build()
                    navHostFragment?.navController?.navigate(R.id.start, null, navOptions)
                }
                .setNegativeButton(resources.getString(R.string.cancel)) { dialog, _ ->
                    dialog.dismiss()
                    Handler(Looper.getMainLooper()).postDelayed({
                        binding.drawerLayout.close()
                    }, 500)
                }
                .setCancelable(false)
                .show()
        }


//        binding.btLogout.setOnClickListener {
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

            textTitleMain.setOnClickListener {
                drawerLayout.close()
            }

            topLayout.ivMenu.setOnClickListener {
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
        Log.e("TAG", "onNewIntent "+intent)
        //navHostFragment!!.navController.navigate(R.id.start)

//        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);


        if (intent!!.hasExtra(Screen)){
            var screen = intent.getStringExtra(Screen)
            Log.e("TAG", "screen "+screen)
//            navHostFragment?.navController?.navigate(R.id.start)
//            navHostFragment?.navController?.popBackStack(R.id.start, true)
//            setIntent(intent)
            navHostFragment?.navController?.navigate(R.id.start)

        }
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase,""))
    }


//    override fun onResume() {
//        super.onResume()
//
//        Log.e("TAG", "onResume1 "+hideValue)
//
//        callBack()
//    }



    fun callBack() {
        binding.apply {
            readData(DataStoreKeys.LOGIN_DATA) { loginUser ->
                if (loginUser == null) {
                    binding.topLayout.topToolbar.visibility = View.GONE
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                } else {
                    binding.topLayout.topToolbar.visibility = View.VISIBLE
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
                    readData(DataStoreKeys.LOGIN_DATA) { loginUser ->
                        if(loginUser != null){
//                            Picasso.get().load(
//                                Gson().fromJson(loginUser, Login::class.java).profile_image_name.url
//                            ).memoryPolicy(MemoryPolicy.NO_CACHE,MemoryPolicy.NO_STORE)
//                                .networkPolicy(NetworkPolicy.OFFLINE, NetworkPolicy.OFFLINE)
//                                .into(binding.topLayout.ivImage)

//                            GlideApp.with(binding.root)
//                                .load(Gson().fromJson(loginUser, Login::class.java).profile_image_name.url)
//                                .apply(myOptionsGlide)
//                                .into(binding.topLayout.ivImage)
                            topLayout.ivImage.loadImage(url = { Gson().fromJson(loginUser, Login::class.java).profile_image_name.url })
                        }
                    }
                }
            }
        }
    }


    fun callFragment() {
        navHostFragment?.navController?.navigateUp()
//         getSupportFragmentManager().popBackStack();

        val navOptions: NavOptions = NavOptions.Builder()
            .setPopUpTo(R.id.navigation_bar, true)
            .build()
        navHostFragment?.navController?.navigate(R.id.start, null, navOptions)
    }




    fun callUpdateData() {

    }
}
