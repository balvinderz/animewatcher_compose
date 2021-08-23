package tired.coder.animewatcher

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tired.coder.animewatcher.ui.theme.AnimewatcherTheme
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.coil.rememberCoilPainter
import dagger.hilt.android.AndroidEntryPoint
import tired.coder.lib.models.RecentAnimeModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainActivityViewModel by viewModels()

    @ExperimentalFoundationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            AnimewatcherTheme {
                NavHost(navController = navController, startDestination = "home") {
                    composable("home") {
                        HomeContent(viewModel = viewModel,navController) }
                    composable("video_page") { VideoPage(navController = navController) }
                    /*...*/
                }
            }
        }
    }

    @ExperimentalFoundationApi
    @Composable
    fun HomeContent(viewModel: MainActivityViewModel, navController: NavHostController) {
        val isLoading = viewModel.isLoading.observeAsState()
        val animeList = viewModel.animeList.observeAsState()
        val context =  LocalContext.current
        Surface(color = MaterialTheme.colors.background) {
            Scaffold(topBar = {
                CommonAppBar()
            }, bottomBar = {
                MyBottomAppBar()
            }) {
                Column(
                    modifier = Modifier
                        .padding(it)
                        .fillMaxSize()
                ) {
                    if (isLoading.value == true)
                        CircularProgressIndicator()
                    else
                        LazyVerticalGrid(
                            cells = GridCells.Fixed(
                                2
                            ), contentPadding = PaddingValues(5.dp)
                        ) {
                            items(animeList.value!!.size) {
                                AnimeCard(recentAnimeModel = animeList.value!![it]) {
//                                  navController.navigate("video_page")
                                    val intent=  Intent(context,VideoActivity::class.java)
                                    intent.apply {
                                        putExtra("anime_page",animeList.value!![it].episodeUrl)
                                    }
                                    startActivity(intent)
                                }

                            }
                        }
                    Box(modifier = Modifier.height(20.dp))
                }


            }
        }
    }

    @Composable
    fun CommonAppBar() {
        TopAppBar(
            title = {
                Text("Anime Watcher")
            },

            actions = {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(Icons.Filled.Search,contentDescription = null)
                }
            }
        )
    }

    @Composable
    fun MyBottomAppBar() {
        val labels = listOf<String>("Dub","Sub","Recent")
        BottomAppBar() {
            for(label in labels)
                BottomNavigationItem(selected = label == "Dub", onClick = { /*TODO*/ },icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.psyduck),
                        contentDescription = null,
                        modifier =   Modifier.size(24.dp)

                        )
                },
                alwaysShowLabel = true,
                    label = {
                        Text(label.toUpperCase(),style =if(label == "Dub") TextStyle(
                            color = Color.Blue,
                        ) else  TextStyle(
                            color = Color.Black,

                        )
                        )
                    },
                    unselectedContentColor = Color.Blue
                    )
        }
    }

    @Preview
    @Composable
    fun BottomAppBarPreview() {
        AnimewatcherTheme {
            MyBottomAppBar()
        }
    }

    @Composable
    fun AnimeCard(recentAnimeModel: RecentAnimeModel, onClick: () -> Unit) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clickable {
                    onClick()
                }
        ) {

            Column(
                modifier = Modifier
            ) {
                Image(
                    painter = rememberCoilPainter(
                        request = recentAnimeModel.imageUrl,
                        previewPlaceholder = R.drawable.sample_image,
                    ),

                    contentDescription = null,
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .fillMaxSize()
                        .height(200.dp)
                        .clip(RoundedCornerShape(10.dp))
                )
                Text(
                    recentAnimeModel.name,
                    style = MaterialTheme.typography.h6,
                    textAlign = TextAlign.Center
                )
                Text(
                    recentAnimeModel.episode, style = MaterialTheme.typography.subtitle2.copy(
                        color = Color.LightGray,
                        textAlign = TextAlign.Center
                    )
                )


            }
        }
    }

    @Preview(widthDp = 400, heightDp = 300)
    @Composable
    fun AnimeCardPreview() {
        AnimewatcherTheme {
            AnimeCard(
                RecentAnimeModel(
                    "Boruto",
                    "Episode 202",
                    "https://googanime.com",
                    "https://placepic.com/200x200"
                )
            ) {

            }
        }
    }

    @Preview
    @Composable
    fun TopAppPreview() {
        AnimewatcherTheme() {
            CommonAppBar()
        }
    }
    @Composable
    fun VideoPage(navController: NavController){
            Box(modifier = Modifier.fillMaxSize().background(Color.Black))
    }

//
//    @ExperimentalFoundationApi
//    @Preview(showBackground = true)
//    @Composable
//    fun DefaultPreview() {
//        AnimewatcherTheme {
//            HomeContent()
//        }
//    }
}