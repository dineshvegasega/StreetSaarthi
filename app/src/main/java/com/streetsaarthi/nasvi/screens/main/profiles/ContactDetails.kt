package com.streetsaarthi.nasvi.screens.main.profiles

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import com.kochia.customer.utils.hideKeyboard
import com.streetsaarthi.nasvi.R
import com.streetsaarthi.nasvi.databinding.ContactDetailsBinding
import com.streetsaarthi.nasvi.datastore.DataStoreKeys
import com.streetsaarthi.nasvi.datastore.DataStoreUtil
import com.streetsaarthi.nasvi.models.Item
import com.streetsaarthi.nasvi.models.login.Login
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject

@AndroidEntryPoint
class ContactDetails : Fragment() {
    private val viewModel: ProfilesVM by activityViewModels()
    private var _binding: ContactDetailsBinding? = null
    private val binding get() = _binding!!

    var itemMain: ArrayList<Item>? = ArrayList()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ContactDetailsBinding.inflate(inflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            DataStoreUtil.readData(DataStoreKeys.LOGIN_DATA) { loginUser ->
                if (loginUser != null) {
                    var data = Gson().fromJson(loginUser, Login::class.java)
                    editTextSelectState.setText("${data.residential_state.name}")
                    editTextSelectDistrict.setText("${data.residential_district.name}")
                    editTextMunicipalityPanchayat.setText("${data.residential_municipality_panchayat.name}")
                    editTextSelectPincode.setText("${data.residential_pincode.pincode}")
                    editTextAddress.setText("${data.residential_address}")

                    viewModel.data.current_state = ""+data.residential_state.id
                    viewModel.data.current_district = ""+data.residential_district.id
                    viewModel.data.municipality_panchayat_current = ""+data.residential_municipality_panchayat.id
                    viewModel.data.current_pincode = ""+data.residential_pincode.pincode
                    viewModel.data.current_address = ""+data.residential_address


                    editTextVendingSelectState.setText("${data.vending_state.name}")
                    editTextVendingSelectDistrict.setText("${data.vending_district.name}")
                    editTextVendingMunicipalityPanchayat.setText("${data.vending_municipality_panchayat.name}")
                    editTextVendingSelectPincode.setText("${data.vending_pincode.pincode}")
                    editTextVendingAddress.setText("${data.vending_address}")

                    viewModel.data.vending_state = ""+data.vending_state.id
                    viewModel.data.vending_district = ""+data.vending_district.id
                    viewModel.data.vending_municipality_panchayat = ""+data.vending_municipality_panchayat.id
                    viewModel.data.vending_pincode = ""+data.vending_pincode.pincode
                    viewModel.data.vending_address = ""+data.vending_address
                }
            }



            btnAddtoCart.setOnClickListener {
                viewModel.data.current_address = ""+editTextAddress.text.toString()
                viewModel.data.vending_address = ""+editTextVendingAddress.text.toString()

                Log.e("TAG", "viewModel2.data2 "+ viewModel.data.toString())
            }




            viewModel.state(view)
            editTextSelectState.setOnClickListener {
                requireActivity().hideKeyboard()
                showDropDownStateDialog()
            }

            editTextSelectDistrict.setOnClickListener {
                requireActivity().hideKeyboard()
                showDropDownDistrictDialog()
            }

            editTextMunicipalityPanchayat.setOnClickListener {
                requireActivity().hideKeyboard()
                showDropDownPanchayatDialog()
            }

            editTextSelectPincode.setOnClickListener {
                requireActivity().hideKeyboard()
                showDropDownPincodeDialog()
            }




            viewModel.stateCurrent(view)
            editTextVendingSelectState.setOnClickListener {
                requireActivity().hideKeyboard()
                showDropDownVendingStateDialog()
            }

            editTextVendingSelectDistrict.setOnClickListener {
                requireActivity().hideKeyboard()
                showDropDownVendingDistrictDialog()
            }

            editTextVendingMunicipalityPanchayat.setOnClickListener {
                requireActivity().hideKeyboard()
                showDropDownVendingPanchayatDialog()
            }

            editTextVendingSelectPincode.setOnClickListener {
                requireActivity().hideKeyboard()
                showDropDownVendingPincodeDialog()
            }

        }
    }




    private fun showDropDownStateDialog() {
        var index = 0
        val list = arrayOfNulls<String>(viewModel.itemState.size)
        for (value in viewModel.itemState) {
            list[index] = value.name
            index++
        }
        MaterialAlertDialogBuilder(requireView().context, R.style.DialogTheme)
            .setTitle(resources.getString(R.string.select_state))
            .setItems(list) {_,which->
                binding.editTextSelectState.setText(list[which])
                viewModel.stateId =  viewModel.itemState[which].id
                view?.let { viewModel.district(it, viewModel.stateId) }
                view?.let { viewModel.panchayat(it, viewModel.stateId) }
                viewModel.data.current_state = ""+viewModel.stateId
            }.show()
    }


    private fun showDropDownDistrictDialog() {
        var index = 0
        val list = arrayOfNulls<String>(viewModel.itemDistrict.size)
        for (value in viewModel.itemDistrict) {
            list[index] = value.name
            index++
        }
        MaterialAlertDialogBuilder(requireView().context, R.style.DialogTheme)
            .setTitle(resources.getString(R.string.select_district))
            .setItems(list) {_,which->
                binding.editTextSelectDistrict.setText(list[which])
                viewModel.districtId =  viewModel.itemDistrict[which].id
                view?.let { viewModel.pincode(it, viewModel.districtId) }
                viewModel.data.current_district = ""+viewModel.districtId
            }.show()
    }


    private fun showDropDownPanchayatDialog() {
        var index = 0
        val list = arrayOfNulls<String>(viewModel.itemPanchayat.size)
        for (value in viewModel.itemPanchayat) {
            list[index] = value.name
            index++
        }
        MaterialAlertDialogBuilder(requireView().context, R.style.DialogTheme)
            .setTitle(resources.getString(R.string.municipality_panchayat))
            .setItems(list) {_,which->
                binding.editTextMunicipalityPanchayat.setText(list[which])
                viewModel.panchayatId =  viewModel.itemPanchayat[which].id
                viewModel.data.municipality_panchayat_current = ""+viewModel.panchayatId
            }.show()
    }


    private fun showDropDownPincodeDialog() {
        var index = 0
        val list = arrayOfNulls<String>(viewModel.itemPincode.size)
        for (value in viewModel.itemPincode) {
            list[index] = value.pincode
            index++
        }
        MaterialAlertDialogBuilder(requireView().context, R.style.DialogTheme)
            .setTitle(resources.getString(R.string.select_pincode))
            .setItems(list) {_,which->
                binding.editTextSelectPincode.setText(list[which])
//                viewModel.pincodeId =  viewModel.itemPincode[which].id
                viewModel.pincodeId = binding.editTextSelectPincode.text.toString().toInt()
                viewModel.data.current_pincode = ""+viewModel.pincodeId
            }.show()
    }





    private fun showDropDownVendingStateDialog() {
        var index = 0
        val list = arrayOfNulls<String>(viewModel.itemStateCurrent.size)
        for (value in viewModel.itemStateCurrent) {
            list[index] = value.name
            index++
        }
        MaterialAlertDialogBuilder(requireView().context, R.style.DialogTheme)
            .setTitle(resources.getString(R.string.select_state))
            .setItems(list) {_,which->
                binding.editTextVendingSelectState.setText(list[which])
                viewModel.stateIdCurrent =  viewModel.itemStateCurrent[which].id
                view?.let { viewModel.districtCurrent(it, viewModel.stateIdCurrent) }
                view?.let { viewModel.panchayatCurrent(it, viewModel.stateIdCurrent) }
                view?.let { viewModel.localOrganisation(it, JSONObject().apply {
                    put("state_id", viewModel.stateIdCurrent)
                })}
                viewModel.data.vending_state = ""+viewModel.stateIdCurrent
            }.show()
    }


    private fun showDropDownVendingDistrictDialog() {
        var index = 0
        val list = arrayOfNulls<String>(viewModel.itemDistrictCurrent.size)
        for (value in viewModel.itemDistrictCurrent) {
            list[index] = value.name
            index++
        }
        MaterialAlertDialogBuilder(requireView().context, R.style.DialogTheme)
            .setTitle(resources.getString(R.string.select_district))
            .setItems(list) {_,which->
                binding.editTextVendingSelectDistrict.setText(list[which])
                viewModel.districtIdCurrent =  viewModel.itemDistrictCurrent[which].id
                view?.let { viewModel.pincodeCurrent(it, viewModel.districtIdCurrent) }
                viewModel.data.vending_district = ""+viewModel.districtIdCurrent
            }.show()
    }


    private fun showDropDownVendingPanchayatDialog() {
        var index = 0
        val list = arrayOfNulls<String>(viewModel.itemPanchayatCurrent.size)
        for (value in viewModel.itemPanchayatCurrent) {
            list[index] = value.name
            index++
        }
        MaterialAlertDialogBuilder(requireView().context, R.style.DialogTheme)
            .setTitle(resources.getString(R.string.municipality_panchayat))
            .setItems(list) {_,which->
                binding.editTextVendingMunicipalityPanchayat.setText(list[which])
                viewModel.panchayatIdCurrent =  viewModel.itemPanchayatCurrent[which].id
                viewModel.data.vending_municipality_panchayat = ""+viewModel.panchayatIdCurrent
            }.show()
    }


    private fun showDropDownVendingPincodeDialog() {
        var index = 0
        val list = arrayOfNulls<String>(viewModel.itemPincodeCurrent.size)
        for (value in viewModel.itemPincodeCurrent) {
            list[index] = value.pincode
            index++
        }
        MaterialAlertDialogBuilder(requireView().context, R.style.DialogTheme)
            .setTitle(resources.getString(R.string.select_pincode))
            .setItems(list) {_,which->
                binding.editTextVendingSelectPincode.setText(list[which])
//                viewModel.pincodeId =  viewModel.itemPincode[which].id
                viewModel.pincodeIdCurrent = binding.editTextVendingSelectPincode.text.toString().toInt()
                viewModel.data.vending_pincode = ""+viewModel.pincodeIdCurrent

            }.show()
    }




    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
