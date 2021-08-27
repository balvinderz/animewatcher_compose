package tired.coder.gogo_anime_scraper

import org.jsoup.Jsoup

class TmdbHelper {
    companion object{
        const val  API_KEY   = "8e70eb3c7bcd2397e9ddf8f8d0d299c8"

    }
    fun searchAnimeByName(name : String ): String{
        val url ="https://api.themoviedb.org/3/search/multi?api_key=$API_KEY&query=$name&page=1&include_adult=true"
      val response =   Jsoup.connect(url).ignoreContentType(true).get()
        val json = response.body().text()
        return json
    }
}
fun main(){
    val tmdbHelper  =TmdbHelper()
    tmdbHelper.searchAnimeByName("Steins gate")
}