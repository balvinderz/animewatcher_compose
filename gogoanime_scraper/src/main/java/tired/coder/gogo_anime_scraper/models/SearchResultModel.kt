package tired.coder.gogo_anime_scraper.models

data class SearchResultModel(
    val name : String,
    val imageUrl :String,
    val pageLink :String,
    val releasedYear: Int
)