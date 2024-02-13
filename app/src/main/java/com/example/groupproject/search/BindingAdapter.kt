package com.example.groupproject.search

import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView

@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<Suggestion>?) {
    val adapter = recyclerView.adapter as GridAdapter
    adapter.submitList(data)
}

@BindingAdapter("data")
fun bindData(recyclerView: RecyclerView, data: List<Suggestion>?) {
    val adapter = recyclerView.adapter as GridAdapter
    adapter.submitList(data)
}

@BindingAdapter("suggestionText")
fun bindTextSuggestion(textView: TextView, suggestion: Suggestion?) {
    if (suggestion != null) {
        textView.text = suggestion.text
    } else {
        textView.text = "error"
    }
}
