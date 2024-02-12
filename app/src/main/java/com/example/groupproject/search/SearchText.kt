package com.example.groupproject.search

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody


data class SearchText(
    val text: String,
    val style: String?,
    val startIndex: Int?,
    val endIndex: Int?
) : Parcelable { }

