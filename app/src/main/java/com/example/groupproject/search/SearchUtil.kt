//package com.example.groupproject.search
//
//import android.view.View
//import android.widget.TextView
//import androidx.databinding.BindingAdapter
//import androidx.recyclerview.widget.RecyclerView
//import com.squareup.moshi.Moshi
//import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
//import okhttp3.MediaType
//import okhttp3.OkHttpClient
//import okhttp3.Request
//import okhttp3.RequestBody
//import retrofit2.Retrofit
//import retrofit2.converter.moshi.MoshiConverterFactory
//import retrofit2.create
//import retrofit2.http.GET
//import java.security.Provider.Service
//
//val client = OkHttpClient()
//
//val mediaType = MediaType.parse("application/json")
//val body = RequestBody.create(mediaType, "{\"style\":\"general\"}")
//val request = Request.Builder()
//    .url("https://api.ai21.com/studio/v1/paraphrase")
//    .post(body)
//    .addHeader("accept", "application/json")
//    .addHeader("content-type", "application/json")
//    .addHeader("Authorization", "Bearer YOUR_API_KEY")
//    .build()
//
//val searchResponse = client.newCall(request).execute()
//
//val searchKey = "6i8rh0iJ3C0Rc9fRcFhX2LarE4oVwa3N"
//
//private const val BASE_URL = "https://api.ai21.com/studio/v1/"
//
//private val moshi = Moshi.Builder()
//    .add(KotlinJsonAdapterFactory())
//    .build()
//
//private val retrofit = Retrofit.Builder()
//    .addConverterFactory(MoshiConverterFactory.create(moshi))
//    .baseUrl(BASE_URL)
//    .build()
//
//interface SearchService {
//    @GET("paraphrase")
//    suspend fun search(): SearchText
//}
//
//object SearchObject {
//    val retrofitService : SearchService by lazy {
//        retrofit.create(SearchService::class.java)
//    }
//}
//
//enum class MyAPIStatus { LOADING, ERROR, DONE }
//
//
//@BindingAdapter("apiStatus")
//fun bindStatus(statusTextView: TextView, status: MyAPIStatus?) {
//    when (status) {
//        MyAPIStatus.LOADING -> {
//            statusTextView.visibility = View.VISIBLE
//            statusTextView.text = "Loading"
//        }
//        MyAPIStatus.ERROR -> {
//            statusTextView.visibility = View.VISIBLE
//            statusTextView.text = "Error"
//
//        }
//        MyAPIStatus.DONE -> {
//            statusTextView.visibility = View.GONE
//
//        }
//
//        else -> {}
//    }
//}
