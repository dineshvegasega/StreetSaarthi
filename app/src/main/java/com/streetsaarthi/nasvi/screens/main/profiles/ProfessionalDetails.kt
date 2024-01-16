package com.streetsaarthi.nasvi.screens.main.profiles

import android.Manifest
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
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
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import com.kochia.customer.utils.hideKeyboard
import com.streetsaarthi.nasvi.R
import com.streetsaarthi.nasvi.databinding.ProfessionalDetailsBinding
import com.streetsaarthi.nasvi.datastore.DataStoreKeys
import com.streetsaarthi.nasvi.datastore.DataStoreUtil
import com.streetsaarthi.nasvi.models.login.Login
import com.streetsaarthi.nasvi.networking.getFormDataBody
import com.streetsaarthi.nasvi.screens.interfaces.CallBackListener
import com.streetsaarthi.nasvi.screens.onboarding.networking.USER_TYPE
import com.streetsaarthi.nasvi.utils.getMediaFilePathFor
import com.streetsaarthi.nasvi.utils.loadImage
import com.streetsaarthi.nasvi.utils.showSnackBar
import dagger.hilt.android.AndroidEntryPoint
import id.zelory.compressor.Compressor
import kotlinx.coroutines.launch
import okhttp3.FormBody
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONObject
import java.io.File
import java.util.Calendar


@AndroidEntryPoint
class ProfessionalDetails : Fragment() , CallBackListener {
    private val viewModel: ProfilesVM by activityViewModels()
    private var _binding: ProfessionalDetailsBinding? = null
    private val binding get() = _binding!!

    companion object{
        var callBackListener: CallBackListener? = null
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ProfessionalDetailsBinding.inflate(inflater)
        return binding.root
    }

    var scrollPoistion = 0







