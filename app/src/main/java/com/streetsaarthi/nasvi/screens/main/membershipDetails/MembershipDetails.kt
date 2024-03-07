package com.streetsaarthi.nasvi.screens.main.membershipDetails

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.graphics.Canvas
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.FileProvider
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import coil.ImageLoader
import coil.disk.DiskCache
import coil.load
import coil.memory.MemoryCache
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import com.streetsaarthi.nasvi.R
import com.streetsaarthi.nasvi.databinding.DialogBottomNetworkBinding
import com.streetsaarthi.nasvi.databinding.MembershipDetailsBinding
import com.streetsaarthi.nasvi.datastore.DataStoreKeys
import com.streetsaarthi.nasvi.datastore.DataStoreUtil.readData
import com.streetsaarthi.nasvi.models.Login
import com.streetsaarthi.nasvi.screens.mainActivity.MainActivity
import com.streetsaarthi.nasvi.screens.mainActivity.MainActivity.Companion.networkFailed
import com.streetsaarthi.nasvi.utils.callNetworkDialog
import com.streetsaarthi.nasvi.utils.showSnackBar
import com.streetsaarthi.nasvi.utils.singleClick
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


@AndroidEntryPoint
class MembershipDetails  : Fragment() {
    private val viewModel: MembershipDetailsVM by viewModels()
    private var _binding: MembershipDetailsBinding? = null
    private val binding get() = _binding!!

    var permissionAlert : AlertDialog?= null

