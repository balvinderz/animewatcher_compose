package tired.coder.animewatcher.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import tired.coder.animewatcher.R
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.coil.rememberCoilPainter
import tired.coder.animewatcher.ui.theme.AnimewatcherTheme
import tired.coder.lib.models.RecentAnimeModel

@Composable
fun AnimeCard(recentAnimeModel: RecentAnimeModel, onClick: (RecentAnimeModel) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable {
                onClick(recentAnimeModel)
            }
    ) {

        Column(
            modifier = Modifier
        ) {
            Image(
                painter = rememberCoilPainter(
                    request = recentAnimeModel.imageUrl,
                    previewPlaceholder = R.drawable.psyduck
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