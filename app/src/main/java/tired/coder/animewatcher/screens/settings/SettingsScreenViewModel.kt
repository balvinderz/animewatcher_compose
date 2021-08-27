package tired.coder.animewatcher.screens.settings

import dagger.hilt.android.lifecycle.HiltViewModel
import tired.coder.animewatcher.BaseViewModel
import tired.coder.animewatcher.utils.PLAY_SOUND
import tired.coder.animewatcher.utils.SharedPrefsHelper
import javax.inject.Inject

@HiltViewModel
class SettingsScreenViewModel @Inject constructor(
    private val prefsHelper: SharedPrefsHelper
) : BaseViewModel<SettingsScreenState>(
    SettingsScreenState(
        prefsHelper.getBoolean(PLAY_SOUND, true)
    )
) {
    override fun onStateChange(newState: SettingsScreenState) {
        super.onStateChange(newState)
        prefsHelper.put(PLAY_SOUND,newState.playSound)
    }
}