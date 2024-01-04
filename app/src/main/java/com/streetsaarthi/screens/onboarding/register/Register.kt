package com.streetsaarthi.screens.onboarding.register

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.demo.utils.PermissionUtils
import com.streetsaarthi.screens.onboarding.networking.USER_TYPE
import com.streetsaarthi.R
import com.streetsaarthi.databinding.RegisterBinding
import com.streetsaarthi.screens.interfaces.CallBackListener
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONObject
import java.io.File
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat


@AndroidEntryPoint
class Register : Fragment() , CallBackListener {
    private var _binding: RegisterBinding? = null
    private val binding get() = _binding!!
    private val viewModel: RegisterVM by activityViewModels()


    companion object{
        var callBackListener: CallBackListener? = null

//        const val ALL_PERMISSIONS = 10
//        @RequiresApi(Build.VERSION_CODES.TIRAMISU)
//        private val REQUIRED_PERMISSIONS =
//            mutableListOf (
////                Manifest.permission.READ_MEDIA_IMAGES,
////                Manifest.permission.WRITE_EXTERNAL_STORAGE
//                Manifest.permission.CAMERA
//            )
//                .apply {
////                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
////                    add(Manifest.permission.READ_MEDIA_IMAGES)
////                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
////                    add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                    add(Manifest.permission.CAMERA)
//            }.toTypedArray()

//        private val REQUIRED_GALLERY_PERMISSIONS =
//            arrayOf(
//                Manifest.permission.READ_EXTERNAL_STORAGE,
//                Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                Manifest.permission.CAMERA
//            )
//        const val CAPTURE_IMAGE_REQUEST = 1001
//        const val GALLERY_IMAGE_REQUEST = 1002
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = RegisterBinding.inflate(inflater)
        return binding.root
    }

var tabPosition: Int = 0;


//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        when (requestCode) {
//            ALL_PERMISSIONS -> {
//                if (grantResults.all { it == PackageManager.PERMISSION_GRANTED }) else {
//
//                }
//            }
//        }
//    }

//
//    private fun checkPremissions() {
//        when {
//            PermissionUtils.isEnabled( requireContext()) -> {
////                setUpLocationListener()
//                Log.e("TAG", "CCCCCCCCCC")
//            }
//            else -> {
//                AlertDialog.Builder(requireContext())
//                    .setTitle(requireContext().getString(R.string.enterOtp))
//                    .setMessage(requireContext().getString(R.string.live_notices))
//                    .setCancelable(false)
//                    .setPositiveButton(requireContext().getString(R.string.all_notices)) { _, _ ->
//                        val viewIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
//                        someActivityResultLauncher.launch(viewIntent)
//
//                    }
//                    .show()
//            }
//        }
//    }
//
//
//
//    var someActivityResultLauncher = registerForActivityResult<Intent, ActivityResult>(
//        ActivityResultContracts.StartActivityForResult()
//    ) { result ->
////        setUpLocationListener()
//        Log.e("TAG", "BBBBBBB")
//    }
//
//    private val permissionsResultCallback = registerForActivityResult(
//        ActivityResultContracts.RequestPermission()){
//        when (it) {
//            true -> {
//                checkPremissions()
//            }
//            false -> {
//                Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        callBackListener = this


//        when {
//            PermissionUtils.isAccessCAMERAGranted( requireContext()) -> {
//                checkPremissions()
//            }
//            else -> {
//                val permission = ContextCompat.checkSelfPermission(
//                    requireContext(), Manifest.permission.CAMERA)
//
//                if (permission != PackageManager.PERMISSION_GRANTED) {
//                    permissionsResultCallback.launch(Manifest.permission.CAMERA)
//                } else {
//                    println("Permission isGranted")
//                    Log.e("TAG", "ZZZZZZZ")
//                }
//            }
//        }

       // ActivityCompat.requestPermissions(requireActivity(), REQUIRED_PERMISSIONS, ALL_PERMISSIONS)

