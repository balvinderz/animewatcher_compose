package tired.coder.animewatcher

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

open class BaseViewModel<T>(initialState: T) : ViewModel() {
    protected  val _screenLiveData  = MutableLiveData<T>(initialState)
    public val screenLiveData :LiveData<T> = _screenLiveData

    open fun onStateChange(newState : T){
        _screenLiveData.value =newState!!
    }
    protected val _navigationLiveData = MutableLiveData<String?>()
    val navigationLiveData : LiveData<String?> = _navigationLiveData
    protected val _toastLiveData = MutableLiveData<String?>()
    val toastLiveData : LiveData<String?> = _toastLiveData
    open fun clearData(){
        _navigationLiveData.postValue(null)
        _toastLiveData.postValue(null)
    }

}