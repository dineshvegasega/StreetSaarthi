package com.streetsaarthi.screens.onboarding.register

import android.Manifest
import android.app.TimePickerDialog
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TimePicker
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
import com.streetsaarthi.databinding.Register2Binding
import com.streetsaarthi.screens.interfaces.CallBackListener
import com.streetsaarthi.utils.getMediaFilePathFor
import com.streetsaarthi.utils.showSnackBar
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.util.Calendar

@AndroidEntryPoint
class Register2  : Fragment() , CallBackListener {

    private var _binding: Register2Binding? = null
    private val binding get() = _binding!!
    private val viewModel: RegisterVM by activityViewModels()

    companion object {
        var TAG = "Register2"
        private val REQUIRED_GALLERY_PERMISSIONS =
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            )
        private const val CAPTURE_IMAGE_REQUEST = 1001
        private const val GALLERY_IMAGE_REQUEST = 1002
        private const val GALLERY_PERMISSION_REQUEST = 1004
        private const val SELECT_ADDRESS_REQUEST_CODE = 1003
        private const val REQUEST_CODE_PERMISSIONS = 1002
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)

        var callBackListener: CallBackListener? = null
    }

    var imagePosition = 0
    private var pickMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                Log.e("PhotoPicker", "Selected URI: $uri")
                //  binding.imageUploadpassportsizeImage.loadImage(url = {  requireContext().getMediaFilePathFor(uri) })
                when (imagePosition) {
                    11 -> {
                        Log.e("TakePicture", "Selected URI1: $imagePosition $uri")
                        // binding.inclideDocuments.cbRememberImageUploadCOV.isChecked = true
                        viewModel.data.ShopImage = requireContext().getMediaFilePathFor(uri)
                        binding.textViewlayoutShopImage.setText(File(viewModel.data.ShopImage!!).name)
                    }

                    1 -> {
                        Log.e("TakePicture", "Selected URI1: $imagePosition $uri")
                        // binding.inclideDocuments.cbRememberImageUploadCOV.isChecked = true
                        viewModel.data.ImageUploadCOV = requireContext().getMediaFilePathFor(uri)
                        binding.inclideDocuments.textViewImageUploadCOV.setText(File(viewModel.data.ImageUploadCOV!!).name)
                    }

                    2 -> {
                        Log.e("TakePicture", "Selected URI2: $imagePosition $uri")
                        //  binding.inclideDocuments.cbRememberImageUploadLOR.isChecked = true
                        viewModel.data.ImageUploadLOR = requireContext().getMediaFilePathFor(uri)
                        binding.inclideDocuments.textViewImageUploadLOR.setText(File(viewModel.data.ImageUploadLOR!!).name)
                    }

                    3 -> {
                        Log.e("TakePicture", "Selected URI3: $imagePosition $uri")
                        //  binding.inclideDocuments.cbRememberUploadSurveyReceipt.isChecked = true
                        viewModel.data.UploadSurveyReceipt =
                            requireContext().getMediaFilePathFor(uri)
                        binding.inclideDocuments.textViewUploadSurveyReceipt.setText(File(viewModel.data.UploadSurveyReceipt!!).name)
                    }

                    4 -> {
                        Log.e("TakePicture", "Selected URI4: $imagePosition $uri")
                        // binding.inclideDocuments.cbRememberUploadChallan.isChecked = true
                        viewModel.data.UploadChallan = requireContext().getMediaFilePathFor(uri)
                        binding.inclideDocuments.textViewUploadChallan.setText(File(viewModel.data.UploadChallan!!).name)
                    }

                    5 -> {
                        Log.e("TakePicture", "Selected URI5: $imagePosition $uri")
                        // binding.inclideDocuments.cbRememberUploadApprovalLetter.isChecked = true
                        viewModel.data.UploadApprovalLetter =
                            requireContext().getMediaFilePathFor(uri)
                        binding.inclideDocuments.textViewUploadApprovalLetter.setText(File(viewModel.data.UploadApprovalLetter!!).name)
                    }
                }
            } else {
                Log.e("PhotoPicker", "No media selected")
            }
        }


    var uriReal: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
    }


    val captureMedia = registerForActivityResult(ActivityResultContracts.TakePicture()) { uri ->
        if (uri == true) {
            Log.e("PhotoPicker", "Selected URI: $uriReal")
            //  binding.imageUploadpassportsizeImage.loadImage(url = {  requireContext().getMediaFilePathFor(uriReal) })
            when (imagePosition) {
                11 -> {
                    Log.e("TakePicture", "Selected URI5: $imagePosition $uri")
                    // binding.inclideDocuments.cbRememberUploadApprovalLetter.isChecked = true
                    viewModel.data.ShopImage = requireContext().getMediaFilePathFor(uriReal!!)
                    binding.textViewlayoutShopImage.setText(File(viewModel.data.ShopImage!!).name)
                }

                1 -> {
                    Log.e("TakePicture", "Selected URI1: $imagePosition $uri")
                    // binding.inclideDocuments.cbRememberImageUploadCOV.isChecked = true
                    viewModel.data.ImageUploadCOV = requireContext().getMediaFilePathFor(uriReal!!)
                    binding.inclideDocuments.textViewImageUploadCOV.setText(File(viewModel.data.ImageUploadCOV!!).name)
                }

                2 -> {
                    Log.e("TakePicture", "Selected URI2: $imagePosition $uri")
                    // binding.inclideDocuments.cbRememberImageUploadLOR.isChecked = true
                    viewModel.data.ImageUploadLOR = requireContext().getMediaFilePathFor(uriReal!!)
                    binding.inclideDocuments.textViewImageUploadLOR.setText(File(viewModel.data.ImageUploadLOR!!).name)
                }

                3 -> {
                    Log.e("TakePicture", "Selected URI3: $imagePosition $uri")
                    // binding.inclideDocuments.cbRememberUploadSurveyReceipt.isChecked = true
                    viewModel.data.UploadSurveyReceipt =
                        requireContext().getMediaFilePathFor(uriReal!!)
                    binding.inclideDocuments.textViewUploadSurveyReceipt.setText(File(viewModel.data.UploadSurveyReceipt!!).name)
                }

                4 -> {
                    Log.e("TakePicture", "Selected URI4: $imagePosition $uri")
                    // binding.inclideDocuments.cbRememberUploadChallan.isChecked = true
                    viewModel.data.UploadChallan = requireContext().getMediaFilePathFor(uriReal!!)
                    binding.inclideDocuments.textViewUploadChallan.setText(File(viewModel.data.UploadChallan!!).name)
                }

                5 -> {
                    Log.e("TakePicture", "Selected URI5: $imagePosition $uri")
                    // binding.inclideDocuments.cbRememberUploadApprovalLetter.isChecked = true
                    viewModel.data.UploadApprovalLetter =
                        requireContext().getMediaFilePathFor(uriReal!!)
                    binding.inclideDocuments.textViewUploadApprovalLetter.setText(File(viewModel.data.UploadApprovalLetter!!).name)
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

            editTextTypeofMarketPlace.setOnClickListener {
                requireActivity().hideKeyboard()
                showDropDownMarketPlaceDialog()
            }

            editTextTypeofVending.setOnClickListener {
                requireActivity().hideKeyboard()
                showDropDownVendingDialog()
            }

            editTextTotalYearsofVending.setOnClickListener {
                requireActivity().hideKeyboard()
                showDropDownYearsDialog()
            }

            editTextVendingTimeOpen.setOnClickListener {
                requireActivity().hideKeyboard()
                showOpenDialog()
            }
            editTextVendingTimeClose.setOnClickListener {
                requireActivity().hideKeyboard()
                showCloseDialog()
            }

            viewModel.stateCurrent(view)
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

            editTextLocalOrganisation.setOnClickListener {
                requireActivity().hideKeyboard()
                showDropDownLocalOrganisationDialog()
            }


            layoutShopImage.setOnClickListener {
                imagePosition = 11
                showOptions()
            }


            ivRdDocumentYes.setOnClickListener {
                viewModel.documentDetails = true
                inclideDocuments.layoutDocuments.visibility = View.VISIBLE
//                val heightOfNewView: Int = visibilityChangedView.getMeasuredHeight()
//                val currentScrollY: Int = binding.scroll.getScrollY()
                //binding.scroll.scrollTo(0, currentScrollY + heightOfNewView)

//                scroll.smoothScrollTo(0, inclideDocuments.layoutDocuments.getY().toInt());
//                //binding.scroll.smoothScrollTo (View.FOCUS_DOWN);
//                scroll.setSmoothScrollingEnabled(true);

            }
            ivRdDocumentNo.setOnClickListener {
                viewModel.documentDetails = false
                inclideDocuments.layoutDocuments.visibility = View.GONE
            }

            inclideDocuments.layoutImageUploadCOV.setOnClickListener {
                imagePosition = 1
                showOptions()
            }
            inclideDocuments.layoutImageUploadLOR.setOnClickListener {
                imagePosition = 2
                showOptions()
            }
            inclideDocuments.layoutUploadSurveyReceipt.setOnClickListener {
                imagePosition = 3
                showOptions()
            }
            inclideDocuments.layoutUploadChallan.setOnClickListener {
                imagePosition = 4
                showOptions()
            }
            inclideDocuments.layoutUploadApprovalLetter.setOnClickListener {
                imagePosition = 5
                showOptions()
            }



            ivRdGovernmentYes.setOnClickListener {
                viewModel.governmentScheme = true
                inclideGovernment.layoutGovernmentScheme.visibility = View.VISIBLE
            }

            ivRdGovernmentNo.setOnClickListener {
                viewModel.governmentScheme = false
                inclideGovernment.layoutGovernmentScheme.visibility = View.GONE
            }


//            btUploadpassportsizeImage.setOnClickListener {
//                requireActivity().hideKeyboard()
//
////                mGetContent.launch(uriReal)
//
//                pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
//
////                showOptions()
//
//
////                PhotoPickerFragment.newInstance(
////                    multiple = false,
////                    allowCamera = true,
////                    maxSelection = 1,
//////                    theme = STYLE_ALIAS.ChiliPhotoPicker
////                ).show(childFragmentManager, javaClass.name)
//
////                if (
////                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
////                    (
////                            ContextCompat.checkSelfPermission(requireContext(), READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED ||
////                                    ContextCompat.checkSelfPermission(requireContext(), READ_MEDIA_VIDEO) == PackageManager.PERMISSION_GRANTED
////                            )
////                ) {
////                    // Full access on Android 13 (API level 33) or higher
////                    Log.e(TAG, "aaaaaaaaa0")
////                } else if (
////                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE &&
////                    ContextCompat.checkSelfPermission(requireContext(), READ_MEDIA_VISUAL_USER_SELECTED) == PackageManager.PERMISSION_GRANTED
////                ) {
////                    // Partial access on Android 14 (API level 34) or higher
////                    Log.e(TAG, "aaaaaaaaa1")
////                }  else if (ContextCompat.checkSelfPermission(requireContext(), READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
////                    // Full access up to Android 12 (API level 32)
////                    Log.e(TAG, "aaaaaaaaa2")
////                } else if (ContextCompat.checkSelfPermission(requireContext(), READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
////                    // Full access up to Android 12 (API level 32)
////                    Log.e(TAG, "aaaaaaaaa2")
////                } else {
////                    // Access denied
////                    Log.e(TAG, "aaaaaaaaa3")
////                }
//
//
//
//
//// Permission request logic
////                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
////                    requestPermissions.launch(arrayOf(READ_MEDIA_IMAGES, READ_MEDIA_VIDEO, READ_MEDIA_VISUAL_USER_SELECTED))
////                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
////                    requestPermissions.launch(arrayOf(READ_MEDIA_IMAGES, READ_MEDIA_VIDEO))
////                } else {
////                    requestPermissions.launch(arrayOf(READ_EXTERNAL_STORAGE))
////                }
//
//            }
        }
    }

//    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
//    override fun onStart() {
//        super.onStart()
//        when {
//            PermissionUtils.isAccessREAD_MEDIA_IMAGESGranted( requireContext()) -> {
//                checkPremissions()
//            }
//            PermissionUtils.isAccessCAMERAGranted(requireContext()) -> {
//                checkPremissions()
//            }
//            else -> {
//                val permission = ContextCompat.checkSelfPermission(
//                    requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
//
//                if (permission != PackageManager.PERMISSION_GRANTED) {
//                    permissionsResultCallback.launch(Manifest.permission.ACCESS_FINE_LOCATION)
//                } else {
//                    println("Permission isGranted")
//                }
//            }
//        }
//    }


//    private fun checkPremissions() {
//        when {
//            PermissionUtils.isEnabled( requireContext()) -> {
//                //setUpLocationListener()
//            }
//            else -> {
//                AlertDialog.Builder(requireContext())
//                    .setTitle(requireContext().getString(R.string.enable_gps))
//                    .setMessage(requireContext().getString(R.string.required_for_this_app))
//                    .setCancelable(false)
//                    .setPositiveButton(requireContext().getString(R.string.enable_now)) { _, _ ->
//                        val viewIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
//                        someActivityResultLauncher.launch(viewIntent)
//
//                    }
//                    .show()
//            }
//        }
//    }


    private fun showOptions() = try {
        val dialogView = layoutInflater.inflate(R.layout.dialog_choose_image_option, null)
        val btnCancel = dialogView.findViewById<AppCompatButton>(R.id.btnCancel)
        val tvPhotos = dialogView.findViewById<AppCompatTextView>(R.id.tvPhotos)
        val tvPhotosDesc = dialogView.findViewById<AppCompatTextView>(R.id.tvPhotosDesc)
        val tvCamera = dialogView.findViewById<AppCompatTextView>(R.id.tvCamera)
        val tvCameraDesc = dialogView.findViewById<AppCompatTextView>(R.id.tvCameraDesc)
        val dialog = BottomSheetDialog(requireContext(), R.style.TransparentDialog)
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
        Log.e("TAG", "errorD " + e.message)
    }

    private fun forCamera() {
        requireActivity().runOnUiThread() {
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
//                editProfileVM.gender.value = list[which]
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
                        strHrsToShow + ":" + datetime.get(
                            Calendar.MINUTE
                        ) + " " + am_pm
                    )
                    //viewModel.data.open = strHrsToShow+":"+datetime.get(Calendar.MINUTE)+" "+am_pm
                    viewModel.data.open = "" + hourOfDay + ":" + minute + ":00"
                    // Log.e("LOG", "DateToStringConversionAA " +getTimeStampFromMillis(datetime.timeInMillis, "HH:mm"))
                    //  viewModel.data.start = getTimeStampFromMillis(datetime.timeInMillis, "HH:mm")
                    Log.e("TAG", "AAAA " + viewModel.data.open)
                }
            },
            hour,
            minute,
            false
        )
        mTimePicker.show()