    var networkAlert : BottomSheetDialog?= null
    var networkCount = 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MembershipDetailsBinding.inflate(inflater)
        return binding.root
    }




    private fun callMediaPermissions() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE){
            activityResultLauncher.launch(
                arrayOf(
                    Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.READ_MEDIA_VIDEO,
                    Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED)
            )
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            activityResultLauncher.launch(
                arrayOf(
                    Manifest.permission.READ_MEDIA_IMAGES)
            )
        } else{
            activityResultLauncher.launch(
                arrayOf(
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
                dispatchTakePictureIntent()
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



    @SuppressLint("ResourceType")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        binding.model = viewModel
//        binding.lifecycleOwner = this
        MainActivity.mainActivity.get()?.callFragment(1)

        binding.apply {
            inclideHeaderSearch.textHeaderTxt.text = getString(R.string.membership_details)
            inclideHeaderSearch.editTextSearch.visibility = View.GONE
//            val scale = resources.displayMetrics.widthPixels.toFloat()
//            Log.e("TAG", "App.scaleAA "+scale)
//
//            val scale2 = scale * 6
//            Log.e("TAG", "App.scaleBB "+scale2)
//
//            val scale3 = scale2 / 100
//            Log.e("TAG", "App.scaleCC "+scale3)
//
//            val px = Math.round(
//                TypedValue.applyDimension(
//                    TypedValue.COMPLEX_UNIT_SP, 14f, resources.getDisplayMetrics()
//                )
//            )
//            Log.e("TAG", "App.scaleDD "+viewModel.scale10)

//            textAssociatedOrganizationTxt.textSize = viewModel.scale10
//            textMarketPlaceTxt.textSize = viewModel.scale10
//            textFirstNameTxt.textSize = viewModel.scale10
//            textFirstNameValueTxt.textSize = viewModel.scale10
//            textLastNameTxt.textSize = viewModel.scale10
//            textLastNameValueTxt.textSize = viewModel.scale10
//            textGenderTxt.textSize = viewModel.scale10
//            textGenderValueTxt.textSize = viewModel.scale10
//            textDOBTxt.textSize = viewModel.scale10
//            textDOBValueTxt.textSize = viewModel.scale10
//            textMobileTxt.textSize = viewModel.scale10
//            textMobileValueTxt.textSize = viewModel.scale10
//            textTypeofVendingTxt.textSize = viewModel.scale10
//            textTypeofVendingValueTxt.textSize = viewModel.scale10
//            textTypeofMarketPlaceTxt.textSize = viewModel.scale10
//            textTypeofMarketPlaceValueTxt.textSize = viewModel.scale10
//            textCurrentVendingAddressTxt.textSize = viewModel.scale10
//            textStateTxt.textSize = viewModel.scale10
//            textStateValueTxt.textSize = viewModel.scale10
//            textDistrictTxt.textSize = viewModel.scale10
//            textDistrictValueTxt.textSize = viewModel.scale10
//            textMunicipalityTxt.textSize = viewModel.scale10
//            textMunicipalityValueTxt.textSize = viewModel.scale10
//            textAddressTxt.textSize = viewModel.scale10
//            textAddressValueTxt.textSize = viewModel.scale10
//            textMembershipTxt.textSize = viewModel.scale10
//            textMembershipValidTxt.textSize = viewModel.scale10
//            btDownload.textSize = viewModel.scale10



            readData(DataStoreKeys.LOGIN_DATA) { loginUser ->
                if (loginUser != null) {
                    val data = Gson().fromJson(loginUser, Login::class.java)
//                    Log.e("TAG", "dataLogin "+data.toString())
                    textFirstNameValueTxt.setText(data.vendor_first_name)
                    textLastNameValueTxt.setText(data.vendor_last_name)

                    val listGender =resources.getStringArray(R.array.gender_array)
                    data.gender?.let{
                        when(it){
                            "Male" -> {
                                textGenderValueTxt.setText(listGender[0])
                            }
                            "Female" -> {
                                textGenderValueTxt.setText(listGender[1])
                            }
                            "Other" -> {
                                textGenderValueTxt.setText(listGender[2])
                            }
                        }
                    }

                    textDOBValueTxt.setText(data.date_of_birth)
                    textMobileValueTxt.setText("+91-"+data.mobile_no)

                    viewModel.counterNetwork.observe(viewLifecycleOwner, Observer {
                        if (it) {
                            if(networkCount == 1){
                                if(networkAlert?.isShowing == true) {
                                    return@Observer
                                }
                                val dialogBinding = DialogBottomNetworkBinding.inflate(root.context.getSystemService(
                                    Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                                )
                                networkAlert = BottomSheetDialog(root.context)
                                networkAlert?.setContentView(dialogBinding.root)
                                networkAlert?.setOnShowListener { dia ->
                                    val bottomSheetDialog = dia as BottomSheetDialog
                                    val bottomSheetInternal: FrameLayout =
                                        bottomSheetDialog.findViewById(com.google.android.material.R.id.design_bottom_sheet)!!
                                    bottomSheetInternal.setBackgroundResource(R.drawable.bg_top_round_corner)
                                }
                                networkAlert?.show()

                                dialogBinding.apply {
                                    btClose.singleClick {
                                        networkAlert?.dismiss()
                                    }
                                    btApply.singleClick {
                                        networkAlert?.dismiss()
                                        callApis(view)
                                    }
                                }
                            }
                            networkCount++
                        }
                    })


                    if(networkFailed) {
                        viewModel.vending(view)
                        viewModel.marketplace(view)
                    } else {
                        requireContext().callNetworkDialog()
                    }
                    viewModel.vendingTrue.observe(viewLifecycleOwner, Observer {
                        if(it == true){
                            for (item in viewModel.itemVending) {
                                if (item.vending_id == data.type_of_vending){
                                    if (item.vending_id == 11){
                                        binding.textTypeofVendingValueTxt.setText(""+data.vending_others)
                                    } else {
                                        binding.textTypeofVendingValueTxt.setText(""+item.name)
                                    }
                                    break
                                } else {
                                    data.vending_others?.let {
                                        binding.textTypeofVendingValueTxt.setText(""+data.vending_others)
                                    }
                                }
                            }
                        }
                    })


                    viewModel.marketPlaceTrue.observe(viewLifecycleOwner, Observer {
                        if(it == true){
                            for (item in viewModel.itemMarketplace) {
                                if (item.marketplace_id == data.type_of_marketplace){
                                    if (item.marketplace_id == 7){
                                        binding.textTypeofMarketPlaceValueTxt.setText(""+data.marketpalce_others)
                                    } else {
                                        binding.textTypeofMarketPlaceValueTxt.setText(""+item.name)
                                    }
                                    break
                                } else {
                                    data.marketpalce_others?.let {
                                        binding.textTypeofMarketPlaceValueTxt.setText(""+data.marketpalce_others)
                                    }
                                }
                            }
                        }
                    })


                    data.vending_state?.let {
                        textStateValueTxt.setText(data.vending_state.name)
                    }
                    data.vending_district?.let {
                        textDistrictValueTxt.setText(data.vending_district.name)
                    }
                    data.vending_municipality_panchayat?.let {
                        textMunicipalityValueTxt.setText(data.vending_municipality_panchayat.name)
                    }

                    if(data.vending_pincode != null){
                        textAddressValueTxt.setText("${data.vending_address.replace("\n", ", ")+", "+data.vending_pincode.pincode}")
                    } else {
                        data.vending_address?.let {
                            textAddressValueTxt.setText("${data.vending_address.replace("\n", ", ")}")
                        }
                    }

                    data.membership_id?.let {
                        textMembershipTxt.setText(requireActivity().getString(R.string.membershipIDSemi, data.membership_id))
                    }

                    if(data.membership_validity != null){
                        textMembershipValidTxt.setText(requireActivity().getString(R.string.validUptoSemi, data.membership_validity))
                    } else {
                        textMembershipValidTxt.setText(requireActivity().getString(R.string.validUptoSemi, ""))
                    }

                    data.local_organisation?.let {
                        layoutMain.setBackgroundResource(R.drawable.membership_card)
                        (layoutMain.layoutParams as ConstraintLayout.LayoutParams).dimensionRatio = "1.06:1"
                        layoutAssociatedOrganization.visibility = View.VISIBLE
                        view1.visibility = View.VISIBLE
                        textMarketPlaceTxt.setText(data.local_organisation.name)
                    }



                    data.profile_image_name?.let {
                        val imageLoader = ImageLoader.Builder(requireContext())
                            .memoryCache {
                                MemoryCache.Builder(requireContext())
                                    .maxSizePercent(0.25)
                                    .build()
                            }
                            .diskCache {
                                DiskCache.Builder()
                                    .directory(requireContext().cacheDir.resolve("image_cache"))
                                    .maxSizePercent(0.02)
                                    .build()
                            }
                            .error(R.drawable.no_image_modified)
                            .placeholder(R.drawable.no_image_modified)
                            .build()
                        ivIcon.load(data.profile_image_name?.url, imageLoader){
                            allowHardware(false)
                        }
                    }

                }
            }

            btDownload.singleClick {
                callMediaPermissions()
            }

        }
    }



    private fun callApis(view: View) {
        networkCount = 1
        readData(DataStoreKeys.LOGIN_DATA) { loginUser ->
            if (loginUser != null) {
                if(networkFailed) {
                    viewModel.vending(view)
                    viewModel.marketplace(view)
                } else {
                    requireContext().callNetworkDialog()
                }
            }
        }
    }


    private fun dispatchTakePictureIntent() {
        val bitmap: Bitmap = getBitmapFromView(binding.layoutMain)
        val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val filename = System.currentTimeMillis().toString() + "." + "png" // change png/pdf
        val file = File(path, filename)
        try {
            if (!path.exists()) path.mkdirs()
            if (!file.exists()) file.createNewFile()
            val ostream: FileOutputStream = FileOutputStream(file)
            bitmap.compress(CompressFormat.PNG, 10, ostream)
            ostream.close()

//            val pdfDocument = PdfDocument()
//            val pageInfo = PdfDocument.PageInfo.Builder(bitmap.width, bitmap.height, 1).create()
//            val page = pdfDocument.startPage(pageInfo)
//            page.canvas.drawBitmap(bitmap, 0F, 0F, null)
//            pdfDocument.finishPage(page)
//            val ostream: FileOutputStream = FileOutputStream(file)
//            pdfDocument.writeTo(ostream)
//            ostream.close()
//            pdfDocument.close()

            val data= if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                FileProvider.getUriForFile(requireContext(), requireContext().getApplicationContext().getPackageName() + ".provider", file)
            }else{
                val imagePath: File = File(file.absolutePath)
                FileProvider.getUriForFile(requireContext(), requireContext().getApplicationContext().getPackageName() + ".provider", imagePath)
            }
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(data, "image/*")  // application/pdf   image/*
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            startActivity(intent)

            showSnackBar(getString(R.string.successfully_downloaded))
            binding.btDownload.setEnabled(false)
            binding.btDownload.setBackgroundTintList(
                ColorStateList.valueOf(
                    ResourcesCompat.getColor(
                        getResources(), R.color._999999, null)))
        } catch (e: IOException) {
            Log.w("ExternalStorage", "Error writing $file", e)
        }
    }

    fun getBitmapFromView(view: View): Bitmap {
        val bitmap = Bitmap.createBitmap(
            view.width, view.height, Bitmap.Config.ARGB_8888
        )

        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }




    override fun onStop() {
        super.onStop()
        binding.apply {
//            banner.autoScrollStop()
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}