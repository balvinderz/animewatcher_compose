package tired.coder.animewatcher.screens.anime_details_screen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import tired.coder.animewatcher.BaseViewModel
import tired.coder.gogo_anime_scraper.GogoAnimeScraper
import tired.coder.gogo_anime_scraper.TmdbHelper
import javax.inject.Inject

@HiltViewModel
class AnimeDetailsScreenViewModel @Inject constructor(
    private val gogoAnimeScraper: GogoAnimeScraper,
    private val savedStateHandle: SavedStateHandle,
    private val tmdbHelper: TmdbHelper,
) : BaseViewModel<AnimeDetailsScreenState>(
    AnimeDetailsScreenState(
        animePageLink = savedStateHandle.get<String>("anime_page_link")!!
    )
) {
    init {
        viewModelScope.launch(Dispatchers.IO) {
            val details =
                gogoAnimeScraper.getAnimeDetails(savedStateHandle.get<String>("anime_page_link")!!)
            if (details == null)
                _toastLiveData.postValue("Something went while fetching anime details")
            else {
                _screenLiveData.postValue(
                    _screenLiveData.value!!.copy(
                        isLoading = false,
                        detailedAnimeModel = details
                    )
                )
                val episodes = gogoAnimeScraper.getEpisodeLinks(
                    details.startEpisode ?: 0,
                    details.endEpisode ?: 0, details.animeId?.toInt() ?: 0
                )
                _screenLiveData.postValue(
                    _screenLiveData.value!!.copy(
                        episodes = episodes
                    )
                )
            }
        }
    }
}