package com.streetsaarthi.screens.onboarding.walkThrough

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.streetsaarthi.databinding.WalkThroughBinding
import com.streetsaarthi.databinding.WalkThroughItemBinding
import com.streetsaarthi.screens.onboarding.onboard.Onboard
import com.squareup.picasso.Picasso

class WalkThroughPagerAdapter :ListAdapter<WalkThrough.Item, WalkThroughPagerAdapter.PremiumPacksViewHolder>(
        DELIVERY_ITEM_COMPARATOR
    ) {
    inner class PremiumPacksViewHolder(private val binding: WalkThroughItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
            }
        }

        fun bind(model: WalkThrough.Item) {
            binding.apply {
                textHeaderadfdsfTxt3.text = ""+model.name
                textHeaderTxt2.text = ""+model.desc
                imageLogo.setImageResource(model.image)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup,viewType: Int): PremiumPacksViewHolder {
        val binding =
            WalkThroughItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PremiumPacksViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PremiumPacksViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null) {
            holder.bind(currentItem)
        }
    }

    companion object {
        private val DELIVERY_ITEM_COMPARATOR = object : DiffUtil.ItemCallback<WalkThrough.Item>() {
            override fun areItemsTheSame(
                oldItem: WalkThrough.Item,
                newItem: WalkThrough.Item
            ): Boolean {
                return false
            }

            override fun areContentsTheSame(
                oldItem: WalkThrough.Item,
                newItem: WalkThrough.Item
            ): Boolean {
                return false
            }
        }
    }
}