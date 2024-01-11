package com.streetsaarthi.nasvi.screens.main.profiles

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import com.kochia.customer.utils.hideKeyboard
import com.squareup.picasso.Picasso
import com.streetsaarthi.nasvi.R
import com.streetsaarthi.nasvi.databinding.OtherDetailsBinding
import com.streetsaarthi.nasvi.datastore.DataStoreKeys
import com.streetsaarthi.nasvi.datastore.DataStoreUtil
import com.streetsaarthi.nasvi.models.Item
import com.streetsaarthi.nasvi.models.login.Login
import com.streetsaarthi.nasvi.screens.mainActivity.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OtherDetails : Fragment() {
    private val viewModel: ProfilesVM by activityViewModels()
    private var _binding: OtherDetailsBinding? = null
    private val binding get() = _binding!!

    var itemMain: ArrayList<Item>? = ArrayList()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = OtherDetailsBinding.inflate(inflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            DataStoreUtil.readData(DataStoreKeys.LOGIN_DATA) { loginUser ->
                if (loginUser != null) {
                    var data = Gson().fromJson(loginUser, Login::class.java)
                    editTextTypeofMarketPlace.setText("${data.type_of_marketplace}")
                    editTextTypeofMarketPlaceEnter.setText("${data.marketpalce_others}")
                    editTextTypeofVending.setText("${data.type_of_vending}")
                    editTextTypeofVendingEnter.setText("${data.vending_others}")
                    editTextTotalYearsofVending.setText("${data.total_years_of_business}")


                    Picasso.get().load(
                        Gson().fromJson(loginUser, Login::class.java).profile_image_name.url
                    ).placeholder(R.drawable.no_image_modified).into(ivImagePassportsizeImage)

                    Picasso.get().load(
                        Gson().fromJson(loginUser, Login::class.java).shop_image.url
                    ).placeholder(R.drawable.no_image_modified).into(ivImageShopimage)

                    Picasso.get().load(
                        Gson().fromJson(loginUser, Login::class.java).identity_image_name.url
                    ).placeholder(R.drawable.no_image_modified).into(ivImageIdentityImage)

                    Picasso.get().load(
                        Gson().fromJson(loginUser, Login::class.java).cov_image.url
                    ).placeholder(R.drawable.no_image_modified).into(ivImageCOV)

                    Picasso.get().load(
                        Gson().fromJson(loginUser, Login::class.java).membership_image.url
                    ).placeholder(R.drawable.no_image_modified).into(ivImageMembershipId)

                    Picasso.get().load(
                        Gson().fromJson(loginUser, Login::class.java).lor_image.url
                    ).placeholder(R.drawable.no_image_modified).into(ivImageLOR)

                }
            }

            viewModel.marketplace(view)
            editTextTypeofMarketPlace.setOnClickListener {
                requireActivity().hideKeyboard()
                showDropDownMarketPlaceDialog()
            }

            viewModel.vending(view)
            editTextTypeofVending.setOnClickListener {
                requireActivity().hideKeyboard()
                showDropDownVendingDialog()
            }

            editTextTotalYearsofVending.setOnClickListener {
                requireActivity().hideKeyboard()
                showDropDownYearsDialog()
            }

        }
    }



    private fun showDropDownMarketPlaceDialog() {
        var index = 0
        val list = arrayOfNulls<String>(viewModel.itemMarketplace.size)
        for (value in viewModel.itemMarketplace) {
            list[index] = value.name
            index++
        }
        MaterialAlertDialogBuilder(requireView().context, R.style.DialogTheme)
            .setTitle(resources.getString(R.string.type_of_market_place))
            .setItems(list) { _, which ->
                binding.editTextTypeofMarketPlace.setText(list[which])
                viewModel.marketplaceId = viewModel.itemMarketplace[which].marketplace_id
                if (viewModel.marketplaceId == 7) {
                    binding.editTextTypeofMarketPlaceEnter.visibility = View.VISIBLE
                } else {
                    binding.editTextTypeofMarketPlaceEnter.visibility = View.GONE
                }
            }.show()
    }

    private fun showDropDownVendingDialog() {
        var index = 0
        val list = arrayOfNulls<String>(viewModel.itemVending.size)
        for (value in viewModel.itemVending) {
            list[index] = value.name
            index++
        }
        MaterialAlertDialogBuilder(requireView().context, R.style.DialogTheme)
            .setTitle(resources.getString(R.string.type_of_vending))
            .setItems(list) { _, which ->
                binding.editTextTypeofVending.setText(list[which])
                viewModel.vendingId = viewModel.itemVending[which].vending_id
                if (viewModel.vendingId == 11) {
                    binding.editTextTypeofVendingEnter.visibility = View.VISIBLE
                } else {
                    binding.editTextTypeofVendingEnter.visibility = View.GONE
                }
            }.show()
    }


    private fun showDropDownYearsDialog() {
        val list = resources.getStringArray(R.array.years_array)
        MaterialAlertDialogBuilder(requireContext(), R.style.DialogTheme)
            .setTitle(resources.getString(R.string.total_years_of_vending))
            .setItems(list) { _, which ->
                binding.editTextTotalYearsofVending.setText(list[which])
//                editProfileVM.gender.value = list[which]
            }.show()
    }




    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
