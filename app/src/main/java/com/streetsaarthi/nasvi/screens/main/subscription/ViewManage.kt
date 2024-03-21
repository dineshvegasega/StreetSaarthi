package com.streetsaarthi.nasvi.screens.main.subscription

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.streetsaarthi.nasvi.R
import com.streetsaarthi.nasvi.databinding.ViewManageBinding
import com.streetsaarthi.nasvi.datastore.DataStoreKeys
import com.streetsaarthi.nasvi.datastore.DataStoreUtil.readData
import com.streetsaarthi.nasvi.models.ItemCouponLiveList
import com.streetsaarthi.nasvi.models.Login
import com.streetsaarthi.nasvi.screens.mainActivity.MainActivity
import com.streetsaarthi.nasvi.screens.mainActivity.MainActivity.Companion.networkFailed
import com.streetsaarthi.nasvi.utils.callNetworkDialog
import com.streetsaarthi.nasvi.utils.decimal2Digits
import com.streetsaarthi.nasvi.utils.hideKeyboard
import com.streetsaarthi.nasvi.utils.showDropDownDialog
import com.streetsaarthi.nasvi.utils.showSnackBar
import com.streetsaarthi.nasvi.utils.singleClick
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject

@AndroidEntryPoint
class ViewManage : Fragment() {
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
                requireActivity().showDropDownDialog(type = 18){
                    binding.editTextSelectMonthYear.setText(name)
                    viewModel.monthYear = position + 1
                }
            }

            editTextChooseNumber.singleClick {
                requireActivity().showDropDownDialog(type = 19){
                    binding.editTextChooseNumber.setText(name)
                    viewModel.number = position + 1
                }
            }

            btCalculatePrice.singleClick {
                requireActivity().hideKeyboard()
                if (viewModel.number > 0 && viewModel.monthYear > 0) {
                    viewModel.validity = when (viewModel.monthYear) {
                        1 -> if (viewModel.number == 1) "${viewModel.number} " + getString(R.string.month) else "${viewModel.number} " + getString(
                            R.string.months
                        )

                        2 -> if (viewModel.number == 1) "${viewModel.number} " + getString(R.string.year) else "${viewModel.number} " + getString(
                            R.string.years
                        )

                        else -> ""
                    }
                    viewModel.validityMonths = when (viewModel.monthYear) {
                        1 -> viewModel.number
                        2 -> viewModel.number * 12
                        else -> 0
                    }
                    viewModel.validityDays = when (viewModel.monthYear) {
                        1 -> viewModel.number * 30
                        2 -> viewModel.number * 365
                        else -> 0
                    }


                    readData(DataStoreKeys.LOGIN_DATA) { loginUser ->
                        if (loginUser != null) {
                            val data = Gson().fromJson(loginUser, Login::class.java)
                            if (networkFailed) {
                                val obj: JSONObject = JSONObject().apply {
                                    put("state_id", data?.residential_state?.id)
                                    put("no_of_month_year", viewModel.number)
                                    put(
                                        "month_year",
                                        if (viewModel.monthYear == 1) "month" else "year"
                                    )
                                }
                                viewModel.couponLiveList(obj)
                            } else {
                                requireContext().callNetworkDialog()
                            }
                        }
                    }


                    viewModel.couponLiveListCalled.observe(viewLifecycleOwner, Observer {
                        Log.e("TAG", "itAAAA " + it)
                        if (it) {
                            val typeToken = object : TypeToken<List<ItemCouponLiveList>>() {}.type
                            val changeValue =
                                Gson().fromJson<List<ItemCouponLiveList>>(
                                    Gson().toJson(viewModel.itemCouponLiveList.value?.data),
                                    typeToken
                                )
                            if (changeValue.size > 0) {
                                viewModel.couponDiscount = changeValue[0].coupon_discount
                            } else {
                                viewModel.couponDiscount = 0.0
                            }
                        } else {
                            viewModel.couponDiscount = 0.0
                        }


                        viewModel.membershipCost =
                            viewModel.subscription.value?.subscription_cost!! * viewModel.validityMonths
                        viewModel.couponDiscountPrice = (viewModel.membershipCost * viewModel.couponDiscount) / 100
                        viewModel.afterCouponDiscount = viewModel.membershipCost - viewModel.couponDiscountPrice
                        viewModel.gstPrice = (viewModel.afterCouponDiscount * viewModel.gst) / 100
                        viewModel.afterGst = viewModel.afterCouponDiscount + viewModel.gstPrice
                        viewModel.totalCost = viewModel.afterGst

                        update(
                            viewModel.membershipCost,
                            "" + viewModel.validityDays,
                            viewModel.gst,
                            viewModel.gstPrice,
                            viewModel.couponDiscount,
                            viewModel.couponDiscountPrice,
                            viewModel.totalCost,
                        )
                    })


                } else {
                    showSnackBar(getString(R.string.please_select_month_year_and_number))
                }
            }



            btPurchaseSubscription.singleClick {
                readData(DataStoreKeys.LOGIN_DATA) { loginUser ->
                    if (loginUser != null) {
                        val data = Gson().fromJson(loginUser, Login::class.java)
                        if (networkFailed) {
                            val obj: JSONObject = JSONObject().apply {
                                put("state_id", data?.residential_state?.id)
                            }
                            viewModel.purchaseSubscription(obj)
                        } else {
                            requireContext().callNetworkDialog()
                        }
                    }
                }
            }
        }
    }

    //    val mrp: BigDecimal= "0.00".toBigDecimal(),
//    val price: BigDecimal = "0.00".toBigDecimal(),
    private fun update(
        membershipCost: Double = 0.00,
        validity: String,
        gst: Double,
        gstPrice: Double,
        couponDiscount: Double,
        couponDiscountPrice: Double,
        totalCost: Double = 0.00
    ) {
        binding.apply {
            textMembershipCostValue.text = resources.getString(
                R.string.rupees, membershipCost.decimal2Digits()
            )
            textValidityValue.text = resources.getString(R.string.days, "${validity}")
//            textGSTValue.text = "${gst} %"
//            textCouponDiscountValue.text = "${couponDiscount} %"

            textCouponDiscountTxt.text = resources.getString(R.string.coupon_discount, "${couponDiscount}%")
            textCouponDiscountValue.text = resources.getString(
                R.string.rupees, couponDiscountPrice.decimal2Digits()
            )
            textGSTTxt.text = resources.getString(R.string.gst, "${gst}%")
            textGSTValue.text = resources.getString(
                R.string.rupees, gstPrice.decimal2Digits()
            )




            textTotalCostValue.text =
                resources.getString(R.string.rupees, totalCost.decimal2Digits())
        }
    }


    private fun showDropDownMonthYearDialog() {
        val list = resources.getStringArray(R.array.month_year_array)
        MaterialAlertDialogBuilder(requireContext(), R.style.DropdownDialogTheme)
            .setTitle(resources.getString(R.string.select_month_year))
            .setItems(list) { _, which ->
                binding.editTextSelectMonthYear.setText(list[which])
                viewModel.monthYear = which + 1
            }.show()
    }


    private fun showDropDownChooseNumberDialog() {
        val list = resources.getStringArray(R.array.numbers_array)
        MaterialAlertDialogBuilder(requireContext(), R.style.DropdownDialogTheme)
            .setTitle(resources.getString(R.string.choose_number))
            .setItems(list) { _, which ->
                binding.editTextChooseNumber.setText(list[which])
                viewModel.number = which + 1
            }.show()
    }


}