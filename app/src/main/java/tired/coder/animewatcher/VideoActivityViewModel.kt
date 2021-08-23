package tired.coder.animewatcher

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Dispatcher
import tired.coder.gogo_anime_scraper.GogoAnimeScraper
import tired.coder.gogo_anime_scraper.models.VideoModel
import javax.inject.Inject

@HiltViewModel
class VideoActivityViewModel @Inject constructor()  : ViewModel(){
    val videoModel = MutableLiveData<VideoModel>(null)
    fun getVideoLink(animePageLink : String){
        viewModelScope.launch(Dispatchers.IO) {
            val scraper = GogoAnimeScraper()
            val x = scraper.getVideoUrl(animePageLink)
            viewModelScope.launch(Dispatchers.Main) {
                videoModel.value = x!!
            }
        }
    }
}