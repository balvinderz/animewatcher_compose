package tired.coder.animewatcher.screens.video_screen

data class VideoScreenState(
    val episodeLink : String,
    val animeName : String,
    val episodeNumber : Int ,
    val videoUrl : String? = null
)
