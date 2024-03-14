package com.streetsaarthi.nasvi.screens.main.subscription

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.streetsaarthi.nasvi.R
import com.streetsaarthi.nasvi.databinding.ViewManageBinding
import com.streetsaarthi.nasvi.screens.mainActivity.MainActivity
import com.streetsaarthi.nasvi.utils.hideKeyboard
import com.streetsaarthi.nasvi.utils.singleClick
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ViewManage : Fragment(){
    private val viewModel: SubscriptionVM by activityViewModels()
    private var _binding: ViewManageBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ViewManageBinding.inflate(inflater, container, false)
        return binding.root
    }


    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        MainActivity.mainActivity.get()?.callFragment(1)

        binding.apply {
            editTextSelectMonthYear.singleClick {
                requireActivity().hideKeyboard()
                showDropDownMonthYearDialog()
            }

            editTextChooseNumber.singleClick {
                requireActivity().hideKeyboard()
                showDropDownChooseNumberDialog()
            }
        }
    }


    private fun showDropDownMonthYearDialog() {
        val list=resources.getStringArray(R.array.month_year_array)
        MaterialAlertDialogBuilder(requireContext(), R.style.DropdownDialogTheme)
            .setTitle(resources.getString(R.string.select_month_year))
            .setItems(list) {_,which->
                binding.editTextSelectMonthYear.setText(list[which])
//                when(which){
//                    0-> viewModel.data.gender = "Male"
//                    1-> viewModel.data.gender = "Female"
//                    2-> viewModel.data.gender = "Other"
//                }
            }.show()
    }


    private fun showDropDownChooseNumberDialog() {
        val list=resources.getStringArray(R.array.numbers_array)
        MaterialAlertDialogBuilder(requireContext(), R.style.DropdownDialogTheme)
            .setTitle(resources.getString(R.string.choose_number))
            .setItems(list) {_,which->
                binding.editTextChooseNumber.setText(list[which])
//                when(which){
//                    0-> viewModel.data.gender = "Male"
//                    1-> viewModel.data.gender = "Female"
//                    2-> viewModel.data.gender = "Other"
//                }
            }.show()
    }

}