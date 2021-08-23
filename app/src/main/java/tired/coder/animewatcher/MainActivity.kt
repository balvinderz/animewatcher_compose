package tired.coder.animewatcher

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import tired.coder.animewatcher.ui.theme.AnimewatcherTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import tired.coder.animewatcher.screens.home.HomeScreen

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @ExperimentalFoundationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            AnimewatcherTheme {
                NavHost(navController = navController, startDestination = "home") {
                    composable(HOME){
                        HomeScreen(navController)
                    }
                }
            }
        }
    }
}