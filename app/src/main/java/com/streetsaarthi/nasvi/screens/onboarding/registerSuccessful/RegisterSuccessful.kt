package com.streetsaarthi.nasvi.screens.onboarding.registerSuccessful

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.streetsaarthi.nasvi.R
import com.streetsaarthi.nasvi.databinding.RegisterSuccessfulBinding
import com.streetsaarthi.nasvi.screens.mainActivity.MainActivity
import com.streetsaarthi.nasvi.utils.changeDateFormat
import com.streetsaarthi.nasvi.utils.singleClick
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterSuccessful : Fragment() {
    private var _binding: RegisterSuccessfulBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = RegisterSuccessfulBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        MainActivity.mainActivity.get()?.callFragment(0)
        binding.apply {
            btSignIn.singleClick {
                requireView().findNavController().navigate(R.id.action_registerSuccessful_to_loginPassword)
            }
//            textHeaderTxt312.text = HtmlCompat.fromHtml("${root.context.resources.getString(R.string.end_date, "<b>"+data.training_end_at.changeDateFormat("yyyy-MM-dd", "dd MMM, yyyy")+"</b>")}", HtmlCompat.FROM_HTML_MODE_LEGACY)

//            textHeaderTxt312.text = HtmlCompat.fromHtml(getString(R.string.congratulations_your_initial_registration_is_complete, ""),  HtmlCompat.FROM_HTML_MODE_LEGACY)
        }
    }


    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}