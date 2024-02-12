package com.example.groupproject.search

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView

@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<Suggestion>?) {
    val adapter = recyclerView.adapter as GridAdapter
    adapter.submitList(data)
}
