package com.streetsaarthi.screens.main.settings

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.demo.genericAdapter.GenericAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.streetsaarthi.R
import com.streetsaarthi.databinding.ItemLanguageStartBinding
import com.streetsaarthi.databinding.SettingsBinding
import com.streetsaarthi.models.Item
import com.streetsaarthi.screens.mainActivity.MainActivity
import com.streetsaarthi.screens.onboarding.networking.Main
import com.streetsaarthi.screens.onboarding.networking.Screen
import com.streetsaarthi.screens.onboarding.networking.Start
import com.streetsaarthi.screens.onboarding.start.StartVM
import com.streetsaarthi.utils.LocaleHelper
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Settings : Fragment() {
    private val viewModel: SettingsVM by viewModels()
    private var _binding: SettingsBinding? = null
    private val binding get() = _binding!!

    var itemMain : ArrayList<Item> ?= ArrayList()
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
        binding.apply {
            inclideHeaderSearch.textHeaderTxt.text = getString(R.string.settings)
            inclideHeaderSearch.editTextSearch.visibility = View.GONE

            if (MainActivity.context.get()!!.getString(R.string.englishVal) == ""){
            } else if (MainActivity.context.get()!!.getString(R.string.englishVal) == ""+viewModel.locale){
                editTextSelectLanguage.setText(R.string.english)
            } else if (MainActivity.context.get()!!.getString(R.string.bengaliVal) == ""+viewModel.locale){
                editTextSelectLanguage.setText(R.string.bengali)
            } else if (MainActivity.context.get()!!.getString(R.string.gujaratiVal) == ""+viewModel.locale){
                editTextSelectLanguage.setText(R.string.gujarati)
            } else if (MainActivity.context.get()!!.getString(R.string.hindiVal) == ""+viewModel.locale){
                editTextSelectLanguage.setText(R.string.hindi)
            } else if (MainActivity.context.get()!!.getString(R.string.kannadaVal) == ""+viewModel.locale){
                editTextSelectLanguage.setText(R.string.kannada)
            } else if (MainActivity.context.get()!!.getString(R.string.malayalamVal) == ""+viewModel.locale){
                editTextSelectLanguage.setText(R.string.malayalam)
            } else if (MainActivity.context.get()!!.getString(R.string.marathiVal) == ""+viewModel.locale){
                editTextSelectLanguage.setText(R.string.marathi)
            } else if (MainActivity.context.get()!!.getString(R.string.punjabiVal) == ""+viewModel.locale){
                editTextSelectLanguage.setText(R.string.punjabi)
            } else if (MainActivity.context.get()!!.getString(R.string.tamilVal) == ""+viewModel.locale){
                editTextSelectLanguage.setText(R.string.tamil)
            } else if (MainActivity.context.get()!!.getString(R.string.teluguVal) == ""+viewModel.locale){
                editTextSelectLanguage.setText(R.string.telugu)
            } else if (MainActivity.context.get()!!.getString(R.string.urduVal) == ""+viewModel.locale){
                editTextSelectLanguage.setText(R.string.urdu)
            }



            editTextSelectLanguage.setOnClickListener {
                val dialogView: View = LayoutInflater.from(requireContext())
                    .inflate(R.layout.dialog_bottom_your_booking2, null)
                val dialog = BottomSheetDialog(requireContext())
                dialog.setContentView(dialogView)
                dialog.show()
                val window = dialog.window
                window!!.setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                window!!.setBackgroundDrawableResource(android.R.color.transparent)

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
                                val refresh =
                                    Intent(Intent(requireActivity(), MainActivity::class.java))
                                refresh.putExtra(Screen, Main)
                                refresh.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                                startActivity(refresh)
                                MainActivity.activity.get()!!.finish()
                                MainActivity.activity.get()!!.finishAffinity()
                            }

                            binding.btImage.setOnClickListener {
                                Log.e("TAG", "asdsfs " + dataClass.name)

                                val list = currentList
                                list.forEach {
                                    it.isSelected = dataClass == it
                                }
                                notifyDataSetChanged()

                                LocaleHelper.setLocale(requireContext(), dataClass.locale)
                                val refresh =
                                    Intent(Intent(requireActivity(), MainActivity::class.java))
                                refresh.putExtra(Screen, Main)
                                refresh.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                                startActivity(refresh)
                                MainActivity.activity.get()!!.finish()
                                MainActivity.activity.get()!!.finishAffinity()
                            }
                        }
                    }
                val recyclerView = dialogView.findViewById<RecyclerView>(R.id.rvList)

                pastBookingAdapter.submitList(viewModel.itemMain)
                recyclerView.adapter = pastBookingAdapter
            }

        }
    }


    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}