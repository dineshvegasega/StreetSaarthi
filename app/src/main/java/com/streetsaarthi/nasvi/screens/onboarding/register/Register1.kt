package com.streetsaarthi.nasvi.screens.onboarding.register

import android.Manifest
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.textclassifier.TextClassifierEvent.LanguageDetectionEvent
import androidx.activity.result.ActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
//import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage
//import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage
//import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions
import com.streetsaarthi.nasvi.utils.hideKeyboard
import com.streetsaarthi.nasvi.R
import com.streetsaarthi.nasvi.databinding.Register1Binding
import com.streetsaarthi.nasvi.screens.interfaces.CallBackListener
import com.streetsaarthi.nasvi.screens.mainActivity.MainActivity
import com.streetsaarthi.nasvi.screens.mainActivity.MainActivity.Companion.networkFailed
import com.streetsaarthi.nasvi.screens.onboarding.networking.IS_LANGUAGE_ALL
import com.streetsaarthi.nasvi.utils.callNetworkDialog
import com.streetsaarthi.nasvi.utils.getMediaFilePathFor
import com.streetsaarthi.nasvi.utils.showSnackBar
import com.streetsaarthi.nasvi.utils.singleClick
import dagger.hilt.android.AndroidEntryPoint
import id.zelory.compressor.Compressor
import kotlinx.coroutines.launch
import java.io.File
import java.time.LocalDate
import java.time.Period
import java.util.Calendar

@AndroidEntryPoint
class Register1  : Fragment() , CallBackListener {
    private var _binding: Register1Binding? = null
    private val binding get() = _binding!!
    private val viewModel: RegisterVM by activityViewModels()

    var permissionAlert : AlertDialog?= null

    companion object{
        var callBackListener: CallBackListener? = null
    }


