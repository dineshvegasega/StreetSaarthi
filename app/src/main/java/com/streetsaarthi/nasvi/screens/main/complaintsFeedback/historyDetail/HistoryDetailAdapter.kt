package com.demo.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.streetsaarthi.nasvi.R
import com.streetsaarthi.nasvi.databinding.ItemChatLeftBinding
import com.streetsaarthi.nasvi.databinding.ItemChatRightBinding
import com.streetsaarthi.nasvi.databinding.ItemLoadingBinding
import com.streetsaarthi.nasvi.models.chat.DataX
import com.streetsaarthi.nasvi.screens.main.complaintsFeedback.history.HistoryAdapter
import com.streetsaarthi.nasvi.utils.changeDateFormat
import com.streetsaarthi.nasvi.utils.glideImage
import com.streetsaarthi.nasvi.utils.imageZoom
import com.streetsaarthi.nasvi.utils.singleClick


class HistoryDetailAdapter () :
    ListAdapter<DataX, RecyclerView.ViewHolder>(
        DELIVERY_ITEM_COMPARATOR
    ) {
    private val LAYOUT_ONE=0
    private val LAYOUT_TWO=1
    private val LAYOUT_THREE=2

    private var isLoadingAdded: Boolean = false
    private var retryPageLoad: Boolean = false

    inner class LeftMessagesViewHolder(private val binding: ItemChatLeftBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(model: DataX) {
            binding.apply {
                if (model.reply != "null" && model.reply != null){
                    tvMessage.text = model.reply
                    tvMessage.visibility = View.VISIBLE
                } else {
                    tvMessage.visibility = View.GONE
                }

                if (model.media?.name != "null" && model.media?.name != null){
                    model.media?.url?.glideImage(binding.root.context, ivMap)

                    ivMap.visibility = View.VISIBLE

                    ivMap.singleClick {
                        model.media?.let {
                            arrayListOf(it.url).imageZoom(ivMap)
                        }
                    }
                } else {
                    ivMap.visibility = View.GONE
                }

                model.reply_date?.let {
                    tvTime.text = "${model.reply_date.changeDateFormat("yyyy-MM-dd HH:mm:ss", "hh:mm a")?.uppercase()}"
                }

                model.reply_date?.let {
                    ivDate.text = "${model.reply_date.changeDateFormat("yyyy-MM-dd HH:mm:ss", "dd MMM yyyy")?.uppercase()}"
                }



                if(model.status == "resolved") {
                    group.visibility = View.GONE
                    ivOpenClose.visibility = View.VISIBLE
                    ivDate.visibility = View.GONE
                    ivOpenClose.text = HtmlCompat.fromHtml(binding.root.resources.getString(R.string.conversation_marked_admin, "<b>"+binding.root.resources.getString(R.string.close)+"</b>" , "<b>"+binding.root.resources.getString(R.string.admin_txt)+"</b>")+" "+model.reply_date.changeDateFormat("yyyy-MM-dd HH:mm:ss", "dd MMM, yyyy hh:mm a"), HtmlCompat.FROM_HTML_MODE_LEGACY)
                } else if(model.status == "re-open") {
                    group.visibility = View.GONE
                    ivOpenClose.visibility = View.VISIBLE
                    ivDate.visibility = View.GONE
                    ivOpenClose.text = HtmlCompat.fromHtml(binding.root.resources.getString(R.string.conversation_marked_admin, "<b>"+binding.root.resources.getString(R.string.re_open)+"</b>" , "<b>"+binding.root.resources.getString(R.string.admin_txt)+"</b>")+" "+model.reply_date.changeDateFormat("yyyy-MM-dd HH:mm:ss", "dd MMM, yyyy hh:mm a"), HtmlCompat.FROM_HTML_MODE_LEGACY)
                }else if(model.status == "closed") {
                    group.visibility = View.GONE
                    ivOpenClose.visibility = View.VISIBLE
                    ivDate.visibility = View.GONE
                    ivOpenClose.text = HtmlCompat.fromHtml(binding.root.resources.getString(R.string.conversation_marked_admin, "<b>"+binding.root.resources.getString(R.string.close)+"</b>" , "<b>"+binding.root.resources.getString(R.string.admin_txt)+"</b>")+" "+model.reply_date.changeDateFormat("yyyy-MM-dd HH:mm:ss", "dd MMM, yyyy hh:mm a"), HtmlCompat.FROM_HTML_MODE_LEGACY)
                }else if(model.status == "in-progress") {
                    group.visibility = View.VISIBLE
                    ivOpenClose.visibility = View.GONE
                    ivDate.visibility = if (model.dateShow == true) View.VISIBLE else View.GONE
                }else if(model.status == "Pending" || model.status == "pending") {
                    group.visibility = View.VISIBLE
                    ivOpenClose.visibility = View.GONE
                    ivDate.visibility = if (model.dateShow == true) View.VISIBLE else View.GONE
                }

            }
        }
    }

    inner class RightMessagesViewHolder(private val binding: ItemChatRightBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(model: DataX) {
            binding.apply {
                if (model.reply != "null" && model.reply != null){
                    tvMessage.text = model.reply
                    tvMessage.visibility = View.VISIBLE
                } else {
                    tvMessage.visibility = View.GONE
                }

                if (model.media?.name != "null" && model.media?.name != null){
                    model.media?.url?.glideImage(binding.root.context, ivMap)
                    ivMap.visibility = View.VISIBLE

                    ivMap.singleClick {
                        model.media?.let {
                            arrayListOf(it.url).imageZoom(ivMap)
                        }
                    }
                } else {
                    ivMap.visibility = View.GONE
                }

                model.reply_date?.let {
                    tvTime.text = "${model.reply_date.changeDateFormat("yyyy-MM-dd HH:mm:ss", "hh:mm a")?.uppercase()}"
                }

                model.reply_date?.let {
                    ivDate.text = "${model.reply_date.changeDateFormat("yyyy-MM-dd HH:mm:ss", "dd MMM yyyy")?.uppercase()}"
                }


                if(model.status == "resolved") {
                    group.visibility = View.GONE
                    ivOpenClose.visibility = View.VISIBLE
                    ivDate.visibility = View.GONE
                    ivOpenClose.text = HtmlCompat.fromHtml(binding.root.resources.getString(R.string.conversation_marked_admin, "<b>"+binding.root.resources.getString(R.string.close)+"</b>" , "<b>"+binding.root.resources.getString(R.string.member_txt)+"</b>")+" "+model.reply_date.changeDateFormat("yyyy-MM-dd HH:mm:ss", "dd MMM, yyyy hh:mm a"), HtmlCompat.FROM_HTML_MODE_LEGACY)
                } else if(model.status == "re-open") {
                    group.visibility = View.GONE
                    ivOpenClose.visibility = View.VISIBLE
                    ivDate.visibility = View.GONE
                    ivOpenClose.text = HtmlCompat.fromHtml(binding.root.resources.getString(R.string.conversation_marked_admin, "<b>"+binding.root.resources.getString(R.string.re_open)+"</b>" , "<b>"+binding.root.resources.getString(R.string.member_txt)+"</b>")+" "+model.reply_date.changeDateFormat("yyyy-MM-dd HH:mm:ss", "dd MMM, yyyy hh:mm a"), HtmlCompat.FROM_HTML_MODE_LEGACY)
                }else if(model.status == "closed") {
                    group.visibility = View.GONE
                    ivOpenClose.visibility = View.VISIBLE
                    ivDate.visibility = View.GONE
                    ivOpenClose.text = HtmlCompat.fromHtml(binding.root.resources.getString(R.string.conversation_marked_admin, "<b>"+binding.root.resources.getString(R.string.close)+"</b>" , "<b>"+binding.root.resources.getString(R.string.member_txt)+"</b>")+" "+model.reply_date.changeDateFormat("yyyy-MM-dd HH:mm:ss", "dd MMM, yyyy hh:mm a"), HtmlCompat.FROM_HTML_MODE_LEGACY)
                }else if(model.status == "in-progress") {
                    group.visibility = View.VISIBLE
                    ivOpenClose.visibility = View.GONE
                    ivDate.visibility = if (model.dateShow == true) View.VISIBLE else View.GONE
                }else if(model.status == "Pending" || model.status == "pending") {
                    group.visibility = View.VISIBLE
                    ivOpenClose.visibility = View.GONE
                    ivDate.visibility = if (model.dateShow == true) View.VISIBLE else View.GONE

                }
            }
        }
    }

    inner class LoaderViewHolder(private val binding: ItemLoadingBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }


    override fun getItemViewType(position: Int): Int {
//        return position % 2
//        if(position==0)
//            return LAYOUT_ONE;
//        else
//            return LAYOUT_TWO;

        if(getItem(position).user_type == "member"){
            return LAYOUT_TWO;
        }else{
            return LAYOUT_ONE;
        }

    }



    override fun onCreateViewHolder(parent: ViewGroup,viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            0 -> {
                val binding=
                    ItemChatLeftBinding.inflate(LayoutInflater.from(parent.context),parent,false)
                return LeftMessagesViewHolder(binding)
            }
            1 -> {
                val binding=
                    ItemChatRightBinding.inflate(LayoutInflater.from(parent.context),parent,false)
                return RightMessagesViewHolder(binding)
            }
            else -> {
                val binding=
                    ItemLoadingBinding.inflate(LayoutInflater.from(parent.context),parent,false)
                return LoaderViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder.getItemViewType() == LAYOUT_ONE) {
            val currentItem=getItem(position)
            (holder as LeftMessagesViewHolder).bind(currentItem)
        }

        if(holder.getItemViewType() == LAYOUT_TWO) {
            val currentItem=getItem(position)
            (holder as RightMessagesViewHolder).bind(currentItem)
        }
    }

    companion object {
        private val DELIVERY_ITEM_COMPARATOR = object : DiffUtil.ItemCallback<DataX>() {
            override fun areItemsTheSame(
                oldItem: DataX,
                newItem: DataX
            ): Boolean {
                return false
            }

            override fun areContentsTheSame(
                oldItem: DataX,
                newItem: DataX
            ): Boolean {
                return false
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(model: DataX)
    }


    fun addLoadingFooter() {
        isLoadingAdded = true
    }

    fun removeLoadingFooter() {
        isLoadingAdded = false
    }
}