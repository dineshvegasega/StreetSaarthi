package com.streetsaarthi.nasvi.screens.main.settings

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageInstaller
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManagerFactory
import com.google.android.play.core.tasks.OnCompleteListener
import com.google.android.play.core.tasks.Task
import com.google.firebase.dynamiclinks.androidParameters
import com.google.firebase.dynamiclinks.dynamicLink
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.dynamiclinks.shortLinkAsync
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.gson.Gson
import com.streetsaarthi.nasvi.R
import com.streetsaarthi.nasvi.databinding.ItemLanguageStartBinding
import com.streetsaarthi.nasvi.databinding.SettingsBinding
import com.streetsaarthi.nasvi.datastore.DataStoreKeys
import com.streetsaarthi.nasvi.datastore.DataStoreUtil.readData
import com.streetsaarthi.nasvi.genericAdapter.GenericAdapter
import com.streetsaarthi.nasvi.models.Login
import com.streetsaarthi.nasvi.networking.USER_TYPE
import com.streetsaarthi.nasvi.screens.mainActivity.MainActivity
import com.streetsaarthi.nasvi.screens.mainActivity.MainActivity.Companion.networkFailed
import com.streetsaarthi.nasvi.utils.callNetworkDialog
import com.streetsaarthi.nasvi.utils.loadHtml
import com.streetsaarthi.nasvi.utils.mainThread
import com.streetsaarthi.nasvi.utils.singleClick
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MultipartBody
import org.jsoup.Jsoup
import java.io.File


@AndroidEntryPoint
class Settings : Fragment() {
    private val viewModel: SettingsVM by viewModels()
    private var _binding: SettingsBinding? = null
    private val binding get() = _binding!!


