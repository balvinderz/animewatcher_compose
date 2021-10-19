package tired.coder.gogo_anime_scraper

import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.json.simple.JSONValue
import org.jsoup.Jsoup
import tired.coder.gogo_anime_scraper.enums.ReleaseType
import tired.coder.gogo_anime_scraper.enums.toGogoAnimeType
import tired.coder.gogo_anime_scraper.models.DetailedAnimeModel
import tired.coder.gogo_anime_scraper.models.EpisodeModel
import tired.coder.gogo_anime_scraper.models.SearchResultModel
import tired.coder.gogo_anime_scraper.models.VideoModel
import tired.coder.lib.models.RecentAnimeModel

class GogoAnimeScraper {
    companion object{
        const val baseUrl = "https://gogoanime.pe/";
    }
    fun getRecentReleases(type: ReleaseType): List<RecentAnimeModel> {
        val recentAnimes = mutableListOf<RecentAnimeModel>()
        val gogoAnimePage =
            Jsoup.connect("https://ajax.gogo-load.com/ajax/page-recent-release.html?page=1&type=${type.toGogoAnimeType()}")
                .get()
        val recentAnimeLis = gogoAnimePage.getElementsByClass("last_episodes loaddub").first()!!
            .getElementsByTag("ul").first()!!.getElementsByTag("li")
        for (recentAnimeLi in recentAnimeLis) {
            val name = recentAnimeLi.getElementsByClass("name").first()!!.text()
            val episode = recentAnimeLi.getElementsByClass("episode").first()!!.text()
            val image = recentAnimeLi.getElementsByTag("img").first()!!.attr("src")
            val episodeUrl =
                recentAnimeLi.getElementsByClass("img").first()!!.getElementsByTag("a").first()!!
                    .absUrl("href").replace("https://ajax.gogo-load.com/",baseUrl)
            recentAnimes.add(RecentAnimeModel(name, episode, episodeUrl, image))
        }
        return recentAnimes
    }

    suspend fun searchAnime(query: String): List<SearchResultModel> {
        val searchResultModels = mutableListOf<SearchResultModel>()
        try {

            val searchPageDocument = Jsoup.connect("${baseUrl}search.html?keyword=$query").get()
            val searchResults = searchPageDocument.getElementsByClass("last_episodes").first()!!
                .getElementsByTag("ul").first()!!.getElementsByTag("li")
            for (searchResult in searchResults) {
                val imageUrl = searchResult.select("img").first()!!.absUrl("src")
                val name = searchResult.select("p.name > a").first()!!.text()
                val pageLink = searchResult.select("p.name > a").first()!!.absUrl("href")
                val releasedYear =
                    searchResult.select("p.released").first()!!.text().split(":")[1].trim()
                        .toInt()
                searchResultModels.add(SearchResultModel(name, imageUrl, pageLink, releasedYear))
            }
        } catch (e: java.lang.Exception) {

        }
        return searchResultModels
    }
    suspend fun getEpisodeLinks(startEpisode : Int,endEpisode :Int ,animeId : Int) : List<EpisodeModel>{
        //TODO Implement paging here
        val episodeList = mutableListOf<EpisodeModel>()
        val url ="https://ajax.gogo-load.com/ajax/load-list-episode?ep_start=$startEpisode&ep_end=$endEpisode&id=$animeId&default_ep=0"
        try {
            val episodeDocument = Jsoup.connect(url).get()
            val lis = episodeDocument.select("li")
            for(li in lis){
                episodeList.add(
                    EpisodeModel(
                    li.select("div.name").text().replace("EP","").trim().toInt(),
                        li.select("a").first()!!.absUrl("href").replace("https://ajax.gogo-load.com/",baseUrl)
                )
                )
            }
        }catch (e: Exception){

        }
        return episodeList
    }

    fun getVideoUrl(animeEpisodeUrl: String): VideoModel? {
        try {
            val animePageDocument = Jsoup.connect(animeEpisodeUrl).get()
            val streamUrl = (animePageDocument.getElementsByTag("iframe").first()!!.absUrl("src"))
            val streamPageDocument =
                Jsoup.connect(streamUrl).get()
            val multiServerUrl = streamPageDocument.getElementsByClass("linkserver").firstOrNull{
                it.hasAttr("data-provider")
            }?.attr("data-video") ?: return null
            val multiServerDocument = Jsoup.connect(multiServerUrl).get()
            val scripts = multiServerDocument.getElementsByTag("script")

            val script = scripts.firstOrNull {
                (it.outerHtml().contains("playerInstance.setup"))
            }
            val scriptRegex = Regex("\\[\\.*.*")

            val jsonArray = JSONValue.parse(
                scriptRegex.find(script!!.outerHtml())!!.value.replace("file", "\"file\"")
                    .replace("\'", "\"")
                    .replace("label", "\"label\"").replace("'type'", "\"type\"")
            ) as JSONArray
            val json = jsonArray[0] as JSONObject
            print(json)
            return VideoModel(
                json["file"] as String,
                json["type"] as String,
                json["label"] as String
            )
        } catch (ex: Exception) {
            print(ex)
            return null;
        }

    }

    fun getAnimeDetails(url: String): DetailedAnimeModel? {
        assert(url.startsWith(baseUrl))
        try {
            val animeDetailPage = Jsoup.connect(url).get()
            val name = animeDetailPage.select("div.anime_info_body_bg > h1").first()!!.text()
            val type =
                animeDetailPage.select("div.anime_info_body_bg > p")[1]?.getElementsByTag("a")
                    ?.first()?.text() ?: ""
            val plotSummary =
                animeDetailPage.select("div.anime_info_body_bg > p")[2]?.text()
                    ?.replace("Plot Summary:", "")
                    ?: ""
            val genre =
                animeDetailPage.select("div.anime_info_body_bg > p")[3]?.text()?.replace("Genre:","") ?: ""
            val released =
                animeDetailPage.select("div.anime_info_body_bg > p")[4]?.text()?.replace("Released:","") ?: ""
            val status =
                animeDetailPage.select("div.anime_info_body_bg > p")[5]?.text()
                    ?.replace("Status:", "") ?: ""
            val otherName =
                animeDetailPage.select("div.anime_info_body_bg > p")[6]?.text()?.replace("Other name:","") ?: ""
            val lis =  animeDetailPage.select("ul#episode_page").select("li")
            val startEpisode =
                lis.first()?.select("a")?.first()
                    ?.attr("ep_start")?.toInt() ?: 0
            val imageUrl =  animeDetailPage.select("div.anime_info_body_bg >img").attr("src")
            val endEpisode =
                lis.last()?.select("a")?.first()
                    ?.attr("ep_end")?.toInt() ?: 0
            val animeId =
                animeDetailPage.select("div.anime_info_episodes_next > input#movie_id").first()
                    ?.attr("value")?.toInt() ?: 0
            return DetailedAnimeModel(
                animeId = animeId,
                name,
                type,
                plotSummary,
                genre,
                released,
                status,
                imageUrl,
                otherName,
                startEpisode,
                endEpisode = endEpisode
            )
        } catch (e: Exception) {
            return null
        }
    }
}

suspend fun main() {
    val scraper = GogoAnimeScraper()
   scraper.getVideoUrl("https://gogoanime.pe/dr-slump-arale-chan-episode-103")
}