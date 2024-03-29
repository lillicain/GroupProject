package com.example.groupproject.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.groupproject.evilgarden.SharedViewModel
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



class SearchUtil(
    private val sharedViewModel: SharedViewModel
//    private val sharedViewModel: SharedViewModel
) : ViewModel() {

    val client = OkHttpClient()


//    private var _userInputSearchData: MutableLiveData<SearchText> = MutableLiveData()
//    val userInputSearchData: MutableLiveData<SearchText>
//        get() = _userInputSearchData

    var userInputSearchData = "No phrase was submitted"

    val searchKey = "6i8rh0iJ3C0Rc9fRcFhX2LarE4oVwa3N"

    private val _suggestions = MutableLiveData<List<Suggestion>?>()
    val suggestions: LiveData<List<Suggestion>?>
        get() = _suggestions

//    var suggestions = listOf<Suggestion>(Suggestion("testestte"))

//    var phraseResponse = PhraseResponse("test", listOf(Suggestion("hello")))


    suspend fun convertDataToClass() {
        println("${suggestions.value?.get(0)?.text.toString()} 9999999999999999999999")
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
        fun addXp() {
            sharedViewModel.updateUserXP(10)
        }
        withContext(Dispatchers.IO) {
            try {
                val startsWithIs = userInputSearchData.startsWith("is ", ignoreCase = true)

                if (startsWithIs) {
                    // Remove "is" from the beginning of the string
                    userInputSearchData = userInputSearchData.substring(3)
                    println(userInputSearchData)
                    println(userInputSearchData.toString())
                    println(userInputSearchData.toString())
                    println(userInputSearchData.toString())
                    println(userInputSearchData.toString())
                    println(userInputSearchData.toString())
                    println(userInputSearchData.toString())
                    println(userInputSearchData.toString())
                    println(userInputSearchData.toString())
                    println(userInputSearchData.toString())
                    println(userInputSearchData.toString())
                }
                val searchResponse = client.newCall(request).execute()
                println(userInputSearchData.toString())

                if (searchResponse.isSuccessful) {

                    // Get the response body as a string
                    val responseBody = searchResponse.body()?.string()

                    // Use Gson to parse the JSON response body into your data class
                    val phraseResponse: PhraseResponse? = Gson().fromJson(responseBody, PhraseResponse::class.java)
                    if (phraseResponse != null) {
                        println(phraseResponse.suggestions.toString())
//
                        _suggestions.postValue(phraseResponse.suggestions)
                    }
                    println(_suggestions.value?.get(0)?.text.toString())
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
        addXp()
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