    var languageAlert: BottomSheetDialog? = null
    var notificationAlert: AlertDialog? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SettingsBinding.inflate(inflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        MainActivity.mainActivity.get()?.callFragment(1)

        binding.apply {
            inclideHeaderSearch.textHeaderTxt.text = getString(R.string.settings)
            inclideHeaderSearch.editTextSearch.visibility = View.GONE


            val manager = requireContext().packageManager
            val info = manager?.getPackageInfo(requireContext().packageName, 0)
            val versionName = info?.versionName
            textAppVersionTxt.text = getString(R.string.app_version_1_0, versionName)

            viewModel.appLanguage.observe(viewLifecycleOwner, Observer {
                editTextSelectLanguage.setText(it)
            })


            editTextSelectLanguage.singleClick {
                if (languageAlert?.isShowing == true) {
                    return@singleClick
                }
                val dialogView: View = LayoutInflater.from(requireContext())
                    .inflate(R.layout.dialog_bottom_your_booking2, null)
                languageAlert = BottomSheetDialog(requireContext())
                languageAlert?.setContentView(dialogView)
                languageAlert?.let {
                    languageAlert?.show()
                }
                val window = languageAlert?.window
                window!!.setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                window?.setBackgroundDrawableResource(android.R.color.transparent)

                val pastBookingAdapter =
                    object : GenericAdapter<ItemLanguageStartBinding, SettingsVM.Item>() {
                        override fun onCreateView(
                            inflater: LayoutInflater,
                            parent: ViewGroup,
                            viewType: Int
                        ) = ItemLanguageStartBinding.inflate(inflater, parent, false)

                        @SuppressLint("NotifyDataSetChanged")
                        override fun onBindHolder(
                            binding: ItemLanguageStartBinding,
                            dataClass: SettingsVM.Item,
                            position: Int
                        ) {
                            binding.btImage.setImageDrawable(
                                ContextCompat.getDrawable(
                                    binding.root.context,
                                    if (dataClass.isSelected == true) R.drawable.radio_sec_filled else R.drawable.radio_sec_empty
                                )
                            );
                            binding.btLanguage.text = dataClass.name
                            binding.btLanguage.singleClick {
                                val list = currentList
                                list.forEach {
                                    it.isSelected = dataClass == it
                                }
                                notifyDataSetChanged()
                                callLanguageApi(dataClass.locale, 1)
                                Handler(Looper.getMainLooper()).postDelayed(Thread {
                                    MainActivity.mainActivity.get()?.runOnUiThread {
                                        languageAlert?.dismiss()
                                    }
                                }, 100)
                            }

                            binding.btImage.singleClick {
                                val list = currentList
                                list.forEach {
                                    it.isSelected = dataClass == it
                                }
                                notifyDataSetChanged()
                                callLanguageApi(dataClass.locale, 1)
                                Handler(Looper.getMainLooper()).postDelayed(Thread {
                                    MainActivity.mainActivity.get()?.runOnUiThread {
                                        languageAlert?.dismiss()
                                    }
                                }, 100)
                            }
                        }
                    }
                val recyclerView = dialogView.findViewById<RecyclerView>(R.id.rvList)

                pastBookingAdapter.submitList(viewModel.itemMain)
                recyclerView.adapter = pastBookingAdapter
            }


            readData(DataStoreKeys.LOGIN_DATA) { loginUser ->
                if (loginUser != null) {
                    val noti = Gson().fromJson(loginUser, Login::class.java).notification
                    switchNotifications.isChecked = if (noti == "Yes") true else false
                }
            }



            switchNotifications.singleClick {
                if (notificationAlert?.isShowing == true) {
                    return@singleClick
                }


                readData(DataStoreKeys.LOGIN_DATA) { loginUser ->
                    val user = Gson().fromJson(loginUser, Login::class.java)
                    if (loginUser != null) {
                        notificationAlert =
                            MaterialAlertDialogBuilder(requireContext(), R.style.LogoutDialogTheme)
                                .setTitle(resources.getString(R.string.app_name))
                                .setMessage(
                                    if (user.notification == "Yes") resources.getString(R.string.are_your_sure_want_to_turn_Off) else resources.getString(
                                        R.string.are_your_sure_want_to_turn_On
                                    )
                                )
                                .setPositiveButton(resources.getString(R.string.yes)) { dialog, _ ->
                                    dialog.dismiss()
                                    val requestBody: MultipartBody.Builder = MultipartBody.Builder()
                                        .setType(MultipartBody.FORM)
                                        .addFormDataPart("user_type", USER_TYPE)
                                    requestBody.addFormDataPart("user_id", "" + user.id)
                                    if (user.notification == "Yes") {
                                        requestBody.addFormDataPart("notification", "No")
                                    } else {
                                        requestBody.addFormDataPart("notification", "Yes")
                                    }
                                    if (networkFailed) {
                                        viewModel.notificationUpdate(
                                            "" + user.id,
                                            requestBody.build(),
                                            0
                                        )
                                    } else {
                                        requireContext().callNetworkDialog()
                                    }
                                }
                                .setNegativeButton(resources.getString(R.string.cancel)) { dialog, _ ->
                                    dialog.dismiss()
                                    Handler(Looper.getMainLooper()).postDelayed({
                                        MainActivity.binding.drawerLayout.close()
                                    }, 500)
                                }
                                .setCancelable(false)
                                .show()
                        switchNotifications.isChecked =
                            if (user.notification == "Yes") true else false
                    }
                }


                viewModel.itemNotificationUpdateResult.value = false
                viewModel.itemNotificationUpdateResult.observe(requireActivity(), Observer {
                    readData(DataStoreKeys.LOGIN_DATA) { loginUser ->
                        if (loginUser != null) {
                            val noti = Gson().fromJson(loginUser, Login::class.java).notification
                            switchNotifications.isChecked = if (noti == "Yes") true else false
                        }
                    }
                })

            }


            editTextChangeNumber.singleClick {
                view.findNavController().navigate(R.id.action_settings_to_changeMobile)
            }


            editTextChangePassword.singleClick {
                view.findNavController().navigate(R.id.action_settings_to_changePassword)
            }


            btLogout.singleClick {
                MainActivity.mainActivity.get()!!.callLogoutDialog()
            }


            btDeleteAccount.singleClick {
                MainActivity.mainActivity.get()!!.callDeleteDialog()
            }


            textAboutUsTxt.singleClick {
                viewModel.show()
                mainThread {
                    openDialog(1)
                }
            }

            textPrivacyPolicyTxt.singleClick {
                viewModel.show()
                mainThread {
                    openDialog(2)
                }
            }

            textTermsConditionsTxt.singleClick {
                viewModel.show()
                mainThread {
                    openDialog(3)
                }
            }

            textRateOurAppTxt.singleClick {
                showRatingUserInterface(requireActivity())
            }

            textShareOurAppTxt.singleClick {
                val shareContent: String =
                    "Hey, have you heard about Street Saarthi?! It’s a Developed by the National Association of Street Vendors of India (NASVI), that actually lets you play flirt against dates as well as your mates! \nYou HAVE to give it a try!..."
                val dynamicLink = Firebase.dynamicLinks.dynamicLink {
                    val builder = Uri.Builder().scheme("https")
                        .authority("com.streetsaarthi.nasvi")
                    //                .appendPath("params")
//                    .appendQueryParameter(queryParam, queryParamValue)
                    link = builder.build()
                    domainUriPrefix = "https://streetsaarthi.page.link"
                    androidParameters("com.streetsaarthi.nasvi") {}
//                iosParameters("com.dtm") {
//    //                appStoreId = "1493006990"
//                }
                }
                Firebase.dynamicLinks.shortLinkAsync {
                    longLink = dynamicLink.uri
                }.addOnSuccessListener { result ->
                    val link = result.shortLink?.toString() ?: ""
                    val shareIntent = Intent()
                    shareIntent.action = Intent.ACTION_SEND
                    shareIntent.type = "text/plain"
                    shareIntent.putExtra(Intent.EXTRA_TEXT, "$shareContent\n\n $link")
                    view.context.startActivity(
                        Intent.createChooser(
                            shareIntent,
                            "Share to"
                        )
                    )
                }.addOnFailureListener {
                    it.printStackTrace()
                }
            }

//            val appUpdateManager = AppUpdateManagerFactory.create(requireContext())
//
//            val appUpdateInfoTask = appUpdateManager.appUpdateInfo
//            appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
//                var ss = appUpdateInfo.availableVersionCode()
//                textAboutUsNewVersionTxt.setText( "AA"+ss)
//            }
//            appUpdateInfoTask.addOnFailureListener {
//                textAboutUsNewVersionTxt.setText( "BB"+this.toString())
//            }
//            appUpdateInfoTask.addOnCompleteListener {
//                textAboutUsNewVersionTxt.setText( "CC "+Gson().toJson(it))
//            }



            textAboutUsNewVersionTxt.singleClick {

//                val playStoreVersionCode: String = FirebaseRemoteConfig.getInstance().getString(
//                    "com.streetsaarthi.nasvi"
//                )

                var newVersion = Jsoup.connect("https://play.google.com/store/apps/details?id=" + requireActivity().getPackageName() + "&hl=it")
                    .timeout(30000)
                    .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                    .referrer("http://www.google.com")
                    .get()
                    .select("div[itemprop=softwareVersion]")
                    .first()
                    ?.ownText();

                Log.e("TAG", "playStoreVersionCode "+newVersion)

//                val pInfo: PackageInfo =
//                    this.getPackageManager().getPackageInfo(getPackageName(), 0)
//                val currentAppVersionCode = pInfo.versionCode
//                if (playStoreVersionCode > currentAppVersionCode) {
////Show update popup or whatever best for you
//                }

                // install(requireContext(), "com.streetsaarthi.nasvi", "https://play.google.com/store/apps/details?id=com.streetsaarthi.nasvi")

//                val appUpdateManager = AppUpdateManagerFactory.create(updateActionListener.getActivity())
//                val appUpdateInfoTask = appUpdateManager!!.appUpdateInfo
//                appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
//                    handleAppUpdateInfo(appUpdateInfo)
//                }


//                val appUpdateManager = AppUpdateManagerFactory.create(requireContext())
//
//                val appUpdateInfoTask = appUpdateManager.appUpdateInfo
//
//                appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
////                    if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
////
////                        && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
////                    ) {
////                        // Request the update.
////                    }
//                    if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
//                        && (appUpdateInfo.clientVersionStalenessDays() ?: -1) >= 4
//                        && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
//                        // Request the update.
//                    }

//                    if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
//                        popupSnackbarForCompleteUpdate()
//                    }

//            }
            }
        }
    }


