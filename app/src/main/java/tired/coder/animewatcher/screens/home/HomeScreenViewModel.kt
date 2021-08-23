package tired.coder.animewatcher.screens.home

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import tired.coder.animewatcher.BaseViewModel
import tired.coder.gogo_anime_scraper.GogoAnimeScraper
import tired.coder.gogo_anime_scraper.enums.ReleaseType
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor() : BaseViewModel<HomeScreenState>(HomeScreenState()) {
    fun onSearchChanged(newText : String){

        _screenLiveData.value = _screenLiveData.value!!.copy(
            searchText = newText,

        )
        if(newText.trim().length>=3) {
            _screenLiveData.postValue(_screenLiveData.value!!.copy(
                isSearchingLoading = true ,
            ))
            viewModelScope.launch(Dispatchers.IO) {

            }
        }
        }
    init{
        viewModelScope.launch(Dispatchers.IO) {
            val scraper = GogoAnimeScraper()
            val recentAnimes  = scraper.getRecentReleases(ReleaseType.Dub)

            viewModelScope.launch(Dispatchers.Main) {
                _screenLiveData.value = _screenLiveData.value!!.copy(
                    isDubLoading = false,
                    dubAnimeList =  mutableStateOf(recentAnimes)
                )
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            val scraper = GogoAnimeScraper()
            val recentAnimes  = scraper.getRecentReleases(ReleaseType.Sub)

            viewModelScope.launch(Dispatchers.Main) {
                _screenLiveData.value = _screenLiveData.value!!.copy(
                    isSubLoading = false,
                    subAnimeList =  mutableStateOf(recentAnimes)
                )
            }
        }

    }
}