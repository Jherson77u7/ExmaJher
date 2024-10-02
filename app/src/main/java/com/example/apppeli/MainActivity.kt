package com.example.apppeli
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.example.apppeli.movie
import com.example.apppeli.MovieResponse
import com.example.apppeli.ApiClient
import com.example.apppeli.ui.theme.AppPeliTheme
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppPeliTheme  {
                Surface(modifier = Modifier.fillMaxSize()) {
                    MovieGrid()
                }
            }
        }
    }
}


@Composable
fun MovieGrid() {
    var movies by remember { mutableStateOf(listOf<movie>()) }
    var errorMessage by remember { mutableStateOf("") }


    LaunchedEffect(Unit) {
        ApiClient.apiService.getPopularMovies().enqueue(object : Callback<MovieResponse> {
            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                if (response.isSuccessful) {
                    movies = response.body()?.results ?: emptyList()
                } else {
                    errorMessage = "Error: ${response.code()}"
                }
            }

            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                errorMessage = t.message ?: "Error desconocido"
            }
        })
    }

    Column {
        if (errorMessage.isNotEmpty()) {
            Text(text = errorMessage, color = MaterialTheme.colorScheme.error)
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.padding(8.dp)
            ) {
                items(movies) { movie ->
                    MovieCard(movie)
                }
            }
        }
    }
}

@Composable
fun MovieCard(movie: movie) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Image(
                painter = rememberImagePainter("https://image.tmdb.org/t/p/w185${movie.poster_path}"),
                contentDescription = movie.original_title,
                modifier = Modifier
                    .size(150.dp)
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = movie.original_title, maxLines = 2)
        }
    }
}
