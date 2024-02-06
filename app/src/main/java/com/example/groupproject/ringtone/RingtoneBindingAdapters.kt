package com.example.groupproject.ringtone

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView

@BindingAdapter("ringtone_listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<RingtoneProperty>) { // this is how you bind your view model to your fragment.
    val adapter = recyclerView.adapter as RingtoneListAdapter
    adapter.submitList(data)
}




