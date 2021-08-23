package tired.coder.gogo_anime_scraper.enums

enum class ReleaseType {
    Sub,
    Dub
}

fun ReleaseType.toGogoAnimeType(): Int {
    return when (this) {
        ReleaseType.Sub -> 1
        ReleaseType.Dub -> 2

    }
}