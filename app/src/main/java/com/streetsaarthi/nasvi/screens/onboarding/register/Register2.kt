package com.streetsaarthi.nasvi.screens.onboarding.register

import android.Manifest
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.TimePickerDialog
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
import android.widget.TimePicker
import androidx.activity.result.ActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.FileProvider
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.kochia.customer.utils.hideKeyboard
import com.streetsaarthi.nasvi.R
import com.streetsaarthi.nasvi.databinding.Register2Binding
import com.streetsaarthi.nasvi.screens.interfaces.CallBackListener
import com.streetsaarthi.nasvi.screens.mainActivity.MainActivity
import com.streetsaarthi.nasvi.utils.getMediaFilePathFor
import com.streetsaarthi.nasvi.utils.showSnackBar
import com.streetsaarthi.nasvi.utils.singleClick
import dagger.hilt.android.AndroidEntryPoint
import id.zelory.compressor.Compressor
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.File
import java.util.Calendar


@AndroidEntryPoint
class Register2  : Fragment() , CallBackListener {

    private var _binding: Register2Binding? = null
    private val binding get() = _binding!!
    private val viewModel: RegisterVM by activityViewModels()

    var scrollPoistion = 0

    var permissionAlert : AlertDialog?= null

    companion object {
        var TAG = "Register2"
        var callBackListener: CallBackListener? = null
    }

