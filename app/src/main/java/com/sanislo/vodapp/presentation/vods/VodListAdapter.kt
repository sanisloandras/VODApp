package com.sanislo.vodapp.presentation.vods

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.sanislo.vodapp.R
import com.sanislo.vodapp.databinding.ItemVodBinding
import com.sanislo.vodapp.domain.model.VodItem

class VodListAdapter(private val onClick: (VodItem) -> Unit) :
    ListAdapter<VodItem, VodListAdapter.VodViewHolder>(object : DiffUtil.ItemCallback<VodItem>() {
        override fun areItemsTheSame(oldItem: VodItem, newItem: VodItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: VodItem, newItem: VodItem): Boolean {
            return oldItem == newItem
        }

    }) {

    inner class VodViewHolder(private val binding: ItemVodBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                onClick(getItem(bindingAdapterPosition))
            }
        }

        fun bind(vodItem: VodItem) {
            Glide.with(itemView).load(vodItem.image)
                .apply(RequestOptions().placeholder(R.drawable.placeholder))
                .into(binding.ivImage)
            binding.tvTitle.text = vodItem.title
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VodViewHolder {
        return VodViewHolder(
            ItemVodBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: VodViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}