    fun install(context: Context, packageName: String, apkPath: String) {

// PackageManager provides an instance of PackageInstaller
        val packageInstaller = context.packageManager.packageInstaller

// Prepare params for installing one APK file with MODE_FULL_INSTALL
// We could use MODE_INHERIT_EXISTING to install multiple split APKs
        val params = PackageInstaller.SessionParams(PackageInstaller.SessionParams.MODE_FULL_INSTALL)
        params.setAppPackageName(packageName)

// Get a PackageInstaller.Session for performing the actual update
        val sessionId = packageInstaller.createSession(params)
        val session = packageInstaller.openSession(sessionId)

// Copy APK file bytes into OutputStream provided by install Session
        val out = session.openWrite(packageName, 0, -1)
        val fis = File(apkPath).inputStream()
        fis.copyTo(out)
        session.fsync(out)
        out.close()

// The app gets killed after installation session commit
        session.commit(
            PendingIntent.getBroadcast(context, sessionId,
            Intent("android.intent.action.MAIN"), PendingIntent.FLAG_IMMUTABLE).intentSender)
    }

    private fun showRatingUserInterface(activity: Activity) {
        val manager = ReviewManagerFactory.create(activity)
        val request: Task<ReviewInfo> = manager.requestReviewFlow()
        request.addOnCompleteListener(object : OnCompleteListener<ReviewInfo?> {
            override fun onComplete(task: Task<ReviewInfo?>) {
                Log.e("TAG", "onComplete1 "+task.result)
                if (task.isSuccessful()) {
                    val reviewInfo: ReviewInfo = task.getResult()
                    val flow: Task<Void> = manager.launchReviewFlow(activity, reviewInfo)
                    flow.addOnCompleteListener(object : OnCompleteListener<Void?> {
                        override fun onComplete(task: Task<Void?>) {
                            Log.e("TAG", "onComplete2 "+task.result)
                        }
                    })
                }
            }
        })
    }

