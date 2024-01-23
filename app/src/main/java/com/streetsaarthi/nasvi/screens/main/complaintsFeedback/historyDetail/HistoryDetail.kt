package com.streetsaarthi.nasvi.screens.main.complaintsFeedback.historyDetail

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.res.ResourcesCompat
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.demo.home.HomeAdapter
import com.streetsaarthi.nasvi.R
import com.streetsaarthi.nasvi.databinding.HistoryDetailBinding
import com.streetsaarthi.nasvi.screens.mainActivity.MainActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HistoryDetail : Fragment() {
    private val viewModel: HistoryDetailVM by viewModels()
    private var _binding: HistoryDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = HistoryDetailBinding.inflate(inflater)
        return binding.root

    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_MODE_CHANGED)
        MainActivity.mainActivity.get()?.callFragment(0)
        binding.apply {
            inclideHeaderSearch.textHeaderTxt.text = HtmlCompat.fromHtml(getString(R.string.trackingId, "<b>12344682</b>"), HtmlCompat.FROM_HTML_MODE_LEGACY);
         //   "Tracking Id: #12344682"
            val typeface: Typeface? = ResourcesCompat.getFont(requireContext(), R.font.roboto_medium)
            inclideHeaderSearch.textHeaderTxt.typeface = typeface
            inclideHeaderSearch.btClose.visibility = View.VISIBLE
            inclideHeaderSearch.editTextSearch.visibility = View.GONE


            val strings = java.util.ArrayList<HistoryDetailVM.ItemModel>()

            strings.add(HistoryDetailVM.ItemModel(R.drawable.item_feedback, "Orders"))
            strings.add(HistoryDetailVM.ItemModel(R.drawable.item_feedback, "How it Works"))
            strings.add(HistoryDetailVM.ItemModel(R.drawable.item_feedback, "About us"))
            strings.add(HistoryDetailVM.ItemModel(R.drawable.item_feedback, "Contact us"))
            viewModel.chatAdapter.submitList(strings)
            viewModel.chatAdapter.notifyDataSetChanged()
            rvGiftCardList.adapter=viewModel.chatAdapter
        }
    }

    override fun onDestroyView() {
        _binding = null
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        super.onDestroyView()
    }

}