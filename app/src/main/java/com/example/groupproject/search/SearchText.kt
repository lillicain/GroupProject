package com.example.groupproject.search

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody

@Parcelize
data class SearchText(
    val text: String,
    val style: String?,
    val startIndex: Int?,
    val endIndex: Int?
) : Parcelable
