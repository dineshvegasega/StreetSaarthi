package com.streetsaarthi.screens.main.training.liveTraining

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.streetsaarthi.R
import com.streetsaarthi.databinding.LiveTrainingBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LiveTraining : Fragment() {
    private val viewModel: LiveTrainingVM by viewModels()
    private var _binding: LiveTrainingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = LiveTrainingBinding.inflate(inflater)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            inclideHeaderSearch.textHeaderTxt.text = getString(R.string.live_training)

            recyclerView.setHasFixedSize(true)
            binding.recyclerView.adapter = viewModel.photosAdapter
            viewModel.photosAdapter.notifyDataSetChanged()
            viewModel.photosAdapter.submitList(viewModel.itemMain)


//            var check_ScrollingUp = false
//            recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                    super.onScrolled(recyclerView, dx, dy)
//                    if (dy > 0) {
//                        // Scrolling up
//                        if (check_ScrollingUp) {
//                            inclideHeaderSearch.root.startAnimation(
//                                AnimationUtils.loadAnimation(
//                                    context,
//                                   R.anim.abc_fade_in
//                                )
//                            )
//                            check_ScrollingUp = false
//                        }
//                    } else {
//                        // User scrolls down
//                        if (!check_ScrollingUp) {
//                            inclideHeaderSearch.root.startAnimation(
//                                    AnimationUtils
//                                        .loadAnimation(context, R.anim.trans_upwards)
//                                )
//                            check_ScrollingUp = true
//                        }
//                    }
//                }
//
//                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
//                    super.onScrollStateChanged(recyclerView, newState)
//                }
//            })
////

//            recyclerView.setOnScrollChangeListener(View.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
//                //scrollPoistion = scrollY
//            })

        }
    }
}