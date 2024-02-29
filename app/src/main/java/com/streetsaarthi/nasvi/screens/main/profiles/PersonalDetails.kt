package com.streetsaarthi.nasvi.screens.main.profiles

import android.Manifest
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
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
import androidx.activity.result.ActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import com.kochia.customer.utils.hideKeyboard
import com.streetsaarthi.nasvi.R
import com.streetsaarthi.nasvi.databinding.PersonalDetailsBinding
import com.streetsaarthi.nasvi.datastore.DataStoreKeys
import com.streetsaarthi.nasvi.datastore.DataStoreUtil
import com.streetsaarthi.nasvi.models.login.Login
import com.streetsaarthi.nasvi.models.mix.ItemOrganization
import com.streetsaarthi.nasvi.screens.interfaces.CallBackListener
import com.streetsaarthi.nasvi.screens.mainActivity.MainActivity
import com.streetsaarthi.nasvi.screens.onboarding.networking.USER_TYPE
import com.streetsaarthi.nasvi.utils.getMediaFilePathFor
import com.streetsaarthi.nasvi.utils.loadImage
import com.streetsaarthi.nasvi.utils.mainThread
import com.streetsaarthi.nasvi.utils.showSnackBar
import com.streetsaarthi.nasvi.utils.singleClick
import dagger.hilt.android.AndroidEntryPoint
import id.zelory.compressor.Compressor
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.time.LocalDate
import java.time.Period
import java.util.Calendar

@AndroidEntryPoint
class PersonalDetails : Fragment(), CallBackListener {
    private val viewModel: ProfilesVM by activityViewModels()
    private var _binding: PersonalDetailsBinding? = null
    private val binding get() = _binding!!

    var permissionAlert: AlertDialog? = null

    companion object {
        var callBackListener: CallBackListener? = null
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = PersonalDetailsBinding.inflate(inflater)
        return binding.root
    }


