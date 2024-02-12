package com.example.groupproject.search

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody


data class SearchText(
    @SerializedName("a") val style: String,
    @SerializedName("b") var text: String,
    @SerializedName("c") val startIndex: Int,
    @SerializedName("d") val endIndex: Int
)
