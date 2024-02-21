package com.example.groupproject.database
import android.widget.ImageView

import androidx.databinding.BindingAdapter

object BindingAdapters {

    @JvmStatic
    @BindingAdapter("app:imageResource")
    fun setImageResource(imageView: ImageView, resource: Int?) {
        resource?.let {
            imageView.setImageResource(it)
        }
    }
}
