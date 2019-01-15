import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.gson.gson
import io.ktor.request.path
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.*
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import org.slf4j.event.Level

data class Search(val search_title: String, val url: String)
data class Movie(var movie_id: Int, val title: String, val popularity: Int, val vote_count: Int, val poster_path: String)

fun main(args: Array<String>) {

    val search_title: String
    val api_key: String
    val url: String
    
    api_key = "6512ee85e1d7fe7f20d919acf680f0b1"
    url = "https://api.themoviedb.org/3/search/movie?api_key=" + api_key + "&language=en-US&query=" + search_title + "&include_adult=false"
    
    val popularity_summary: String
    
    val movieList = ArrayList<Movie>();
    val response = """{
            "movie_id": movie_id,
            "title": title,
            "poster_path": poster_path,
            "popularity_summary": "Popularity Score: " + popularity + " Total Votes: " + vote_count,
        }"""

    val image_url_path: String = "https://image.tmdb.org/t/p/w500" + poster_path
    
    var app = new Vue({
      el: '#app',
      data: {
        title: title,
        image: image_url_path,
        popularity_summary: popularity_summary
      },
      methods: {
        searchMovie: function (val search_title: String) {
          url = "https://api.themoviedb.org/3/search/movie?api_key=" + api_key + "&language=en-US&query=" + search_title + "&include_adult=false" 
          get(/search/movie{url})
        }
      }
    })

    embeddedServer(Netty, 8080) {
    
        install(CallLogging) {
            level = Level.DEBUG
            filter { call -> call.request.path().startsWith("/movie") }
            filter { call -> call.request.path().startsWith("/search") }
        }
        install(ContentNegotiation) {
            gson {
                setPrettyPrinting()
            }
        }
        routing() {
            route("/search") {
                post {
                    var movie = call.receive<Movie>();
                    movie.id = movieList.size;
                    movieList.add(movie);
                    --call.respond("Added")

                }
                delete("/{id}") {
                    call.respond(toDoList.removeAt(call.parameters["id"]!!.toInt()));
                }
                get("/{id}") {

                    call.respond(toDoList[call.parameters["id"]!!.toInt()]);
                }
                get {
                    call.respond(movieList);
                }
            }
        get("/search/movie{url}"){
            call.respond(movieList);

        }

        }
        
    }.start(wait = true)
}
