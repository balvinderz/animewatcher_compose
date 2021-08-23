package tired.coder.animewatcher.screens.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import tired.coder.animewatcher.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import tired.coder.animewatcher.composables.CommonAppBar
import tired.coder.animewatcher.composables.AnimeCard

@Composable
fun HomeScreen(navController: NavController, viewModel: HomeScreenViewModel = hiltViewModel()) {
    val homeScreenState = viewModel.screenLiveData.observeAsState()
    HomeScreenWithoutViewModel(
        navController = navController,
        homeScreenState = homeScreenState.value!!
    ) {
        viewModel.onStateChange(newState = it)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable()
fun HomeScreenWithoutViewModel(
    navController: NavController,
    homeScreenState: HomeScreenState,
    onStateChange: (HomeScreenState) -> Unit
) {
    val animeList = homeScreenState.animeList
    Surface(color = MaterialTheme.colors.background) {
        Scaffold(topBar = {
            CommonAppBar()
        }, bottomBar = {
            MyBottomAppBar(homeScreenState.currentIndex){
                onStateChange(homeScreenState.copy(
                    currentIndex =  it
                ))
            }
        }) {
            Column(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize()
            ) {
                if (homeScreenState.isLoading)
                    Box(Modifier.fillMaxSize()) {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }
                else
                    LazyVerticalGrid(
                        cells = GridCells.Fixed(
                            2
                        ), contentPadding = PaddingValues(5.dp) ,
                        state = rememberLazyListState()
                    ) {
                        items(animeList.size) {
                            AnimeCard(recentAnimeModel = animeList[it]) {
//                                  navController.navigate("video_page")
                            }

                        }
                    }
                Box(modifier = Modifier.height(20.dp))
            }


        }
    }

}

@Composable
fun MyBottomAppBar(selectedItemIndex: Int, onBottomBarIconClicked: (Int) -> Unit) {
    val labels = listOf<String>("Dub", "Sub", "Recent")
    BottomAppBar() {
        for (i in labels.indices) {
            val label = labels[i]
            BottomNavigationItem(selected = i == selectedItemIndex, onClick = {
                onBottomBarIconClicked(i)
            }, icon = {
                Icon(
                    painter = painterResource(id = tired.coder.animewatcher.R.drawable.psyduck),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)

                )
            },
                alwaysShowLabel = true,
                label = {
                    Text(
                        label.toUpperCase(), style = if (i == selectedItemIndex) TextStyle(
                            color = Color.Blue,
                        ) else TextStyle(
                            color = Color.Black,

                            )
                    )
                },
                unselectedContentColor = Color.Blue
            )
        }
    }
}