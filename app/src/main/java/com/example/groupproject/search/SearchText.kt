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

val client = OkHttpClient()

val mediaType = MediaType.parse("application/json")
val body = RequestBody.create(mediaType, "{\"style\":\"general\"}")
val request = Request.Builder()
    .url("https://api.ai21.com/studio/v1/paraphrase")
    .post(body)
    .addHeader("accept", "application/json")
    .addHeader("content-type", "application/json")
    .addHeader("Authorization", "Bearer YOUR_API_KEY")
    .build()

val response = client.newCall(request).execute()

val key = "6i8rh0iJ3C0Rc9fRcFhX2LarE4oVwa3N"