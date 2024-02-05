package com.streetsaarthi.nasvi.screens.onboarding.start

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.demo.genericAdapter.GenericAdapter
import com.streetsaarthi.nasvi.screens.onboarding.networking.Screen
import com.streetsaarthi.nasvi.screens.onboarding.networking.Start
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.streetsaarthi.nasvi.screens.mainActivity.MainActivity
import com.streetsaarthi.nasvi.databinding.StartBinding
import com.streetsaarthi.nasvi.R
import com.streetsaarthi.nasvi.databinding.ItemLanguageStartBinding
import com.streetsaarthi.nasvi.utils.LocaleHelper
import com.streetsaarthi.nasvi.utils.singleClick
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class Start : Fragment() {

    private var _binding: StartBinding? = null
    private val binding get() = _binding!!
    private val viewModel: StartVM by viewModels()

    @get:JvmName("getAdapterContext")
    var context : Context ?= null

    @get:JvmName("getAdapterContext")
    lateinit var resources : Resources

    var languageAlert : BottomSheetDialog?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = requireContext()
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = StartBinding.inflate(inflater)
        Log.e("TAG", "onCreateView")
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.e("TAG", "onViewCreated")
        //val current = resources.configuration.locale

        MainActivity.mainActivity.get()?.callFragment(0)

        binding.apply {
            btSignIn.singleClick {
                    view.findNavController().navigate(R.id.action_start_to_walkThrough)
            }
            btSignIn.setText(R.string.explore_app)
            btSignIn.setEnabled(false)

            viewModel.appLanguage.observe(viewLifecycleOwner, Observer {
                if(it == ""){
                    btSignIn.setEnabled(false)
                }else{
                    btLanguage.setText(it)
                    btSignIn.setEnabled(true)
                    btSignIn.setBackgroundTintList(
                        ColorStateList.valueOf(
                            ResourcesCompat.getColor(
                                getResources(), R.color._E79D46, null)))
                }
            })

            btLanguage.singleClick {
                if(languageAlert?.isShowing == true) {
                    return@singleClick
                }
                val dialogView: View = LayoutInflater.from(requireContext())
                    .inflate(R.layout.dialog_bottom_your_booking2, null)
                languageAlert = BottomSheetDialog(requireContext())
                languageAlert?.setContentView(dialogView)
                languageAlert?.let {
                    languageAlert?.show()
                }
                val window=languageAlert?.window
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
                        binding.btLanguage.singleClick {
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

                        binding.btImage.singleClick {
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



    override fun onDestroyView() {
        languageAlert?.let {
            languageAlert!!.cancel()
        }
        _binding = null
        super.onDestroyView()
        Log.e("TAG", "onDestroyView")
    }

}