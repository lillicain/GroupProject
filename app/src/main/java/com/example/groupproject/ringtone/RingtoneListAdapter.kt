package com.example.groupproject.ringtone

import android.content.DialogInterface
import android.content.DialogInterface.OnClickListener
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.groupproject.databinding.ListViewItemBinding

class RingtoneListAdapter(private val onClickListener: OnClickListener): ListAdapter<RingtoneProperty, RingtoneListAdapter.RingtoneViewHolder>(DiffCallback) {
    class RingtoneViewHolder(private var binding: ListViewItemBinding):
            RecyclerView.ViewHolder(binding.root) {
                fun bind(ringtoneProperty: RingtoneProperty) {
                    binding.property = ringtoneProperty
                    binding.executePendingBindings()
                }
            }

    companion object DiffCallback: DiffUtil.ItemCallback<RingtoneProperty>() {
        override fun areItemsTheSame(oldItem: RingtoneProperty, newItem: RingtoneProperty): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: RingtoneProperty, newItem: RingtoneProperty): Boolean {
            return oldItem.name == newItem.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RingtoneViewHolder {
       return RingtoneViewHolder(ListViewItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: RingtoneViewHolder, position: Int) {
        val ringtoneProperty = getItem(position)
        holder.itemView.setOnClickListener {
            onClickListener.onClick(ringtoneProperty)
        }
        holder.bind(ringtoneProperty)
    }

    class OnClickListener(val clickListener:(ringtoneProperty:RingtoneProperty) -> Unit) {
        fun onClick(ringtoneProperty: RingtoneProperty) = clickListener(ringtoneProperty)
    }
}