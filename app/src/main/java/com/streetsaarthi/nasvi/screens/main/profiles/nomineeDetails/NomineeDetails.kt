package com.streetsaarthi.nasvi.screens.main.profiles.nomineeDetails

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import com.kochia.customer.utils.hideKeyboard
import com.streetsaarthi.nasvi.R
import com.streetsaarthi.nasvi.databinding.NomineeDetailsBinding
import com.streetsaarthi.nasvi.datastore.DataStoreKeys
import com.streetsaarthi.nasvi.datastore.DataStoreUtil.readData
import com.streetsaarthi.nasvi.models.login.Login
import com.streetsaarthi.nasvi.networking.getJsonRequestBody
import com.streetsaarthi.nasvi.screens.mainActivity.MainActivity
import com.streetsaarthi.nasvi.utils.relationType
import com.streetsaarthi.nasvi.utils.showSnackBar
import com.streetsaarthi.nasvi.utils.singleClick
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject
import java.util.Collections

@AndroidEntryPoint
class NomineeDetails : Fragment() {
    private val viewModel: NomineeDetailsVM by activityViewModels()
    private var _binding: NomineeDetailsBinding? = null
    private val binding get() = _binding!!



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = NomineeDetailsBinding.inflate(inflater)
        return binding.root
    }


    @SuppressLint("NotifyDataSetChanged", "SetTextI18n", "SuspiciousIndentation")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        MainActivity.mainActivity.get()?.callFragment(0)
        binding.apply {
            inclideHeaderSearch.textHeaderTxt.text = getString(R.string.nominee_details)
            inclideHeaderSearch.editTextSearch.visibility = View.GONE

            inclideHeaderSearch.textHeaderEditTxt.visibility = View.GONE
            btSave.visibility = View.GONE
            btCancel.visibility = View.GONE
            btEdit.visibility = View.VISIBLE
            btEdit.singleClick {
                btSave.visibility = View.VISIBLE
                btCancel.visibility = View.VISIBLE
                btEdit.visibility = View.GONE
                viewModel.isEditable.value = true
            }
            btCancel.singleClick {
                btSave.visibility = View.GONE
                btCancel.visibility = View.GONE
                btEdit.visibility = View.VISIBLE
                viewModel.isEditable.value = false
            }

            image1.singleClick {
                spinnerRelationType1.setText("")
                editTextName1.setText("")
                viewModel.relationType1 = ""
                viewModel.relationName1 = ""
                image1.visibility = View.GONE

            }
            image2.singleClick {
                spinnerRelationType2.setText("")
                editTextName2.setText("")
                viewModel.relationType2 = ""
                viewModel.relationName2 = ""
                image2.visibility = View.GONE
            }
            image3.singleClick {
                spinnerRelationType3.setText("")
                editTextName3.setText("")
                viewModel.relationType3 = ""
                viewModel.relationName3 = ""
                image3.visibility = View.GONE
            }
            image4.singleClick {
                spinnerRelationType4.setText("")
                editTextName4.setText("")
                viewModel.relationType4 = ""
                viewModel.relationName4 = ""
                image4.visibility = View.GONE
            }
            image5.singleClick {
                spinnerRelationType5.setText("")
                editTextName5.setText("")
                viewModel.relationType5 = ""
                viewModel.relationName5 = ""
                image5.visibility = View.GONE
            }

            fieldsEdit()

//            readData(DataStoreKeys.LOGIN_DATA) { loginUser ->
//                if (loginUser != null) {
//                    val _id = Gson().fromJson(loginUser, Login::class.java).id
//                    viewModel.nomineeDetails(view, JSONObject().apply {
//                        put("member_id", _id)
//                    }.getJsonRequestBody())
//                }
//            }

            viewModel.isEditable.value = false
            viewModel.updateNominee.value = false
            viewModel.updateNominee.observe(viewLifecycleOwner, Observer {
                btSave.visibility = View.GONE
                btCancel.visibility = View.GONE
                btEdit.visibility = View.VISIBLE
                if(it) {
                    viewModel.isEditable.value = false
                }

                readData(DataStoreKeys.LOGIN_DATA) { loginUser ->
                    if (loginUser != null) {
                        val _id = Gson().fromJson(loginUser, Login::class.java).id
                        viewModel.nomineeDetails(view, JSONObject().apply {
                            put("member_id", _id)
                        }.getJsonRequestBody())
                    }
                }
            })

            viewModel.nomineeMutableLiveData.value = false
            viewModel.nomineeMutableLiveData.observe(viewLifecycleOwner, Observer {
                if(it){
                    val data = viewModel.nomineeArrayList
                    Log.e("TAG", "dataXXX "+data.size)
                    val relationArray = resources.getStringArray(R.array.relation_array)

                    emptyAll()

                    when(data.size){
                        1-> {
                            viewModel.relationType1 = ""+data[0].first
                            spinnerRelationType1.setText(""+data[0].first.relationType(relationArray))
                            editTextName1.setText(""+data[0].second)
                            image1.visibility = View.VISIBLE
                        }
                        2 -> {
                            viewModel.relationType1 = ""+data[0].first
                            spinnerRelationType1.setText(""+data[0].first.relationType(relationArray))
                            editTextName1.setText(""+data[0].second)

                            viewModel.relationType2 = ""+data[1].first
                            spinnerRelationType2.setText(""+data[1].first.relationType(relationArray))
                            editTextName2.setText(""+data[1].second)
                            image1.visibility = View.VISIBLE
                            image2.visibility = View.VISIBLE
                        }
                        3 -> {
                            viewModel.relationType1 = ""+data[0].first
                            spinnerRelationType1.setText(""+data[0].first.relationType(relationArray))
                            editTextName1.setText(""+data[0].second)

                            viewModel.relationType2 = ""+data[1].first
                            spinnerRelationType2.setText(""+data[1].first.relationType(relationArray))
                            editTextName2.setText(""+data[1].second)

                            viewModel.relationType3 = ""+data[2].first
                            spinnerRelationType3.setText(""+data[2].first.relationType(relationArray))
                            editTextName3.setText(""+data[2].second)
                            image1.visibility = View.VISIBLE
                            image2.visibility = View.VISIBLE
                            image3.visibility = View.VISIBLE
                        }
                        4 -> {
                            viewModel.relationType1 = ""+data[0].first
                            spinnerRelationType1.setText(""+data[0].first.relationType(relationArray))
                            editTextName1.setText(""+data[0].second)

                            viewModel.relationType2 = ""+data[1].first
                            spinnerRelationType2.setText(""+data[1].first.relationType(relationArray))
                            editTextName2.setText(""+data[1].second)

                            viewModel.relationType3 = ""+data[2].first
                            spinnerRelationType3.setText(""+data[2].first.relationType(relationArray))
                            editTextName3.setText(""+data[2].second)

                            viewModel.relationType4 = ""+data[3].first
                            spinnerRelationType4.setText(""+data[3].first.relationType(relationArray))
                            editTextName4.setText(""+data[3].second)
                            image1.visibility = View.VISIBLE
                            image2.visibility = View.VISIBLE
                            image3.visibility = View.VISIBLE
                            image4.visibility = View.VISIBLE
                        }
                        5 -> {
                            viewModel.relationType1 = ""+data[0].first
                            spinnerRelationType1.setText(""+data[0].first.relationType(relationArray))
                            editTextName1.setText(""+data[0].second)

                            viewModel.relationType2 = ""+data[1].first
                            spinnerRelationType2.setText(""+data[1].first.relationType(relationArray))
                            editTextName2.setText(""+data[1].second)

                            viewModel.relationType3 = ""+data[2].first
                            spinnerRelationType3.setText(""+data[2].first.relationType(relationArray))
                            editTextName3.setText(""+data[2].second)

                            viewModel.relationType4 = ""+data[3].first
                            spinnerRelationType4.setText(""+data[3].first.relationType(relationArray))
                            editTextName4.setText(""+data[3].second)

                            viewModel.relationType5 = ""+data[4].first
                            spinnerRelationType5.setText(""+data[4].first.relationType(relationArray))
                            editTextName5.setText(""+data[4].second)
                            image1.visibility = View.VISIBLE
                            image2.visibility = View.VISIBLE
                            image3.visibility = View.VISIBLE
                            image4.visibility = View.VISIBLE
                            image5.visibility = View.VISIBLE
                        } else -> {
                            image1.visibility = View.GONE
                            image2.visibility = View.GONE
                            image3.visibility = View.GONE
                            image4.visibility = View.GONE
                            image5.visibility = View.GONE

                            emptyAll()
                        }
                    }

//                    if(data.size == 1){
//                        viewModel.relationType1 = ""+data[0].first
//                        spinnerRelationType1.setText(""+data[0].first.relationType(relationArray))
//                        editTextName1.setText(""+data[0].second)
//                    }
//
//                    if(data.size == 2){
//                        viewModel.relationType1 = ""+data[0].first
//                        spinnerRelationType1.setText(""+data[0].first.relationType(relationArray))
//                        editTextName1.setText(""+data[0].second)
//
//                        viewModel.relationType2 = ""+data[1].first
//                        spinnerRelationType2.setText(""+data[1].first.relationType(relationArray))
//                        editTextName2.setText(""+data[1].second)
//                    }
//
//                    if(data.size == 3){
//                        viewModel.relationType1 = ""+data[0].first
//                        spinnerRelationType1.setText(""+data[0].first.relationType(relationArray))
//                        editTextName1.setText(""+data[0].second)
//
//                        viewModel.relationType2 = ""+data[1].first
//                        spinnerRelationType2.setText(""+data[1].first.relationType(relationArray))
//                        editTextName2.setText(""+data[1].second)
//
//                        viewModel.relationType3 = ""+data[2].first
//                        spinnerRelationType3.setText(""+data[2].first.relationType(relationArray))
//                        editTextName3.setText(""+data[2].second)
//                    }
//
//                    if(data.size == 4){
//                        viewModel.relationType1 = ""+data[0].first
//                        spinnerRelationType1.setText(""+data[0].first.relationType(relationArray))
//                        editTextName1.setText(""+data[0].second)
//
//                        viewModel.relationType2 = ""+data[1].first
//                        spinnerRelationType2.setText(""+data[1].first.relationType(relationArray))
//                        editTextName2.setText(""+data[1].second)
//
//                        viewModel.relationType3 = ""+data[2].first
//                        spinnerRelationType3.setText(""+data[2].first.relationType(relationArray))
//                        editTextName3.setText(""+data[2].second)
//
//                        viewModel.relationType4 = ""+data[3].first
//                        spinnerRelationType4.setText(""+data[3].first.relationType(relationArray))
//                        editTextName4.setText(""+data[3].second)
//                    }
//
//                    if(data.size == 5){
//                        viewModel.relationType1 = ""+data[0].first
//                        spinnerRelationType1.setText(""+data[0].first.relationType(relationArray))
//                        editTextName1.setText(""+data[0].second)
//
//                        viewModel.relationType2 = ""+data[1].first
//                        spinnerRelationType2.setText(""+data[1].first.relationType(relationArray))
//                        editTextName2.setText(""+data[1].second)
//
//                        viewModel.relationType3 = ""+data[2].first
//                        spinnerRelationType3.setText(""+data[2].first.relationType(relationArray))
//                        editTextName3.setText(""+data[2].second)
//
//                        viewModel.relationType4 = ""+data[3].first
//                        spinnerRelationType4.setText(""+data[3].first.relationType(relationArray))
//                        editTextName4.setText(""+data[3].second)
//
//                        viewModel.relationType5 = ""+data[4].first
//                        spinnerRelationType5.setText(""+data[4].first.relationType(relationArray))
//                        editTextName5.setText(""+data[4].second)
//                    }
                }
            })


            btSave.singleClick {
                viewModel.relationName1 = editTextName1.text.toString()
                viewModel.relationName2 = editTextName2.text.toString()
                viewModel.relationName3 = editTextName3.text.toString()
                viewModel.relationName4 = editTextName4.text.toString()
                viewModel.relationName5 = editTextName5.text.toString()


                if(viewModel.relationType1.isEmpty() &&
                    viewModel.relationType2.isEmpty() &&
                    viewModel.relationType3.isEmpty() &&
                    viewModel.relationType4.isEmpty() &&
                    viewModel.relationType5.isEmpty()){
                        showSnackBar(resources.getString(R.string.select_nominee_type))
                } else {
                    if((viewModel.relationType1.isNotEmpty() && (viewModel.relationType1.isEmpty() || viewModel.relationName1.isEmpty())) ||
                        (viewModel.relationName1.isNotEmpty() && (viewModel.relationType1.isEmpty() || viewModel.relationName1.isEmpty()))){
                        showSnackBar(resources.getString(R.string.enter_nominee_name))
                    } else if((viewModel.relationType2.isNotEmpty() && (viewModel.relationType2.isEmpty() || viewModel.relationName2.isEmpty())) ||
                            (viewModel.relationName2.isNotEmpty() && (viewModel.relationType2.isEmpty() || viewModel.relationName2.isEmpty()))){
                        showSnackBar(resources.getString(R.string.enter_nominee_name))
                    } else if((viewModel.relationType3.isNotEmpty() && (viewModel.relationType3.isEmpty() || viewModel.relationName3.isEmpty()))||
                            (viewModel.relationName3.isNotEmpty() && (viewModel.relationType3.isEmpty() || viewModel.relationName3.isEmpty()))){
                        showSnackBar(resources.getString(R.string.enter_nominee_name))
                    } else if((viewModel.relationType4.isNotEmpty() && (viewModel.relationType4.isEmpty() || viewModel.relationName4.isEmpty()))||
                    (viewModel.relationName4.isNotEmpty() && (viewModel.relationType4.isEmpty() || viewModel.relationName4.isEmpty()))){
                        showSnackBar(resources.getString(R.string.enter_nominee_name))
                    } else if((viewModel.relationType5.isNotEmpty() && (viewModel.relationType5.isEmpty() || viewModel.relationName5.isEmpty()))||
                    (viewModel.relationName5.isNotEmpty() && (viewModel.relationType5.isEmpty() || viewModel.relationName5.isEmpty()))){
                        showSnackBar(resources.getString(R.string.enter_nominee_name))
                    } else {
                        readData(DataStoreKeys.LOGIN_DATA) { loginUser ->
                            if (loginUser != null) {
                                val _id = Gson().fromJson(loginUser, Login::class.java).id
                                val hashMap = HashMap<String, String>().apply {
                                    if(viewModel.relationType1.isNotEmpty()){
                                        put("nominee[]["+viewModel.relationType1+"]", viewModel.relationName1)
                                    }
                                    if(viewModel.relationType2.isNotEmpty()){
                                        put("nominee[]["+viewModel.relationType2+"]", viewModel.relationName2)

                                    }
                                    if(viewModel.relationType3.isNotEmpty()){
                                        put("nominee[]["+viewModel.relationType3+"]", viewModel.relationName3)

                                    }
                                    if(viewModel.relationType4.isNotEmpty()){
                                        put("nominee[]["+viewModel.relationType4+"]", viewModel.relationName4)

                                    }
                                    if(viewModel.relationType5.isNotEmpty()){
                                        put("nominee[]["+viewModel.relationType5+"]", viewModel.relationName5)

                                    }
                                }
                                viewModel.updateNomineeDetails(view = requireView(), ""+_id, hashMap)
                            }
                        }
                    }
                }
            }

            spinnerRelationType1.singleClick {
                requireActivity().hideKeyboard()
                showDropDownDialog1()
            }

            spinnerRelationType2.singleClick {
                requireActivity().hideKeyboard()
                showDropDownDialog2()
            }

            spinnerRelationType3.singleClick {
                requireActivity().hideKeyboard()
                showDropDownDialog3()
            }

            spinnerRelationType4.singleClick {
                requireActivity().hideKeyboard()
                showDropDownDialog4()
            }

            spinnerRelationType5.singleClick {
                requireActivity().hideKeyboard()
                showDropDownDialog5()
            }
        }
    }

    private fun emptyAll() {
        binding.apply {
            spinnerRelationType1.setText("")
            editTextName1.setText("")
            viewModel.relationType1 = ""
            viewModel.relationName1 = ""

            spinnerRelationType2.setText("")
            editTextName2.setText("")
            viewModel.relationType2 = ""
            viewModel.relationName2 = ""

            spinnerRelationType3.setText("")
            editTextName3.setText("")
            viewModel.relationType3 = ""
            viewModel.relationName3 = ""

            spinnerRelationType4.setText("")
            editTextName4.setText("")
            viewModel.relationType4 = ""
            viewModel.relationName4 = ""

            spinnerRelationType5.setText("")
            editTextName5.setText("")
            viewModel.relationType5 = ""
            viewModel.relationName5 = ""
        }
    }


    private fun showDropDownDialog1() {
        val list=resources.getStringArray(R.array.relation_array)
        MaterialAlertDialogBuilder(requireContext(), R.style.DialogTheme)
            .setTitle(resources.getString(R.string.relationship_TypeStar))
            .setItems(list) {_,which->
                binding.spinnerRelationType1.setText(list[which])
                when(which){
                    0-> viewModel.relationType1 = "father"
                    1-> viewModel.relationType1 = "mother"
                    2-> viewModel.relationType1 = "son"
                    3-> viewModel.relationType1 = "daughter"
                    4-> viewModel.relationType1 = "sister"
                    5-> viewModel.relationType1 = "brother"
                    6-> viewModel.relationType1 = "husband"
                    7-> viewModel.relationType1 = "wife"
                }
            }.show()
    }


    private fun showDropDownDialog2() {
        val list=resources.getStringArray(R.array.relation_array)
        MaterialAlertDialogBuilder(requireContext(), R.style.DialogTheme)
            .setTitle(resources.getString(R.string.relationship_TypeStar))
            .setItems(list) {_,which->
                binding.spinnerRelationType2.setText(list[which])
                when(which){
                    0-> viewModel.relationType2 = "father"
                    1-> viewModel.relationType2 = "mother"
                    2-> viewModel.relationType2 = "son"
                    3-> viewModel.relationType2 = "daughter"
                    4-> viewModel.relationType2 = "sister"
                    5-> viewModel.relationType2 = "brother"
                    6-> viewModel.relationType2 = "husband"
                    7-> viewModel.relationType2 = "wife"
                }
            }.show()
    }

    private fun showDropDownDialog3() {
        val list=resources.getStringArray(R.array.relation_array)
        MaterialAlertDialogBuilder(requireContext(), R.style.DialogTheme)
            .setTitle(resources.getString(R.string.relationship_TypeStar))
            .setItems(list) {_,which->
                binding.spinnerRelationType3.setText(list[which])
                when(which){
                    0-> viewModel.relationType3 = "father"
                    1-> viewModel.relationType3 = "mother"
                    2-> viewModel.relationType3 = "son"
                    3-> viewModel.relationType3 = "daughter"
                    4-> viewModel.relationType3 = "sister"
                    5-> viewModel.relationType3 = "brother"
                    6-> viewModel.relationType3 = "husband"
                    7-> viewModel.relationType3 = "wife"
                }
            }.show()
    }

    private fun showDropDownDialog4() {
        val list=resources.getStringArray(R.array.relation_array)
        MaterialAlertDialogBuilder(requireContext(), R.style.DialogTheme)
            .setTitle(resources.getString(R.string.relationship_TypeStar))
            .setItems(list) {_,which->
                binding.spinnerRelationType4.setText(list[which])
                when(which){
                    0-> viewModel.relationType4 = "father"
                    1-> viewModel.relationType4 = "mother"
                    2-> viewModel.relationType4 = "son"
                    3-> viewModel.relationType4 = "daughter"
                    4-> viewModel.relationType4 = "sister"
                    5-> viewModel.relationType4 = "brother"
                    6-> viewModel.relationType4 = "husband"
                    7-> viewModel.relationType4 = "wife"
                }
            }.show()
    }

    private fun showDropDownDialog5() {
        val list=resources.getStringArray(R.array.relation_array)
        MaterialAlertDialogBuilder(requireContext(), R.style.DialogTheme)
            .setTitle(resources.getString(R.string.relationship_TypeStar))
            .setItems(list) {_,which->
                binding.spinnerRelationType5.setText(list[which])
                when(which){
                    0-> viewModel.relationType5 = "father"
                    1-> viewModel.relationType5 = "mother"
                    2-> viewModel.relationType5 = "son"
                    3-> viewModel.relationType5 = "daughter"
                    4-> viewModel.relationType5 = "sister"
                    5-> viewModel.relationType5 = "brother"
                    6-> viewModel.relationType5 = "husband"
                    7-> viewModel.relationType5 = "wife"
                }
            }.show()
    }


    private fun fieldsEdit() {
        binding.apply {
            viewModel.isEditable.observe(viewLifecycleOwner, Observer {
                spinnerRelationType1.isEnabled = it
                spinnerRelationType2.isEnabled = it
                spinnerRelationType3.isEnabled = it
                spinnerRelationType4.isEnabled = it
                spinnerRelationType5.isEnabled = it
                editTextName1.isEnabled = it
                editTextName2.isEnabled = it
                editTextName3.isEnabled = it
                editTextName4.isEnabled = it
                editTextName5.isEnabled = it
                image1.isEnabled = it
                image2.isEnabled = it
                image3.isEnabled = it
                image4.isEnabled = it
                image5.isEnabled = it
            })
        }
    }
}