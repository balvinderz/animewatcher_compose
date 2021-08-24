package tired.coder.animewatcher.screens.anime_details_screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun AnimeDetailsScreenWithViewModel(viewModel : AnimeDetailsScreenViewModel = hiltViewModel()){
    val animeDetailsScreenState  = viewModel.screenLiveData.observeAsState()
}
@Composable
fun AnimeDetailsScreen(animeDetailsScreenState: AnimeDetailsScreenState){

}