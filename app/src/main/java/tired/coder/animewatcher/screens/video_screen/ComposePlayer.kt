package tired.coder.animewatcher.screens.video_screen

import android.net.Uri
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import tired.coder.gogo_anime_scraper.GogoAnimeScraper

@Composable
fun ComposePlayer(url : String) {
    val context =  LocalContext.current
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    val player = remember {
        val player = SimpleExoPlayer.Builder(context)
            .build()

        val defaultHttpDataSourceFactory = DefaultHttpDataSourceFactory("test")
        defaultHttpDataSourceFactory.setDefaultRequestProperties(hashMapOf(
            "Referer" to GogoAnimeScraper.baseUrl
        ))
        player.prepare(
            if(url.endsWith(".m3u8") || url.endsWith(".ts"))
            HlsMediaSource.Factory(defaultHttpDataSourceFactory)
                .createMediaSource(Uri.parse(url)) else ProgressiveMediaSource.Factory(defaultHttpDataSourceFactory).createMediaSource(Uri.parse(url))
        )
        player.playWhenReady = true
        player
    }
    DisposableEffect(key1 = Unit, effect = {
        onDispose {
            player.release()
        }
    } )

    val playerView = remember {
        val playerView = PlayerView(context)
        lifecycle.addObserver(object : LifecycleObserver {
            @OnLifecycleEvent(Lifecycle.Event.ON_START)
            fun onStart() {
                playerView.onResume()
                player.playWhenReady = true
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
            fun onStop() {
                playerView.onPause()
                player.playWhenReady = false
            }
        })
        playerView
    }

    AndroidView(
        modifier = Modifier
            .fillMaxWidth(),
        factory = {
            playerView
        }
    ) {
        playerView.player = player
    }
}