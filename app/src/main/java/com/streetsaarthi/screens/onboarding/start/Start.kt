package com.streetsaarthi.screens.onboarding.start

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.demo.genericAdapter.GenericAdapter
import com.demo.networking.Screen
import com.demo.networking.Start
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.streetsaarthi.MainActivity
import com.streetsaarthi.databinding.StartBinding
import com.streetsaarthi.R
import com.streetsaarthi.databinding.ItemLanguageStartBinding
import com.streetsaarthi.utils.LocaleHelper

import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale


@AndroidEntryPoint
class Start : Fragment() {

    private var _binding: StartBinding? = null
    private val binding get() = _binding!!
    private val viewModel: StartVM by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = StartBinding.inflate(inflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //val current = resources.configuration.locale




        binding.apply {
            btSignIn.setOnClickListener {
                requireView().findNavController().navigate(R.id.action_start_to_walkThrough)
            }

            btSignIn.setEnabled(false)
            if (MainActivity.context.get()!!.getString(R.string.englishVal) == ""){
                btSignIn.setEnabled(false)
            } else if (MainActivity.context.get()!!.getString(R.string.englishVal) == ""+viewModel.locale){
                btLanguage.setText(R.string.english)
                btSignIn.setEnabled(true)
                btSignIn.setBackgroundTintList(
                    ColorStateList.valueOf(
                        ResourcesCompat.getColor(
                            getResources(), R.color._E79D46, null)))
            } else if (MainActivity.context.get()!!.getString(R.string.bengaliVal) == ""+viewModel.locale){
                btLanguage.setText(R.string.bengali)
                btSignIn.setEnabled(true)
                btSignIn.setBackgroundTintList(
                    ColorStateList.valueOf(
                        ResourcesCompat.getColor(
                            getResources(), R.color._E79D46, null)))
            } else if (MainActivity.context.get()!!.getString(R.string.gujaratiVal) == ""+viewModel.locale){
                btLanguage.setText(R.string.gujarati)
                btSignIn.setEnabled(true)
                btSignIn.setBackgroundTintList(
                    ColorStateList.valueOf(
                        ResourcesCompat.getColor(
                            getResources(), R.color._E79D46, null)))
            } else if (MainActivity.context.get()!!.getString(R.string.hindiVal) == ""+viewModel.locale){
                btLanguage.setText(R.string.hindi)
                btSignIn.setEnabled(true)
                btSignIn.setBackgroundTintList(
                    ColorStateList.valueOf(
                        ResourcesCompat.getColor(
                            getResources(), R.color._E79D46, null)))
            } else if (MainActivity.context.get()!!.getString(R.string.kannadaVal) == ""+viewModel.locale){
                btLanguage.setText(R.string.kannada)
                btSignIn.setEnabled(true)
                btSignIn.setBackgroundTintList(
                    ColorStateList.valueOf(
                        ResourcesCompat.getColor(
                            getResources(), R.color._E79D46, null)))
            } else if (MainActivity.context.get()!!.getString(R.string.malayalamVal) == ""+viewModel.locale){
                btLanguage.setText(R.string.malayalam)
                btSignIn.setEnabled(true)
                btSignIn.setBackgroundTintList(
                    ColorStateList.valueOf(
                        ResourcesCompat.getColor(
                            getResources(), R.color._E79D46, null)))
            } else if (MainActivity.context.get()!!.getString(R.string.marathiVal) == ""+viewModel.locale){
                btLanguage.setText(R.string.marathi)
                btSignIn.setEnabled(true)
                btSignIn.setBackgroundTintList(
                    ColorStateList.valueOf(
                        ResourcesCompat.getColor(
                            getResources(), R.color._E79D46, null)))
            } else if (MainActivity.context.get()!!.getString(R.string.punjabiVal) == ""+viewModel.locale){
                btLanguage.setText(R.string.punjabi)
                btSignIn.setEnabled(true)
                btSignIn.setBackgroundTintList(
                    ColorStateList.valueOf(
                        ResourcesCompat.getColor(
                            getResources(), R.color._E79D46, null)))
            } else if (MainActivity.context.get()!!.getString(R.string.tamilVal) == ""+viewModel.locale){
                btLanguage.setText(R.string.tamil)
                btSignIn.setEnabled(true)
                btSignIn.setBackgroundTintList(
                    ColorStateList.valueOf(
                        ResourcesCompat.getColor(
                            getResources(), R.color._E79D46, null)))
            }



            btLanguage.setOnClickListener {
                val dialogView: View = LayoutInflater.from(requireContext())
                    .inflate(R.layout.dialog_bottom_your_booking2, null)
                val dialog = BottomSheetDialog(requireContext())
                dialog.setContentView(dialogView)
                dialog.show()
                val window=dialog.window
                window!!.setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                window!!.setBackgroundDrawableResource(android.R.color.transparent)

                val pastBookingAdapter = object : GenericAdapter<ItemLanguageStartBinding, StartVM.Item>() {
                    override fun onCreateView(
                        inflater: LayoutInflater,
                        parent: ViewGroup,
                        viewType: Int
                    ) = ItemLanguageStartBinding.inflate(inflater, parent, false)
                    @SuppressLint("NotifyDataSetChanged")
                    override fun onBindHolder(
                        binding: ItemLanguageStartBinding,
                        dataClass: StartVM.Item,
                        position: Int
                    ) {
                        binding.btImage.setImageDrawable(ContextCompat.getDrawable(binding.root.context, if (dataClass.isSelected == true) R.drawable.radio_sec_filled else R.drawable.radio_sec_empty));
                        binding.btLanguage.text = dataClass.name
                        binding.btLanguage.setOnClickListener {
                            Log.e("TAG" , "asdsfs "+dataClass.name)

                            val list = currentList
                            list.forEach {
                                it.isSelected = dataClass == it
                            }
                            notifyDataSetChanged()

                            LocaleHelper.setLocale(requireContext(),dataClass.locale)
                            val refresh = Intent(Intent(requireActivity(), MainActivity::class.java))
                            refresh.putExtra(Screen, Start)
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
               // fragmentManager!!.beginTransaction().detach(this@Start).commitNow();

            }
        }


    }






}