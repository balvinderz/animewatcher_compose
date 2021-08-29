package tired.coder.animewatcher.screens.anime_details_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.coil.rememberCoilPainter
import tired.coder.animewatcher.R
import tired.coder.animewatcher.composables.CommonAppBar
import tired.coder.animewatcher.ui.theme.LightBlue
import tired.coder.animewatcher.ui.theme.NotoSans

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
        CommonAppBar(title = ""){
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
                LazyColumn(contentPadding = PaddingValues(16.dp),modifier = Modifier.fillMaxSize()){
                    item{
                        Image(rememberCoilPainter(request = animeDetailsScreenState.backdropImage ?:  animeDetailsScreenState.detailedAnimeModel.imageUrl),contentDescription = null,modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                            .clip(
                                RoundedCornerShape(8.dp)
                            ),contentScale = ContentScale.FillWidth)
                    }
                    item{
                        Text(animeDetailsScreenState.detailedAnimeModel.name ?: "",style = MaterialTheme.typography.h3.copy(
                            color = Color.Black,
                            fontSize = 20.sp,
                            lineHeight = 25.sp,
                            textAlign = TextAlign.Center
                        ),modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp))
                    }
                    item{
                        Button(onClick = { /*TODO*/ },colors = ButtonDefaults.buttonColors(
                            backgroundColor = LightBlue
                        ),modifier = Modifier
                            .fillMaxWidth() ,shape = RoundedCornerShape(4.dp)) {
                            Text(stringResource(id = R.string.play),style = TextStyle(
                                fontSize = 16.sp,
                                lineHeight = 20.sp,
                                color = Color.White,
                                textAlign = TextAlign.Center,
                                fontFamily = NotoSans
                            ),modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp))
                        }
                    }
                    item {
                        Text(animeDetailsScreenState.detailedAnimeModel.plotSummary ?: "",style = TextStyle(
                            color = Color.LightGray,
                            fontSize = 14.sp,
                            lineHeight = 18.sp,
                            fontFamily = NotoSans
                        ),modifier = Modifier.padding(vertical = 8.dp) )
                    }
                    items(detailedAnimeModel.endEpisode ?:0 - (detailedAnimeModel.startEpisode  ?: 0)) {
                        EpisodeCard(episodeNumber = it + (detailedAnimeModel.startEpisode ?: 0),
                            imageUrl = animeDetailsScreenState.backdropImage
                                ?: animeDetailsScreenState.detailedAnimeModel.imageUrl,
                            modifier = Modifier.padding(8.dp)
                        ) {

                        }
                    }
                }
        }
    }
}
@Composable
fun EpisodeCard( episodeNumber : Int,imageUrl : String,modifier: Modifier = Modifier,onPlayClicked : ()-> Unit){
    Card(modifier  = modifier
        .height(50.dp)
        .fillMaxWidth(),shape = RoundedCornerShape(8.dp)){
        Row(modifier = Modifier
            .fillMaxSize()
            .padding(end = 8.dp),verticalAlignment = Alignment.CenterVertically){
            Image(painter = rememberCoilPainter(request = imageUrl),contentScale = ContentScale.FillBounds,modifier = Modifier
                .padding(4.dp)
                .fillMaxHeight().size(40.dp),contentDescription = null)
            Text("E${episodeNumber.toString().padStart(2,'0')}")
            Spacer(modifier = Modifier.weight(1F))
            Icon(Icons.Default.PlayArrow,tint = Color.Black,contentDescription = null)
        }

    }
}