    var imagePosition = 0
    private var pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        lifecycleScope.launch {
            if (uri != null) {
                when (imagePosition) {
                    1 -> {
                        val compressedImageFile = Compressor.compress(requireContext(), File(requireContext().getMediaFilePathFor(uri)))
                        viewModel.data.passportSizeImage = compressedImageFile.path
                        binding.textViewPassportSizeImage.setText(File(viewModel.data.passportSizeImage!!).name)
                    }
                    2 -> {
                        val compressedImageFile = Compressor.compress(requireContext(), File(requireContext().getMediaFilePathFor(uri)))
                        viewModel.data.identificationImage = compressedImageFile.path
                        binding.textViewIdentificationImage.setText(File(viewModel.data.identificationImage!!).name)
                    }
                }
            }
        }

    }



    var uriReal : Uri?= null
    val captureMedia = registerForActivityResult(ActivityResultContracts.TakePicture()) { uri ->
        lifecycleScope.launch {
            if (uri == true) {
                when (imagePosition) {
                    1 -> {
                        val compressedImageFile = Compressor.compress(requireContext(), File(requireContext().getMediaFilePathFor(uriReal!!)))
                        viewModel.data.passportSizeImage = compressedImageFile.path
                        binding.textViewPassportSizeImage.setText(compressedImageFile.name)
                    }
                    2 -> {
                        val compressedImageFile = Compressor.compress(requireContext(), File(requireContext().getMediaFilePathFor(uriReal!!)))
                        viewModel.data.identificationImage = compressedImageFile.path
                        binding.textViewIdentificationImage.setText(compressedImageFile.name)
                    }
                }
            }
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




//        binding.btSignIn.singleClick {
//            val requestBody: MultipartBody.Builder = MultipartBody.Builder()
//                .setType(MultipartBody.FORM)
//                .addFormDataPart("user_role", USER_TYPE)
//            requestBody.addFormDataPart("mobile_no", "1234567825")
//            requestBody.addFormDataPart("password", "Test@123")
//
//            requestBody.addFormDataPart(
//                "profile_image_name",
//                File(viewModel.data.PassportSizeImage).name,
//                File(viewModel.data.PassportSizeImage).asRequestBody("image/png".toMediaTypeOrNull())
//            )
//
//
//            viewModel.registerWithFiles(view = requireView(), requestBody.build())
//
//
//        }


        binding.apply {
            editTextGender.singleClick {
                requireActivity().hideKeyboard()
                showDropDownGenderDialog()
            }

            editTextDateofBirth.singleClick {
                requireActivity().hideKeyboard()
                showDOBDialog()
            }

            editTextSocialCategory.singleClick {
                requireActivity().hideKeyboard()
                showDropDownCategoryDialog()
            }

            editTextEducationQualifacation.singleClick {
                requireActivity().hideKeyboard()
                showDropDownEducationQualifacationDialog()
            }

            editTextMaritalStatus.singleClick {
                requireActivity().hideKeyboard()
                showDropDownMaritalStatusDialog()
            }

            if(networkFailed) {
                viewModel.state(view)
            } else {
                requireContext().callNetworkDialog()
            }

            editTextSelectState.singleClick {
                requireActivity().hideKeyboard()
                if(viewModel.itemState.size > 0){
                    showDropDownStateDialog()
                } else {
                    showSnackBar(getString(R.string.not_state))
                }
            }

            editTextSelectDistrict.singleClick {
                requireActivity().hideKeyboard()
                if (!(viewModel.stateId > 0)){
                    showSnackBar(getString(R.string.select_state_))
                }else{
                    if(viewModel.itemDistrict.size > 0){
                        showDropDownDistrictDialog()
                    } else {
                        showSnackBar(getString(R.string.not_district))
                    }
                }
            }

            editTextMunicipalityPanchayat.singleClick {
                requireActivity().hideKeyboard()
                if (!(viewModel.stateId > 0)){
                    showSnackBar(getString(R.string.select_state_))
                }else{
                    if(viewModel.itemPanchayat.size > 0){
                        showDropDownPanchayatDialog()
                    } else {
                        showSnackBar(getString(R.string.not_municipality_panchayat))
                    }
                }
            }

            editTextSelectPincode.singleClick {
                requireActivity().hideKeyboard()
                if (!(viewModel.districtId > 0)){
                    showSnackBar(getString(R.string.select_district_))
                } else {
                    if(viewModel.itemPincode.size > 0){
                        showDropDownPincodeDialog()
                    } else {
                        showSnackBar(getString(R.string.not_pincode))
                    }
                }
            }

            layoutPassportSizeImage.singleClick {
                imagePosition = 1
                callMediaPermissions()
            }

            layoutIdentificationImage.singleClick {
                imagePosition = 2
                callMediaPermissions()
            }

        }

    }

    private fun callMediaPermissions() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE){
            activityResultLauncher.launch(
                arrayOf(Manifest.permission.CAMERA,
                    Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.READ_MEDIA_VIDEO,
                    Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED)
            )
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            activityResultLauncher.launch(
                arrayOf(Manifest.permission.CAMERA,
                    Manifest.permission.READ_MEDIA_IMAGES)
            )
        } else{
            activityResultLauncher.launch(
                arrayOf(Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
            )
        }
    }


    private val activityResultLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions())
        { permissions ->
            if(!permissions.entries.toString().contains("false")){
                showOptions()
            } else {
                callPermissionDialog()
            }
        }



    var someActivityResultLauncher = registerForActivityResult<Intent, ActivityResult>(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        callMediaPermissions()
    }

    @SuppressLint("SuspiciousIndentation")
    fun callPermissionDialog() {
        if(permissionAlert?.isShowing == true) {
            return
        }
        permissionAlert = MaterialAlertDialogBuilder(requireContext(), R.style.LogoutDialogTheme)
            .setTitle(resources.getString(R.string.app_name))
            .setMessage(resources.getString(R.string.required_permissions))
            .setPositiveButton(resources.getString(R.string.yes)) { dialog, _ ->
                dialog.dismiss()
                val i=Intent()
                i.action=Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                i.addCategory(Intent.CATEGORY_DEFAULT)
                i.data= Uri.parse("package:" + requireActivity().packageName)
                someActivityResultLauncher.launch(i)
            }
            .setNegativeButton(resources.getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
                Handler(Looper.getMainLooper()).postDelayed({
                    MainActivity.binding.drawerLayout.close()
                }, 500)
            }
            .setCancelable(false)
            .show()
    }


    private fun showDropDownGenderDialog() {
        val list=resources.getStringArray(R.array.gender_array)
        MaterialAlertDialogBuilder(requireContext(), R.style.DropdownDialogTheme)
            .setTitle(resources.getString(R.string.gender))
            .setItems(list) {_,which->
                binding.editTextGender.setText(list[which])
                when(which){
                    0-> viewModel.data.gender = "Male"
                    1-> viewModel.data.gender = "Female"
                    2-> viewModel.data.gender = "Other"
                }
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


            var mm : String = (monthOfYear+1).toString()
            if (mm.length == 1){
                mm = "0"+mm
            }

            var dd : String = ""+dayOfMonth
            if (dd.length == 1){
                dd = "0"+dd
            }

            if(p.getYears() > 13){
                binding.editTextDateofBirth.setText("" + year + "-" + mm  + "-" + dd)
            }else{
                showSnackBar(getString(R.string.age_minimum))
                binding.editTextDateofBirth.setText("")
            }
        }, year, month, day)
        dpd.show()
    }

    private fun showDropDownCategoryDialog() {
        val list=resources.getStringArray(R.array.socialCategory_array)
        MaterialAlertDialogBuilder(requireContext(), R.style.DropdownDialogTheme)
            .setTitle(resources.getString(R.string.social_category))
            .setItems(list) {_,which->
                binding.editTextSocialCategory.setText(list[which])
            }.show()
    }


    private fun showDropDownEducationQualifacationDialog() {
        val list=resources.getStringArray(R.array.socialEducation_array)
        MaterialAlertDialogBuilder(requireContext(), R.style.DropdownDialogTheme)
            .setTitle(resources.getString(R.string.education_qualifacation))
            .setItems(list) {_,which->
                binding.editTextEducationQualifacation.setText(list[which])
                when(which){
                    0-> viewModel.data.education_qualification = "No Education"
                    1-> viewModel.data.education_qualification = "Primary Education(1st To 5th)"
                    2-> viewModel.data.education_qualification = "Middle Education(6th To 9th)"
                    3-> viewModel.data.education_qualification = "Higher Education(10th To 12th)"
                    4-> viewModel.data.education_qualification = "Graduation"
                    5-> viewModel.data.education_qualification = "Post Graduation"
                }
            }.show()
    }


    private fun showDropDownMaritalStatusDialog() {
        val list=resources.getStringArray(R.array.maritalStatus_array)
        MaterialAlertDialogBuilder(requireContext(), R.style.DropdownDialogTheme)
            .setTitle(resources.getString(R.string.marital_status))
            .setItems(list) {_,which->
                binding.editTextMaritalStatus.setText(list[which])
                if (list[which] == getString(R.string.married)){
                    binding.editTextSpouseName.visibility = View.VISIBLE
                }else{
                    binding.editTextSpouseName.visibility = View.GONE
                }
                when(which){
                    0-> viewModel.data.marital_status = "Single"
                    1-> viewModel.data.marital_status = "Married"
                    2-> viewModel.data.marital_status = "Widowed"
                    3-> viewModel.data.marital_status = "Divorced"
                    4-> viewModel.data.marital_status = "Separated"
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
        MaterialAlertDialogBuilder(requireView().context, R.style.DropdownDialogTheme)
            .setTitle(resources.getString(R.string.select_state))
            .setItems(list) {_,which->
                binding.editTextSelectState.setText(list[which])
                viewModel.stateId =  viewModel.itemState[which].id
                if(networkFailed) {
                    view?.let { viewModel.district(it, viewModel.stateId) }
                    if (!IS_LANGUAGE_ALL){
                        view?.let { viewModel.panchayat(it, viewModel.stateId) }
                    }
                } else {
                    requireContext().callNetworkDialog()
                }
                binding.editTextSelectDistrict.setText("")
                binding.editTextMunicipalityPanchayat.setText("")
                viewModel.districtId = 0
                viewModel.panchayatId = 0
            }.show()
    }


    private fun showDropDownDistrictDialog() {
        var index = 0
        val list = arrayOfNulls<String>(viewModel.itemDistrict.size)
        for (value in viewModel.itemDistrict) {
            list[index] = value.name
            index++
        }
        MaterialAlertDialogBuilder(requireView().context, R.style.DropdownDialogTheme)
            .setTitle(resources.getString(R.string.select_district))
            .setItems(list) {_,which->
                binding.editTextSelectDistrict.setText(list[which])
                viewModel.districtId =  viewModel.itemDistrict[which].id
                if(networkFailed) {
                    view?.let { viewModel.pincode(it, viewModel.districtId) }
                } else {
                    requireContext().callNetworkDialog()
                }
                binding.editTextSelectPincode.setText("")
                viewModel.pincodeId = ""
            }.show()
    }


    private fun showDropDownPanchayatDialog() {
        var index = 0
        val list = arrayOfNulls<String>(viewModel.itemPanchayat.size)
        for (value in viewModel.itemPanchayat) {
            list[index] = value.name
            index++
        }
        MaterialAlertDialogBuilder(requireView().context, R.style.DropdownDialogTheme)
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
        MaterialAlertDialogBuilder(requireView().context, R.style.DropdownDialogTheme)
            .setTitle(resources.getString(R.string.select_pincode))
            .setItems(list) {_,which->
                binding.editTextSelectPincode.setText(list[which])
                viewModel.pincodeId = binding.editTextSelectPincode.text.toString()
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
        dialog.setContentView(dialogView)
        dialog.show()

        btnCancel.singleClick {
            dialog.dismiss()
        }
        tvCamera.singleClick {
            dialog.dismiss()
            forCamera()
        }
        tvCameraDesc.singleClick {
            dialog.dismiss()
            forCamera()
        }

        tvPhotos.singleClick {
            dialog.dismiss()
            forGallery()
        }
        tvPhotosDesc.singleClick {
            dialog.dismiss()
            forGallery()
        }

    } catch (e: Exception) {
        e.printStackTrace()
    }




    private fun forCamera() {
        requireActivity().runOnUiThread(){
            val directory = File(requireContext().filesDir, "camera_images")
            if(!directory.exists()){
                directory.mkdirs()
            }
            val file = File(directory,"${Calendar.getInstance().timeInMillis}.png")
            uriReal = FileProvider.getUriForFile(requireContext(), requireContext().getPackageName() + ".provider", file)
            captureMedia.launch(uriReal)
        }
    }

    private fun forGallery() {
        requireActivity().runOnUiThread(){
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }




    override fun onCallBack(pos: Int) {
        binding.apply {
//            Register.callBackListener!!.onCallBack(2)
            if( pos == 1) {
                if(editTextFN.text.toString().isEmpty()){
                    showSnackBar(getString(R.string.first_name))
                } else if (editTextLN.text.toString().isEmpty()){
                    showSnackBar(getString(R.string.last_name))
                } else if (editTextFatherFN.text.toString().isEmpty()){
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
                } else if (editTextMaritalStatus.text.toString() == getString(R.string.married) && editTextSpouseName.text.toString().isEmpty()){
                    showSnackBar(getString(R.string.spouse_s_name))
                } else {
                    if (!(viewModel.stateId > 0)){
                        showSnackBar(getString(R.string.select_state))
                    } else if (!(viewModel.districtId > 0)){
                        showSnackBar(getString(R.string.select_district))
                    } else if (!(viewModel.panchayatId > 0)){
                        showSnackBar(getString(R.string.municipality_panchayat))
                    } else if (editTextAddress.text.toString().isEmpty()){
                        showSnackBar(getString(R.string.address_mention_village))
                    } else if (viewModel.data.passportSizeImage == null){
                        showSnackBar(getString(R.string.passport_size_image))
                    } else if (viewModel.data.identificationImage == null){
                        showSnackBar(getString(R.string.identification_image))
                    } else {
                        viewModel.data.vendor_first_name = editTextFN.text.toString()
                        viewModel.data.vendor_last_name = editTextLN.text.toString()
                        viewModel.data.parent_first_name = editTextFatherFN.text.toString()
                        viewModel.data.parent_last_name = editTextFatherLN.text.toString()
                        viewModel.data.date_of_birth = editTextDateofBirth.text.toString()
                        viewModel.data.social_category = editTextSocialCategory.text.toString()
                        viewModel.data.spouse_name = editTextSpouseName.text.toString()
                        viewModel.data.current_state = ""+viewModel.stateId
                        viewModel.data.current_district = ""+viewModel.districtId
                        viewModel.data.municipality_panchayat_current = ""+viewModel.panchayatId
                        viewModel.data.current_pincode = ""+viewModel.pincodeId
                        viewModel.currentAddress = editTextAddress.text.toString()
                        viewModel.data.current_address = ""+viewModel.currentAddress
                        Register.callBackListener!!.onCallBack(2)
                    }
                }
            }
        }
    }


    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}