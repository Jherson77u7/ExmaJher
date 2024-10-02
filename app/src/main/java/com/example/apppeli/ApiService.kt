import com.example.apppeli.MovieResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("discover/movie")
    fun getPopularMovies(
        @Query("sort_by") sortBy: String = "popularity.desc",
        @Query("api_key") apiKey: String = "fa3e844ce31744388e07fa47c7c5d8c3"
    ): Call<MovieResponse>
}
