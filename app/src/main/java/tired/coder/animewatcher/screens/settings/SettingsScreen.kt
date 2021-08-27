package tired.coder.animewatcher.screens.settings

import android.widget.Space
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import tired.coder.animewatcher.R
import tired.coder.animewatcher.composables.CommonAppBar

@Composable
fun SettingsScreenWithViewModel(navController: NavController,viewModel: SettingsScreenViewModel = hiltViewModel()){
    val screenState = viewModel.screenLiveData.observeAsState()
    SettingsScreen(navController = navController,screenState = screenState.value!!) {
            viewModel.onStateChange(it)
    }
}
@Composable
fun SettingsScreen(navController: NavController,screenState: SettingsScreenState,onStateChange : (SettingsScreenState)-> Unit){
    Scaffold(topBar = {
        CommonAppBar(title = stringResource(id = R.string.settings)) {
            navController.navigateUp()
        }
    }) {

        Row(modifier = Modifier.padding(it)
            .fillMaxWidth()
            .padding(16.dp),verticalAlignment = Alignment.CenterVertically){
            Image(painterResource(id = R.drawable.baseline_audiotrack_black_18dp),contentDescription = null,modifier = Modifier.size(30.dp))
            Text(stringResource(id = R.string.play_sound))
            Spacer(modifier = Modifier.weight(1F))
            Switch(checked = screenState.playSound, onCheckedChange ={
                onStateChange(screenState.copy(
                    playSound = it
                ))
            } )
        }
    }
}