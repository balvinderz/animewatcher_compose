package tired.coder.gogo_anime_scraper

import org.jsoup.Jsoup

class TmdbHelper(val apiKey : String ) {
    fun searchAnimeByName(name : String ): String{

        val url ="https://api.themoviedb.org/3/search/multi?api_key=$apiKey&query=$name&page=1&include_adult=true"
      val response =   Jsoup.connect(url).ignoreContentType(true).get()
        val json = response.body().text()
        return json
    }
}
fun main(){
    val tmdbHelper  =TmdbHelper("")
    tmdbHelper.searchAnimeByName("Steins gate")
}