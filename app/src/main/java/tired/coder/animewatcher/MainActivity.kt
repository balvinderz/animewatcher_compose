package tired.coder.animewatcher

import android.media.MediaPlayer
import android.media.MediaPlayer.OnCompletionListener
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.navigation.NavArgs
import androidx.navigation.NavArgument
import androidx.navigation.NavType
import tired.coder.animewatcher.ui.theme.AnimewatcherTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import tired.coder.animewatcher.screens.anime_details_screen.AnimeDetailsScreenWithViewModel
import tired.coder.animewatcher.screens.home.HomeScreen
import tired.coder.animewatcher.screens.settings.SettingsScreen
import tired.coder.animewatcher.screens.settings.SettingsScreenWithViewModel
import tired.coder.animewatcher.screens.video_screen.VideoScreenWithViewModel
import tired.coder.animewatcher.utils.PLAY_SOUND
import tired.coder.animewatcher.utils.SharedPrefsHelper
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject lateinit var  prefsHelper: SharedPrefsHelper
    @ExperimentalFoundationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()
            AnimewatcherTheme {
                NavHost(navController = navController, startDestination = HOME) {
                    composable(HOME){
                        HomeScreen(navController)
                    }
                    composable("$ANIME_DETAIL_SCREEN/{anime_page_link}",arguments = listOf(
                        navArgument(
                            "anime_page_link"
                        ){
                            type = NavType.StringType
                        })){
                        AnimeDetailsScreenWithViewModel(navController)
                    }
                    composable(SETTINGS){
                        SettingsScreenWithViewModel(navController = navController)
                    }
                    composable("$VIDEO_SCREEN/{episode_link}/{anime_name}/{episode_number}",arguments = listOf(
                        navArgument(
                            "episode_link"
                        ){
                            type = NavType.StringType
                        }, navArgument(
                            "anime_name"
                        ){
                            type = NavType.StringType
                        }, navArgument(
                            "episode_number"
                        ){
                            type = NavType.IntType
                        },)){
                        VideoScreenWithViewModel(navController,activity = this@MainActivity)
                    }
                }
            }
        }
        if(prefsHelper.getBoolean(PLAY_SOUND,true)){
            playTuturuSound()

        }
    }

    private fun playTuturuSound() {
        val mediaPlayer = MediaPlayer.create(this, R.raw.tuturu)
        mediaPlayer.start()
        mediaPlayer.setOnCompletionListener { mediaPlayer.release() }
    }
}