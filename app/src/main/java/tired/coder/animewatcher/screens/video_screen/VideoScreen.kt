package tired.coder.animewatcher.screens.video_screen

import android.app.Activity
import android.content.pm.ActivityInfo
import android.os.Build
import android.view.View
import android.view.WindowManager
import androidx.activity.compose.LocalActivityResultRegistryOwner
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import tired.coder.animewatcher.R

@Composable
fun VideoScreenWithViewModel(
    navController: NavController,
    videoScreenViewModel: VideoScreenViewModel = hiltViewModel(),
    activity: Activity
) {
    val screenState = videoScreenViewModel.screenLiveData.observeAsState()
    VideoScreen(navController, screenState.value!!) {
        videoScreenViewModel.onStateChange(it)
    }
    val flags = remember {activity.window.decorView.systemUiVisibility}
    val statusBarColour = remember {activity.window.statusBarColor}
    val orientation = remember {
         activity.requestedOrientation
    }
    LocalActivityResultRegistryOwner
    DisposableEffect(Unit) {
        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE
        activity.window.apply {
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                decorView.systemUiVisibility =
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            } else {
                decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            }
            statusBarColor = android.graphics.Color.TRANSPARENT
        }
        WindowCompat.setDecorFitsSystemWindows(activity.window, false)

        onDispose {
            activity.requestedOrientation = orientation
            WindowCompat.setDecorFitsSystemWindows(activity.window, true)
            activity.window.decorView.systemUiVisibility = flags
            activity.window.statusBarColor = statusBarColour
        }
    }
}

@Composable
fun VideoScreen(
    navController: NavController,
    screenState: VideoScreenState,
    onStateChange: (VideoScreenState) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {

        if(screenState.videoUrl == null)
        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        else
            ComposePlayer(screenState.videoUrl)
        Text(
            text = stringResource(
                id = R.string.name_episode_placeholder,
                screenState.animeName,
                screenState.episodeNumber
            ), style = TextStyle(
                color = Color.White,
                fontSize = 20.sp,
                lineHeight = 28.sp
            ), modifier = Modifier.padding(top = 20.dp, start = 20.dp)
        )
    }
}
