package tired.coder.animewatcher.screens.anime_details_screen

import tired.coder.gogo_anime_scraper.models.DetailedAnimeModel

data class AnimeDetailsScreenState(
    val isLoading: Boolean = true,
    val animePageLink: String  = "",
    val detailedAnimeModel: DetailedAnimeModel = DetailedAnimeModel()
)