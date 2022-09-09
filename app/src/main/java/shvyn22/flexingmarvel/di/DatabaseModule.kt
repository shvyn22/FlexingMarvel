package shvyn22.flexingmarvel.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import shvyn22.flexingmarvel.data.local.AppDatabase
import shvyn22.flexingmarvel.data.local.dao.CharacterDao
import shvyn22.flexingmarvel.data.local.dao.EventDao
import shvyn22.flexingmarvel.data.local.dao.SeriesDao
import shvyn22.flexingmarvel.util.DATABASE_NAME
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(app: Application): AppDatabase =
        Room
            .databaseBuilder(
                app,
                AppDatabase::class.java,
                DATABASE_NAME
            )
            .fallbackToDestructiveMigration()
            .build()

    @Singleton
    @Provides
    fun provideCharacterDao(db: AppDatabase): CharacterDao =
        db.characterDao()

    @Singleton
    @Provides
    fun provideEventDao(db: AppDatabase): EventDao =
        db.eventDao()

    @Singleton
    @Provides
    fun provideSeriesDao(db: AppDatabase): SeriesDao =
        db.seriesDao()
}