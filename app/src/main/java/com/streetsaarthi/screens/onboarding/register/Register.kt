package com.streetsaarthi.screens.onboarding.register

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.demo.networking.USER_TYPE
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


@AndroidEntryPoint
class Register : Fragment() , CallBackListener {
    private var _binding: RegisterBinding? = null
    private val binding get() = _binding!!
    private val viewModel: RegisterVM by activityViewModels()


    companion object{
        var callBackListener: CallBackListener? = null

        const val ALL_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS =
            mutableListOf (
                Manifest.permission.CAMERA
            ).apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    add(Manifest.permission.READ_MEDIA_IMAGES)
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                    add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }.toTypedArray()
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


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            ALL_PERMISSIONS -> {
                if (grantResults.all { it == PackageManager.PERMISSION_GRANTED }) else {

                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        callBackListener = this

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
//                if (tabPosition == QuickRegister){
//                    requireView().findNavController().navigate(R.id.action_register_to_registerSuccessful)
//                } else if (screen == CompleteRegister){
                    if (tabPosition == 0){
                       // introViewPager.setCurrentItem(1, false)
                        Register1.callBackListener!!.onCallBack(1)
                        btSignIn.setText(getString(R.string.continues))
                    } else if (tabPosition == 1){
                        // introViewPager.setCurrentItem(2, false)
                        Register2.callBackListener!!.onCallBack(3)
                        btSignIn.setText(getString(R.string.RegisterNow))
                    }else if (tabPosition == 2){
                        Register3.callBackListener!!.onCallBack(5)
//                        requireView().findNavController().navigate(R.id.action_register_to_registerSuccessful)
                    }
//                }
                loadProgress(tabPosition)
            }

            textBack.setOnClickListener {
                Log.e("Selected_PagetabPosition", ""+tabPosition)
                if (tabPosition == 0){
                    view.findNavController().navigateUp()
                } else if (tabPosition == 1){
                    introViewPager.setCurrentItem(0, false)
                    btSignIn.setText(getString(R.string.continues))
                }else if (tabPosition == 2){
                    introViewPager.setCurrentItem(1, false)
                    btSignIn.setText(getString(R.string.continues))
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
                    .addFormDataPart("vendor_first_name", viewModel.data.vendor_first_name!!)
                    .addFormDataPart("vendor_last_name", viewModel.data.vendor_last_name!!)
                    .addFormDataPart("parent_first_name", viewModel.data.parent_first_name!!)
                    .addFormDataPart("parent_last_name", viewModel.data.parent_last_name!!)
                    .addFormDataPart("gender", viewModel.data.gender!!)
                    .addFormDataPart("date_of_birth", viewModel.data.date_of_birth!!)
                    .addFormDataPart("social_category", viewModel.data.social_category!!)
                    .addFormDataPart("education_qualification", viewModel.data.education_qualification!!)
                    .addFormDataPart("marital_status", viewModel.data.marital_status!!)
//                    if(viewModel.data.marital_status == R.string.){
//                        requestBody.addFormDataPart("availed_scheme", "PM Swanidhi Scheme")
//                    }
                requestBody.addFormDataPart("spouse_name", viewModel.data.spouse_name!!)
                    .addFormDataPart("residential_state", viewModel.data.current_state!!)
                    .addFormDataPart("residential_district", viewModel.data.current_district!!)
                    .addFormDataPart("residential_municipality_panchayat", viewModel.data.municipality_panchayat_current!!)
                    .addFormDataPart("residential_pincode", viewModel.data.current_pincode!!)
                    .addFormDataPart("residential_address", viewModel.data.current_address!!)

                    .addFormDataPart("type_of_marketplace", viewModel.data.type_of_marketplace!!)
                    .addFormDataPart("type_of_vending", viewModel.data.type_of_vending!!)
                    .addFormDataPart("total_years_of_business", viewModel.data.total_years_of_business!!)
                    .addFormDataPart("vending_time_from", viewModel.data.open!!)
                    .addFormDataPart("vending_time_to", viewModel.data.close!!)
                    .addFormDataPart("vending_state", viewModel.data.birth_state!!)
                    .addFormDataPart("vending_district", viewModel.data.birth_district!!)
                    .addFormDataPart("vending_municipality_panchayat", viewModel.data.municipality_panchayat_birth!!)
                    .addFormDataPart("vending_pincode", viewModel.data.birth_pincode!!)
                    .addFormDataPart("vending_address", viewModel.data.birth_address!!)

                    .addFormDataPart("vending_documents", docs.toString())
                    .addFormDataPart("vending_address", viewModel.data.birth_address!!)

                    if(viewModel.data.pmSwanidhiScheme == true){
                        requestBody.addFormDataPart("availed_scheme", getString(R.string.pm_swanidhi_scheme))
                    }
                    requestBody.addFormDataPart("vending_others", getString(R.string.others_please_name))
                    if(viewModel.data.othersName == true){
                        requestBody.addFormDataPart("marketpalce_others", viewModel.data.schemeName!!)
                    }

                requestBody.addFormDataPart("mobile_no", viewModel.data.mobile_no!!)
                requestBody.addFormDataPart("password", viewModel.data.password!!)
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
                viewModel.registerWithFiles(view = requireView(), requestBody.build())
            }
        }
    }
}