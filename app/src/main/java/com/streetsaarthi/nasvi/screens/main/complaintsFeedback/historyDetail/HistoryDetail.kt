package com.streetsaarthi.nasvi.screens.main.complaintsFeedback.historyDetail

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Typeface
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
import android.view.WindowManager
import androidx.activity.result.ActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.content.res.ResourcesCompat
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import com.streetsaarthi.nasvi.R
import com.streetsaarthi.nasvi.databinding.HistoryDetailBinding
import com.streetsaarthi.nasvi.datastore.DataStoreKeys
import com.streetsaarthi.nasvi.datastore.DataStoreUtil
import com.streetsaarthi.nasvi.models.chat.DataX
import com.streetsaarthi.nasvi.models.login.Login
import com.streetsaarthi.nasvi.screens.mainActivity.MainActivity
import com.streetsaarthi.nasvi.screens.onboarding.networking.USER_TYPE
import com.streetsaarthi.nasvi.utils.changeDateFormat
import com.streetsaarthi.nasvi.utils.getMediaFilePathFor
import com.streetsaarthi.nasvi.utils.loadImage
import com.streetsaarthi.nasvi.utils.showSnackBar
import dagger.hilt.android.AndroidEntryPoint
import id.zelory.compressor.Compressor
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.util.Calendar


@AndroidEntryPoint
class HistoryDetail : Fragment() {
    private val viewModel: HistoryDetailVM by viewModels()
    private var _binding: HistoryDetailBinding? = null
    private val binding get() = _binding!!

    val strings = ArrayList<DataX>()

    var logoutAlert : AlertDialog?= null
    var permissionAlert : AlertDialog?= null

    @SuppressLint("SuspiciousIndentation")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = HistoryDetailBinding.inflate(inflater)
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_MODE_CHANGED)
//        } else {
            activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
//        }
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        MainActivity.mainActivity.get()?.callFragment(0)
        var feedbackId = arguments?.getString("key")
