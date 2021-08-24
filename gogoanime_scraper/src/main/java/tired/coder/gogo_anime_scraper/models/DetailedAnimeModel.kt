package tired.coder.gogo_anime_scraper.models

data class DetailedAnimeModel(
    val animeId: Int? = null,
    val name: String? = null,
    val type : String? = null,
    val plotSummary: String? = null,
    val genre: String? = null ,
    val released : String? = null ,
    val status: String? = null ,
    val imageUrl : String ="" ,
    val otherName: String? = null ,
    val startEpisode: Int? = null ,
    val endEpisode: Int? = null ,
)