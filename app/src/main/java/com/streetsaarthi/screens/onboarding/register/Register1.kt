package com.streetsaarthi.screens.onboarding.register

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.kochia.customer.utils.hideKeyboard
import com.streetsaarthi.R
import com.streetsaarthi.databinding.Register1Binding
import com.streetsaarthi.screens.interfaces.CallBackListener
import com.streetsaarthi.utils.getMediaFilePathFor
import com.streetsaarthi.utils.showSnackBar
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.time.LocalDate
import java.time.Period
import java.util.Calendar

@AndroidEntryPoint
class Register1  : Fragment() , CallBackListener {
    private var _binding: Register1Binding? = null
    private val binding get() = _binding!!
    private val viewModel: RegisterVM by activityViewModels()

    companion object{
        var callBackListener: CallBackListener? = null
    }


    var imagePosition = 0
    private var pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            Log.e("PhotoPicker", "Selected URI: $uri")
            when (imagePosition) {
                1 -> {
                    Log.e("TakePicture", "Selected URI1: $imagePosition $uri")
                    // binding.inclideDocuments.cbRememberImageUploadCOV.isChecked = true
                    viewModel.data.PassportSizeImage = requireContext().getMediaFilePathFor(uri)
                    binding.textViewPassportSizeImage.setText(File(viewModel.data.PassportSizeImage!!).name)
                }
                2 -> {
                    Log.e("TakePicture", "Selected URI2: $imagePosition $uri")
                    //  binding.inclideDocuments.cbRememberImageUploadLOR.isChecked = true
                    viewModel.data.IdentificationImage = requireContext().getMediaFilePathFor(uri)
                    binding.textViewIdentificationImage.setText(File(viewModel.data.IdentificationImage!!).name)
                }
            }
        } else {
            Log.e("PhotoPicker", "No media selected")
        }
    }



    var uriReal : Uri?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val directory = File(requireContext().filesDir, "camera_images")
        if(!directory.exists()){
            directory.mkdirs()
        }
        val file = File(directory,"${Calendar.getInstance().timeInMillis}.png")
        uriReal = FileProvider.getUriForFile(requireContext(), requireContext().getPackageName() + ".provider", file)
    }


    val captureMedia = registerForActivityResult(ActivityResultContracts.TakePicture()) { uri ->
        if (uri == true) {
            Log.e("PhotoPicker", "Selected URI: $uriReal")
            //  binding.imageUploadpassportsizeImage.loadImage(url = {  requireContext().getMediaFilePathFor(uriReal) })
            when (imagePosition) {
                1 -> {
                    Log.e("TakePicture", "Selected URI1: $imagePosition $uri")
                    // binding.inclideDocuments.cbRememberImageUploadCOV.isChecked = true
                    viewModel.data.PassportSizeImage = requireContext().getMediaFilePathFor(uriReal!!)
                    binding.textViewPassportSizeImage.setText(File(viewModel.data.PassportSizeImage!!).name)
                }
                2 -> {
                    Log.e("TakePicture", "Selected URI2: $imagePosition $uri")
                    // binding.inclideDocuments.cbRememberImageUploadLOR.isChecked = true
                    viewModel.data.IdentificationImage = requireContext().getMediaFilePathFor(uriReal!!)
                    binding.textViewIdentificationImage.setText(File(viewModel.data.IdentificationImage!!).name)
                }
            }
        } else {
            Log.e("PhotoPicker", "No media selected")
        }
    }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = Register1Binding.inflate(inflater)
        return binding.root
    }


    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        callBackListener = this


        binding.apply {
            editTextGender.setOnClickListener {
                requireActivity().hideKeyboard()
                showDropDownGenderDialog()
            }

            editTextDateofBirth.setOnClickListener {
                requireActivity().hideKeyboard()
                showDOBDialog()
            }

            editTextSocialCategory.setOnClickListener {
                requireActivity().hideKeyboard()
                showDropDownCategoryDialog()
            }

            editTextEducationQualifacation.setOnClickListener {
                requireActivity().hideKeyboard()
                showDropDownEducationQualifacationDialog()
            }

            editTextMaritalStatus.setOnClickListener {
                requireActivity().hideKeyboard()
                showDropDownMaritalStatusDialog()
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

            layoutPassportSizeImage.setOnClickListener {
                imagePosition = 1
                showOptions()
            }

            layoutIdentificationImage.setOnClickListener {
                imagePosition = 2
                showOptions()
            }

        }

    }


    private fun showDropDownGenderDialog() {
        val list=resources.getStringArray(R.array.gender_array)
        MaterialAlertDialogBuilder(requireContext(), R.style.DialogTheme)
            .setTitle(resources.getString(R.string.gender))
            .setItems(list) {_,which->
                binding.editTextGender.setText(list[which])
//                editProfileVM.gender.value = list[which]
            }.show()
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun showDOBDialog() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val dpd = DatePickerDialog(requireContext(), R.style.CalendarDatePickerDialog,DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            val today= LocalDate.now()
            val birthday: LocalDate=LocalDate.of(year,(monthOfYear+1),dayOfMonth)
                val p: Period =Period.between(birthday,today)
            if(p.getYears() > 13){
                binding.editTextDateofBirth.setText("" + year + "-" + (monthOfYear+1)  + "-" + dayOfMonth)
            }else{
                showSnackBar(getString(R.string.age_minimum))
                binding.editTextDateofBirth.setText("")
            }
        }, year, month, day)
        dpd.show()
    }

    private fun showDropDownCategoryDialog() {
        val list=resources.getStringArray(R.array.socialCategory_array)
        MaterialAlertDialogBuilder(requireContext(), R.style.DialogTheme)
            .setTitle(resources.getString(R.string.social_category))
            .setItems(list) {_,which->
                binding.editTextSocialCategory.setText(list[which])
//                editProfileVM.gender.value = list[which]
            }.show()
    }


    private fun showDropDownEducationQualifacationDialog() {
        val list=resources.getStringArray(R.array.socialEducation_array)
        MaterialAlertDialogBuilder(requireContext(), R.style.DialogTheme)
            .setTitle(resources.getString(R.string.education_qualifacation))
            .setItems(list) {_,which->
                binding.editTextEducationQualifacation.setText(list[which])
//                editProfileVM.gender.value = list[which]
            }.show()
    }


    private fun showDropDownMaritalStatusDialog() {
        val list=resources.getStringArray(R.array.maritalStatus_array)
        MaterialAlertDialogBuilder(requireContext(), R.style.DialogTheme)
            .setTitle(resources.getString(R.string.marital_status))
            .setItems(list) {_,which->
                binding.editTextMaritalStatus.setText(list[which])
//                editProfileVM.gender.value = list[which]
                if (list[which] == getString(R.string.married)){
                    binding.editTextSpouseName.visibility = View.VISIBLE
                }else{
                    binding.editTextSpouseName.visibility = View.GONE
                }
            }.show()
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
                viewModel.pincodeId =  viewModel.itemPincode[which].id
            }.show()
    }





    private fun showOptions() =try {
        val dialogView=layoutInflater.inflate(R.layout.dialog_choose_image_option,null)
        val btnCancel=dialogView.findViewById<AppCompatButton>(R.id.btnCancel)
        val tvPhotos=dialogView.findViewById<AppCompatTextView>(R.id.tvPhotos)
        val tvPhotosDesc=dialogView.findViewById<AppCompatTextView>(R.id.tvPhotosDesc)
        val tvCamera=dialogView.findViewById<AppCompatTextView>(R.id.tvCamera)
        val tvCameraDesc=dialogView.findViewById<AppCompatTextView>(R.id.tvCameraDesc)
        val dialog= BottomSheetDialog(requireContext(),R.style.TransparentDialog)
        btnCancel.setOnClickListener {
            dialog.dismiss()
        }
        tvCamera.setOnClickListener {
            dialog.dismiss()
            forCamera()
        }
        tvCameraDesc.setOnClickListener {
            dialog.dismiss()
            forCamera()
        }

        tvPhotos.setOnClickListener {
            dialog.dismiss()
            forGallery()
        }
        tvPhotosDesc.setOnClickListener {
            dialog.dismiss()
            forGallery()
        }
        dialog.setContentView(dialogView)
        dialog.show()
    } catch (e: Exception) {
        e.printStackTrace()
        Log.e("TAG","errorD " + e.message)
    }

    private fun forCamera() {
        requireActivity().runOnUiThread(){
            captureMedia.launch(uriReal)
        }
    }

    private fun forGallery() {
        requireActivity().runOnUiThread(){
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }




    override fun onCallBack(pos: Int) {
        Log.e("TAG", "onCallBackA " + pos)
        binding.apply {
            if( pos == 1) {
                if(editTextFN.text.toString().isEmpty()){
                    showSnackBar(getString(R.string.first_name))
                } else if (editTextLN.text.toString().isEmpty()){
                    showSnackBar(getString(R.string.last_name))
                } else if (editTextFaterFN.text.toString().isEmpty()){
                    showSnackBar(getString(R.string.father_s_first_name))
                } else if (editTextFatherLN.text.toString().isEmpty()){
                    showSnackBar(getString(R.string.father_s_last_name))
                } else if (editTextGender.text.toString().isEmpty()){
                    showSnackBar(getString(R.string.gender))
                } else if (editTextDateofBirth.text.toString().isEmpty()){
                    showSnackBar(getString(R.string.date_of_birth))
                } else if (editTextSocialCategory.text.toString().isEmpty()){
                    showSnackBar(getString(R.string.social_category))
                } else if (editTextEducationQualifacation.text.toString().isEmpty()){
                    showSnackBar(getString(R.string.education_qualifacation))
                } else if (editTextMaritalStatus.text.toString().isEmpty()){
                    showSnackBar(getString(R.string.marital_status))
                } else if (editTextMaritalStatus.text.toString() == getString(R.string.married)){
                    if (editTextSpouseName.text.toString().isEmpty()){
                        showSnackBar(getString(R.string.spouse_s_name))
                    } else{
                        if (!(viewModel.stateId > 0)){
                            showSnackBar(getString(R.string.select_state))
                        } else if (!(viewModel.districtId > 0)){
                            showSnackBar(getString(R.string.select_district))
                        } else if (!(viewModel.panchayatId > 0)){
                            showSnackBar(getString(R.string.municipality_panchayat))
                        } else if (editTextAddress.text.toString().isEmpty()){
                            showSnackBar(getString(R.string.address_mention_village))
                        } else if (viewModel.data.PassportSizeImage == null){
                            showSnackBar(getString(R.string.passport_size_image))
                        } else if (viewModel.data.IdentificationImage == null){
                            showSnackBar(getString(R.string.identification_image))
                        } else {
                            viewModel.data.vendor_first_name = editTextFN.text.toString()
                            viewModel.data.vendor_last_name = editTextLN.text.toString()
                            viewModel.data.parent_first_name = editTextFaterFN.text.toString()
                            viewModel.data.parent_last_name = editTextFatherLN.text.toString()
                            viewModel.data.gender = editTextGender.text.toString()
                            viewModel.data.date_of_birth = editTextDateofBirth.text.toString()
                            viewModel.data.social_category = editTextSocialCategory.text.toString()
                            viewModel.data.education_qualification = editTextEducationQualifacation.text.toString()
                            viewModel.data.marital_status = editTextMaritalStatus.text.toString()
                            viewModel.data.spouse_name = editTextSpouseName.text.toString()

                            viewModel.data.current_state = ""+viewModel.stateId
                            viewModel.data.current_district = ""+viewModel.districtId
                            viewModel.data.municipality_panchayat_current = ""+viewModel.panchayatId
                            viewModel.data.current_pincode = ""+viewModel.pincodeId
                            viewModel.currentAddress = editTextAddress.text.toString()
                            viewModel.data.current_address = ""+viewModel.currentAddress

                            Log.e("TAG", "viewModel.dataA "+viewModel.data.toString())
                            Register.callBackListener!!.onCallBack(2)
                        }
                    }
                } else {
                    if (!(viewModel.stateId > 0)){
                        showSnackBar(getString(R.string.select_state))
                    } else if (!(viewModel.districtId > 0)){
                        showSnackBar(getString(R.string.select_district))
                    } else if (!(viewModel.panchayatId > 0)){
                        showSnackBar(getString(R.string.municipality_panchayat))
                    } else if (editTextAddress.text.toString().isEmpty()){
                        showSnackBar(getString(R.string.address_mention_village))
                    } else if (viewModel.data.PassportSizeImage == null){
                        showSnackBar(getString(R.string.passport_size_image))
                    } else if (viewModel.data.IdentificationImage == null){
                        showSnackBar(getString(R.string.identification_image))
                    } else {
                        viewModel.data.vendor_first_name = editTextFN.text.toString()
                        viewModel.data.vendor_last_name = editTextLN.text.toString()
                        viewModel.data.parent_first_name = editTextFaterFN.text.toString()
                        viewModel.data.parent_last_name = editTextFatherLN.text.toString()
                        viewModel.data.gender = editTextGender.text.toString()
                        viewModel.data.date_of_birth = editTextDateofBirth.text.toString()
                        viewModel.data.social_category = editTextSocialCategory.text.toString()
                        viewModel.data.education_qualification = editTextEducationQualifacation.text.toString()
                        viewModel.data.marital_status = editTextMaritalStatus.text.toString()
                        viewModel.data.spouse_name = editTextSpouseName.text.toString()

                        viewModel.data.current_state = ""+viewModel.stateId
                        viewModel.data.current_district = ""+viewModel.districtId
                        viewModel.data.municipality_panchayat_current = ""+viewModel.panchayatId
                        viewModel.data.current_pincode = ""+viewModel.pincodeId
                        viewModel.currentAddress = editTextAddress.text.toString()
                        viewModel.data.current_address = ""+viewModel.currentAddress

                        Log.e("TAG", "viewModel.dataA "+viewModel.data.toString())
                        Register.callBackListener!!.onCallBack(2)
                    }
                }
            }
        }
    }
}