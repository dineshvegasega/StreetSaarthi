package com.demo.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.streetsaarthi.databinding.ItemChatLeftBinding
import com.streetsaarthi.screens.main.complaintsFeedback.historyDetail.HistoryDetailVM


class HomeAdapter () :
    ListAdapter<HistoryDetailVM.ItemModel, HomeAdapter.PremiumPacksViewHolder>(
        DELIVERY_ITEM_COMPARATOR
    ) {


    inner class PremiumPacksViewHolder(private val binding: ItemChatLeftBinding) :

        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
            }
        }

        fun bind(model: HistoryDetailVM.ItemModel, position: Int) {
            binding.apply {
                binding.tvMessage.text = model.name
//                if(position % 2 == 0){
//                    binding.ivNotification.setImageResource(model.color)
//                    binding.txtName.setTextColor(binding.root.context.getResources().getColorStateList(R.color._164871))
//                    binding.mainRoot.setBackgroundTintList(binding.root.context.getResources().getColorStateList(R.color._7ed2f2))
//                    binding.roundCircle.setBackgroundTintList(binding.root.context.getResources().getColorStateList(R.color._164871))
//                    binding.iconColor.setBackgroundTintList(binding.root.context.getResources().getColorStateList(R.color._7ed2f2))
//                }else{
//                    binding.ivNotification.setImageResource(model.color)
//                    binding.txtName.setTextColor(binding.root.context.getResources().getColorStateList(R.color._7ed2f2))
//                    binding.mainRoot.setBackgroundTintList(binding.root.context.getResources().getColorStateList(R.color._164871))
//                    binding.roundCircle.setBackgroundTintList(binding.root.context.getResources().getColorStateList(R.color._7ed2f2))
//                    binding.iconColor.setBackgroundTintList(binding.root.context.getResources().getColorStateList(R.color._164871))
//                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup,viewType: Int): PremiumPacksViewHolder {
        val binding =
            ItemChatLeftBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PremiumPacksViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PremiumPacksViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null) {
            holder.bind(currentItem, position)

        }
    }

    companion object {
        private val DELIVERY_ITEM_COMPARATOR = object : DiffUtil.ItemCallback<HistoryDetailVM.ItemModel>() {
            override fun areItemsTheSame(
                oldItem: HistoryDetailVM.ItemModel,
                newItem: HistoryDetailVM.ItemModel
            ): Boolean {
                return false
            }

            override fun areContentsTheSame(
                oldItem: HistoryDetailVM.ItemModel,
                newItem: HistoryDetailVM.ItemModel
            ): Boolean {
                return false
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(model: HistoryDetailVM.ItemModel)
    }

}