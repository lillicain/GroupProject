//import androidx.lifecycle.ViewModel

package com.example.groupproject.search

import android.widget.EditText
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import java.lang.Exception

class SearchViewModel : ViewModel() { }
//
//    private val _status = MutableLiveData<MyAPIStatus>()
//    val status: MutableLiveData<MyAPIStatus>
//        get() = _status
//
//    private val _properties = MutableLiveData<List<SearchText>>()
//
//    val properties: LiveData<List<SearchText>>
//        get() = _properties
//
//    private val _navigateToSelectedProperty = MutableLiveData<SearchText>()
//    val navigateToSelectedProperty: LiveData<SearchText>
//        get() = _navigateToSelectedProperty
//
//    private var viewModelJob = Job()
//    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main )
//
//    init {
//        getItemDetails()
//    }
//
//    fun getItemDetails() {
//        viewModelScope.launch {
//            _status.value = MyAPIStatus.LOADING
//            try {
//                _status.value = MyAPIStatus.DONE
//            } catch (e: Exception) {
//                print(e)
//                _status.value = MyAPIStatus.ERROR
//                _properties.value = ArrayList()
//            }
//        }
//    }
//    override fun onCleared() {
//        super.onCleared()
//        viewModelJob.cancel()
//    }
//
//    fun displayDetails(property: SearchText) {
//        _navigateToSelectedProperty.value = property
//    }
//
//    fun displayAllDetails() {
//        _navigateToSelectedProperty.value = null
//    }
//
//    fun updateFilter() {
//        getItemDetails()
//    }
//}

//    enum class PhraseApiStatus { LOADING, ERROR, DONE }
//
//    private var _status = MutableLiveData<PhraseApiStatus>()
//    val status: LiveData<PhraseApiStatus>
//        get() = _status
//
//    private val _phraseResponses = MutableLiveData<List<PhraseResponse>>()
//
//
//    val dictionaryWord: LiveData<List<PhraseResponse>>
//        get() = _phraseResponses
//
//    private fun getDictionaryWords(word: String) {
//        println(word)
//        viewModelScope.launch {
//            _status.value = PhraseApiStatus.LOADING
//            try {
//                //TODO: Add Filter for searching here
//                _phraseResponses.value = DictionaryApi.retrofitService.getProperties(word)
//                _status.value = PhraseApiStatus.LOADING
//            } catch (e: Exception) {
//                _status.value = PhraseApiStatus.ERROR
//                _phraseResponses.value = ArrayList()
//            }
//        }
//    }
//
//    fun executeSearch(coroutineScope: CoroutineScope) {
//        coroutineScope.launch {
//
//            try {
//                // Update UI to show loading state
//                _status.value = PhraseApiStatus.LOADING
//
//                // Call the API using Retrofit
//                val response = withContext(Dispatchers.IO) {
//                    searchService.search()
//                }
//
//                // Parse the response and update UI with paraphrased text
//                val paraphrasedText = response.suggestions[0].text
//                // (Display paraphrasedText in your UI element)
//
//                _status.value = PhraseApiStatus.DONE
//            } catch (e: Exception) {
//                // Handle errors gracefully
//                _status.value = PhraseApiStatus.ERROR
//                // (Display an appropriate error message in your UI)
//            }
//        }
//    }
//}
//
//private const val BASE_URL = "https://api.dictionaryapi.dev/api/v2/entries/en/"
//
//private  val moshi = Moshi.Builder()
//    .add(KotlinJsonAdapterFactory())
//    .build()
//
//private val retrofit = Retrofit.Builder()
//    .addConverterFactory(MoshiConverterFactory.create(moshi))
//    .baseUrl(BASE_URL)
//    .build()
//
//interface DictionaryApiService {
//
//    @GET("{searchedWord}")
//    suspend fun getProperties(@Path("searchedWord") searchedWord: String): List<DictionaryWord>
//}
//
//
//object DictionaryApi {
//    val retrofitService : DictionaryApiService by lazy { retrofit.create(DictionaryApiService::class.java) }
//}
