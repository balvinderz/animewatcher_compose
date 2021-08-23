package tired.coder.animewatcher.screens.home

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import tired.coder.lib.models.RecentAnimeModel

data class HomeScreenState(
    val currentIndex : Int = 1,
 private   val dubAnimeList : State<List<RecentAnimeModel>> = mutableStateOf(emptyList()),
  private  val subAnimeList : State<List<RecentAnimeModel>> = mutableStateOf(emptyList()),
   private val recentWatchedAnimeList : State<List<RecentAnimeModel>> = mutableStateOf(emptyList()),
   val isSearching : Boolean = false,
    val isSearchingLoading : Boolean = true,
    private val isDubLoading : Boolean = true,
   private val isSubLoading : Boolean =true ,
    val searchText : String  = "",
   private val isRecentLoading : Boolean= true,
){
    val isLoading : Boolean
    get(){
        if(currentIndex ==0 && isDubLoading)
            return true
        else if(currentIndex ==1 && isSubLoading)
            return true
        else if(currentIndex ==2 && isRecentLoading)
        return true
        else if(isSearching && isSearchingLoading)
            return true
        return false
    }
    val animeList: List<RecentAnimeModel>
    get(){
        return when (currentIndex) {
            0 -> dubAnimeList.value
            1 -> subAnimeList.value
            else -> recentWatchedAnimeList.value
        }
    }
}