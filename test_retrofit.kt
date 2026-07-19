import retrofit2.Retrofit
import retrofit2.http.POST
import retrofit2.http.Query

interface GeminiApiService {
    @POST("v1beta/models/gemini-1.5-flash:generateContent")
    fun generateContent(@Query("key") apiKey: String): retrofit2.Call<Any>
}

fun main() {
    val retrofit = Retrofit.Builder()
        .baseUrl("https://generativelanguage.googleapis.com/")
        .build()
    
    val service = retrofit.create(GeminiApiService::class.java)
    val call = service.generateContent("TEST_KEY")
    println(call.request().url().toString())
}
