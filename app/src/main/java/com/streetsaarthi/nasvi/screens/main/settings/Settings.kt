package com.streetsaarthi.nasvi.screens.main.settings

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.demo.genericAdapter.GenericAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.streetsaarthi.nasvi.R
import com.streetsaarthi.nasvi.databinding.ItemLanguageStartBinding
import com.streetsaarthi.nasvi.databinding.SettingsBinding
import com.streetsaarthi.nasvi.models.Item
import com.streetsaarthi.nasvi.screens.mainActivity.MainActivity
import com.streetsaarthi.nasvi.screens.onboarding.networking.Main
import com.streetsaarthi.nasvi.screens.onboarding.networking.Screen
import com.streetsaarthi.nasvi.screens.onboarding.networking.Start
import com.streetsaarthi.nasvi.screens.onboarding.start.StartVM
import com.streetsaarthi.nasvi.utils.LocaleHelper
import com.streetsaarthi.nasvi.utils.OtpTimer
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Settings : Fragment() {
    private val viewModel: SettingsVM by viewModels()
    private var _binding: SettingsBinding? = null
    private val binding get() = _binding!!

    var itemMain : ArrayList<Item> ?= ArrayList()

    var languageAlert : BottomSheetDialog?= null

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
        MainActivity.mainActivity.get()?.callFragment(0)
        binding.apply {
            inclideHeaderSearch.textHeaderTxt.text = getString(R.string.settings)
            inclideHeaderSearch.editTextSearch.visibility = View.GONE

            viewModel.appLanguage.observe(viewLifecycleOwner, Observer {
                editTextSelectLanguage.setText(it)
            })



            editTextSelectLanguage.setOnClickListener {
                if(languageAlert?.isShowing == true) {
                    return@setOnClickListener
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
                            binding.btLanguage.setOnClickListener {
                                Log.e("TAG", "asdsfs " + dataClass.name)

                                val list = currentList
                                list.forEach {
                                    it.isSelected = dataClass == it
                                }
                                notifyDataSetChanged()

                                LocaleHelper.setLocale(requireContext(), dataClass.locale)
//                                Handler(Looper.getMainLooper()).postDelayed({
                                    val refresh =
                                        Intent(Intent(requireActivity(), MainActivity::class.java))
                                    refresh.putExtra(Screen, Main)
                                    refresh.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                                    startActivity(refresh)
                                    MainActivity.activity.get()!!.finish()
                                    MainActivity.activity.get()!!.finishAffinity()
//                                },100)

                            }

                            binding.btImage.setOnClickListener {
                                Log.e("TAG", "asdsfs " + dataClass.name)

                                val list = currentList
                                list.forEach {
                                    it.isSelected = dataClass == it
                                }
                                notifyDataSetChanged()

                                LocaleHelper.setLocale(requireContext(), dataClass.locale)
//                                Handler(Looper.getMainLooper()).postDelayed({
                                    val refresh =
                                        Intent(Intent(requireActivity(), MainActivity::class.java))
                                    refresh.putExtra(Screen, Main)
                                    refresh.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                                    startActivity(refresh)
                                    MainActivity.activity.get()!!.finish()
                                    MainActivity.activity.get()!!.finishAffinity()
//                                },100)
                            }
                        }
                    }
                val recyclerView = dialogView.findViewById<RecyclerView>(R.id.rvList)

                pastBookingAdapter.submitList(viewModel.itemMain)
                recyclerView.adapter = pastBookingAdapter
            }


            btLogout.setOnClickListener {
                MainActivity.mainActivity.get()!!.callLogoutDialog()
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