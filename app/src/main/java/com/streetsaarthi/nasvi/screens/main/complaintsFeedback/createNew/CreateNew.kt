package com.streetsaarthi.nasvi.screens.main.complaintsFeedback.createNew

import android.Manifest
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import com.kochia.customer.utils.hideKeyboard
import com.streetsaarthi.nasvi.R
import com.streetsaarthi.nasvi.databinding.CreateNewBinding
import com.streetsaarthi.nasvi.datastore.DataStoreKeys
import com.streetsaarthi.nasvi.datastore.DataStoreUtil
import com.streetsaarthi.nasvi.models.login.Login
import com.streetsaarthi.nasvi.screens.mainActivity.MainActivity
import com.streetsaarthi.nasvi.utils.getMediaFilePathFor
import com.streetsaarthi.nasvi.utils.showSnackBar
import com.streetsaarthi.nasvi.utils.singleClick
import dagger.hilt.android.AndroidEntryPoint
import id.zelory.compressor.Compressor
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.util.Calendar

@AndroidEntryPoint
class CreateNew : Fragment() {
    private val viewModel: CreateNewVM by viewModels()
    private var _binding: CreateNewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CreateNewBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        MainActivity.mainActivity.get()?.callFragment(0)
        binding.apply {
            inclideHeaderSearch.textHeaderTxt.text = getString(R.string.complaintsSlashfeedback)
            inclideHeaderSearch.editTextSearch.visibility = View.GONE

            btCancel.singleClick {
               findNavController().navigateUp()
            }

            editTextSelectYourChoice.singleClick {
                showDropDownDialog()
            }

            viewModel.complaintType(view)
            editTextSelectComplaintType.singleClick {
                requireActivity().hideKeyboard()
                if(viewModel.itemComplaintType.size > 0){
                    showDropDownComplaintTypeDialog()
                } else {
                    showSnackBar(getString(R.string.not_complaint_type))
                }
            }

            btUploadMedia.singleClick {
                imagePosition = 1
                isFree = true
                callMediaPermissions()
            }

            btSubmit.singleClick {
                if(editTextSubjectOfComplaint.text.toString().isEmpty()){
                    if (viewModel.type == "complaint"){
                        showSnackBar(getString(R.string.subject_of_complaint))
                    } else if (viewModel.type == "feedback"){
                        showSnackBar(getString(R.string.subject_of_feedback))
                    }
                } else if (viewModel.type == "complaint" && viewModel.complaintTypeId == 0){
                    showSnackBar(getString(R.string.select_complaint_type))
                } else if (editTextYourName.text.toString().isEmpty()) {
                    showSnackBar(getString(R.string.your_full_name))
                } else if (editTextYourMobileNumber.text.toString().isEmpty() || editTextYourMobileNumber.text.toString().length != 10){
                    showSnackBar(getString(R.string.your_mobile_number))
                } else if (editTextTypeHere.text.toString().isEmpty()) {
                    showSnackBar(getString(R.string.description))
//                } else if (viewModel.uploadMediaImage == null){
//                    showSnackBar(getString(R.string.upload_media))
                } else {
                    Log.e("TAG", "typeXX "+viewModel.type)
                    val requestBody: MultipartBody.Builder = MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
//                        .addFormDataPart("user_role", USER_TYPE)
                    requestBody.addFormDataPart("type", viewModel.type)
                    requestBody.addFormDataPart("subject", editTextSubjectOfComplaint.text.toString())
                    if (viewModel.type == "complaint" && viewModel.complaintTypeId != 0){
                        requestBody.addFormDataPart("complaint_type", ""+viewModel.complaintTypeId)
                    }
                    requestBody.addFormDataPart("name", editTextYourName.text.toString())
                    requestBody.addFormDataPart("mobile_number", editTextYourMobileNumber.text.toString())
                    requestBody.addFormDataPart("message", editTextTypeHere.text.toString())
                    if(viewModel.uploadMediaImage != null){
                        requestBody.addFormDataPart(
                            "media",
                            File(viewModel.uploadMediaImage!!).name,
                            File(viewModel.uploadMediaImage!!).asRequestBody("image/*".toMediaTypeOrNull())
                        )
                    }
                    requestBody.addFormDataPart("status", "pending")
                    DataStoreUtil.readData(DataStoreKeys.LOGIN_DATA) { loginUser ->
                        if (loginUser != null) {
                           var user = Gson().fromJson(loginUser, Login::class.java)
                            requestBody.addFormDataPart("user_id", ""+user?.id)
                            requestBody.addFormDataPart("state_id", ""+user.residential_state?.id)
                            requestBody.addFormDataPart("district_id", ""+user.residential_district?.id)
                            requestBody.addFormDataPart("municipality_id", ""+user.residential_municipality_panchayat?.id)
                            viewModel.newFeedback(view = requireView(), requestBody.build())
                        }
                    }
                }
            }
        }
    }


    private fun showDropDownDialog() {
        val list=resources.getStringArray(R.array.type_array)
        MaterialAlertDialogBuilder(requireContext(), R.style.DialogTheme)
            .setTitle(resources.getString(R.string.select_your_choice))
            .setItems(list) {_,which->
                binding.editTextSelectYourChoice.setText(list[which])
                when(which){
                    0-> {
                        viewModel.type = "complaint"
                        binding.textSubjectOfComplaintTxt.text = getString(R.string.subject_of_complaint)
                        binding.textTypeTxt.visibility = View.VISIBLE
                        binding.editTextSelectComplaintType.visibility = View.VISIBLE
                    }
                    1-> {
                        viewModel.type = "feedback"
                        binding.textSubjectOfComplaintTxt.text = getString(R.string.subject_of_feedback)
                        binding.textTypeTxt.visibility = View.GONE
                        binding.editTextSelectComplaintType.visibility = View.GONE
                    }
                }
            }.show()
    }



    private fun showDropDownComplaintTypeDialog() {
        var index = 0
        val list = arrayOfNulls<String>(viewModel.itemComplaintType.size)
        for (value in viewModel.itemComplaintType) {
            list[index] = value.name
            index++
        }
        MaterialAlertDialogBuilder(requireView().context, R.style.DialogTheme)
            .setTitle(resources.getString(R.string.select_complaint_type))
            .setItems(list) {_,which->
                binding.editTextSelectComplaintType.setText(list[which])
                viewModel.complaintTypeId =  viewModel.itemComplaintType[which].id
            }.show()
    }





    var imagePosition = 0
    private var pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        lifecycleScope.launch {
            if (uri != null) {
                when (imagePosition) {
                    1 -> {
                        val compressedImageFile = Compressor.compress(requireContext(), File(requireContext().getMediaFilePathFor(uri)))
                        viewModel.uploadMediaImage = compressedImageFile.path
                        binding.textViewUploadMedia.setText(File(viewModel.uploadMediaImage!!).name)
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
                        viewModel.uploadMediaImage = compressedImageFile.path
                        binding.textViewUploadMedia.setText(compressedImageFile.name)
                    }
                }
            }
        }
    }



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


    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}