        binding.apply {
            var adapter= RegisterAdapter(requireActivity())
            adapter.notifyDataSetChanged()
            introViewPager.adapter=adapter
            introViewPager.setUserInputEnabled(false);


            viewModel.isSendMutable.observe(viewLifecycleOwner, Observer {
                if (it == true){
                    btSignIn.setEnabled(true)
                    btSignIn.setBackgroundTintList(
                        ColorStateList.valueOf(
                            ResourcesCompat.getColor(
                                getResources(), R.color._E79D46, null)))
                }
            })

//            var screen = arguments?.getString(Screen)
//            if (screen == QuickRegister){
//                loading.visibility = View.GONE
//            } else if (screen == CompleteRegister){
//                loading.visibility = View.VISIBLE
//            } else if (screen == LoginPassword){
//               // loading.visibility = View.GONE
//            }

            loadProgress(tabPosition)

            btSignIn.setOnClickListener {
                    if (tabPosition == 0){
                        Register1.callBackListener!!.onCallBack(1)
                        btSignIn.setText(getString(R.string.continues))
                    } else if (tabPosition == 1){
                        Register2.callBackListener!!.onCallBack(3)
                        btSignIn.setText(getString(R.string.RegisterNow))
                    }else if (tabPosition == 2){
                        Register3.callBackListener!!.onCallBack(5)
                    }
                loadProgress(tabPosition)


            }

            textBack.setOnClickListener {
                Log.e("Selected_PagetabPosition", ""+tabPosition)
                if (tabPosition == 0){
                    view.findNavController().navigateUp()
                } else if (tabPosition == 1){
                    introViewPager.setCurrentItem(0, false)
                    btSignIn.setText(getString(R.string.continues))
                    btSignIn.setEnabled(true)
                    btSignIn.setBackgroundTintList(
                        ColorStateList.valueOf(
                            ResourcesCompat.getColor(
                                getResources(), R.color._E79D46, null)))
                }else if (tabPosition == 2){
                    introViewPager.setCurrentItem(1, false)
                    btSignIn.setText(getString(R.string.continues))
                    btSignIn.setEnabled(true)
                    btSignIn.setBackgroundTintList(
                        ColorStateList.valueOf(
                            ResourcesCompat.getColor(
                                getResources(), R.color._E79D46, null)))
                }
                loadProgress(tabPosition)
            }

            introViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                    super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                }

                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    tabPosition = position
                    Log.e("Selected_Page", position.toString())
                    if(position == 2){
                        btSignIn.setEnabled(false)
                        btSignIn.setBackgroundTintList(
                            ColorStateList.valueOf(
                                ResourcesCompat.getColor(
                                    getResources(), R.color._999999, null)))
                    }
                }

                override fun onPageScrollStateChanged(state: Int) {
                    super.onPageScrollStateChanged(state)
                }
            })
        }

    }

    private fun loadProgress(tabPosition: Int) {
        if (tabPosition == 0){
            binding.view1.setBackgroundTintList(ColorStateList.valueOf(
                ResourcesCompat.getColor(
            getResources(), R.color._E79D46, null)))
            binding.view2.setBackgroundTintList(ColorStateList.valueOf(
                ResourcesCompat.getColor(
                    getResources(), R.color._D9D9D9, null)))
            binding.view3.setBackgroundTintList(ColorStateList.valueOf(
                ResourcesCompat.getColor(
                    getResources(), R.color._D9D9D9, null)))
        }else if (tabPosition == 1){
            binding.view1.setBackgroundTintList(ColorStateList.valueOf(
                ResourcesCompat.getColor(
                    getResources(), R.color._E79D46, null)))
            binding.view2.setBackgroundTintList(ColorStateList.valueOf(
                ResourcesCompat.getColor(
                    getResources(), R.color._E79D46, null)))
            binding.view3.setBackgroundTintList(ColorStateList.valueOf(
                ResourcesCompat.getColor(
                    getResources(), R.color._D9D9D9, null)))
        } else if (tabPosition == 2){
            binding.view1.setBackgroundTintList(ColorStateList.valueOf(
                ResourcesCompat.getColor(
                    getResources(), R.color._E79D46, null)))
            binding.view2.setBackgroundTintList(ColorStateList.valueOf(
                ResourcesCompat.getColor(
                    getResources(), R.color._E79D46, null)))
            binding.view3.setBackgroundTintList(ColorStateList.valueOf(
                ResourcesCompat.getColor(
                    getResources(), R.color._E79D46, null)))
        }
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onCallBack(pos: Int) {
        Log.e("TAG", "onCallBack " + pos)
        binding.apply {
            if (pos == 2){
                introViewPager.setCurrentItem(1, false)
                btSignIn.setText(getString(R.string.continues))
            } else if (pos == 4) {
                introViewPager.setCurrentItem(2, false)
                btSignIn.setText(getString(R.string.RegisterNow))
            } else if (pos == 6) {
                var docs = StringBuffer()
                if(viewModel.data.ImageUploadCOVBoolean == true){
                    docs.append(getString(R.string.COVText))
                }
                if(viewModel.data.ImageUploadLORBoolean == true){
                    docs.append(getString(R.string.LORText))
                }
                if(viewModel.data.UploadSurveyReceiptBoolean == true){
                    docs.append(getString(R.string.Survery_ReceiptText))
                }
                if(viewModel.data.UploadChallanBoolean == true){
                    docs.append(getString(R.string.ChallanText))
                }
                if(viewModel.data.UploadApprovalLetterBoolean == true){
                    docs.append(getString(R.string.Approval_LetterText))
                }

                val requestBody: MultipartBody.Builder = MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("user_role", USER_TYPE)
                    if(viewModel.data.vendor_first_name  != null){
                        requestBody.addFormDataPart("vendor_first_name", viewModel.data.vendor_first_name!!)
                    }
                    if(viewModel.data.vendor_last_name  != null){
                        requestBody.addFormDataPart("vendor_last_name", viewModel.data.vendor_last_name!!)
                    }
                    if(viewModel.data.parent_first_name  != null){
                        requestBody.addFormDataPart("parent_first_name", viewModel.data.parent_first_name!!)
                    }
                    if(viewModel.data.parent_last_name  != null){
                        requestBody.addFormDataPart("parent_last_name", viewModel.data.parent_last_name!!)
                    }
                    if(viewModel.data.gender  != null){
                        requestBody.addFormDataPart("gender", viewModel.data.gender!!)
                    }
                    if(viewModel.data.date_of_birth  != null){
                        requestBody.addFormDataPart("date_of_birth", viewModel.data.date_of_birth!!)
                    }
                    if(viewModel.data.social_category  != null){
                        requestBody.addFormDataPart("social_category", viewModel.data.social_category!!)
                    }
                    if(viewModel.data.education_qualification  != null){
                        requestBody.addFormDataPart("education_qualification", viewModel.data.education_qualification!!)
                    }
                    if(viewModel.data.marital_status  != null){
                        requestBody.addFormDataPart("marital_status", viewModel.data.marital_status!!)
                    }
                    if(viewModel.data.spouse_name  != null){
                        requestBody.addFormDataPart("spouse_name", viewModel.data.spouse_name!!)
                    }
                    if(viewModel.data.current_state  != null){
                        requestBody.addFormDataPart("residential_state", viewModel.data.current_state!!)
                    }
                    if(viewModel.data.current_district  != null){
                        requestBody.addFormDataPart("residential_district", viewModel.data.current_district!!)
                    }
                    if(viewModel.data.municipality_panchayat_current  != null){
                        requestBody.addFormDataPart("residential_municipality_panchayat", viewModel.data.municipality_panchayat_current!!)
                    }
                    if(viewModel.data.current_pincode  != null){
                        requestBody.addFormDataPart("residential_pincode", viewModel.data.current_pincode!!)
                    }
                    if(viewModel.data.current_address  != null){
                        requestBody.addFormDataPart("residential_address", viewModel.data.current_address!!)
                    }

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

                    if(viewModel.data.birth_state  != null){
                        requestBody.addFormDataPart("vending_state", viewModel.data.birth_state!!)
                    }
                    if(viewModel.data.birth_district  != null){
                        requestBody.addFormDataPart("vending_district", viewModel.data.birth_district!!)
                    }
                    if(viewModel.data.municipality_panchayat_birth  != null){
                        requestBody.addFormDataPart("vending_municipality_panchayat", viewModel.data.municipality_panchayat_birth!!)
                    }
                if(viewModel.data.birth_pincode  != null){
                    requestBody.addFormDataPart("vending_pincode", viewModel.data.birth_pincode!!)
                }
                if(viewModel.data.birth_address  != null){
                    requestBody.addFormDataPart("vending_address", viewModel.data.birth_address!!)
                }
                if(viewModel.data.localOrganisation  != null){
                    requestBody.addFormDataPart("local_organisation", viewModel.data.localOrganisation!!)
                }
//                if(viewModel.data.localOrganisation  != null){
//                    requestBody.addFormDataPart("local_organisation_others", viewModel.data.localOrganisation!!)
//                }
                if(!docs.toString().isEmpty()){
                    requestBody.addFormDataPart("vending_documents", docs.toString())
                } else{
                    requestBody.addFormDataPart("vending_documents", "null")
                }

                if(!viewModel.data.schemeName!!.isEmpty()){
                    requestBody.addFormDataPart("availed_scheme",viewModel.data.schemeName!!)
                } else {
                    requestBody.addFormDataPart("availed_scheme", "null")
                }

                if(viewModel.data.mobile_no  != null){
                    requestBody.addFormDataPart("mobile_no", viewModel.data.mobile_no!!)
                }
                if(viewModel.data.password  != null){
                    requestBody.addFormDataPart("password", viewModel.data.password!!)
                }




//                requestBody.addFormDataPart("status", "unverified")

                if(viewModel.data.PassportSizeImage != null){
                        requestBody.addFormDataPart(
                            "profile_image_name",
                            File(viewModel.data.PassportSizeImage!!).name,
                            File(viewModel.data.PassportSizeImage!!).asRequestBody("image/*".toMediaTypeOrNull())
                        )
                }

                if(viewModel.data.IdentificationImage != null){
                    requestBody.addFormDataPart(
                        "identity_image_name",
                        File(viewModel.data.IdentificationImage!!).name,
                        File(viewModel.data.IdentificationImage!!).asRequestBody("image/*".toMediaTypeOrNull())
                    )
                }

                if(viewModel.data.ShopImage != null){
                    requestBody.addFormDataPart(
                        "shop_image",
                        File(viewModel.data.ShopImage!!).name,
                        File(viewModel.data.ShopImage!!).asRequestBody("image/*".toMediaTypeOrNull())
                    )
                }

                if(viewModel.data.ImageUploadCOV != null){
                    requestBody.addFormDataPart(
                        "cov_image",
                        File(viewModel.data.ImageUploadCOV!!).name,
                        File(viewModel.data.ImageUploadCOV!!).asRequestBody("image/*".toMediaTypeOrNull())
                    )
                }

                if(viewModel.data.ImageUploadLOR != null){
                    requestBody.addFormDataPart(
                        "lor_image",
                        File(viewModel.data.ImageUploadLOR!!).name,
                        File(viewModel.data.ImageUploadLOR!!).asRequestBody("image/*".toMediaTypeOrNull())
                    )
                }

                if(viewModel.data.UploadSurveyReceipt != null){
                    requestBody.addFormDataPart(
                        "survey_receipt_image",
                        File(viewModel.data.UploadSurveyReceipt!!).name,
                        File(viewModel.data.UploadSurveyReceipt!!).asRequestBody("image/*".toMediaTypeOrNull())
                    )
                }

                if(viewModel.data.UploadChallan != null){
                    requestBody.addFormDataPart(
                        "challan_image",
                        File(viewModel.data.UploadChallan!!).name,
                        File(viewModel.data.UploadChallan!!).asRequestBody("image/*".toMediaTypeOrNull())
                    )
                }

                if(viewModel.data.UploadApprovalLetter != null){
                    requestBody.addFormDataPart(
                        "approval_letter_image",
                        File(viewModel.data.UploadApprovalLetter!!).name,
                        File(viewModel.data.UploadApprovalLetter!!).asRequestBody("image/*".toMediaTypeOrNull())
                    )
                }

                Log.e("TAG", "viewModel.dataAll "+viewModel.data.toString())
               // viewModel.registerWithFiles(view = requireView(), requestBody.build())
            }
        }
    }
}