    fun callLanguageApi(locale: String, value: Int) {
        readData(DataStoreKeys.LOGIN_DATA) { loginUser ->
            if (loginUser != null) {
                val _id = Gson().fromJson(loginUser, Login::class.java).id
                val requestBody: MultipartBody.Builder = MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("user_type", USER_TYPE)
                requestBody.addFormDataPart("user_id", "" + _id)
                requestBody.addFormDataPart("language", "/en/" + locale)
                if (networkFailed) {
                    viewModel.notificationUpdate("" + _id, requestBody.build(), value)
                } else {
                    requireContext().callNetworkDialog()
                }
            }
        }
    }


    private fun openDialog(type: Int) {
        val mybuilder = Dialog(requireActivity())
        mybuilder.setContentView(R.layout.dialog_load_html)
        mybuilder.show()
        val window = mybuilder.window
        window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        window.setBackgroundDrawableResource(R.color._00000000)
        val yes = mybuilder.findViewById<AppCompatImageView>(R.id.imageCross)
        val title = mybuilder.findViewById<AppCompatTextView>(R.id.textTitleMain)
        val text = mybuilder.findViewById<AppCompatTextView>(R.id.textTitleText)
        when (type) {
            1 -> title.text = resources.getString(R.string.about_us)
            2 -> title.text = resources.getString(R.string.privacy_policy)
            3 -> title.text = resources.getString(R.string.terms_amp_conditions)
        }
        requireContext().loadHtml(type) {
            text.text = HtmlCompat.fromHtml(this, HtmlCompat.FROM_HTML_MODE_LEGACY)
        }
        yes?.singleClick {
            mybuilder.dismiss()
        }
        viewModel.hide()
    }


    override fun onDestroyView() {
        languageAlert?.let {
            languageAlert!!.cancel()
        }
        _binding = null
        super.onDestroyView()
    }
}