//        var feedbackId = "84"
        var status = ""
        binding.apply {
            inclideHeaderSearch.textHeaderTxt.text = HtmlCompat.fromHtml(getString(R.string.trackingId, "<b>"+feedbackId+"</b>"), HtmlCompat.FROM_HTML_MODE_LEGACY);
         //   "Tracking Id: #12344682"
            val typeface: Typeface? = ResourcesCompat.getFont(requireContext(), R.font.roboto_medium)
            inclideHeaderSearch.textHeaderTxt.typeface = typeface
            inclideHeaderSearch.btClose.visibility = View.VISIBLE
            inclideHeaderSearch.editTextSearch.visibility = View.GONE


            inclideHeaderSearch.btClose.setOnClickListener {
                var msg = ""
                val requestBody: MultipartBody.Builder = MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("user_type", USER_TYPE)
                if (status == "resolved"){
                    requestBody.addFormDataPart("status", "re-open")
                    msg = view.resources.getString(R.string.open_conversation)
                } else if (status == "re-open"){
                    requestBody.addFormDataPart("status", "resolved")
                    msg = view.resources.getString(R.string.close_conversation)
                } else if (status == "Pending" || status == "pending"){
                    requestBody.addFormDataPart("status", "resolved")
                    msg = view.resources.getString(R.string.close_conversation)
                } else if (status == "in-progress"){
                    requestBody.addFormDataPart("status", "resolved")
                    msg = view.resources.getString(R.string.close_conversation)
                }

                if(logoutAlert?.isShowing == true) {
                    return@setOnClickListener
                }
                logoutAlert = MaterialAlertDialogBuilder(requireContext(), R.style.LogoutDialogTheme)
                    .setTitle(resources.getString(R.string.app_name))
                    .setMessage(msg)
                    .setPositiveButton(resources.getString(R.string.yes)) { dialog, _ ->
                        dialog.dismiss()
                        DataStoreUtil.readData(DataStoreKeys.LOGIN_DATA) { loginUser ->
                            if (loginUser != null) {
                                var user = Gson().fromJson(loginUser, Login::class.java)
                                requestBody.addFormDataPart("user_id", ""+user?.id)
                                requestBody.addFormDataPart("feedback_id", ""+feedbackId)
                                viewModel.addFeedbackConversationDetails(view = requireView(), requestBody.build())
                            }
                        }
                    }
                    .setNegativeButton(resources.getString(R.string.no)) { dialog, _ ->
                        dialog.dismiss()
                    }
                    .setCancelable(false)
                    .show()
            }

            viewModel.chatAdapter.submitList(strings)
            viewModel.chatAdapter.notifyDataSetChanged()
            rvGiftCardList.adapter=viewModel.chatAdapter


            viewModel.feedbackConversationDetails(view, ""+feedbackId)

            viewModel.feedbackConversationLive.observe(requireActivity()) {
                var complaintfeedback = if (it.type == "complaint"){
                root.context.getString(R.string.complaint)
                } else {
                    root.context.getString(R.string.feedback)
                }
                inclideHistoryType.textTypeValue.text = complaintfeedback
                inclideHistoryType.textRegistrationDateValue.text = "${it.registration_date.changeDateFormat("yyyy-MM-dd", "dd MMM yyyy")}"
                inclideHistoryType.textSubjectValue.text = it.subject
//                inclideHistoryType.textConsecteturValue.text = it.message
                status = it.status

                if (status == "resolved"){
                    vBottom.visibility = View.GONE
                    inclideHeaderSearch.btClose.text = view.resources.getString(R.string.re_open)
                    inclideHeaderSearch.btClose.icon = ContextCompat.getDrawable(root.context,R.drawable.check)
                    inclideHeaderSearch.btClose.backgroundTintList = ContextCompat.getColorStateList(root.context,R.color._138808)
                } else if (status == "re-open"){
                    vBottom.visibility = View.VISIBLE
                    inclideHeaderSearch.btClose.text = view.resources.getString(R.string.x_close)
                    inclideHeaderSearch.btClose.backgroundTintList = ContextCompat.getColorStateList(root.context,R.color._ED2525)
                } else if (status == "Pending" || status == "pending"){
                    vBottom.visibility = View.VISIBLE
                    inclideHeaderSearch.btClose.text = view.resources.getString(R.string.x_close)
                    inclideHeaderSearch.btClose.backgroundTintList = ContextCompat.getColorStateList(root.context,R.color._ED2525)
                } else if (status == "in-progress"){
                    vBottom.visibility = View.VISIBLE
                    inclideHeaderSearch.btClose.text = view.resources.getString(R.string.x_close)
                    inclideHeaderSearch.btClose.backgroundTintList = ContextCompat.getColorStateList(root.context,R.color._ED2525)
                } else {
                    vBottom.visibility = View.GONE
                }

                var old = ""
                it.data.data.map {
                    var date = it.reply_date.changeDateFormat("yyyy-MM-dd HH:mm:ss", "dd MMM yyyy")
                    if (old != date){
                        old = date!!
                        it.dateShow = true
                    }
                }


                strings.clear()

                strings.add(DataX(it.media, it.message, it.registration_date , "in-progress" , it.user_id, it.user_type, false))

                strings.addAll(it.data.data)
                viewModel.chatAdapter.submitList(strings)

                rvGiftCardList.postDelayed({
                    (rvGiftCardList.getLayoutManager() as LinearLayoutManager).scrollToPositionWithOffset( (strings.size
                        ?: 0) - 1, 0)
                    viewModel.chatAdapter.notifyDataSetChanged()
                }, 50)
            }


            viewModel.addFeedbackConversationLive.observe(requireActivity()) {
                viewModel.feedbackConversationDetails(view, ""+feedbackId)
            }


            ivAttach.setOnClickListener {
                imagePosition = 1
                callMediaPermissions()
            }

            ivSend.setOnClickListener {
                val requestBody: MultipartBody.Builder = MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("user_type", USER_TYPE)
                if (!etTypingMessage.text!!.isEmpty() || viewModel.uploadMediaImage != null){
                    DataStoreUtil.readData(DataStoreKeys.LOGIN_DATA) { loginUser ->
                        if (loginUser != null) {
                            var user = Gson().fromJson(loginUser, Login::class.java)
                            requestBody.addFormDataPart("user_id", ""+user?.id)
                            requestBody.addFormDataPart("feedback_id", ""+feedbackId)
                            requestBody.addFormDataPart("reply", ""+etTypingMessage.text.toString())
                            requestBody.addFormDataPart("status", "in-progress")
                            if(viewModel.uploadMediaImage != null){
                                requestBody.addFormDataPart(
                                    "media",
                                    File(viewModel.uploadMediaImage!!).name,
                                    File(viewModel.uploadMediaImage!!).asRequestBody("image/*".toMediaTypeOrNull())
                                )
                            }
                            viewModel.addFeedbackConversationDetails(view = requireView(), requestBody.build())
                            etTypingMessage.setText("")
                            viewModel.uploadMediaImage = null
                            binding.relative1.visibility = View.GONE
                        }
                    }
                } else {
                    showSnackBar(view.resources.getString(R.string.Pleaseentertextormedia))
                }

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
                        viewModel.uploadMediaImage = compressedImageFile.path
                        binding.ivImageImage.loadImage(url = { viewModel.uploadMediaImage!! })
                        binding.relative1.visibility = View.VISIBLE
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
                        binding.ivImageImage.loadImage(url = { viewModel.uploadMediaImage!! })
                        binding.relative1.visibility = View.VISIBLE
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




    override fun onDestroyView() {
        _binding = null
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        super.onDestroyView()
    }

}