package com.streetsaarthi.nasvi.screens.main.profiles

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.util.Log
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
import com.streetsaarthi.nasvi.R
import com.streetsaarthi.nasvi.databinding.PersonalDetailsBinding
import com.streetsaarthi.nasvi.datastore.DataStoreKeys
import com.streetsaarthi.nasvi.datastore.DataStoreUtil
import com.streetsaarthi.nasvi.models.login.Login
import com.streetsaarthi.nasvi.utils.showSnackBar
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
                    viewModel.data.gender = data.gender
                    viewModel.data.date_of_birth = data.date_of_birth
                    viewModel.data.marital_status = data.marital_status
                    viewModel.data.spouse_name = data.spouse_name
                    viewModel.data.social_category = data.social_category
                    viewModel.data.education_qualification = data.education_qualification

                    val listGender =resources.getStringArray(R.array.gender_array)
                    data.gender.let{
                        when(it){
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

                    val listMaritalStatus= resources.getStringArray(R.array.maritalStatus_array)
                    data.marital_status.let{
                        when(it){
                            "Single" -> {
                                editTextMaritalStatus.setText(listMaritalStatus[0])
                                editTextSpouseName.visibility = View.GONE
                            }
                            "Married" -> {
                                editTextMaritalStatus.setText(listMaritalStatus[1])
                                editTextSpouseName.setText("${data.spouse_name}")
                                editTextSpouseName.visibility = View.VISIBLE
                            }
                            "Widowed" -> {
                                editTextMaritalStatus.setText(listMaritalStatus[2])
                                editTextSpouseName.visibility = View.GONE
                            }
                            "Divorced" -> {
                                editTextMaritalStatus.setText(listMaritalStatus[3])
                                editTextSpouseName.visibility = View.GONE
                            }
                            "Separated" -> {
                                editTextMaritalStatus.setText(listMaritalStatus[4])
                                editTextSpouseName.visibility = View.GONE
                            }
                        }
                    }

                    editTextSocialCategory.setText(data.social_category)

                    val listEducation= resources.getStringArray(R.array.socialEducation_array)
                    data.education_qualification.let{
                        when(it){
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
                }
            }


            btnAddtoCart.setOnClickListener {
                if(editTextFN.text.toString().isEmpty()){
                    showSnackBar(getString(R.string.first_name))
                } else if (editTextLN.text.toString().isEmpty()){
                    showSnackBar(getString(R.string.last_name))
                } else if (editTextFatherFN.text.toString().isEmpty()){
                    showSnackBar(getString(R.string.father_s_first_name))
                } else if (editTextFatherLN.text.toString().isEmpty()){
                    showSnackBar(getString(R.string.father_s_last_name))
                } else if (editTextGender.text.toString().isEmpty()){
                    showSnackBar(getString(R.string.gender))
                } else if (editTextDateofBirth.text.toString().isEmpty()){
                    showSnackBar(getString(R.string.date_of_birth))
                } else if (editTextMaritalStatus.text.toString().isEmpty()){
                    showSnackBar(getString(R.string.marital_status))
                } else if (editTextMaritalStatus.text.toString() == getString(R.string.married) && editTextSpouseName.text.toString().isEmpty()){
                    showSnackBar(getString(R.string.spouse_s_name))
                } else if (editTextSocialCategory.text.toString().isEmpty()){
                    showSnackBar(getString(R.string.social_category))
                } else if (editTextEducationQualifacation.text.toString().isEmpty()){
                    showSnackBar(getString(R.string.education_qualifacation))
                } else {
                    viewModel.data.vendor_first_name = editTextFN.text.toString()
                    viewModel.data.vendor_last_name = editTextLN.text.toString()
                    viewModel.data.parent_first_name = editTextFatherFN.text.toString()
                    viewModel.data.parent_last_name = editTextFatherLN.text.toString()

                    viewModel.data.spouse_name = editTextSpouseName.text.toString()

                    if (viewModel.data.marital_status == "Married"){
                        viewModel.data.spouse_name = editTextSpouseName.text.toString()
                    } else {
                        viewModel.data.spouse_name = null
                    }

                    viewModel.data.social_category = editTextSocialCategory.text.toString()
                }


                Log.e("TAG", "viewModel.data "+ viewModel.data.toString())
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







        }


    }




    private fun showDropDownGenderDialog() {
        val list=resources.getStringArray(R.array.gender_array)
        MaterialAlertDialogBuilder(requireContext(), R.style.DialogTheme)
            .setTitle(resources.getString(R.string.gender))
            .setItems(list) {_,which->
                binding.editTextGender.setText(list[which])
                when(which){
                    0-> viewModel.data.gender = "Male"
                    1-> viewModel.data.gender = "Female"
                    2-> viewModel.data.gender = "Other"
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
                viewModel.data.date_of_birth = "" + year + "-" + mm  + "-" + dd
            }else{
                showSnackBar(getString(R.string.age_minimum))
                binding.editTextDateofBirth.setText("")
                viewModel.data.date_of_birth = null
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
                when(which){
                    0-> viewModel.data.education_qualification = "No Education"
                    1-> viewModel.data.education_qualification = "Primary Education(1st To 5th)"
                    2-> viewModel.data.education_qualification = "Middle Education(6th To 9th)"
                    3-> viewModel.data.education_qualification = "Higher Education(10th To 12th)"
                    4-> viewModel.data.education_qualification = "Graduation"
                    5-> viewModel.data.education_qualification = "Post Graduation"
                }
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
                    viewModel.data.spouse_name = null
                }
                when(which){
                    0-> viewModel.data.marital_status = "Single"
                    1-> viewModel.data.marital_status = "Married"
                    2-> viewModel.data.marital_status = "Widowed"
                    3-> viewModel.data.marital_status = "Divorced"
                    4-> viewModel.data.marital_status = "Graduation"
                    5-> viewModel.data.marital_status = "Separated"
                }
            }.show()
    }



    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
