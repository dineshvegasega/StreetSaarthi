package com.streetsaarthi.screens.onboarding.walkThrough

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import com.demo.genericAdapter.GenericAdapter
import com.demo.networking.Repository
import com.streetsaarthi.MainActivity
import com.streetsaarthi.R
import com.streetsaarthi.databinding.OnboardItemBinding
import com.streetsaarthi.databinding.WalkThroughItemBinding
import com.streetsaarthi.screens.onboarding.onboard.Onboard
import com.squareup.picasso.Picasso
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WalkThroughVM @Inject constructor(private val repository: Repository): ViewModel() {

    var itemMain : ArrayList<WalkThrough.Item> ?= ArrayList()

    init {
        itemMain?.add(WalkThrough.Item(MainActivity.context.get()!!.getString(R.string.LearnAboutSchemes), MainActivity.context.get()!!.getString(R.string.desc1),R.drawable.walk1))
        itemMain?.add(WalkThrough.Item(MainActivity.context.get()!!.getString(R.string.ComplaintRedressal), MainActivity.context.get()!!.getString(R.string.desc2),R.drawable.walk2))
        itemMain?.add(WalkThrough.Item(MainActivity.context.get()!!.getString(R.string.KnowYourRights), MainActivity.context.get()!!.getString(R.string.desc3),R.drawable.walk3))
    }

    val photosAdapter = object : GenericAdapter<WalkThroughItemBinding, Onboard.Item>() {
        override fun onCreateView(
            inflater: LayoutInflater,
            parent: ViewGroup,
            viewType: Int
        ) = OnboardItemBinding.inflate(inflater, parent, false)

        override fun onBindHolder(binding: WalkThroughItemBinding, dataClass: Onboard.Item, position: Int) {
//            binding.textHeaderadfdsfTxt3.text = dataClass.name
//            Picasso.get().load(
//                dataClass.image
//            ).into(binding!!.imageLogo)
//            binding.root.setOnClickListener {
//                when(position) {
//                    0 -> it.findNavController().navigate(R.id.action_onboard_to_quickRegistration)
//                    1 -> it.findNavController().navigate(R.id.action_onboard_to_loginPassword)
//                    2 -> it.findNavController().navigate(R.id.action_onboard_to_loginOtp)
//                    3 -> it.findNavController().navigate(R.id.action_onboard_to_completeRegistration)
//                    4 -> it.findNavController().navigate(R.id.action_onboard_to_completeRegistration)
//
//                }
//            }
        }
    }



//    class WalkThroughPagerAdapter(itemMain: ArrayList<Onboard.Item>?) : RecyclerView.Adapter<WalkThroughPagerAdapter.ViewHolder>() {
//        class ViewHolder(val binding: WalkThroughItemBinding) : RecyclerView.ViewHolder(binding.root) {
//
//        }
//
//        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
//            WalkThroughItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        )
//
//        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//            with(holder) {
//                binding.apply {
//                    //Bind views with some data here
////                textHeaderadfdsfTxt3.text = dataClass.name
////                Picasso.get().load(
////                    itemMain[position]
////                ).into(imageLogo)
//                }
//            }
//        }
//
//        override fun getItemCount() = 3
//    }

}