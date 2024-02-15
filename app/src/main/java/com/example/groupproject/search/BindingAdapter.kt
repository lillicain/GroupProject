package com.example.groupproject.search

import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView

@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<Suggestion>?) {
    val adapter = recyclerView.adapter as GridAdapter
    adapter.submitList(data)
}



