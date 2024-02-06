package com.example.groupproject.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.groupproject.databinding.FragmentSearchBinding

class GridAdapter(val onClickListener: OnClickListener) : ListAdapter<SearchText, GridAdapter.PropertyViewHolder>(DiffCallback) {
    class PropertyViewHolder(private var binding: FragmentSearchBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(searchText: SearchText) {
            binding.text
            binding.executePendingBindings()
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<SearchText>() {
        override fun areItemsTheSame(oldItem: SearchText, newItem: SearchText): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: SearchText, newItem: SearchText): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PropertyViewHolder {
        return PropertyViewHolder(FragmentSearchBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: PropertyViewHolder, position: Int) {
        val property = getItem(position)
        holder.itemView.setOnClickListener {
            onClickListener.onClick(property)
        }
        holder.bind(property)
    }


    class OnClickListener(val clickListener: (property: SearchText) -> Unit) {
        fun onClick(property: SearchText) = clickListener(property)
    }
}
