package com.example.groupproject.database
import android.view.View
import android.widget.ImageView
import android.widget.TextView

import androidx.databinding.BindingAdapter

object BindingAdapters {

    @JvmStatic
    @BindingAdapter("app:imageResource")
    fun setImageResource(imageView: ImageView, resource: Int?) {
        resource?.let {
            imageView.setImageResource(it)
        }
    }
    @BindingAdapter("android:visibility")
    @JvmStatic fun TextView.setVisibility(b: Boolean) {
        visibility = if (b) View.VISIBLE else View.GONE
    }
}
