package tired.coder.animewatcher.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.components.SingletonComponent
import tired.coder.gogo_anime_scraper.GogoAnimeScraper
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesGogoAnimeScraper(
        // Potential dependencies of this type
    ): GogoAnimeScraper {
        return GogoAnimeScraper()
    }
}