package tired.coder.animewatcher.screens.anime_details_screen

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import tired.coder.animewatcher.ANIME_DETAIL_SCREEN
import tired.coder.animewatcher.R
import tired.coder.animewatcher.VIDEO_SCREEN
import tired.coder.animewatcher.composables.CommonAppBar
import tired.coder.animewatcher.ui.theme.LightBlue
import tired.coder.animewatcher.ui.theme.NotoSans
import tired.coder.gogo_anime_scraper.models.EpisodeModel

@Composable
fun AnimeDetailsScreenWithViewModel(
    navController: NavController,
    viewModel: AnimeDetailsScreenViewModel = hiltViewModel()
) {
    val animeDetailsScreenState = viewModel.screenLiveData.observeAsState()
    AnimeDetailsScreen(animeDetailsScreenState = animeDetailsScreenState.value!!,{
        navController.navigateUp()
    }) {
        navController.navigate("$VIDEO_SCREEN/${Uri.encode(it.episodeUrl)}/${animeDetailsScreenState.value!!.detailedAnimeModel.name ?: ""}/${it.episodeNumber}")
    }
}

@Composable
fun AnimeDetailsScreen(
    animeDetailsScreenState: AnimeDetailsScreenState,
    onBackPressed: () -> Unit,
    onEpisodeChosen : (EpisodeModel)-> Unit,
) {
    Scaffold(topBar = {
        CommonAppBar(title = animeDetailsScreenState.detailedAnimeModel.name ?: "") {
            onBackPressed()
        }
    }) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            if (animeDetailsScreenState.isLoading)
                Box(modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
            else
                LazyColumn(
                    contentPadding = PaddingValues(0.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                        ) {
                            Image(
                                painter = rememberCoilPainter(request = animeDetailsScreenState.detailedAnimeModel.imageUrl),
                                modifier = Modifier
                                    .padding(top = 50.dp)
                                    .height(400.dp)

                                    .fillMaxWidth()
                                    .alpha(0.3F),
                                contentDescription = null
                            )
                            Text(
                                animeDetailsScreenState.detailedAnimeModel.plotSummary ?: "",
                                style = TextStyle(
                                    color = Color.Black,
                                    fontSize = 15.sp,
                                ),
                                modifier = Modifier.padding(50.dp)
                            )
                        }
                    }

                    item {

                        Text(
                            stringResource(id = R.string.episodes), style = TextStyle(
                                color = Color.Black,
                                fontSize = 24.sp,
                                lineHeight = 32.sp,

                                ), modifier = Modifier
                                .padding(10.dp)
                                .padding(top = 10.dp)
                        )
                    }
                    item {
                        Divider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(),
                            color = Color(0xFFaaaaaa)
                        )
                    }
                    items(animeDetailsScreenState.episodes.size) {
                        val episode =
                            animeDetailsScreenState.episodes[animeDetailsScreenState.episodes.size - it - 1]
                        EpisodeCard(
                            name = animeDetailsScreenState.detailedAnimeModel.name ?: "",
                            episode = episode
                        ) {
                            onEpisodeChosen(it)
                        }

                    }
                }
        }
    }
}

@Composable
fun EpisodeCard(
    name: String,
    episode: EpisodeModel,
    modifier: Modifier = Modifier,
    onClicked: (EpisodeModel) -> Unit
) {
    Text(
        stringResource(
            id = R.string.name_episode_placeholder,
            name,
            episode.episodeNumber
        ), modifier = modifier
            .padding(20.dp)
            .fillMaxWidth()
            .clickable {
                onClicked(episode)
            },
        style = TextStyle(
            fontSize = 16.sp,
            lineHeight = 24.sp,
            color = Color.Gray
        )
    )
}