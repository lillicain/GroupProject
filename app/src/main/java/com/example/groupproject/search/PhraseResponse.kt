package com.example.groupproject.search

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import java.util.UUID

@Parcelize
data class PhraseResponse(
    var id: String,
    var suggestions: List<Suggestion>
) : Parcelable

@Parcelize
data class Suggestion(
    var text: String,
    @Transient
    var id: String = generateId()
) : Parcelable

fun generateId() : String {
    // Generate a random UUID
    val myUuid = UUID.randomUUID()
    val myUuidAsString = myUuid.toString()
    return myUuidAsString
}