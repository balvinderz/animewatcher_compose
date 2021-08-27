package tired.coder.animewatcher.di

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import tired.coder.animewatcher.utils.SharedPrefsHelper
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
    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext appContext: Context): SharedPreferences {
        return appContext.getSharedPreferences(SharedPrefsHelper.SHARED_PREF_FILE_NAME, Context.MODE_PRIVATE)
    }
}