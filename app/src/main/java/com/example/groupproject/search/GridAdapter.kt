package com.example.groupproject.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.groupproject.databinding.FragmentSearchBinding
import com.example.groupproject.databinding.GridViewItemBinding

class GridAdapter(val onClickListener: OnClickListener) : ListAdapter<Suggestion, GridAdapter.PropertyViewHolder>(DiffCallback) {
    class PropertyViewHolder(private var binding: GridViewItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(suggestion: Suggestion) {
            binding.suggestion = suggestion
            binding.executePendingBindings()
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Suggestion>() {
        override fun areItemsTheSame(oldItem: Suggestion, newItem: Suggestion): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Suggestion, newItem: Suggestion): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PropertyViewHolder {
        return PropertyViewHolder(GridViewItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: PropertyViewHolder, position: Int) {
        val property = getItem(position)
        holder.itemView.setOnClickListener {
            onClickListener.onClick(property)
        }
        holder.bind(property)
    }


    class OnClickListener(val clickListener: (property: Suggestion) -> Unit) {
        fun onClick(property: Suggestion) = clickListener(property)
    }
}
