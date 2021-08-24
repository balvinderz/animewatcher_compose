package tired.coder.animewatcher

import tired.coder.gogo_anime_scraper.models.SearchResultModel
import tired.coder.lib.models.RecentAnimeModel


fun SearchResultModel.toRecentAnimeModel() : RecentAnimeModel{
    return RecentAnimeModel(
        name = this.name,
        episode = "",
        episodeUrl = this.pageLink,
        imageUrl = this.imageUrl
    )
}
fun List<SearchResultModel>.toRecentAnimeModelList() : List<RecentAnimeModel>{
    return this.map { it.toRecentAnimeModel() }
}