package com.example.groupproject.search

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SearchResponse(
    val id: String,
    val suggestions: List<SearchText>?
) : Parcelable { }