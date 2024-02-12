package com.example.groupproject.search

import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import java.io.IOException

data class Post(
    var style: String,
    var text: String,
    var startIndex: Int,
    var endIndex: Int
)

class SearchUtil() {

    val client = OkHttpClient()

//    val test = Post("general", "This is a test text", 0, 15)

//    private var _userInputSearchData: MutableLiveData<SearchText> = MutableLiveData()
//    val userInputSearchData: MutableLiveData<SearchText>
//        get() = _userInputSearchData

    var userInputSearchData = "No phrase was submitted"

    val gson = Gson()

//    val jsonBody = gson.toJson(testData)

    val searchKey = "6i8rh0iJ3C0Rc9fRcFhX2LarE4oVwa3N"





    suspend fun convertDataToClass() {

        val mediaType = MediaType.parse("application/json")
//        val body = RequestBody.create(mediaType, "{\"text\":\"${_userInputSearchData.value?.text}\",\"style\":\"general\",\"startIndex\":0,\"endIndex\":${userInputSearchData.value?.text?.count()}}")
        val body = RequestBody.create(mediaType, "{\"text\":\"${userInputSearchData}\",\"style\":\"general\",\"startIndex\":0,\"endIndex\":${userInputSearchData.count()}}")
        val request = Request.Builder()
            .url("https://api.ai21.com/studio/v1/paraphrase")
            .post(body)
            .addHeader("accept", "application/json")
            .addHeader("content-type", "application/json")
            .addHeader("Authorization", "Bearer ${searchKey}")
            .build()

        withContext(Dispatchers.IO) {
            try {
                val searchResponse = client.newCall(request).execute()
                println(userInputSearchData.toString())

                if (searchResponse.isSuccessful) {
                    // Get the response body as a string
                    val responseBody = searchResponse.body()?.string()

                    // Use Gson to parse the JSON response body into your data class
                    val phraseResponse: PhraseResponse? = Gson().fromJson(responseBody, PhraseResponse::class.java)
                    println("${userInputSearchData.count()}")
                    println(responseBody.toString())

                    // Now you have your data class object
                    if (phraseResponse != null) {
                        println(phraseResponse.suggestions.toString())
                    }
                } else {
                    // Handle unsuccessful response
                    println("Error: ${searchResponse.code()}")
                }

                // Close the response to release resources
                searchResponse.close()
            } catch (e: IOException) {
                // Handle IO exceptions
                println("IOException: ${e.message}")
            }
        }
    }

//    private val moshi = Moshi.Builder()
//        .add(KotlinJsonAdapterFactory())
//        .build()
//
//    private val retrofit = Retrofit.Builder()
//        .addConverterFactory(MoshiConverterFactory.create(moshi))
//        .baseUrl("https://api.ai21.com/studio/v1/paraphrase")
//        .build()
//
//    interface SearchService {
//        @GET("paraphrase")
//        suspend fun search(): PhraseResponse
//    }
//
//    enum class MyAPIStatus { LOADING, ERROR, DONE }
//
//    @BindingAdapter("apiStatus")
//    fun bindStatus(statusTextView: TextView, status: MyAPIStatus?) {
//        when (status) {
//            MyAPIStatus.LOADING -> {
//                statusTextView.visibility = View.VISIBLE
//                statusTextView.text = "Loading"
//            }
//
//            MyAPIStatus.ERROR -> {
//                statusTextView.visibility = View.VISIBLE
//                statusTextView.text = "Error"
//            }
//
//            MyAPIStatus.DONE -> {
//                statusTextView.visibility = View.GONE
//            }
//
//            else -> {}
//        }
//    }
//
//
//// ... (your existing code)
//
//    private val searchService = retrofit.create(SearchService::class.java)

}