package tired.coder.animewatcher.composables

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable

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