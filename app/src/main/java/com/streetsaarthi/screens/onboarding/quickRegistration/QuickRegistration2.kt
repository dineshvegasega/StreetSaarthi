package com.streetsaarthi.screens.onboarding.quickRegistration

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.streetsaarthi.R
import com.streetsaarthi.databinding.QuickRegistration2Binding
import com.streetsaarthi.screens.interfaces.CallBackListener
import com.streetsaarthi.screens.onboarding.register.RegisterVM
import com.streetsaarthi.utils.showSnackBar
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
            textTerms.setOnClickListener {
                openTermConditionsDialog()
                Log.e("sadf", "ffff")
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
                } else if (editTextReEnterPassword.text.toString().isEmpty()){
                    showSnackBar(getString(R.string.reEnterPassword))
                } else if(editTextReEnterPassword.text.toString().length >= 0 && editTextReEnterPassword.text.toString().length < 8){
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
        yes?.setOnClickListener {
            mybuilder.dismiss()
        }
    }

}