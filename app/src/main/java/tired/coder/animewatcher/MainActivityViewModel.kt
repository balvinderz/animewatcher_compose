package tired.coder.animewatcher

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import tired.coder.gogo_anime_scraper.GogoAnimeScraper
import tired.coder.lib.models.RecentAnimeModel
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor() : ViewModel() {
    val isLoading :  MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>(true)
    }
    val animeList by lazy { MutableLiveData<List<RecentAnimeModel>>() }
    init{
        viewModelScope.launch(Dispatchers.IO) {
            val scraper = GogoAnimeScraper()
           val recentAnimes  = scraper.getRecentReleases()

            viewModelScope.launch(Dispatchers.Main) {
                isLoading.value =false
                animeList.value = recentAnimes
            }
        }
    }


}