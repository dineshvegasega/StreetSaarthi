package com.streetsaarthi.nasvi.screens.main.settings

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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
import com.streetsaarthi.nasvi.utils.singleClick
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MultipartBody

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
//                                MainActivity.mainActivity.get()?.reloadActivity(dataClass.locale, Main)
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
//                                MainActivity.mainActivity.get()?.reloadActivity(dataClass.locale, Main)
                            }
                        }
                    }
                val recyclerView = dialogView.findViewById<RecyclerView>(R.id.rvList)

                pastBookingAdapter.submitList(viewModel.itemMain)
                recyclerView.adapter = pastBookingAdapter
            }


            readData(DataStoreKeys.LOGIN_DATA) { loginUser ->
                if (loginUser != null) {
                    val noti =  Gson().fromJson(loginUser, Login::class.java).notification
                    switchNotifications.isChecked = if (noti == "Yes") true else false
                }
            }



            switchNotifications.singleClick {
                if (notificationAlert?.isShowing == true) {
                    return@singleClick
                }


                readData(DataStoreKeys.LOGIN_DATA) { loginUser ->
                    var user = Gson().fromJson(loginUser, Login::class.java)
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
                                    requestBody.addFormDataPart("user_id", ""+user.id)
                                    if (user.notification == "Yes") {
                                        requestBody.addFormDataPart("notification", "No")
                                    } else {
                                        requestBody.addFormDataPart("notification", "Yes")
                                    }
                                    if(networkFailed) {
                                        viewModel.notificationUpdate(""+user.id, requestBody.build(), 0)
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
                        switchNotifications.isChecked = if (user.notification == "Yes") true else false
                    }
                }


                viewModel.itemNotificationUpdateResult.value = false
                viewModel.itemNotificationUpdateResult.observe(requireActivity(), Observer {
                    readData(DataStoreKeys.LOGIN_DATA) { loginUser ->
                        if (loginUser != null) {
                            val noti =  Gson().fromJson(loginUser, Login::class.java).notification
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
        }
    }


   fun callLanguageApi(locale: String, value : Int) {
       readData(DataStoreKeys.LOGIN_DATA) { loginUser ->
           if (loginUser != null) {
              val _id = Gson().fromJson(loginUser, Login::class.java).id
               val requestBody: MultipartBody.Builder = MultipartBody.Builder()
                   .setType(MultipartBody.FORM)
                   .addFormDataPart("user_type", USER_TYPE)
               requestBody.addFormDataPart("user_id", ""+_id)
                   requestBody.addFormDataPart("language", "/en/"+locale)
               if(networkFailed) {
                   viewModel.notificationUpdate(""+_id, requestBody.build(), value)
               } else {
                   requireContext().callNetworkDialog()
              }
           }
       }
   }


    override fun onDestroyView() {
        languageAlert?.let {
            languageAlert!!.cancel()
        }
        _binding = null
        super.onDestroyView()
    }
}