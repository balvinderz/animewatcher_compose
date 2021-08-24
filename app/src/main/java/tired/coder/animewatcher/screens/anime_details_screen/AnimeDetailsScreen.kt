package tired.coder.animewatcher.screens.anime_details_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.coil.rememberCoilPainter
import tired.coder.animewatcher.composables.CommonAppBar

@Composable
fun AnimeDetailsScreenWithViewModel(navController: NavController ,viewModel : AnimeDetailsScreenViewModel = hiltViewModel()){
    val animeDetailsScreenState  = viewModel.screenLiveData.observeAsState()
    AnimeDetailsScreen(animeDetailsScreenState = animeDetailsScreenState.value!!){
        navController.navigateUp()
    }
}
@Composable
fun AnimeDetailsScreen(animeDetailsScreenState: AnimeDetailsScreenState,onBackPressed : ()-> Unit){
    val   detailedAnimeModel = animeDetailsScreenState.detailedAnimeModel
    Scaffold(topBar = {
        CommonAppBar(title = animeDetailsScreenState.detailedAnimeModel.name ?: ""){
            onBackPressed()
        }
    }) {
        Column(modifier = Modifier
            .padding(it)
            .fillMaxSize()) {
            if(animeDetailsScreenState.isLoading)
                Box(modifier = Modifier.fillMaxSize()){
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
            else
                Image(painter = rememberCoilPainter(request = detailedAnimeModel.imageUrl),contentDescription = "Banner")
        }
    }
}