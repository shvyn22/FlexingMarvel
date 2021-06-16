package shvyn22.marvelapplication.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import shvyn22.marvelapplication.api.ApiInterface
import shvyn22.marvelapplication.data.local.AppDatabase
import shvyn22.marvelapplication.data.local.dao.CharacterDao
import shvyn22.marvelapplication.data.local.dao.EventDao
import shvyn22.marvelapplication.data.local.dao.SeriesDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(ApiInterface.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideApiInterface(retrofit: Retrofit): ApiInterface =
        retrofit.create(ApiInterface::class.java)

    @Provides
    @Singleton
    fun provideDatabase(app: Application): AppDatabase =
        Room
            .databaseBuilder(app, AppDatabase::class.java, "appDatabase")
            .build()

    @Provides
    fun provideCharacterDao(db: AppDatabase): CharacterDao =
        db.characterDao()

    @Provides
    fun provideEventDao(db: AppDatabase): EventDao =
        db.eventDao()

    @Provides
    fun provideSeriesDao(db: AppDatabase): SeriesDao =
        db.seriesDao()
}