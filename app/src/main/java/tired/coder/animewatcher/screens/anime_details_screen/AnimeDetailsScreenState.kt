package tired.coder.animewatcher.screens.anime_details_screen

import tired.coder.gogo_anime_scraper.models.DetailedAnimeModel
import tired.coder.gogo_anime_scraper.models.EpisodeModel

data class AnimeDetailsScreenState(
    val isLoading: Boolean = true,
    val animePageLink: String  = "",
    val episodes : List<EpisodeModel> = emptyList(),
    val detailedAnimeModel: DetailedAnimeModel = DetailedAnimeModel()
)