    private fun callMediaPermissions() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE){
            activityResultLauncher.launch(
                arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.READ_MEDIA_VIDEO,
                    Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED)
            )
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            activityResultLauncher.launch(
                arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_MEDIA_IMAGES)
            )
        } else{
            activityResultLauncher.launch(
                arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
            )
        }
    }

    var isFree = false
    private val activityResultLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions())
        { permissions ->
            // Handle Permission granted/rejected
            permissions.entries.forEach {
                val permissionName = it.key
                val isGranted = it.value
                Log.e("TAG", "00000 "+permissionName)
                if (isGranted) {
                    Log.e("TAG", "11111"+permissionName)
                    if(isFree){
                        showOptions()
                    }
                    isFree = false
                } else {
                    // Permission is denied
                    Log.e("TAG", "222222"+permissionName)
                }
            }
        }


    var imagePosition = 0
    private var pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        lifecycleScope.launch {
            if (uri != null) {
                when (imagePosition) {
                    1 -> {
                        val compressedImageFile = Compressor.compress(requireContext(), File(requireContext().getMediaFilePathFor(uri)))
                        viewModel.data.shopImage = compressedImageFile.path
                        binding.ivImageShopImage.loadImage(url = { viewModel.data.shopImage!! })
                    }
                    2 -> {
                        val compressedImageFile = Compressor.compress(requireContext(), File(requireContext().getMediaFilePathFor(uri)))
                        viewModel.data.ImageUploadCOV = compressedImageFile.path
                        binding.ivImageCOV.loadImage(url = { viewModel.data.ImageUploadCOV!! })
                    }
                    3 -> {
                    val compressedImageFile = Compressor.compress(requireContext(), File(requireContext().getMediaFilePathFor(uri)))
                        viewModel.data.UploadSurveyReceipt = compressedImageFile.path
                        binding.ivImageSurveyReceipt.loadImage(url = { viewModel.data.UploadSurveyReceipt!! })
                    }
                    4 -> {
                        val compressedImageFile = Compressor.compress(requireContext(), File(requireContext().getMediaFilePathFor(uri)))
                        viewModel.data.ImageUploadLOR = compressedImageFile.path
                        binding.ivImageLOR.loadImage(url = { viewModel.data.ImageUploadLOR!! })
                    }
                    5 -> {
                        val compressedImageFile = Compressor.compress(requireContext(), File(requireContext().getMediaFilePathFor(uri)))
                        viewModel.data.UploadChallan = compressedImageFile.path
                        binding.ivImageUploadChallan.loadImage(url = { viewModel.data.UploadChallan!! })
                    }
                    6 -> {
                        val compressedImageFile = Compressor.compress(requireContext(), File(requireContext().getMediaFilePathFor(uri)))
                        viewModel.data.UploadApprovalLetter = compressedImageFile.path
                        binding.ivImageApprovalLetter.loadImage(url = { viewModel.data.UploadApprovalLetter!! })
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
                        viewModel.data.shopImage = compressedImageFile.path
                        binding.ivImageShopImage.loadImage(url = { viewModel.data.shopImage!! })
                    }
                    2 -> {
                        val compressedImageFile = Compressor.compress(requireContext(), File(requireContext().getMediaFilePathFor(uriReal!!)))
                        viewModel.data.ImageUploadCOV = compressedImageFile.path
                        binding.ivImageCOV.loadImage(url = { viewModel.data.ImageUploadCOV!! })
                    }
                    3 -> {
                        val compressedImageFile = Compressor.compress(requireContext(), File(requireContext().getMediaFilePathFor(uriReal!!)))
                        viewModel.data.UploadSurveyReceipt = compressedImageFile.path
                        binding.ivImageSurveyReceipt.loadImage(url = { viewModel.data.UploadSurveyReceipt!! })
                    }
                    4 -> {
                        val compressedImageFile = Compressor.compress(requireContext(), File(requireContext().getMediaFilePathFor(uriReal!!)))
                        viewModel.data.ImageUploadLOR = compressedImageFile.path
                        binding.ivImageLOR.loadImage(url = { viewModel.data.ImageUploadLOR!! })
                    }
                    5 -> {
                        val compressedImageFile = Compressor.compress(requireContext(), File(requireContext().getMediaFilePathFor(uriReal!!)))
                        viewModel.data.UploadChallan = compressedImageFile.path
                        binding.ivImageUploadChallan.loadImage(url = { viewModel.data.UploadChallan!! })
                    }
                    6 -> {
                        val compressedImageFile = Compressor.compress(requireContext(), File(requireContext().getMediaFilePathFor(uriReal!!)))
                        viewModel.data.UploadApprovalLetter = compressedImageFile.path
                        binding.ivImageApprovalLetter.loadImage(url = { viewModel.data.UploadApprovalLetter!! })
                    }
                }
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e("TAG", "onCreate")
        callBackListener = this
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        callBackListener = this

        binding.apply {
            DataStoreUtil.readData(DataStoreKeys.LOGIN_DATA) { loginUser ->
                if (loginUser != null) {
                    val data = Gson().fromJson(loginUser, Login::class.java)
                    Log.e("TAG", "dataZZ "+data.toString())
                    viewModel.marketplace(view)
                    viewModel.marketPlaceTrue.observe(viewLifecycleOwner, Observer {
                        if(it == true){
                            for (item in viewModel.itemMarketplace) {
                                if (item.marketplace_id == data.type_of_marketplace){
                                    binding.editTextTypeofMarketPlace.setText(""+item.name)
                                }
                            }
                        }
                    })
                    viewModel.marketplaceId = data.type_of_marketplace.toInt()
                    viewModel.data.type_of_marketplace = ""+viewModel.marketplaceId
                    viewModel.data.marketpalce_others = ""+data.marketpalce_others
                    editTextTypeofMarketPlaceEnter.setText("${data.marketpalce_others}")




                    viewModel.vending(view)
                    viewModel.vendingTrue.observe(viewLifecycleOwner, Observer {
                        if(it == true){
                            for (item in viewModel.itemVending) {
                                if (item.vending_id == data.type_of_vending){
                                    binding.editTextTypeofVending.setText(""+item.name)
                                }
                            }
                        }
                    })
                    viewModel.vendingId = data.type_of_vending.toInt()
                    viewModel.data.type_of_vending = ""+viewModel.vendingId
                    viewModel.data.vending_others = ""+data.vending_others
                    editTextTypeofVendingEnter.setText("${data.vending_others}")


                    editTextTotalYearsofVending.setText("${data.total_years_of_business}")
                    editTextVendingTimeOpen.setText("${data.vending_time_from}")
                    editTextVendingTimeClose.setText("${data.vending_time_to}")


                    viewModel.data.total_years_of_business = ""+data.total_years_of_business
                    viewModel.data.open = ""+data.vending_time_from
                    viewModel.data.close = ""+data.vending_time_to

                    data.vending_time_from.let {
                        val datetime = Calendar.getInstance()
                        datetime[Calendar.HOUR_OF_DAY] = data.vending_time_from.split(":")[0].toInt()
                        datetime[Calendar.MINUTE] = data.vending_time_from.split(":")[1].toInt()
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
                            strHrsToShow + ":" + (if (data.vending_time_from.split(":")[1].toString().length == 1) "0"+datetime.get(
                                Calendar.MINUTE)  else datetime.get(Calendar.MINUTE)) + " " + am_pm
                        )
                    }



                    data.vending_time_to.let {
                        val datetime = Calendar.getInstance()
                        datetime[Calendar.HOUR_OF_DAY] = data.vending_time_to.split(":")[0].toInt()
                        datetime[Calendar.MINUTE] = data.vending_time_to.split(":")[1].toInt()
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
                            strHrsToShow + ":" + (if (data.vending_time_to.split(":")[1].toString().length == 1) "0"+datetime.get(
                                Calendar.MINUTE)  else datetime.get(Calendar.MINUTE)) + " " + am_pm
                        )
                    }



                    editTextVendingSelectState.setText("${data.vending_state.name}")
                    editTextVendingSelectDistrict.setText("${data.vending_district.name}")
                    editTextVendingMunicipalityPanchayat.setText("${data.vending_municipality_panchayat.name}")
                    if(data.vending_pincode != null){
                        editTextVendingSelectPincode.setText(""+data.vending_pincode.pincode.toInt())
                    } else {
                        editTextVendingSelectPincode.setText("")
                    }
                    editTextVendingAddress.setText("${data.vending_address}")

                    viewModel.data.vending_state = ""+data.vending_state.id
                    viewModel.data.vending_district = ""+data.vending_district.id
                    viewModel.data.vending_municipality_panchayat = ""+data.vending_municipality_panchayat.id
                    if(data.vending_pincode != null){
                        viewModel.pincodeIdVending = data.vending_pincode.pincode
                    } else {
                        viewModel.pincodeIdVending = ""
                    }
                    viewModel.data.vending_address = ""+data.vending_address


                    Log.e("TAG", "data.local_organisationAA "+data.local_organisation)

                    if (data.local_organisation != null){
                        editTextLocalOrganisation.setText("${data.local_organisation.name}")
                        editTextLocalOrganisation.visibility = View.VISIBLE
                        ivRdLocalOrgnaizationYes.isChecked = true
                        viewModel.data.localOrganisation = ""+data.local_organisation.id
                    } else {
                        editTextLocalOrganisation.visibility = View.GONE
                        ivRdLocalOrgnaizationYes.isChecked = false
                    }

                    viewModel.data.shopImage = data.shop_image.url
                    viewModel.data.ImageUploadCOV = data.cov_image.url
                    viewModel.data.UploadSurveyReceipt = data.survey_receipt_image.url
                    viewModel.data.ImageUploadLOR = data.lor_image.url
                    viewModel.data.UploadChallan = data.challan_image.url
                    viewModel.data.UploadApprovalLetter = data.approval_letter_image.url

                    ivImageShopImage.loadImage(url = { data.shop_image.url })
                    ivImageCOV.loadImage(url = { data.cov_image.url })
                    ivImageSurveyReceipt.loadImage(url = { data.survey_receipt_image.url })
                    ivImageLOR.loadImage(url = { data.lor_image.url })
                    ivImageUploadChallan.loadImage(url = { data.challan_image.url })
                    ivImageApprovalLetter.loadImage(url = { data.approval_letter_image.url })
                }
            }



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


            ivRdLocalOrgnaizationYes.setOnClickListener {
                editTextLocalOrganisation.visibility = View.VISIBLE
                setScrollPosition(1, true)
            }

            ivRdLocalOrgnaizationNo.setOnClickListener {
                editTextLocalOrganisation.visibility = View.GONE
                setScrollPosition(1, false)
                viewModel.data.localOrganisation = "-1"
            }


            editTextLocalOrganisation.setOnClickListener {
                requireActivity().hideKeyboard()
                showDropDownLocalOrganisationDialog()
            }




            btnImageShopImage.setOnClickListener {
                imagePosition = 1
                isFree = true
                callMediaPermissions()
            }
            btnImageCOV.setOnClickListener {
                imagePosition = 2
                isFree = true
                callMediaPermissions()
            }
            btnImageSurveyReceipt.setOnClickListener {
                imagePosition = 3
                isFree = true
                callMediaPermissions()
            }
            btnImageLOR.setOnClickListener {
                imagePosition = 4
                isFree = true
                callMediaPermissions()
            }
            btnImageUploadChallan.setOnClickListener {
                imagePosition = 5
                isFree = true
                callMediaPermissions()
            }
            btnImageApprovalLetter.setOnClickListener {
                imagePosition = 6
                isFree = true
                callMediaPermissions()
            }

            binding.scroll.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
                scrollPoistion = scrollY
            })




            btnAddtoCart.setOnClickListener {
                viewModel.data.marketpalce_others = ""+ editTextTypeofMarketPlaceEnter.text.toString()
                viewModel.data.vending_others = ""+ editTextTypeofVendingEnter.text.toString()

                viewModel.data.vending_address = ""+editTextVendingAddress.text.toString()

                Log.e("TAG", "viewModel2.data2 "+ viewModel.data.toString())



                update()

            }


        }
    }



    private fun setScrollPosition(type : Int, ifTrue: Boolean) {
        when(type){
            1 -> {
                ObjectAnimator.ofInt(binding.scroll, "scrollY",  scrollPoistion).setDuration(100).start()
            }

            2 -> {
//                Handler(Looper.getMainLooper()).postDelayed(Runnable {
//                    val itemHeight =
//                        binding.inclideGovernment.layoutGovernmentScheme.height
//                    binding.scroll.smoothScrollTo(0,scrollPoistion * itemHeight)
//                }, 50)
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
                viewModel.data.type_of_marketplace = ""+viewModel.marketplaceId
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
                viewModel.data.type_of_vending = ""+viewModel.vendingId
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
                viewModel.data.total_years_of_business = list[which]
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
                        strHrsToShow + ":" + (if (minute.toString().length == 1) "0"+datetime.get(
                            Calendar.MINUTE)  else datetime.get(Calendar.MINUTE)) + " " + am_pm
                    )

                    viewModel.data.open = "" + hourOfDay + ":" + (if (minute.toString().length == 1) "0"+minute else minute) + ":00"
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
                        strHrsToShow + ":" + (if (minute.toString().length == 1) "0"+datetime.get(
                            Calendar.MINUTE)  else datetime.get(Calendar.MINUTE)) + " " + am_pm
                    )

                    // viewModel.data.close = strHrsToShow+":"+datetime.get(Calendar.MINUTE)+" "+am_pm
                    viewModel.data.close = "" + hourOfDay + ":" + (if (minute.toString().length == 1) "0"+minute else minute) + ":00"

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
                viewModel.data.localOrganisation = ""+viewModel.localOrganizationIdVending
            }.show()
    }




    private fun showDropDownVendingStateDialog() {
        var index = 0
        val list = arrayOfNulls<String>(viewModel.itemStateVending.size)
        for (value in viewModel.itemStateVending) {
            list[index] = value.name
            index++
        }
        MaterialAlertDialogBuilder(requireView().context, R.style.DialogTheme)
            .setTitle(resources.getString(R.string.select_state))
            .setItems(list) {_,which->
                binding.editTextVendingSelectState.setText(list[which])
                viewModel.stateIdVending =  viewModel.itemStateVending[which].id
                view?.let { viewModel.districtCurrent(it, viewModel.stateIdVending) }
                view?.let { viewModel.panchayatCurrent(it, viewModel.stateIdVending) }
                view?.let { viewModel.localOrganisation(it, JSONObject().apply {
                    put("state_id", viewModel.stateIdVending)
                })}
                viewModel.data.vending_state = ""+viewModel.stateIdVending
                binding.editTextVendingSelectDistrict.setText("")
                binding.editTextVendingMunicipalityPanchayat.setText("")
                viewModel.districtIdVending = 0
                viewModel.panchayatIdVending = 0
            }.show()
    }


    private fun showDropDownVendingDistrictDialog() {
        var index = 0
        val list = arrayOfNulls<String>(viewModel.itemDistrictVending.size)
        for (value in viewModel.itemDistrictVending) {
            list[index] = value.name
            index++
        }
        MaterialAlertDialogBuilder(requireView().context, R.style.DialogTheme)
            .setTitle(resources.getString(R.string.select_district))
            .setItems(list) {_,which->
                binding.editTextVendingSelectDistrict.setText(list[which])
                viewModel.districtIdVending =  viewModel.itemDistrictVending[which].id
                view?.let { viewModel.pincodeCurrent(it, viewModel.districtIdVending) }
                viewModel.data.vending_district = ""+viewModel.districtIdVending
                view?.let { viewModel.localOrganisation(it, JSONObject().apply {
                    put("state_id", viewModel.stateIdVending)
                    put("district_id", viewModel.districtIdVending)
                })}
                binding.editTextVendingSelectPincode.setText("")
                viewModel.districtIdVending = 0
            }.show()
    }


    private fun showDropDownVendingPanchayatDialog() {
        var index = 0
        val list = arrayOfNulls<String>(viewModel.itemPanchayatVending.size)
        for (value in viewModel.itemPanchayatVending) {
            list[index] = value.name
            index++
        }
        MaterialAlertDialogBuilder(requireView().context, R.style.DialogTheme)
            .setTitle(resources.getString(R.string.municipality_panchayat))
            .setItems(list) {_,which->
                binding.editTextVendingMunicipalityPanchayat.setText(list[which])
                viewModel.panchayatIdVending =  viewModel.itemPanchayatVending[which].id
                viewModel.data.vending_municipality_panchayat = ""+viewModel.panchayatIdVending
            }.show()
    }


    private fun showDropDownVendingPincodeDialog() {
        var index = 0
        val list = arrayOfNulls<String>(viewModel.itemPincodeVending.size)
        for (value in viewModel.itemPincodeVending) {
            list[index] = value.pincode
            index++
        }
        MaterialAlertDialogBuilder(requireView().context, R.style.DialogTheme)
            .setTitle(resources.getString(R.string.select_pincode))
            .setItems(list) {_,which->
                binding.editTextVendingSelectPincode.setText(list[which])
//                viewModel.pincodeId =  viewModel.itemPincode[which].id
                viewModel.pincodeIdVending = binding.editTextVendingSelectPincode.text.toString()
                viewModel.data.vending_pincode = ""+viewModel.pincodeIdVending

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

    } catch (e: Exception) {
        e.printStackTrace()
        Log.e("TAG","errorD " + e.message)
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
        Log.e("TAG", "onCallBackProfessional " + pos)
        update()
    }


    private fun update() {
        binding.apply {
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
            } else if (binding.ivRdLocalOrgnaizationYes.isChecked == true && editTextLocalOrganisation.text.toString().isEmpty()) {
                showSnackBar(getString(R.string.localOrganisation))
            } else {
                val requestBody: MultipartBody.Builder = MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("user_role", USER_TYPE)

                if(viewModel.data.type_of_marketplace  != null){
                    requestBody.addFormDataPart("type_of_marketplace", viewModel.data.type_of_marketplace!!)
                }
                if(viewModel.data.marketpalce_others  != null){
                    requestBody.addFormDataPart("marketpalce_others", viewModel.data.marketpalce_others!!)
                }
                if(viewModel.data.type_of_vending  != null){
                    requestBody.addFormDataPart("type_of_vending", viewModel.data.type_of_vending!!)
                }
                if(viewModel.data.vending_others  != null){
                    requestBody.addFormDataPart("vending_others", viewModel.data.vending_others!!)
                }
                if(viewModel.data.total_years_of_business  != null){
                    requestBody.addFormDataPart("total_years_of_business", viewModel.data.total_years_of_business!!)
                }
                if(viewModel.data.open  != null){
                    requestBody.addFormDataPart("vending_time_from", viewModel.data.open!!)
                }
                if(viewModel.data.close  != null){
                    requestBody.addFormDataPart("vending_time_to", viewModel.data.close!!)
                }

                if(viewModel.data.vending_state  != null){
                    requestBody.addFormDataPart("vending_state", viewModel.data.vending_state!!)
                }
                if(viewModel.data.vending_district  != null){
                    requestBody.addFormDataPart("vending_district", viewModel.data.vending_district!!)
                }
                if(viewModel.data.vending_municipality_panchayat  != null){
                    requestBody.addFormDataPart("vending_municipality_panchayat", viewModel.data.vending_municipality_panchayat!!)
                }
                if(viewModel.data.vending_pincode  != null){
                    requestBody.addFormDataPart("vending_pincode", viewModel.data.vending_pincode!!)
                }
                if(viewModel.data.vending_address  != null){
                    requestBody.addFormDataPart("vending_address", viewModel.data.vending_address!!)
                }
//                if(viewModel.data.localOrganisation  != null){
//
//                }
                requestBody.addFormDataPart("local_organisation", ""+viewModel.data.localOrganisation)

//                val requestBody = RequestBody.create(parse.parse("text/plain"), "Test")
//
//                val requestBody: RequestBody = Builder()
//                    .setType(MultipartBody.FORM)
//                    .addFormDataPart("param1", param1)
//                    .addFormDataPart("param2", param2)
//                    .build()
//                val body: RequestBody = RequestBody.create(MediaType.parse("text/plain"), "somevalue")
//                RequestBody requestBody = new MultipartBody.Builder().addFormDataPart("username", username)
//                val image: RequestBody =
//                    MultipartBody.getFormDataBody()
//                requestBody.addPart(image)

//
//                val formBody: RequestBody = FormBody.Builder()
//                    .add("message", null)
//                    .build()
                //requestBody.addPart(MultipartBody.Part.createFormData("local_organisation" , "null")



                //val fileName = s.toRequestBody("multipart/form-data".toMediaTypeOrNull())



//                }

//                if(!docs.toString().isEmpty()){
//                    requestBody.addFormDataPart("vending_documents", docs.toString())
//                } else{
//                    requestBody.addFormDataPart("vending_documents", "null")
//                }

//                if(!viewModel.data.schemeName!!.isEmpty()){
//                    requestBody.addFormDataPart("availed_scheme",viewModel.data.schemeName!!)
//                } else {
//                    requestBody.addFormDataPart("availed_scheme", "null")
//                }




                if(viewModel.data.shopImage != null && (!viewModel.data.shopImage!!.startsWith("http"))){
                    requestBody.addFormDataPart(
                        "shop_image",
                        File(viewModel.data.shopImage!!).name,
                        File(viewModel.data.shopImage!!).asRequestBody("image/*".toMediaTypeOrNull())
                    )
                }

                if(viewModel.data.ImageUploadCOV != null && (!viewModel.data.ImageUploadCOV!!.startsWith("http"))){
                    requestBody.addFormDataPart(
                        "cov_image",
                        File(viewModel.data.ImageUploadCOV!!).name,
                        File(viewModel.data.ImageUploadCOV!!).asRequestBody("image/*".toMediaTypeOrNull())
                    )
                }

                if(viewModel.data.ImageUploadLOR != null && (!viewModel.data.ImageUploadLOR!!.startsWith("http"))){
                    requestBody.addFormDataPart(
                        "lor_image",
                        File(viewModel.data.ImageUploadLOR!!).name,
                        File(viewModel.data.ImageUploadLOR!!).asRequestBody("image/*".toMediaTypeOrNull())
                    )
                }

                if(viewModel.data.UploadSurveyReceipt != null && (!viewModel.data.UploadSurveyReceipt!!.startsWith("http"))){
                    requestBody.addFormDataPart(
                        "survey_receipt_image",
                        File(viewModel.data.UploadSurveyReceipt!!).name,
                        File(viewModel.data.UploadSurveyReceipt!!).asRequestBody("image/*".toMediaTypeOrNull())
                    )
                }

                if(viewModel.data.UploadChallan != null && (!viewModel.data.UploadChallan!!.startsWith("http"))){
                    requestBody.addFormDataPart(
                        "challan_image",
                        File(viewModel.data.UploadChallan!!).name,
                        File(viewModel.data.UploadChallan!!).asRequestBody("image/*".toMediaTypeOrNull())
                    )
                }

                if(viewModel.data.UploadApprovalLetter != null && (!viewModel.data.UploadApprovalLetter!!.startsWith("http"))){
                    requestBody.addFormDataPart(
                        "approval_letter_image",
                        File(viewModel.data.UploadApprovalLetter!!).name,
                        File(viewModel.data.UploadApprovalLetter!!).asRequestBody("image/*".toMediaTypeOrNull())
                    )
                }

                Log.e("TAG", "viewModel.dataAll22 "+viewModel.data.toString())
                DataStoreUtil.readData(DataStoreKeys.LOGIN_DATA) { loginUser ->
                    if (loginUser != null) {
                        val data = Gson().fromJson(loginUser, Login::class.java)
                        viewModel.profileUpdate(view = requireView(), ""+data.id , requestBody.build())
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
