package tired.coder.animewatcher.screens.video_screen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import tired.coder.animewatcher.BaseViewModel
import tired.coder.gogo_anime_scraper.GogoAnimeScraper
import javax.inject.Inject
@HiltViewModel
class VideoScreenViewModel @Inject  constructor(
    private val savedStateHandle: SavedStateHandle,
    val gogoAnimeScraper: GogoAnimeScraper
) : BaseViewModel<VideoScreenState>(
    VideoScreenState(
    episodeLink = savedStateHandle.get<String>("episode_link")!!,
    animeName = savedStateHandle.get<String>("anime_name")!!,
    episodeNumber = savedStateHandle.get<Int>("episode_number")!!,
)){
    init{
        viewModelScope.launch(Dispatchers.IO) {
           val url = gogoAnimeScraper.getVideoUrl(screenLiveData.value!!.episodeLink)
            _screenLiveData.postValue(screenLiveData.value!!.copy(
                videoUrl =  url?.url
            ))
        }
    }
}