    private fun callMediaPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            activityResultLauncher.launch(
                arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.READ_MEDIA_VIDEO,
                    Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
                )
            )
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            activityResultLauncher.launch(
                arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_MEDIA_IMAGES
                )
            )
        } else {
            activityResultLauncher.launch(
                arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            )
        }
    }

    private val activityResultLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        )
        { permissions ->
            if (!permissions.entries.toString().contains("false")) {
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
        if (permissionAlert?.isShowing == true) {
            return
        }
        permissionAlert = MaterialAlertDialogBuilder(requireContext(), R.style.LogoutDialogTheme)
            .setTitle(resources.getString(R.string.app_name))
            .setMessage(resources.getString(R.string.required_permissions))
            .setPositiveButton(resources.getString(R.string.yes)) { dialog, _ ->
                dialog.dismiss()
                val i = Intent()
                i.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                i.addCategory(Intent.CATEGORY_DEFAULT)
                i.data = Uri.parse("package:" + requireActivity().packageName)
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


    var imagePosition = 0
    private var pickMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            lifecycleScope.launch {
                if (uri != null) {
                    when (imagePosition) {
                        1 -> {
                            val compressedImageFile = Compressor.compress(
                                requireContext(),
                                File(requireContext().getMediaFilePathFor(uri))
                            )
                            viewModel.data.passportSizeImage = compressedImageFile.path
                            binding.ivImagePassportsizeImage.loadImage(url = { viewModel.data.passportSizeImage!! })
                        }

                        2 -> {
                            val compressedImageFile = Compressor.compress(
                                requireContext(),
                                File(requireContext().getMediaFilePathFor(uri))
                            )
                            viewModel.data.identificationImage = compressedImageFile.path
                            binding.ivImageIdentityImage.loadImage(url = { viewModel.data.identificationImage!! })
                        }
                    }
                }
            }
        }


    var uriReal: Uri? = null
    val captureMedia = registerForActivityResult(ActivityResultContracts.TakePicture()) { uri ->
        lifecycleScope.launch {
            if (uri == true) {
                when (imagePosition) {
                    1 -> {
                        val compressedImageFile = Compressor.compress(
                            requireContext(),
                            File(requireContext().getMediaFilePathFor(uriReal!!))
                        )
                        viewModel.data.passportSizeImage = compressedImageFile.path
                        binding.ivImagePassportsizeImage.loadImage(url = { viewModel.data.passportSizeImage!! })
                    }

                    2 -> {
                        val compressedImageFile = Compressor.compress(
                            requireContext(),
                            File(requireContext().getMediaFilePathFor(uriReal!!))
                        )
                        viewModel.data.identificationImage = compressedImageFile.path
                        binding.ivImageIdentityImage.loadImage(url = { viewModel.data.identificationImage!! })
                    }
                }
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        callBackListener = this

        binding.apply {

            fieldsEdit()

            DataStoreUtil.readData(DataStoreKeys.LOGIN_DATA) { loginUser ->
                if (loginUser != null) {
                    val data = Gson().fromJson(loginUser, Login::class.java)
                    editTextFN.setText(data.vendor_first_name)
                    editTextLN.setText(data.vendor_last_name)
                    editTextFatherFN.setText(data.parent_first_name)
                    editTextFatherLN.setText(data.parent_last_name)
                    viewModel.data.gender = data.gender
                    viewModel.data.date_of_birth = data.date_of_birth
                    viewModel.data.marital_status = data.marital_status
                    viewModel.data.spouse_name = data.spouse_name
                    viewModel.data.social_category = data.social_category
                    viewModel.data.education_qualification = data.education_qualification

                    data.profile_image_name?.let {
                        ivImagePassportsizeImage.loadImage(url = { data.profile_image_name.url })
                        viewModel.data.passportSizeImage = data.profile_image_name.url
                    }

                    data.identity_image_name?.let {
                        ivImageIdentityImage.loadImage(url = { data.identity_image_name.url })
                        viewModel.data.identificationImage = data.identity_image_name.url
                    }


                    val listGender = resources.getStringArray(R.array.gender_array)
                    data.gender?.let {
                        when (it) {
                            "Male" -> {
                                editTextGender.setText(listGender[0])
                            }

                            "Female" -> {
                                editTextGender.setText(listGender[1])
                            }

                            "Other" -> {
                                editTextGender.setText(listGender[2])
                            }
                        }
                    }

                    editTextDateofBirth.setText(data.date_of_birth)

                    val listMaritalStatus = resources.getStringArray(R.array.maritalStatus_array)
                    data.marital_status?.let {
                        when (it) {
                            "Single" -> {
                                editTextMaritalStatus.setText(listMaritalStatus[0])
                                textSpouseNameTxt.visibility = View.GONE
                                editTextSpouseName.visibility = View.GONE
                            }

                            "Married" -> {
                                editTextMaritalStatus.setText(listMaritalStatus[1])
                                editTextSpouseName.setText("${data.spouse_name}")
                                textSpouseNameTxt.visibility = View.VISIBLE
                                editTextSpouseName.visibility = View.VISIBLE
                            }

                            "Widowed" -> {
                                editTextMaritalStatus.setText(listMaritalStatus[2])
                                textSpouseNameTxt.visibility = View.GONE
                                editTextSpouseName.visibility = View.GONE
                            }

                            "Divorced" -> {
                                editTextMaritalStatus.setText(listMaritalStatus[3])
                                textSpouseNameTxt.visibility = View.GONE
                                editTextSpouseName.visibility = View.GONE
                            }

                            "Separated" -> {
                                editTextMaritalStatus.setText(listMaritalStatus[4])
                                textSpouseNameTxt.visibility = View.GONE
                                editTextSpouseName.visibility = View.GONE
                            }
                        }
                    }

                    editTextSocialCategory.setText(data.social_category)

                    val listEducation = resources.getStringArray(R.array.socialEducation_array)
                    data.education_qualification?.let {
                        when (it) {
                            "No Education" -> {
                                editTextEducationQualifacation.setText(listEducation[0])
                            }

                            "Primary Education(1st To 5th)" -> {
                                editTextEducationQualifacation.setText(listEducation[1])
                            }

                            "Middle Education(6th To 9th)" -> {
                                editTextEducationQualifacation.setText(listEducation[2])
                            }

                            "Higher Education(10th To 12th)" -> {
                                editTextEducationQualifacation.setText(listEducation[3])
                            }

                            "Graduation" -> {
                                editTextEducationQualifacation.setText(listEducation[4])
                            }

                            "Post Graduation" -> {
                                editTextEducationQualifacation.setText(listEducation[5])
                            }
                        }
                    }


                    runBlocking {
                        mainThread {
                            data.residential_state?.let {
                                if (MainActivity.context.get()!!
                                        .getString(R.string.englishVal) == "" + viewModel.locale
                                ) {
                                    editTextSelectState.setText("${data.residential_state.name}")
                                } else {
                                    viewModel.show()
                                    val nameChanged: String =
                                        viewModel.callApiTranslate("" + viewModel.locale, data.residential_state.name)
                                    editTextSelectState.setText("${nameChanged}")
                                    viewModel.hide()
                                }
                            }

                            data.residential_district?.let {
                                if (MainActivity.context.get()!!
                                        .getString(R.string.englishVal) == "" + viewModel.locale
                                ) {
                                    editTextSelectDistrict.setText("${data.residential_district.name}")
                                } else {
                                    viewModel.show()
                                    val nameChanged: String =
                                        viewModel.callApiTranslate("" + viewModel.locale, data.residential_district.name)
                                    editTextSelectDistrict.setText("${nameChanged}")
                                    viewModel.hide()
                                }
                            }

                            data.residential_municipality_panchayat?.let {
                                if (MainActivity.context.get()!!
                                        .getString(R.string.englishVal) == "" + viewModel.locale
                                ) {
                                    editTextMunicipalityPanchayat.setText("${data.residential_municipality_panchayat.name}")
                                } else {
                                    viewModel.show()
                                    val nameChanged: String =
                                        viewModel.callApiTranslate("" + viewModel.locale, data.residential_municipality_panchayat.name)
                                    editTextMunicipalityPanchayat.setText("${nameChanged}")
                                    viewModel.hide()
                                }

                            }
                        }
                    }




                    if (data.residential_pincode != null) {
                        editTextSelectPincode.setText("" + data.residential_pincode.pincode.toInt())
                    } else {
                        editTextSelectPincode.setText("")
                    }

                    data.residential_address?.let {
                        editTextAddress.setText("${data.residential_address}")
                    }

                    data.residential_state?.let {
                        viewModel.data.current_state = "" + data.residential_state.id
                    }
                    data.residential_district?.let {
                        viewModel.data.current_district = "" + data.residential_district.id
                    }
                    data.residential_municipality_panchayat?.let {
                        viewModel.data.municipality_panchayat_current =
                            "" + data.residential_municipality_panchayat.id
                    }

                    if (data.residential_pincode != null) {
                        viewModel.data.current_pincode = "" + data.residential_pincode.pincode
                    } else {
                        viewModel.data.current_pincode = "0"
                    }
                    viewModel.data.current_address = "" + data.residential_address


                    data.residential_state?.let {
                        viewModel.stateId = data.residential_state.id
                    }
                    data.residential_district?.let {
                        viewModel.districtId = data.residential_district.id
                    }
                    data.residential_municipality_panchayat?.let {
                        viewModel.panchayatId = data.residential_municipality_panchayat.id
                    }

                    if (data.residential_pincode != null) {
                        viewModel.pincodeId = data.residential_pincode.pincode
                    } else {
                        viewModel.pincodeId = ""
                    }

//                } else if ((viewModel.stateId > 0)){
//                    showSnackBar(getString(R.string.select_state))
//                } else if ((viewModel.districtId > 0)){
//                    showSnackBar(getString(R.string.select_district))
//                } else if ((viewModel.panchayatId > 0)){
//
                }
            }


            btnImagePassportsize.singleClick {
                imagePosition = 1
                callMediaPermissions()
            }
            btnIdentityImage.singleClick {
                imagePosition = 2
                callMediaPermissions()
            }



            viewModel.state(view)
            editTextSelectState.singleClick {
                requireActivity().hideKeyboard()
                showDropDownStateDialog()
            }

            editTextSelectDistrict.singleClick {
                requireActivity().hideKeyboard()
                if (!(viewModel.stateId > 0)) {
                    showSnackBar(getString(R.string.select_state_))
                } else {
                    if (viewModel.itemDistrict.size > 0) {
                        showDropDownDistrictDialog()
                    } else {
                        showSnackBar(getString(R.string.not_district))
                    }
                }
            }

            editTextMunicipalityPanchayat.singleClick {
                requireActivity().hideKeyboard()
                if (!(viewModel.stateId > 0)) {
                    showSnackBar(getString(R.string.select_state_))
                } else {
                    if (viewModel.itemPanchayat.size > 0) {
                        showDropDownPanchayatDialog()
                    } else {
                        showSnackBar(getString(R.string.not_municipality_panchayat))
                    }
                }
            }

            editTextSelectPincode.singleClick {
                requireActivity().hideKeyboard()
                if (!(viewModel.districtId > 0)) {
                    showSnackBar(getString(R.string.select_district_))
                } else {
                    if (viewModel.itemPincode.size > 0) {
                        showDropDownPincodeDialog()
                    } else {
                        showSnackBar(getString(R.string.not_pincode))
                    }
                }
            }


            btnAddtoCart.singleClick {
                if (editTextFN.text.toString().isEmpty()) {
                    showSnackBar(getString(R.string.first_name))
                } else if (editTextLN.text.toString().isEmpty()) {
                    showSnackBar(getString(R.string.last_name))
                } else if (editTextFatherFN.text.toString().isEmpty()) {
                    showSnackBar(getString(R.string.father_s_first_name))
                } else if (editTextFatherLN.text.toString().isEmpty()) {
                    showSnackBar(getString(R.string.father_s_last_name))
                } else if (editTextGender.text.toString().isEmpty()) {
                    showSnackBar(getString(R.string.gender))
                } else if (editTextDateofBirth.text.toString().isEmpty()) {
                    showSnackBar(getString(R.string.date_of_birth))
                } else if (editTextMaritalStatus.text.toString().isEmpty()) {
                    showSnackBar(getString(R.string.marital_status))
                } else if (editTextMaritalStatus.text.toString() == getString(R.string.married) && editTextSpouseName.text.toString()
                        .isEmpty()
                ) {
                    showSnackBar(getString(R.string.spouse_s_name))
                } else if (editTextSocialCategory.text.toString().isEmpty()) {
                    showSnackBar(getString(R.string.social_category))
                } else if (editTextEducationQualifacation.text.toString().isEmpty()) {
                    showSnackBar(getString(R.string.education_qualifacation))
                } else if (!(viewModel.stateId > 0)) {
                    showSnackBar(getString(R.string.select_state))
                } else if (!(viewModel.districtId > 0)) {
                    showSnackBar(getString(R.string.select_district))
                } else if (!(viewModel.panchayatId > 0)) {
                    showSnackBar(getString(R.string.municipality_panchayat))
                } else if (editTextAddress.text.toString().isEmpty()) {
                    showSnackBar(getString(R.string.address_mention_village))
                } else {
                    viewModel.data.vendor_first_name = editTextFN.text.toString()
                    viewModel.data.vendor_last_name = editTextLN.text.toString()
                    viewModel.data.parent_first_name = editTextFatherFN.text.toString()
                    viewModel.data.parent_last_name = editTextFatherLN.text.toString()

                    viewModel.data.spouse_name = editTextSpouseName.text.toString()

                    if (viewModel.data.marital_status == "Married") {
                        viewModel.data.spouse_name = editTextSpouseName.text.toString()
                    } else {
                        viewModel.data.spouse_name = null
                    }

                    viewModel.data.social_category = editTextSocialCategory.text.toString()
                    viewModel.data.current_address = editTextAddress.text.toString()

                    val requestBody: MultipartBody.Builder = MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("user_role", USER_TYPE)
                    if (viewModel.data.vendor_first_name != null) {
                        requestBody.addFormDataPart(
                            "vendor_first_name",
                            viewModel.data.vendor_first_name!!
                        )
                    }
                    if (viewModel.data.vendor_last_name != null) {
                        requestBody.addFormDataPart(
                            "vendor_last_name",
                            viewModel.data.vendor_last_name!!
                        )
                    }
                    if (viewModel.data.parent_first_name != null) {
                        requestBody.addFormDataPart(
                            "parent_first_name",
                            viewModel.data.parent_first_name!!
                        )
                    }
                    if (viewModel.data.parent_last_name != null) {
                        requestBody.addFormDataPart(
                            "parent_last_name",
                            viewModel.data.parent_last_name!!
                        )
                    }
                    if (viewModel.data.gender != null) {
                        requestBody.addFormDataPart("gender", viewModel.data.gender!!)
                    }
                    if (viewModel.data.date_of_birth != null) {
                        requestBody.addFormDataPart("date_of_birth", viewModel.data.date_of_birth!!)
                    }
                    if (viewModel.data.social_category != null) {
                        requestBody.addFormDataPart(
                            "social_category",
                            viewModel.data.social_category!!
                        )
                    }
                    if (viewModel.data.education_qualification != null) {
                        requestBody.addFormDataPart(
                            "education_qualification",
                            viewModel.data.education_qualification!!
                        )
                    }
                    if (viewModel.data.marital_status != null) {
                        requestBody.addFormDataPart(
                            "marital_status",
                            viewModel.data.marital_status!!
                        )
                    }
                    if (viewModel.data.spouse_name != null) {
                        requestBody.addFormDataPart("spouse_name", viewModel.data.spouse_name!!)
                    }
                    if (viewModel.data.current_state != null) {
                        requestBody.addFormDataPart(
                            "residential_state",
                            viewModel.data.current_state!!
                        )
                    }
                    if (viewModel.data.current_district != null) {
                        requestBody.addFormDataPart(
                            "residential_district",
                            viewModel.data.current_district!!
                        )
                    }
                    if (viewModel.data.municipality_panchayat_current != null) {
                        requestBody.addFormDataPart(
                            "residential_municipality_panchayat",
                            viewModel.data.municipality_panchayat_current!!
                        )
                    }
                    if (viewModel.data.current_pincode != null) {
                        requestBody.addFormDataPart(
                            "residential_pincode",
                            viewModel.data.current_pincode!!
                        )
                    }
                    if (viewModel.data.current_address != null) {
                        requestBody.addFormDataPart(
                            "residential_address",
                            viewModel.data.current_address!!
                        )
                    }
                    if (viewModel.data.passportSizeImage != null && (!viewModel.data.passportSizeImage!!.startsWith(
                            "http"
                        ))
                    ) {
                        requestBody.addFormDataPart(
                            "profile_image_name",
                            File(viewModel.data.passportSizeImage!!).name,
                            File(viewModel.data.passportSizeImage!!).asRequestBody("image/*".toMediaTypeOrNull())
                        )
                    }

                    if (viewModel.data.identificationImage != null && (!viewModel.data.identificationImage!!.startsWith(
                            "http"
                        ))
                    ) {
                        requestBody.addFormDataPart(
                            "identity_image_name",
                            File(viewModel.data.identificationImage!!).name,
                            File(viewModel.data.identificationImage!!).asRequestBody("image/*".toMediaTypeOrNull())
                        )
                    }

//                    DataStoreUtil.readData(DataStoreKeys.LOGIN_DATA) { loginUser ->
//                        if (loginUser != null) {
//                            val data = Gson().fromJson(loginUser, Login::class.java)
//                            viewModel.profileUpdate(view = requireView(), ""+data.id , requestBody.build())
//                        }
//                    }
                }


                Log.e("TAG", "viewModel.data " + viewModel.data.toString())
            }


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


        }


    }


    private fun fieldsEdit() {
        binding.apply {
            viewModel.isEditable.observe(viewLifecycleOwner, Observer {
                editTextFN.isEnabled = it
                editTextLN.isEnabled = it
                editTextFatherFN.isEnabled = it
                editTextFatherLN.isEnabled = it
                editTextGender.isEnabled = it
                editTextDateofBirth.isEnabled = it
                editTextMaritalStatus.isEnabled = it
                editTextSpouseName.isEnabled = it
                editTextSocialCategory.isEnabled = it
                editTextEducationQualifacation.isEnabled = it
                editTextSelectState.isEnabled = it
                editTextSelectDistrict.isEnabled = it
                editTextMunicipalityPanchayat.isEnabled = it
                editTextSelectPincode.isEnabled = it
                editTextAddress.isEnabled = it
                btnImagePassportsize.isEnabled = it
                btnIdentityImage.isEnabled = it
            })
        }
    }


    private fun showDropDownGenderDialog() {
        val list = resources.getStringArray(R.array.gender_array)
        MaterialAlertDialogBuilder(requireContext(), R.style.DialogTheme)
            .setTitle(resources.getString(R.string.gender))
            .setItems(list) { _, which ->
                binding.editTextGender.setText(list[which])
                when (which) {
                    0 -> viewModel.data.gender = "Male"
                    1 -> viewModel.data.gender = "Female"
                    2 -> viewModel.data.gender = "Other"
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
        val dpd = DatePickerDialog(requireContext(), R.style.CalendarDatePickerDialog,
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                val today = LocalDate.now()
                val birthday: LocalDate = LocalDate.of(year, (monthOfYear + 1), dayOfMonth)
                val p: Period = Period.between(birthday, today)


                var mm: String = (monthOfYear + 1).toString()
                if (mm.length == 1) {
                    mm = "0" + mm
                }

                var dd: String = "" + dayOfMonth
                if (dd.length == 1) {
                    dd = "0" + dd
                }

                if (p.getYears() > 13) {
                    binding.editTextDateofBirth.setText("" + year + "-" + mm + "-" + dd)
                    viewModel.data.date_of_birth = "" + year + "-" + mm + "-" + dd
                } else {
                    showSnackBar(getString(R.string.age_minimum))
                    binding.editTextDateofBirth.setText("")
                    viewModel.data.date_of_birth = null
                }
            }, year, month, day
        )
        dpd.show()
    }

    private fun showDropDownCategoryDialog() {
        val list = resources.getStringArray(R.array.socialCategory_array)
        MaterialAlertDialogBuilder(requireContext(), R.style.DialogTheme)
            .setTitle(resources.getString(R.string.social_category))
            .setItems(list) { _, which ->
                binding.editTextSocialCategory.setText(list[which])
            }.show()
    }


    private fun showDropDownEducationQualifacationDialog() {
        val list = resources.getStringArray(R.array.socialEducation_array)
        MaterialAlertDialogBuilder(requireContext(), R.style.DialogTheme)
            .setTitle(resources.getString(R.string.education_qualifacation))
            .setItems(list) { _, which ->
                binding.editTextEducationQualifacation.setText(list[which])
                when (which) {
                    0 -> viewModel.data.education_qualification = "No Education"
                    1 -> viewModel.data.education_qualification = "Primary Education(1st To 5th)"
                    2 -> viewModel.data.education_qualification = "Middle Education(6th To 9th)"
                    3 -> viewModel.data.education_qualification = "Higher Education(10th To 12th)"
                    4 -> viewModel.data.education_qualification = "Graduation"
                    5 -> viewModel.data.education_qualification = "Post Graduation"
                }
            }.show()
    }


    private fun showDropDownMaritalStatusDialog() {
        val list = resources.getStringArray(R.array.maritalStatus_array)
        MaterialAlertDialogBuilder(requireContext(), R.style.DialogTheme)
            .setTitle(resources.getString(R.string.marital_status))
            .setItems(list) { _, which ->
                binding.editTextMaritalStatus.setText(list[which])
                if (list[which] == getString(R.string.married)) {
                    binding.textSpouseNameTxt.visibility = View.VISIBLE
                    binding.editTextSpouseName.visibility = View.VISIBLE
                } else {
                    binding.textSpouseNameTxt.visibility = View.GONE
                    binding.editTextSpouseName.visibility = View.GONE
                    viewModel.data.spouse_name = null
                }
                when (which) {
                    0 -> viewModel.data.marital_status = "Single"
                    1 -> viewModel.data.marital_status = "Married"
                    2 -> viewModel.data.marital_status = "Widowed"
                    3 -> viewModel.data.marital_status = "Divorced"
                    4 -> viewModel.data.marital_status = "Separated"
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
            .setItems(list) { _, which ->
                binding.editTextSelectState.setText(list[which])
                viewModel.stateId = viewModel.itemState[which].id
                view?.let { viewModel.district(it, viewModel.stateId) }
//                view?.let { viewModel.panchayat(it, viewModel.stateId) }
                viewModel.data.current_state = "" + viewModel.stateId
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
        MaterialAlertDialogBuilder(requireView().context, R.style.DialogTheme)
            .setTitle(resources.getString(R.string.select_district))
            .setItems(list) { _, which ->
                binding.editTextSelectDistrict.setText(list[which])
                viewModel.districtId = viewModel.itemDistrict[which].id
                view?.let { viewModel.pincode(it, viewModel.districtId) }
                viewModel.data.current_district = "" + viewModel.districtId
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
        MaterialAlertDialogBuilder(requireView().context, R.style.DialogTheme)
            .setTitle(resources.getString(R.string.municipality_panchayat))
            .setItems(list) { _, which ->
                binding.editTextMunicipalityPanchayat.setText(list[which])
                viewModel.panchayatId = viewModel.itemPanchayat[which].id
                viewModel.data.municipality_panchayat_current = "" + viewModel.panchayatId
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
            .setItems(list) { _, which ->
                binding.editTextSelectPincode.setText(list[which])
//                viewModel.pincodeId =  viewModel.itemPincode[which].id
                viewModel.pincodeId = binding.editTextSelectPincode.text.toString()
                viewModel.data.current_pincode = "" + viewModel.pincodeId
            }.show()
    }


    private fun showOptions() = try {
        val dialogView = layoutInflater.inflate(R.layout.dialog_choose_image_option, null)
        val btnCancel = dialogView.findViewById<AppCompatButton>(R.id.btnCancel)
        val tvPhotos = dialogView.findViewById<AppCompatTextView>(R.id.tvPhotos)
        val tvPhotosDesc = dialogView.findViewById<AppCompatTextView>(R.id.tvPhotosDesc)
        val tvCamera = dialogView.findViewById<AppCompatTextView>(R.id.tvCamera)
        val tvCameraDesc = dialogView.findViewById<AppCompatTextView>(R.id.tvCameraDesc)
        val dialog = BottomSheetDialog(requireContext(), R.style.TransparentDialog)
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
        Log.e("TAG", "errorD " + e.message)
    }


    private fun forCamera() {
        requireActivity().runOnUiThread() {
            val directory = File(requireContext().filesDir, "camera_images")
            if (!directory.exists()) {
                directory.mkdirs()
            }
            val file = File(directory, "${Calendar.getInstance().timeInMillis}.png")
            uriReal = FileProvider.getUriForFile(
                requireContext(),
                requireContext().getPackageName() + ".provider",
                file
            )
            captureMedia.launch(uriReal)
        }
    }

    private fun forGallery() {
        requireActivity().runOnUiThread() {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }


    override fun onCallBack(pos: Int) {
        Log.e("TAG", "onCallBackPersonal " + pos)
        if (pos == 1) {
            update()
        }
    }

    private fun update() {
        binding.apply {
            if (editTextFN.text.toString().isEmpty()) {
                showSnackBar(getString(R.string.first_name))
            } else if (editTextLN.text.toString().isEmpty()) {
                showSnackBar(getString(R.string.last_name))
            } else if (editTextFatherFN.text.toString().isEmpty()) {
                showSnackBar(getString(R.string.father_s_first_name))
            } else if (editTextFatherLN.text.toString().isEmpty()) {
                showSnackBar(getString(R.string.father_s_last_name))
            } else if (editTextGender.text.toString().isEmpty()) {
                showSnackBar(getString(R.string.gender))
            } else if (editTextDateofBirth.text.toString().isEmpty()) {
                showSnackBar(getString(R.string.date_of_birth))
            } else if (editTextMaritalStatus.text.toString().isEmpty()) {
                showSnackBar(getString(R.string.marital_status))
            } else if (editTextMaritalStatus.text.toString() == getString(R.string.married) && editTextSpouseName.text.toString()
                    .isEmpty()
            ) {
                showSnackBar(getString(R.string.spouse_s_name))
            } else if (editTextSocialCategory.text.toString().isEmpty()) {
                showSnackBar(getString(R.string.social_category))
            } else if (editTextEducationQualifacation.text.toString().isEmpty()) {
                showSnackBar(getString(R.string.education_qualifacation))
            } else if (!(viewModel.stateId > 0)) {
                showSnackBar(getString(R.string.select_state))
            } else if (!(viewModel.districtId > 0)) {
                showSnackBar(getString(R.string.select_district))
            } else if (!(viewModel.panchayatId > 0)) {
                showSnackBar(getString(R.string.municipality_panchayat))
            } else if (editTextAddress.text.toString().isEmpty()) {
                showSnackBar(getString(R.string.address_mention_village))
            } else {
                viewModel.data.vendor_first_name = editTextFN.text.toString()
                viewModel.data.vendor_last_name = editTextLN.text.toString()
                viewModel.data.parent_first_name = editTextFatherFN.text.toString()
                viewModel.data.parent_last_name = editTextFatherLN.text.toString()

                viewModel.data.spouse_name = editTextSpouseName.text.toString()

                if (viewModel.data.marital_status == "Married") {
                    viewModel.data.spouse_name = editTextSpouseName.text.toString()
                } else {
                    viewModel.data.spouse_name = null
                }

                viewModel.data.social_category = editTextSocialCategory.text.toString()
                viewModel.data.current_address = editTextAddress.text.toString()

                val requestBody: MultipartBody.Builder = MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("user_role", USER_TYPE)
                if (viewModel.data.vendor_first_name != null) {
                    requestBody.addFormDataPart(
                        "vendor_first_name",
                        viewModel.data.vendor_first_name!!
                    )
                }
                if (viewModel.data.vendor_last_name != null) {
                    requestBody.addFormDataPart(
                        "vendor_last_name",
                        viewModel.data.vendor_last_name!!
                    )
                }
                if (viewModel.data.parent_first_name != null) {
                    requestBody.addFormDataPart(
                        "parent_first_name",
                        viewModel.data.parent_first_name!!
                    )
                }
                if (viewModel.data.parent_last_name != null) {
                    requestBody.addFormDataPart(
                        "parent_last_name",
                        viewModel.data.parent_last_name!!
                    )
                }
                if (viewModel.data.gender != null) {
                    requestBody.addFormDataPart("gender", viewModel.data.gender!!)
                }
                if (viewModel.data.date_of_birth != null) {
                    requestBody.addFormDataPart("date_of_birth", viewModel.data.date_of_birth!!)
                }
                if (viewModel.data.social_category != null) {
                    requestBody.addFormDataPart("social_category", viewModel.data.social_category!!)
                }
                if (viewModel.data.education_qualification != null) {
                    requestBody.addFormDataPart(
                        "education_qualification",
                        viewModel.data.education_qualification!!
                    )
                }
                if (viewModel.data.marital_status != null) {
                    requestBody.addFormDataPart("marital_status", viewModel.data.marital_status!!)
                }
                if (viewModel.data.spouse_name != null) {
                    requestBody.addFormDataPart("spouse_name", viewModel.data.spouse_name!!)
                }
                if (viewModel.data.current_state != null) {
                    requestBody.addFormDataPart("residential_state", viewModel.data.current_state!!)
                }
                if (viewModel.data.current_district != null) {
                    requestBody.addFormDataPart(
                        "residential_district",
                        viewModel.data.current_district!!
                    )
                }
                if (viewModel.data.municipality_panchayat_current != null) {
                    requestBody.addFormDataPart(
                        "residential_municipality_panchayat",
                        viewModel.data.municipality_panchayat_current!!
                    )
                }
                if (viewModel.data.current_pincode != null) {
                    requestBody.addFormDataPart(
                        "residential_pincode",
                        viewModel.data.current_pincode!!
                    )
                }
                if (viewModel.data.current_address != null) {
                    requestBody.addFormDataPart(
                        "residential_address",
                        viewModel.data.current_address!!
                    )
                }
                if (viewModel.data.passportSizeImage != null && (!viewModel.data.passportSizeImage!!.startsWith(
                        "http"
                    ))
                ) {
                    requestBody.addFormDataPart(
                        "profile_image_name",
                        File(viewModel.data.passportSizeImage!!).name,
                        File(viewModel.data.passportSizeImage!!).asRequestBody("image/*".toMediaTypeOrNull())
                    )
                }

                if (viewModel.data.identificationImage != null && (!viewModel.data.identificationImage!!.startsWith(
                        "http"
                    ))
                ) {
                    requestBody.addFormDataPart(
                        "identity_image_name",
                        File(viewModel.data.identificationImage!!).name,
                        File(viewModel.data.identificationImage!!).asRequestBody("image/*".toMediaTypeOrNull())
                    )
                }



                if (ProfessionalDetails.callBackListener != null) {
                    DataStoreUtil.readData(DataStoreKeys.LOGIN_DATA) { loginUser ->
                        if (loginUser != null) {
                            val data = Gson().fromJson(loginUser, Login::class.java)
                            viewModel.profileUpdate(
                                view = requireView(),
                                "" + data.id,
                                requestBody.build(),
                                222
                            )
                        }
                    }
                } else {
                    DataStoreUtil.readData(DataStoreKeys.LOGIN_DATA) { loginUser ->
                        if (loginUser != null) {
                            val data = Gson().fromJson(loginUser, Login::class.java)
                            viewModel.profileUpdate(
                                view = requireView(),
                                "" + data.id,
                                requestBody.build(),
                                111
                            )
                        }
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
