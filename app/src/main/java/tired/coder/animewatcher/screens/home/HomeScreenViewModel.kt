package tired.coder.animewatcher.screens.home

import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import tired.coder.animewatcher.ANIME_DETAIL_SCREEN
import tired.coder.animewatcher.BaseViewModel
import tired.coder.animewatcher.VIDEO_SCREEN
import tired.coder.animewatcher.toRecentAnimeModelList
import tired.coder.gogo_anime_scraper.GogoAnimeScraper
import tired.coder.gogo_anime_scraper.enums.ReleaseType
import tired.coder.lib.models.RecentAnimeModel
import java.util.*
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val gogoAnimeScraper : GogoAnimeScraper
) : BaseViewModel<HomeScreenState>(HomeScreenState()) {
    fun onSearchChanged(newText : String){

        _screenLiveData.value = _screenLiveData.value!!.copy(
            searchText = newText,

        )
        if(newText.trim().length>=3) {
            _screenLiveData.postValue(_screenLiveData.value!!.copy(
                isSearchingLoading = true ,
            ))
            viewModelScope.launch(Dispatchers.IO) {
            val searchedList = gogoAnimeScraper.searchAnime(query = newText.trim())
                _screenLiveData.postValue(_screenLiveData.value!!.copy(
                    searchedAnimeList = mutableStateOf(searchedList.toRecentAnimeModelList()),
                    isSearchingLoading = false,
                ))
            }
        }
        }

    fun onAnimeCardClicked(animeModel : RecentAnimeModel) {
        if(_screenLiveData.value!!.showingSearchedList) {
            _navigationLiveData.postValue("$ANIME_DETAIL_SCREEN/${Uri.encode(animeModel.episodeUrl)}")
        }
        else
        {
            _navigationLiveData.postValue("$VIDEO_SCREEN/${Uri.encode(animeModel.episodeUrl)}/${animeModel.name}/${animeModel.episode.split(" ")[1].toInt()}")

        }
    }

    init{
        viewModelScope.launch(Dispatchers.IO) {
            val recentAnimes  = gogoAnimeScraper.getRecentReleases(ReleaseType.Dub)

            viewModelScope.launch(Dispatchers.Main) {
                _screenLiveData.value = _screenLiveData.value!!.copy(
                    isDubLoading = false,
                    dubAnimeList =  mutableStateOf(recentAnimes)
                )
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            val recentAnimes  = gogoAnimeScraper.getRecentReleases(ReleaseType.Sub)

            viewModelScope.launch(Dispatchers.Main) {
                _screenLiveData.value = _screenLiveData.value!!.copy(
                    isSubLoading = false,
                    subAnimeList =  mutableStateOf(recentAnimes)
                )
            }
        }

    }
}