//        val timeSetListener =
//            OnTimeSetListener { view, hourOfDay, minute ->
//                val datetime = Calendar.getInstance()
//                datetime[Calendar.HOUR_OF_DAY] = hourOfDay
//                datetime[Calendar.MINUTE] = minute
//                val strHrsToShow =
//                    if (datetime.get(Calendar.HOUR) === 0) "12" else Integer.toString(
//                        datetime.get(Calendar.HOUR)
//                    )
//                var am_pm = ""
//                if (datetime.get(Calendar.AM_PM) == Calendar.AM)
//                    am_pm = "AM";
//                else if (datetime.get(Calendar.AM_PM) == Calendar.PM)
//                    am_pm = "PM";
//                binding.editTextVendingTimeOpen.setText(strHrsToShow+":"+datetime.get(Calendar.MINUTE)+" "+am_pm)
//                viewModel.data.open = ""+hourOfDay+":"+minute+":00"
//                Log.e("TAG", "AAAA "+ viewModel.data.open)
//            }
//        val timePickerDialog = CustomTimePickerDialog(
//            requireContext(), timeSetListener,
//            Calendar.getInstance()[Calendar.HOUR],
//            CustomTimePickerDialog.getRoundedMinute(Calendar.getInstance()[Calendar.MINUTE] + CustomTimePickerDialog.TIME_PICKER_INTERVAL),
//            false,
//            R.style.TimeDialogTheme
//        )
//        timePickerDialog.show()

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
                        strHrsToShow + ":" + datetime.get(
                            Calendar.MINUTE
                        ) + " " + am_pm
                    )
                    // viewModel.data.close = strHrsToShow+":"+datetime.get(Calendar.MINUTE)+" "+am_pm
                    viewModel.data.close = "" + hourOfDay + ":" + minute + ":00"
                    // Log.e("LOG", "DateToStringConversionAA " +getTimeStampFromMillis(datetime.timeInMillis, "HH:mm"))
                    //  viewModel.data.start = getTimeStampFromMillis(datetime.timeInMillis, "HH:mm")
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
        val list = arrayOfNulls<String>(viewModel.itemStateCurrent.size)
        for (value in viewModel.itemStateCurrent) {
            list[index] = value.name
            index++
        }
        MaterialAlertDialogBuilder(requireView().context, R.style.DialogTheme)
            .setTitle(resources.getString(R.string.select_state))
            .setItems(list) { _, which ->
                binding.editTextSelectState.setText(list[which])
                viewModel.stateIdCurrent = viewModel.itemStateCurrent[which].id
                view?.let { viewModel.districtCurrent(it, viewModel.stateIdCurrent) }
                view?.let { viewModel.panchayatCurrent(it, viewModel.stateIdCurrent) }
               // view?.let { viewModel.localOrganisation(it, viewModel.stateIdCurrent, viewModel.districtIdCurrent) }
            }.show()
    }


    private fun showDropDownDistrictDialog() {
        var index = 0
        val list = arrayOfNulls<String>(viewModel.itemDistrictCurrent.size)
        for (value in viewModel.itemDistrictCurrent) {
            list[index] = value.name
            index++
        }
        MaterialAlertDialogBuilder(requireView().context, R.style.DialogTheme)
            .setTitle(resources.getString(R.string.select_district))
            .setItems(list) { _, which ->
                binding.editTextSelectDistrict.setText(list[which])
                viewModel.districtIdCurrent = viewModel.itemDistrictCurrent[which].id
                view?.let { viewModel.pincodeCurrent(it, viewModel.districtIdCurrent) }
               // view?.let { viewModel.localOrganisation(it, viewModel.stateIdCurrent, viewModel.districtIdCurrent) }
            }.show()
    }


    private fun showDropDownPanchayatDialog() {
        var index = 0
        val list = arrayOfNulls<String>(viewModel.itemPanchayatCurrent.size)
        for (value in viewModel.itemPanchayatCurrent) {
            list[index] = value.name
            index++
        }
        MaterialAlertDialogBuilder(requireView().context, R.style.DialogTheme)
            .setTitle(resources.getString(R.string.municipality_panchayat))
            .setItems(list) { _, which ->
                binding.editTextMunicipalityPanchayat.setText(list[which])
                viewModel.panchayatIdCurrent = viewModel.itemPanchayatCurrent[which].id
            }.show()
    }


    private fun showDropDownPincodeDialog() {
        var index = 0
        val list = arrayOfNulls<String>(viewModel.itemPincodeCurrent.size)
        for (value in viewModel.itemPincodeCurrent) {
            list[index] = value.pincode
            index++
        }
        MaterialAlertDialogBuilder(requireView().context, R.style.DialogTheme)
            .setTitle(resources.getString(R.string.select_pincode))
            .setItems(list) { _, which ->
                binding.editTextSelectPincode.setText(list[which])
//                viewModel.pincodeIdCurrent = viewModel.itemPincodeCurrent[which].id
                viewModel.pincodeIdCurrent = binding.editTextSelectPincode.text.toString().toInt()
            }.show()
    }


    private fun showDropDownLocalOrganisationDialog() {
        var index = 0
        val list = arrayOfNulls<String>(viewModel.itemLocalOrganizationCurrent.size)
        for (value in viewModel.itemLocalOrganizationCurrent) {
            list[index] = value.local_organization_name
            index++
        }
        MaterialAlertDialogBuilder(requireView().context, R.style.DialogTheme)
            .setTitle(resources.getString(R.string.localOrganisation))
            .setItems(list) { _, which ->
                binding.editTextLocalOrganisation.setText(list[which])
                viewModel.localOrganizationIdCurrent = viewModel.itemLocalOrganizationCurrent[which].id
            }.show()
    }




    override fun onCallBack(pos: Int) {
        Log.e("TAG", "onCallBackB " + pos)
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
                } else if (!(viewModel.stateIdCurrent > 0)) {
                    showSnackBar(getString(R.string.select_state))
                } else if (!(viewModel.districtIdCurrent > 0)) {
                    showSnackBar(getString(R.string.select_district))
                } else if (!(viewModel.panchayatIdCurrent > 0)) {
                    showSnackBar(getString(R.string.municipality_panchayat))
                } else if (editTextAddress.text.toString().isEmpty()) {
                    showSnackBar(getString(R.string.address_mention_village))
                } else if (viewModel.data.ShopImage == null) {
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
                                Log.e(TAG, "schemeNameAA22 ")
                                if(binding.inclideGovernment.cbRememberPMSwanidhiScheme.isChecked == true){
                                    schemeName.append(getString(R.string.pm_swanidhi_scheme)+", ")
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
                        Log.e(TAG, "schemeNameBB ")
                        viewModel.data.governmentScheme = binding.ivRdGovernmentYes.isChecked
                        viewModel.data.schemeName = ""
                        setAddData()
                    }
                    Log.e(TAG, "schemeNameCC " + schemeName.toString())
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
            viewModel.data.birth_state = ""+viewModel.stateIdCurrent
            viewModel.data.birth_district = ""+viewModel.districtIdCurrent
            viewModel.data.municipality_panchayat_birth = ""+viewModel.panchayatIdCurrent
            viewModel.data.birth_pincode = ""+viewModel.pincodeIdCurrent
            viewModel.data.birth_address = ""+editTextAddress.text.toString()

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


}