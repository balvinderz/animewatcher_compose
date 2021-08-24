package tired.coder.animewatcher.screens.home

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import tired.coder.animewatcher.BaseViewModel
import tired.coder.animewatcher.toRecentAnimeModelList
import tired.coder.gogo_anime_scraper.GogoAnimeScraper
import tired.coder.gogo_anime_scraper.enums.ReleaseType
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