import com.example.kotlin.movil.model.ninja.ninjaRespones
import com.example.kotlin.movil.model.ninja.ninjaResponesItem
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface RetrofitService {

    @GET("dnslookup")
    fun getDNSLookup(
        @Query("domain") domain: String,
        @Header("X-Api-Key") apiKey: String
    ): Call<ninjaRespones>
}
