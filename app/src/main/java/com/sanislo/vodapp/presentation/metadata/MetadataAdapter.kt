package com.sanislo.vodapp.presentation.metadata

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sanislo.vodapp.databinding.ItemMetadataBinding
import com.sanislo.vodapp.domain.model.MetadataItem

class MetadataAdapter : ListAdapter<MetadataItem, MetadataAdapter.ViewHolder>(object :
    DiffUtil.ItemCallback<MetadataItem>() {
    //never used anyway
    override fun areItemsTheSame(oldItem: MetadataItem, newItem: MetadataItem): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: MetadataItem, newItem: MetadataItem): Boolean {
        return oldItem == newItem
    }

}) {

    inner class ViewHolder(private val binding: ItemMetadataBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: MetadataItem) {
            binding.tvName.text = item.name
            binding.tvValue.text = item.value
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemMetadataBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}