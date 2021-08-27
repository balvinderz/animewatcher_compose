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
    private val savedStateHandle: SavedStateHandle
) : BaseViewModel<AnimeDetailsScreenState>(
    AnimeDetailsScreenState(
        animePageLink = savedStateHandle.get<String>("anime_page_link")!!
    )
) {
    init {
        viewModelScope.launch(Dispatchers.IO) {
            val details =
                gogoAnimeScraper.getAnimeDetails(savedStateHandle.get<String>("anime_page_link")!!)
            if(details == null)
                _toastLiveData.postValue("Something went while fetching anime details")
            else {
                _screenLiveData.postValue(_screenLiveData.value!!.copy(
                    isLoading = false,
                    detailedAnimeModel = details
                ))
                try {
                    val image =
                        "https://www.themoviedb.org/t/p/w1920_and_h800_multi_faces/" + JSONObject(
                            TmdbHelper().searchAnimeByName(details.name ?: "")
                        ).getJSONArray("results").getJSONObject(0).getString("backdrop_path")
                    _screenLiveData.postValue(
                        _screenLiveData.value!!.copy(
                            backdropImage = image
                        )
                    )
                }catch (e: Exception){

                }
            }
        }
    }
}