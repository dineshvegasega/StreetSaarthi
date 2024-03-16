package com.streetsaarthi.nasvi.screens.main.subscription

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
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
import com.streetsaarthi.nasvi.utils.showSnackBar
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

            btCalculatePrice.singleClick {
                requireActivity().hideKeyboard()
                if(viewModel.number > 0 && viewModel.monthYear > 0){
                    viewModel.validity = when(viewModel.monthYear) {
                        1 ->  if(viewModel.number == 1) "${viewModel.number} "+getString(R.string.month) else "${viewModel.number} "+getString(R.string.months)
                        2 ->  if(viewModel.number == 1) "${viewModel.number} "+getString(R.string.year) else "${viewModel.number} "+getString(R.string.years)
                        else -> ""
                    }
                    viewModel.validityMonths = when(viewModel.monthYear) {
                        1 -> viewModel.number
                        2 -> viewModel.number * 12
                        else -> 0
                    }
                    viewModel.membershipCost = viewModel.subscription.value?.subscription_cost!! * viewModel.validityMonths
                    viewModel.afterGst = viewModel.membershipCost + (viewModel.membershipCost * viewModel.gst) / 100
                    viewModel.totalCost = viewModel.afterGst - ((viewModel.afterGst * viewModel.couponDiscount) / 100)

                    update(
                        viewModel.membershipCost,
                        viewModel.validity,
                        viewModel.gst,
                        viewModel.couponDiscount,
                        viewModel.totalCost,
                    )
                } else {
                    showSnackBar(getString(R.string.please_select_month_year_and_number))
                }




            }
        }
    }

    private fun update(
        membershipCost: Double,
        validity: String,
        gst: Double,
        couponDiscount: Double,
        totalCost: Double
    ) {
       binding.apply {
           textMembershipCostValue.text = "${membershipCost}"
           textValidityValue.text = validity
           textGSTValue.text = "${gst}"
           textCouponDiscountValue.text = "${couponDiscount}"
           textTotalCostValue.text = "${totalCost}"
       }
    }


    private fun showDropDownMonthYearDialog() {
        val list=resources.getStringArray(R.array.month_year_array)
        MaterialAlertDialogBuilder(requireContext(), R.style.DropdownDialogTheme)
            .setTitle(resources.getString(R.string.select_month_year))
            .setItems(list) {_,which->
                binding.editTextSelectMonthYear.setText(list[which])
                viewModel.monthYear = which+1
            }.show()
    }


    private fun showDropDownChooseNumberDialog() {
        val list=resources.getStringArray(R.array.numbers_array)
        MaterialAlertDialogBuilder(requireContext(), R.style.DropdownDialogTheme)
            .setTitle(resources.getString(R.string.choose_number))
            .setItems(list) {_,which->
                binding.editTextChooseNumber.setText(list[which])
                viewModel.number = which+1
            }.show()
    }

}