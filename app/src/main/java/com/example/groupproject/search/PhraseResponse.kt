package com.example.groupproject.search

import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class PhraseResponse(
    var id: String,
    var suggestions: List<Suggestion>
) : Parcelable

@Parcelize
data class Suggestion(
    var text: String,
) : Parcelable