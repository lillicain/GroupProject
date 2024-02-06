import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.groupproject.search.SearchText
import com.google.gson.Gson
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

val client = OkHttpClient()

val testData = SearchText("world war three has started", "general", 1, 10)

val jsonBody = Gson().toJson(testData)

val searchKey = "6i8rh0iJ3C0Rc9fRcFhX2LarE4oVwa3N"


val mediaType = MediaType.parse("application/json")
val body = RequestBody.create(mediaType, jsonBody)
val request = Request.Builder()
    .url("https://api.ai21.com/studio/v1/paraphrase")
    .post(body)
    .addHeader("accept", "application/json")
    .addHeader("content-type", "application/json")
    .addHeader("Authorization", "Bearer ${searchKey}")
    .build()

val searchResponse = client.newCall(request).execute()

val response = client.newCall(request).execute()

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl("https://api.ai21.com/studio/v1/paraphrase")
    .build()

interface SearchService {
    @GET("paraphrase")
    suspend fun search(): SearchText
}

enum class MyAPIStatus { LOADING, ERROR, DONE }

@BindingAdapter("apiStatus")
fun bindStatus(statusTextView: TextView, status: MyAPIStatus?) {
    when (status) {
        MyAPIStatus.LOADING -> {
            statusTextView.visibility = View.VISIBLE
            statusTextView.text = "Loading"
        }
        MyAPIStatus.ERROR -> {
            statusTextView.visibility = View.VISIBLE
            statusTextView.text = "Error"
        }
        MyAPIStatus.DONE -> {
            statusTextView.visibility = View.GONE
        }
        else -> {}
    }
}
