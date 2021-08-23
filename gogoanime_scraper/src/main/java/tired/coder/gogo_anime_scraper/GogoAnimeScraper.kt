package tired.coder.gogo_anime_scraper

import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.json.simple.JSONValue
import org.jsoup.Jsoup
import tired.coder.gogo_anime_scraper.enums.ReleaseType
import tired.coder.gogo_anime_scraper.enums.toGogoAnimeType
import tired.coder.gogo_anime_scraper.models.VideoModel
import tired.coder.lib.models.RecentAnimeModel

class GogoAnimeScraper {
    private val baseUrl = "https://gogoanime.pe/";
    fun getRecentReleases(type : ReleaseType): List<RecentAnimeModel> {
        val recentAnimes = mutableListOf<RecentAnimeModel>()
        val gogoAnimePage = Jsoup.connect("https://ajax.gogo-load.com/ajax/page-recent-release.html?page=1&type=${type.toGogoAnimeType()}").get()
        val recentAnimeLis = gogoAnimePage.getElementsByClass("last_episodes loaddub").first()!!
            .getElementsByTag("ul").first()!!.getElementsByTag("li")
        for (recentAnimeLi in recentAnimeLis) {
            val name = recentAnimeLi.getElementsByClass("name").first()!!.text()
            val episode = recentAnimeLi.getElementsByClass("episode").first()!!.text()
            val image = recentAnimeLi.getElementsByTag("img").first()!!.attr("src")
            val episodeUrl =
                recentAnimeLi.getElementsByClass("img").first()!!.getElementsByTag("a").first()!!
                    .absUrl("href")
            recentAnimes.add(RecentAnimeModel(name, episode, episodeUrl, image))
        }
        return recentAnimes
    }
    suspend fun getVideoUrl(animeEpisodeUrl: String): VideoModel? {
        try {
            val animePageDocument = Jsoup.connect(animeEpisodeUrl).get()
            val streamUrl = (animePageDocument.getElementsByTag("iframe").first()!!.absUrl("src"))
            val streamPageDocument =
                Jsoup.connect(streamUrl.replace("streaming.php", "loadserver.php")).get()
            val scripts = streamPageDocument.getElementsByTag("script")

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
            return VideoModel(json["file"] as String, json["type"] as String,json["label"] as String)
        }catch (ex : Exception)
        {
            print(ex)
            return null;
        }

    }
}
suspend fun main(){
    val scraper = GogoAnimeScraper()
    val animeList = scraper.getVideoUrl("https://gogoanime.pe/dragon-ball-kai-2014-episode-69")
    print(animeList)
}