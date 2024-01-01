package com.streetsaarthi.screens.dashboard.details

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.os.Parcelable
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.demo.ui.detail.DetailVM
import com.streetsaarthi.databinding.DetailBinding
import com.streetsaarthi.models.Item
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class Detail : Fragment() {
    var TAG = "Detail"
    private val viewModel: DetailVM by viewModels()
    private var _binding: DetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DetailBinding.inflate(inflater)
        return binding.root
    }
    inline fun <reified T : Parcelable> Intent.parcelable(key: String): T? = when {
        SDK_INT >= 33 -> getParcelableExtra(key, T::class.java)
        else -> @Suppress("DEPRECATION") getParcelableExtra(key) as? T
    }
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var data = if (SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable("data" , Item::class.java)
        } else {
            arguments?.getParcelable<Item>("data") as Item
        }


        binding.txtTitle.text = data?.name
        Picasso.get().load(
            data?.owner?.avatar_url
        ).into(binding!!.ivIcon)

        binding.txtUrl.text = Html.fromHtml(data?.html_url)
        binding.txtUrl.setOnClickListener {
            view.context.startActivity(
                Intent(Intent.ACTION_VIEW, Uri.parse(data?.html_url))
            )
        }
        binding.txtDesc.text = data?.description
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.adapter = viewModel.photosAdapter
        viewModel.readResult.observe(viewLifecycleOwner, Observer {
            viewModel.photosAdapter.submitList(it)
            viewModel.photosAdapter.notifyDataSetChanged()
        })


//        viewModel.getContributors(data?.owner?.login, data?.name)
    }
}