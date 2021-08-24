package tired.coder.animewatcher.screens.home

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import tired.coder.animewatcher.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import tired.coder.animewatcher.composables.CommonAppBar
import tired.coder.animewatcher.composables.AnimeCard
import tired.coder.lib.models.RecentAnimeModel
import java.util.*

@Composable
fun HomeScreen(navController: NavController, viewModel: HomeScreenViewModel = hiltViewModel()) {
    val homeScreenState = viewModel.screenLiveData.observeAsState()
   val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(key1 = Unit){
        viewModel.navigationLiveData.observe(lifecycleOwner){
            it?.let {
                navController.navigate(it)
            }
        }
        onDispose {
            viewModel.clearData()
        }
    }
    HomeScreenWithoutViewModel(
        navController = navController,
        homeScreenState = homeScreenState.value!!
    ,onSearchChanged = {
        viewModel.onSearchChanged(it)
        },onAnimeCardClicked = {

        }) {
        viewModel.onStateChange(newState = it)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable()
fun HomeScreenWithoutViewModel(
    navController: NavController,
    homeScreenState: HomeScreenState,
    onSearchChanged : (String)-> Unit,
    onAnimeCardClicked :(RecentAnimeModel)-> Unit,
    onStateChange: (HomeScreenState) -> Unit,

    ) {
    val animeList = homeScreenState.animeList
    Surface(color = MaterialTheme.colors.background) {
        Scaffold(topBar = {
            TopAppBar(
                title = {
                    if(!homeScreenState.isSearching)
                    Text("Anime Watcher")
                    else
                        Row(modifier = Modifier.fillMaxWidth(),verticalAlignment = Alignment.CenterVertically){
                            IconButton(onClick = {
                                onStateChange(homeScreenState.copy(
                                    isSearching = false,
                                    searchText = ""
                                ))
                            }) {
                                Icon(Icons.Filled.ArrowBack,contentDescription = null)
                            }
                            OutlinedTextField(value = homeScreenState.searchText, onValueChange = {
                                onSearchChanged(it)
                            },colors =TextFieldDefaults.outlinedTextFieldColors(
                                backgroundColor = Color.Transparent,
                                cursorColor = Color.Blue,
                                disabledBorderColor = Color.Transparent,
                                errorBorderColor = Color.Transparent,
                                focusedBorderColor = Color.Transparent,
                                unfocusedBorderColor = Color.Transparent
                            ),modifier = Modifier.weight(1F))
                        }

                },

                actions = {

                    IconButton(onClick = {
                        onStateChange(homeScreenState.copy(
                            isSearching = !homeScreenState.isSearching
                        ))
                    }) {
                        Icon(if(!homeScreenState.isSearching) Icons.Filled.Search else Icons.Filled.Clear,contentDescription = null)
                    }
                    IconButton(onClick = { }) {
                        Icon(Icons.Filled.Menu,contentDescription = null)
                    }
                }
            )
        }, bottomBar = {
            MyBottomAppBar(homeScreenState.currentIndex){
                onStateChange(homeScreenState.copy(
                    currentIndex =  it
                ))
            }
        }) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)

            ) {
                if(homeScreenState.isSearching)
                BackHandler() {
                        onStateChange(
                            homeScreenState.copy(
                                isSearching = false,
                                searchText = ""
                            )
                        )
                    }
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
                                onAnimeCardClicked(animeList[it])
//                                  navController.navigate("video_page")
                            }

                        }
                    }
                }

            }


        }
    }



@Composable
fun MyBottomAppBar(selectedItemIndex: Int, onBottomBarIconClicked: (Int) -> Unit) {
    val labels = listOf("Dub", "Sub", "Recent")
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
                        label.uppercase(Locale.getDefault()), style = if (i == selectedItemIndex) TextStyle(
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