package tired.coder.animewatcher.screens.anime_details_screen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import tired.coder.animewatcher.BaseViewModel
import tired.coder.gogo_anime_scraper.GogoAnimeScraper
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
        }
    }
}