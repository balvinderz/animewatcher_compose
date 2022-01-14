package tired.coder.animewatcher.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import tired.coder.animewatcher.R
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.coil.rememberCoilPainter
import tired.coder.animewatcher.ui.theme.AnimewatcherTheme
import tired.coder.lib.models.RecentAnimeModel

@Composable
fun AnimeCard(recentAnimeModel: RecentAnimeModel, onClick: (RecentAnimeModel) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
            .clickable {
                onClick(recentAnimeModel)
            }
    ) {
        Row(
            modifier = Modifier
        ) {
            Column(modifier = Modifier.wrapContentHeight().weight(1F)) {
                Text(
                    recentAnimeModel.name,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.padding(5.dp),
                    style = TextStyle(
                        fontSize = 14.sp,

                    )
                )
                Text(
                    recentAnimeModel.episode, style = TextStyle(
                        color = Color.LightGray,
                        textAlign = TextAlign.Center,
                        fontSize = 12.sp
                    ),modifier = Modifier.padding(5.dp)
                )
            }
            Image(
                painter = rememberCoilPainter(
                    request = recentAnimeModel.imageUrl,
                    previewPlaceholder = R.drawable.psyduck
                ),

                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .weight(1F)
                    .height(225.dp)
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