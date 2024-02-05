package com.streetsaarthi.nasvi.screens.onboarding.quickRegistration

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.streetsaarthi.nasvi.R
import com.streetsaarthi.nasvi.databinding.QuickRegistration2Binding
import com.streetsaarthi.nasvi.screens.interfaces.CallBackListener
import com.streetsaarthi.nasvi.utils.isValidPassword
import com.streetsaarthi.nasvi.utils.showSnackBar
import com.streetsaarthi.nasvi.utils.singleClick
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class QuickRegistration2 : Fragment() , CallBackListener {
    private var _binding: QuickRegistration2Binding? = null
    private val binding get() = _binding!!
    private val viewModel: QuickRegistrationVM by activityViewModels()

    companion object{
        var callBackListener: CallBackListener? = null
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = QuickRegistration2Binding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        callBackListener = this
        binding.apply {

            var counter = 0
            var start: Int
            var end: Int
            imgCreatePassword.singleClick {
                if(counter == 0){
                    counter = 1
                    imgCreatePassword.setImageResource(R.drawable.ic_eye_open)
                    start=editTextCreatePassword.getSelectionStart()
                    end=editTextCreatePassword.getSelectionEnd()
                    editTextCreatePassword.setTransformationMethod(null)
                    editTextCreatePassword.setSelection(start,end)
                }else{
                    counter = 0
                    imgCreatePassword.setImageResource(R.drawable.ic_eye_closed)
                    start=editTextCreatePassword.getSelectionStart()
                    end=editTextCreatePassword.getSelectionEnd()
                    editTextCreatePassword.setTransformationMethod(PasswordTransformationMethod())
                    editTextCreatePassword.setSelection(start,end)
                }
            }


            var counter2 = 0
            var start2: Int
            var end2: Int
            imgReEnterPassword.singleClick {
                if(counter2 == 0){
                    counter2 = 1
                    imgReEnterPassword.setImageResource(R.drawable.ic_eye_open)
                    start2=editTextReEnterPassword.getSelectionStart()
                    end2=editTextReEnterPassword.getSelectionEnd()
                    editTextReEnterPassword.setTransformationMethod(null)
                    editTextReEnterPassword.setSelection(start2,end2)
                }else{
                    counter2 = 0
                    imgReEnterPassword.setImageResource(R.drawable.ic_eye_closed)
                    start2=editTextReEnterPassword.getSelectionStart()
                    end2=editTextReEnterPassword.getSelectionEnd()
                    editTextReEnterPassword.setTransformationMethod(PasswordTransformationMethod())
                    editTextReEnterPassword.setSelection(start2,end2)
                }
            }


            cbRememberMe.singleClick {
                getAgreeValue()
            }

            textTerms.singleClick {
                openTermConditionsDialog()
                Log.e("sadf", "ffff")
            }


            editTextCreatePassword.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    Log.e("TAG", "sAA "+s)
//                    if (editTextCreatePassword == null) {
//                        editTextCreatePassword.setText(s)
//                    }
//                    else {
//                       // editTextCreatePassword.setText("")
//                    }
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if(!editTextCreatePassword.text.toString().isEmpty()){
                        if(editTextCreatePassword.text.toString().length >= 0 && editTextCreatePassword.text.toString().length < 8){
                            textCreatePasswrordMsg.setText(R.string.InvalidPassword)
                            textCreatePasswrordMsg.visibility = View.VISIBLE
                        } else if(!isValidPassword(editTextCreatePassword.text.toString().trim())){
                            textCreatePasswrordMsg.setText(R.string.InvalidPassword)
                            textCreatePasswrordMsg.visibility = View.VISIBLE
                        } else {
                            textCreatePasswrordMsg.visibility = View.GONE
                        }
                    }
                    editTextCreatePassword.requestFocus()
                }
            })


            editTextReEnterPassword.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                @SuppressLint("SuspiciousIndentation")
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if(!editTextCreatePassword.text.toString().isEmpty()){
                        if(!editTextReEnterPassword.text.toString().isEmpty()){
//                            if(editTextReEnterPassword.text.toString().length >= 0 && editTextReEnterPassword.text.toString().length < 8){
//                                textReEnterPasswrordMsg.setText(R.string.InvalidPassword)
//                                textReEnterPasswrordMsg.visibility = View.VISIBLE
//                            } else
                                if (editTextCreatePassword.text.toString() != editTextReEnterPassword.text.toString()){
                                    textReEnterPasswrordMsg.setText(R.string.CreatePasswordReEnterPasswordisnotsame)
                                    textReEnterPasswrordMsg.visibility = View.VISIBLE
                                    getAgreeValue()
                            } else {
                                textReEnterPasswrordMsg.visibility = View.GONE
                                    getAgreeValue()
                            }
                        }
                    }
                    editTextReEnterPassword.requestFocus()
                }
            })


        }

    }


    fun getAgreeValue(){
        binding.apply {
            if(editTextCreatePassword.text.toString().isEmpty()){
                viewModel.isAgree.value = false
            } else if(editTextCreatePassword.text.toString().length >= 0 && editTextCreatePassword.text.toString().length < 8){
                viewModel.isAgree.value = false
            } else if(!isValidPassword(editTextCreatePassword.text.toString().trim())){
                viewModel.isAgree.value = false
            } else if (editTextReEnterPassword.text.toString().isEmpty()){
                viewModel.isAgree.value = false
//            } else if(editTextReEnterPassword.text.toString().length >= 0 && editTextReEnterPassword.text.toString().length < 8){
//                viewModel.isAgree.value = false
//            } else if(!isValidPassword(editTextReEnterPassword.text.toString().trim())){
//                viewModel.isAgree.value = false
            } else if (editTextCreatePassword.text.toString() != editTextReEnterPassword.text.toString()){
                viewModel.isAgree.value = false
            }  else if (!cbRememberMe.isChecked){
                viewModel.isAgree.value = false
            } else {
                viewModel.isAgree.value = true
            }
        }
    }


    override fun onCallBack(pos: Int) {
        Log.e("TAG", "onCallBackB "+pos)

        binding.apply {
            if (pos == 3) {
                if(editTextCreatePassword.text.toString().isEmpty()){
                    showSnackBar(getString(R.string.createPassword))
                } else if(editTextCreatePassword.text.toString().length >= 0 && editTextCreatePassword.text.toString().length < 8){
                    showSnackBar(getString(R.string.InvalidPassword))
                } else if(!isValidPassword(editTextCreatePassword.text.toString().trim())){
                    showSnackBar(getString(R.string.InvalidPassword))
                } else if (editTextReEnterPassword.text.toString().isEmpty()){
                    showSnackBar(getString(R.string.reEnterPassword))
                } else if(editTextReEnterPassword.text.toString().length >= 0 && editTextReEnterPassword.text.toString().length < 8){
                    showSnackBar(getString(R.string.InvalidPassword))
                } else if(!isValidPassword(editTextReEnterPassword.text.toString().trim())){
                    showSnackBar(getString(R.string.InvalidPassword))
                } else if (editTextCreatePassword.text.toString() != editTextReEnterPassword.text.toString()){
                    showSnackBar(getString(R.string.CreatePasswordReEnterPasswordisnotsame))
                }  else if (!cbRememberMe.isChecked){
                    showSnackBar(getString(R.string.Pleaseselectagree))
                } else {
                    viewModel.data.password = editTextCreatePassword.text.toString()
                    Log.e("TAG", "viewModel.dataB "+viewModel.data.toString())
                    QuickRegistration.callBackListener!!.onCallBack(4)
                }
            }
        }
    }


    private fun openTermConditionsDialog() {
        val mybuilder= Dialog(requireActivity())
        mybuilder.setContentView(R.layout.dialog_termsconditions)
        mybuilder.show()
        val window=mybuilder.window
        window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        window!!.setBackgroundDrawableResource(R.color._00000000)

        val yes = mybuilder.findViewById<AppCompatImageView>(R.id.imageCross)
        yes?.singleClick {
            mybuilder.dismiss()
        }
    }


    override fun onResume() {
        super.onResume()
        Log.e("TAG", "onResumeZZ")
        Handler(Looper.getMainLooper()).postDelayed({
            getAgreeValue()
        }, 200)
    }

    override fun onStop() {
        super.onStop()
        Log.e("TAG", "onStopZZ")

    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}