    var imagePosition = 0
    private var pickMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            lifecycleScope.launch {
                if (uri != null) {
                    when (imagePosition) {
                        11 -> {
                            val compressedImageFile = Compressor.compress(requireContext(), File(requireContext().getMediaFilePathFor(uri)))
                            viewModel.data.shopImage = compressedImageFile.path
                            binding.textViewlayoutShopImage.setText(File(viewModel.data.shopImage!!).name)
                        }

                        1 -> {
                            val compressedImageFile = Compressor.compress(requireContext(), File(requireContext().getMediaFilePathFor(uri)))
                            viewModel.data.ImageUploadCOV = compressedImageFile.path
                            binding.inclideDocuments.textViewImageUploadCOV.setText(File(viewModel.data.ImageUploadCOV!!).name)
                        }

                        2 -> {
                            val compressedImageFile = Compressor.compress(requireContext(), File(requireContext().getMediaFilePathFor(uri)))
                            viewModel.data.ImageUploadLOR = compressedImageFile.path
                            binding.inclideDocuments.textViewImageUploadLOR.setText(File(viewModel.data.ImageUploadLOR!!).name)
                        }

                        3 -> {
                            val compressedImageFile = Compressor.compress(requireContext(), File(requireContext().getMediaFilePathFor(uri)))
                            viewModel.data.UploadSurveyReceipt = compressedImageFile.path
                            binding.inclideDocuments.textViewUploadSurveyReceipt.setText(File(viewModel.data.UploadSurveyReceipt!!).name)
                        }

                        4 -> {
                            val compressedImageFile = Compressor.compress(requireContext(), File(requireContext().getMediaFilePathFor(uri)))
                            viewModel.data.UploadChallan = compressedImageFile.path
                            binding.inclideDocuments.textViewUploadChallan.setText(File(viewModel.data.UploadChallan!!).name)
                        }

                        5 -> {
                            val compressedImageFile = Compressor.compress(requireContext(), File(requireContext().getMediaFilePathFor(uri)))
                            viewModel.data.UploadApprovalLetter = compressedImageFile.path
                            binding.inclideDocuments.textViewUploadApprovalLetter.setText(File(viewModel.data.UploadApprovalLetter!!).name)
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
                    11 -> {
                        val compressedImageFile = Compressor.compress(requireContext(), File(requireContext().getMediaFilePathFor(uriReal!!)))
                        viewModel.data.shopImage = compressedImageFile.path
                        binding.textViewlayoutShopImage.setText(compressedImageFile.name)
                    }

                    1 -> {
                        val compressedImageFile = Compressor.compress(requireContext(), File(requireContext().getMediaFilePathFor(uriReal!!)))
                        viewModel.data.ImageUploadCOV = compressedImageFile.path
                        binding.inclideDocuments.textViewImageUploadCOV.setText(compressedImageFile.name)
                    }

                    2 -> {
                        val compressedImageFile = Compressor.compress(requireContext(), File(requireContext().getMediaFilePathFor(uriReal!!)))
                        viewModel.data.ImageUploadLOR = compressedImageFile.path
                        binding.inclideDocuments.textViewImageUploadLOR.setText(compressedImageFile.name)
                    }

                    3 -> {
                        val compressedImageFile = Compressor.compress(requireContext(), File(requireContext().getMediaFilePathFor(uriReal!!)))
                        viewModel.data.UploadSurveyReceipt = compressedImageFile.path
                        binding.inclideDocuments.textViewUploadSurveyReceipt.setText(compressedImageFile.name)
                    }

                    4 -> {
                        val compressedImageFile = Compressor.compress(requireContext(), File(requireContext().getMediaFilePathFor(uriReal!!)))
                        viewModel.data.UploadChallan = compressedImageFile.path
                        binding.inclideDocuments.textViewUploadChallan.setText(compressedImageFile.name)
                    }

                    5 -> {
                        val compressedImageFile = Compressor.compress(requireContext(), File(requireContext().getMediaFilePathFor(uriReal!!)))
                        viewModel.data.UploadApprovalLetter = compressedImageFile.path
                        binding.inclideDocuments.textViewUploadApprovalLetter.setText(compressedImageFile.name)
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
        _binding = Register2Binding.inflate(inflater)
        return binding.root
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        callBackListener = this


        binding.apply {
            viewModel.vending(view)
            viewModel.marketplace(view)

//            btSignIn.singleClick {
//                Log.e("TAG", "viewModel.dataB "+viewModel.data.toString())
//            }

            editTextTypeofMarketPlace.singleClick {
                requireActivity().hideKeyboard()
                showDropDownMarketPlaceDialog()
            }

            editTextTypeofVending.singleClick {
                requireActivity().hideKeyboard()
                showDropDownVendingDialog()
            }

            editTextTotalYearsofVending.singleClick {
                requireActivity().hideKeyboard()
                showDropDownYearsDialog()
            }

            editTextVendingTimeOpen.singleClick {
                requireActivity().hideKeyboard()
                showOpenDialog()
            }
            editTextVendingTimeClose.singleClick {
                requireActivity().hideKeyboard()
                showCloseDialog()
            }

            viewModel.stateCurrent(view)
            editTextSelectState.singleClick {
                requireActivity().hideKeyboard()
                if(viewModel.itemStateVending.size > 0){
                    showDropDownStateDialog()
                } else {
                    showSnackBar(getString(R.string.not_state))
                }
            }

            editTextSelectDistrict.singleClick {
                requireActivity().hideKeyboard()
                if (!(viewModel.stateIdVending > 0)){
                    showSnackBar(getString(R.string.select_state_))
                }else{
                    if(viewModel.itemDistrictVending.size > 0){
                        showDropDownDistrictDialog()
                    } else {
                        showSnackBar(getString(R.string.not_district))
                    }
                }
            }

            editTextMunicipalityPanchayat.singleClick {
                requireActivity().hideKeyboard()
                if (!(viewModel.stateIdVending > 0)){
                    showSnackBar(getString(R.string.select_state_))
                }else{
                    if(viewModel.itemPanchayatVending.size > 0){
                        showDropDownPanchayatDialog()
                    } else {
                        showSnackBar(getString(R.string.not_municipality_panchayat))
                    }
                }
            }

            editTextSelectPincode.singleClick {
                requireActivity().hideKeyboard()
                if (!(viewModel.districtIdVending > 0)){
                    showSnackBar(getString(R.string.select_district_))
                } else {
                    if(viewModel.itemPincodeVending.size > 0){
                        showDropDownPincodeDialog()
                    } else {
                        showSnackBar(getString(R.string.not_pincode))
                    }
                }
            }


            ivRdLocalOrgnaizationYes.singleClick {
                editTextLocalOrganisation.visibility = View.VISIBLE
                setScrollPosition(1, true)
            }

            ivRdLocalOrgnaizationNo.singleClick {
                editTextLocalOrganisation.visibility = View.GONE
                setScrollPosition(1, false)
            }


            editTextLocalOrganisation.singleClick {
                requireActivity().hideKeyboard()
                if (viewModel.itemLocalOrganizationVending.size > 0){
                    showDropDownLocalOrganisationDialog()
                } else {
                    showSnackBar(getString(R.string.not_Organisation))
                }
            }


            layoutShopImage.singleClick {
                imagePosition = 11
                callMediaPermissions()
            }


            ivRdDocumentYes.singleClick {
                viewModel.documentDetails = true
                inclideDocuments.layoutDocuments.visibility = View.VISIBLE
                setScrollPosition(1, true)
            }
            ivRdDocumentNo.singleClick {
                viewModel.documentDetails = false
                inclideDocuments.layoutDocuments.visibility = View.GONE
                setScrollPosition(1, false)
            }

            inclideDocuments.layoutImageUploadCOV.singleClick {
                imagePosition = 1
                callMediaPermissions()
            }
            inclideDocuments.layoutImageUploadLOR.singleClick {
                imagePosition = 2
                callMediaPermissions()
            }
            inclideDocuments.layoutUploadSurveyReceipt.singleClick {
                imagePosition = 3
                callMediaPermissions()
            }
            inclideDocuments.layoutUploadChallan.singleClick {
                imagePosition = 4
                callMediaPermissions()
            }
            inclideDocuments.layoutUploadApprovalLetter.singleClick {
                imagePosition = 5
                callMediaPermissions()
            }



            binding.scroll.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
                    scrollPoistion = scrollY
            })
            Handler(Looper.getMainLooper()).postDelayed({
                inclideGovernment.layoutGovernmentScheme.visibility = View.GONE
            }, 100)
            ivRdGovernmentYes.singleClick {
                viewModel.governmentScheme = true
                inclideGovernment.layoutGovernmentScheme.visibility = View.VISIBLE
                setScrollPosition(2, true)
            }

            ivRdGovernmentNo.singleClick {
                viewModel.governmentScheme = false
                inclideGovernment.layoutGovernmentScheme.visibility = View.GONE
                setScrollPosition(2, false)
            }

        }
    }


    private fun setScrollPosition(type : Int, ifTrue: Boolean) {
        when(type){
            1 -> {
                ObjectAnimator.ofInt(binding.scroll, "scrollY",  scrollPoistion).setDuration(100).start()
            }

            2 -> {
                Handler(Looper.getMainLooper()).postDelayed(Runnable {
                    val itemHeight =
                        binding.inclideGovernment.layoutGovernmentScheme.height
                    binding.scroll.smoothScrollTo(0,scrollPoistion * itemHeight)
                }, 50)
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
                val i= Intent()
                i.action= Settings.ACTION_APPLICATION_DETAILS_SETTINGS
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


    private fun showOptions() = try {
        val dialogView = layoutInflater.inflate(R.layout.dialog_choose_image_option, null)
        val btnCancel = dialogView.findViewById<AppCompatButton>(R.id.btnCancel)
        val tvPhotos = dialogView.findViewById<AppCompatTextView>(R.id.tvPhotos)
        val tvPhotosDesc = dialogView.findViewById<AppCompatTextView>(R.id.tvPhotosDesc)
        val tvCamera = dialogView.findViewById<AppCompatTextView>(R.id.tvCamera)
        val tvCameraDesc = dialogView.findViewById<AppCompatTextView>(R.id.tvCameraDesc)
        val dialog = BottomSheetDialog(requireContext(), R.style.TransparentDialog)
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
        dialog.setContentView(dialogView)
        dialog.show()
    } catch (e: Exception) {
        e.printStackTrace()
    }

    private fun forCamera() {
        requireActivity().runOnUiThread() {
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
        requireActivity().runOnUiThread() {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
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
            }.show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showOpenDialog() {
        val mTimePicker: TimePickerDialog
        val mcurrentTime = Calendar.getInstance()
        val hour = mcurrentTime.get(Calendar.HOUR_OF_DAY)
        val minute = mcurrentTime.get(Calendar.MINUTE)
        mTimePicker = TimePickerDialog(
            requireContext(),
            R.style.TimeDialogTheme,
            object : TimePickerDialog.OnTimeSetListener {
                override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
                    val datetime = Calendar.getInstance()
                    datetime[Calendar.HOUR_OF_DAY] = hourOfDay
                    datetime[Calendar.MINUTE] = minute
                    val strHrsToShow =
                        if (datetime.get(Calendar.HOUR) === 0) "12" else Integer.toString(
                            datetime.get(Calendar.HOUR)
                        )
                    var am_pm = ""
                    if (datetime.get(Calendar.AM_PM) == Calendar.AM)
                        am_pm = "AM";
                    else if (datetime.get(Calendar.AM_PM) == Calendar.PM)
                        am_pm = "PM";
                    binding.editTextVendingTimeOpen.setText(
                        strHrsToShow + ":" + (if (minute.toString().length == 1) "0"+datetime.get(Calendar.MINUTE)  else datetime.get(Calendar.MINUTE)) + " " + am_pm
                    )

                    if (minute.toString().length == 1){

                    } else {

                    }
                    viewModel.data.open = "" + hourOfDay + ":" + (if (minute.toString().length == 1) "0"+minute else minute) + ":00"
                }
            },
            hour,
            minute,
            false
        )
        mTimePicker.show()
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun showCloseDialog() {
        val mTimePicker: TimePickerDialog
        val mcurrentTime = Calendar.getInstance()
        val hour = mcurrentTime.get(Calendar.HOUR_OF_DAY)
        val minute = mcurrentTime.get(Calendar.MINUTE)
        mTimePicker = TimePickerDialog(
            requireContext(),
            R.style.TimeDialogTheme,
            object : TimePickerDialog.OnTimeSetListener {
                @SuppressLint("SetTextI18n")
                override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
                    val datetime = Calendar.getInstance()
                    datetime[Calendar.HOUR_OF_DAY] = hourOfDay
                    datetime[Calendar.MINUTE] = minute
                    val strHrsToShow =
                        if (datetime.get(Calendar.HOUR) === 0) "12" else Integer.toString(
                            datetime.get(Calendar.HOUR)
                        )

                    var am_pm = ""
                    if (datetime.get(Calendar.AM_PM) == Calendar.AM)
                        am_pm = "AM";
                    else if (datetime.get(Calendar.AM_PM) == Calendar.PM)
                        am_pm = "PM";
                    binding.editTextVendingTimeClose.setText(
                        strHrsToShow + ":" + (if (minute.toString().length == 1) "0" + datetime.get(
                            Calendar.MINUTE
                        ) else datetime.get(Calendar.MINUTE)) + " " + am_pm
                    )
                    viewModel.data.close =
                        "" + hourOfDay + ":" + (if (minute.toString().length == 1) "0" + minute else minute) + ":00"
                }
            },
            hour,
            minute,
            false
        )
        mTimePicker.show()
    }


    private fun showDropDownStateDialog() {
        var index = 0
        val list = arrayOfNulls<String>(viewModel.itemStateVending.size)
        for (value in viewModel.itemStateVending) {
            list[index] = value.name
            index++
        }
        MaterialAlertDialogBuilder(requireView().context, R.style.DialogTheme)
            .setTitle(resources.getString(R.string.select_state))
            .setItems(list) { _, which ->
                binding.editTextSelectState.setText(list[which])
                viewModel.stateIdVending = viewModel.itemStateVending[which].id
                view?.let { viewModel.districtCurrent(it, viewModel.stateIdVending) }
//                view?.let { viewModel.panchayatCurrent(it, viewModel.stateIdVending) }
                if(viewModel.stateIdVending != 0 && viewModel.districtIdVending != 0){
                    view?.let { viewModel.localOrganisation(it, JSONObject().apply {
                        put("state_id", viewModel.stateIdVending)
                        put("district_id", viewModel.districtIdVending)
                    })}
                }

                binding.editTextSelectDistrict.setText("")
                binding.editTextMunicipalityPanchayat.setText("")
                viewModel.districtIdVending = 0
                viewModel.panchayatIdVending = 0
            }.show()
    }


    private fun showDropDownDistrictDialog() {
        var index = 0
        val list = arrayOfNulls<String>(viewModel.itemDistrictVending.size)
        for (value in viewModel.itemDistrictVending) {
            list[index] = value.name
            index++
        }
        MaterialAlertDialogBuilder(requireView().context, R.style.DialogTheme)
            .setTitle(resources.getString(R.string.select_district))
            .setItems(list) { _, which ->
                binding.editTextSelectDistrict.setText(list[which])
                viewModel.districtIdVending = viewModel.itemDistrictVending[which].id
                view?.let { viewModel.pincodeCurrent(it, viewModel.districtIdVending) }
                if(viewModel.stateIdVending != 0 && viewModel.districtIdVending != 0){
                    view?.let { viewModel.localOrganisation(it, JSONObject().apply {
                        put("state_id", viewModel.stateIdVending)
                        put("district_id", viewModel.districtIdVending)
                    })}
                }

                binding.editTextSelectPincode.setText("")
                viewModel.pincodeIdVending = ""
            }.show()
    }


    private fun showDropDownPanchayatDialog() {
        var index = 0
        val list = arrayOfNulls<String>(viewModel.itemPanchayatVending.size)
        for (value in viewModel.itemPanchayatVending) {
            list[index] = value.name
            index++
        }
        MaterialAlertDialogBuilder(requireView().context, R.style.DialogTheme)
            .setTitle(resources.getString(R.string.municipality_panchayat))
            .setItems(list) { _, which ->
                binding.editTextMunicipalityPanchayat.setText(list[which])
                viewModel.panchayatIdVending = viewModel.itemPanchayatVending[which].id
            }.show()
    }


    private fun showDropDownPincodeDialog() {
        var index = 0
        val list = arrayOfNulls<String>(viewModel.itemPincodeVending.size)
        for (value in viewModel.itemPincodeVending) {
            list[index] = value.pincode
            index++
        }
        MaterialAlertDialogBuilder(requireView().context, R.style.DialogTheme)
            .setTitle(resources.getString(R.string.select_pincode))
            .setItems(list) { _, which ->
                binding.editTextSelectPincode.setText(list[which])
                viewModel.pincodeIdVending = binding.editTextSelectPincode.text.toString()
            }.show()
    }


    private fun showDropDownLocalOrganisationDialog() {
        var index = 0
        val list = arrayOfNulls<String>(viewModel.itemLocalOrganizationVending.size)
        for (value in viewModel.itemLocalOrganizationVending) {
            list[index] = value.local_organisation_name
            index++
        }
        MaterialAlertDialogBuilder(requireView().context, R.style.DialogTheme)
            .setTitle(resources.getString(R.string.localOrganisation))
            .setItems(list) { _, which ->
                binding.editTextLocalOrganisation.setText(list[which])
                viewModel.localOrganizationIdVending = viewModel.itemLocalOrganizationVending[which].id
            }.show()
    }




    override fun onCallBack(pos: Int) {
        binding.apply {
            if (pos == 3) {
                if (editTextTypeofMarketPlace.text.toString().isEmpty()) {
                    showSnackBar(getString(R.string.type_of_market_place))
                } else if (viewModel.marketplaceId == 7 && editTextTypeofMarketPlaceEnter.text.toString()
                        .isEmpty()
                ) {
                    showSnackBar(getString(R.string.enter_market_place))
                } else if (editTextTypeofVending.text.toString().isEmpty()) {
                    showSnackBar(getString(R.string.type_of_vending))
                } else if (viewModel.vendingId == 11 && editTextTypeofVendingEnter.text.toString().isEmpty()) {
                    showSnackBar(getString(R.string.enter_vending))
                } else if (editTextTotalYearsofVending.text.toString().isEmpty()) {
                    showSnackBar(getString(R.string.total_years_of_vending))
                } else if (editTextVendingTimeOpen.text.toString().isEmpty()) {
                    showSnackBar(getString(R.string.open_time))
                } else if (editTextVendingTimeClose.text.toString().isEmpty()) {
                    showSnackBar(getString(R.string.close_time))
                } else if (!(viewModel.stateIdVending > 0)) {
                    showSnackBar(getString(R.string.select_state))
                } else if (!(viewModel.districtIdVending > 0)) {
                    showSnackBar(getString(R.string.select_district))
                } else if (!(viewModel.panchayatIdVending > 0)) {
                    showSnackBar(getString(R.string.municipality_panchayat))
                } else if (editTextAddress.text.toString().isEmpty()) {
                    showSnackBar(getString(R.string.address_mention_village))
                } else if (binding.ivRdLocalOrgnaizationYes.isChecked == true && editTextLocalOrganisation.text.toString().isEmpty()) {
                    showSnackBar(getString(R.string.localOrganisation))
                } else if (viewModel.data.shopImage == null) {
                    showSnackBar(getString(R.string.upload_shop_image))
                } else {
                    val schemeName = StringBuffer()
                    if(binding.ivRdGovernmentYes.isChecked == true && binding.ivRdGovernmentNo.isChecked == false){
                        //Log.e(TAG, "schemeNameAA ")
                        viewModel.data.governmentScheme = binding.ivRdGovernmentYes.isChecked
                        if(binding.inclideGovernment.cbRememberPMSwanidhiScheme.isChecked == true || binding.inclideGovernment.cbRememberOthersPleaseName.isChecked == true){
                            if(binding.inclideGovernment.cbRememberOthersPleaseName.isChecked == true && binding.inclideGovernment.editTextSchemeName.text.toString().isEmpty()){
                                showSnackBar(getString(R.string.scheme_name))
                            }else{
                                if(binding.inclideGovernment.cbRememberPMSwanidhiScheme.isChecked == true){
                                    schemeName.append(getString(R.string.pm_swanidhi_schemeSingle)+" ")
                                }
                                if(binding.inclideGovernment.cbRememberOthersPleaseName.isChecked == true){
                                    schemeName.append(binding.inclideGovernment.editTextSchemeName.text.toString())
                                }
                                viewModel.data.schemeName = schemeName.toString()
                                setAddData()
                            }
                        } else {
                            showSnackBar(getString(R.string.select_scheme))
                        }
                    } else if(binding.ivRdGovernmentYes.isChecked == false && binding.ivRdGovernmentNo.isChecked == true){
                        viewModel.data.governmentScheme = binding.ivRdGovernmentYes.isChecked
                        viewModel.data.schemeName = ""
                        setAddData()
                    }
                }
            }
        }
    }

    private fun setAddData() {
        binding.apply {
            viewModel.data.type_of_marketplace = viewModel.marketplaceId.toString()
            viewModel.data.marketpalce_others = editTextTypeofMarketPlaceEnter.text.toString()
            viewModel.data.type_of_vending = viewModel.vendingId.toString()
            viewModel.data.vending_others = editTextTypeofVendingEnter.text.toString()

            viewModel.data.total_years_of_business = editTextTotalYearsofVending.text.toString()
            viewModel.data.vending_state = ""+viewModel.stateIdVending
            viewModel.data.vending_district = ""+viewModel.districtIdVending
            viewModel.data.vending_municipality_panchayat = ""+viewModel.panchayatIdVending
            viewModel.data.vending_pincode = ""+viewModel.pincodeIdVending
            viewModel.data.vending_address = ""+editTextAddress.text.toString()
            viewModel.data.localOrganisation = ""+viewModel.localOrganizationIdVending

            viewModel.data.documentDetails = viewModel.documentDetails
            viewModel.data.ImageUploadCOVBoolean = binding.inclideDocuments.cbRememberImageUploadCOV.isChecked
            viewModel.data.ImageUploadLORBoolean = binding.inclideDocuments.cbRememberImageUploadLOR.isChecked
            viewModel.data.UploadSurveyReceiptBoolean = binding.inclideDocuments.cbRememberUploadSurveyReceipt.isChecked
            viewModel.data.UploadChallanBoolean = binding.inclideDocuments.cbRememberUploadChallan.isChecked
            viewModel.data.UploadApprovalLetterBoolean = binding.inclideDocuments.cbRememberUploadApprovalLetter.isChecked

            viewModel.data.governmentScheme = viewModel.governmentScheme
            viewModel.data.pmSwanidhiScheme = binding.inclideGovernment.cbRememberPMSwanidhiScheme.isChecked
            viewModel.data.otherScheme = binding.inclideGovernment.cbRememberOthersPleaseName.isChecked

            Register.callBackListener!!.onCallBack(4)
        }
    }


    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}