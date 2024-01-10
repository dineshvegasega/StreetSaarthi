package com.streetsaarthi.screens.main.profiles

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import com.kochia.customer.utils.hideKeyboard
import com.streetsaarthi.R
import com.streetsaarthi.databinding.PersonalDetailsBinding
import com.streetsaarthi.datastore.DataStoreKeys
import com.streetsaarthi.datastore.DataStoreUtil
import com.streetsaarthi.models.login.Login
import com.streetsaarthi.utils.showSnackBar
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.time.Period
import java.util.Calendar

@AndroidEntryPoint
class PersonalDetails : Fragment() {
    private val viewModel: ProfilesVM by activityViewModels()
    private var _binding: PersonalDetailsBinding? = null
    private val binding get() = _binding!!

//    var itemMain: ArrayList<Item>? = ArrayList()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = PersonalDetailsBinding.inflate(inflater)
        return binding.root
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            DataStoreUtil.readData(DataStoreKeys.LOGIN_DATA) { loginUser ->
                if (loginUser != null) {
                    val data = Gson().fromJson(loginUser, Login::class.java)
                    editTextFN.setText(data.vendor_first_name)
                    editTextLN.setText(data.vendor_last_name)
                    editTextFatherFN.setText(data.parent_first_name)
                    editTextFatherLN.setText(data.parent_last_name)
                    editTextGender.setText(data.gender)
                    editTextDateofBirth.setText(data.date_of_birth)
                    editTextMaritalStatus.setText(data.marital_status)
                    editTextSocialCategory.setText(data.social_category)
                    editTextEducationQualifacation.setText(data.education_qualification)
                }
            }

            editTextGender.setOnClickListener {
                requireActivity().hideKeyboard()
                showDropDownGenderDialog()
            }

            editTextDateofBirth.setOnClickListener {
                requireActivity().hideKeyboard()
                showDOBDialog()
            }

            editTextSocialCategory.setOnClickListener {
                requireActivity().hideKeyboard()
                showDropDownCategoryDialog()
            }

            editTextEducationQualifacation.setOnClickListener {
                requireActivity().hideKeyboard()
                showDropDownEducationQualifacationDialog()
            }

            editTextMaritalStatus.setOnClickListener {
                requireActivity().hideKeyboard()
                showDropDownMaritalStatusDialog()
            }



            btnAddtoCart.setOnClickListener {

            }

        }


    }




    private fun showDropDownGenderDialog() {
        val list=resources.getStringArray(R.array.gender_array)
        MaterialAlertDialogBuilder(requireContext(), R.style.DialogTheme)
            .setTitle(resources.getString(R.string.gender))
            .setItems(list) {_,which->
                binding.editTextGender.setText(list[which])
//                when(which){
//                    0-> viewModel.data.gender = "Male"
//                    1-> viewModel.data.gender = "Female"
//                    2-> viewModel.data.gender = "Other"
//                }
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
            val today= LocalDate.now()
            val birthday: LocalDate = LocalDate.of(year,(monthOfYear+1),dayOfMonth)
            val p: Period = Period.between(birthday,today)


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
        MaterialAlertDialogBuilder(requireContext(), R.style.DialogTheme)
            .setTitle(resources.getString(R.string.social_category))
            .setItems(list) {_,which->
                binding.editTextSocialCategory.setText(list[which])
            }.show()
    }


    private fun showDropDownEducationQualifacationDialog() {
        val list=resources.getStringArray(R.array.socialEducation_array)
        MaterialAlertDialogBuilder(requireContext(), R.style.DialogTheme)
            .setTitle(resources.getString(R.string.education_qualifacation))
            .setItems(list) {_,which->
                binding.editTextEducationQualifacation.setText(list[which])
//                when(which){
//                    0-> viewModel.data.education_qualification = "No Education"
//                    1-> viewModel.data.education_qualification = "Primary Education(1st To 5th)"
//                    2-> viewModel.data.education_qualification = "Middle Education(6th To 9th)"
//                    3-> viewModel.data.education_qualification = "Higher Education(10th To 12th)"
//                    4-> viewModel.data.education_qualification = "Graduation"
//                    5-> viewModel.data.education_qualification = "Post Graduation"
//                }
            }.show()
    }


    private fun showDropDownMaritalStatusDialog() {
        val list=resources.getStringArray(R.array.maritalStatus_array)
        MaterialAlertDialogBuilder(requireContext(), R.style.DialogTheme)
            .setTitle(resources.getString(R.string.marital_status))
            .setItems(list) {_,which->
                binding.editTextMaritalStatus.setText(list[which])
                if (list[which] == getString(R.string.married)){
                    binding.editTextSpouseName.visibility = View.VISIBLE
                }else{
                    binding.editTextSpouseName.visibility = View.GONE
                }
//                when(which){
//                    0-> viewModel.data.marital_status = "Single"
//                    1-> viewModel.data.marital_status = "Married"
//                    2-> viewModel.data.marital_status = "Widowed"
//                    3-> viewModel.data.marital_status = "Divorced"
//                    4-> viewModel.data.marital_status = "Graduation"
//                    5-> viewModel.data.marital_status = "Separated"
//                }
            }.show()
    }



    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
