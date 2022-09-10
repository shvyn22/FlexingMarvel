package shvyn22.flexingmarvel.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import shvyn22.flexingmarvel.data.remote.api.ApiService
import shvyn22.flexingmarvel.data.local.dao.CharacterDao
import shvyn22.flexingmarvel.data.local.dao.EventDao
import shvyn22.flexingmarvel.data.local.dao.SeriesDao
import shvyn22.flexingmarvel.data.local.model.CharacterModel
import shvyn22.flexingmarvel.data.local.model.EventModel
import shvyn22.flexingmarvel.data.local.model.SeriesModel
import shvyn22.flexingmarvel.domain.repository.local.CharacterLocalRepository
import shvyn22.flexingmarvel.domain.repository.local.EventLocalRepository
import shvyn22.flexingmarvel.domain.repository.local.LocalRepository
import shvyn22.flexingmarvel.domain.repository.local.SeriesLocalRepository
import shvyn22.flexingmarvel.domain.repository.remote.CharacterRemoteRepository
import shvyn22.flexingmarvel.domain.repository.remote.EventRemoteRepository
import shvyn22.flexingmarvel.domain.repository.remote.RemoteRepository
import shvyn22.flexingmarvel.domain.repository.remote.SeriesRemoteRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideCharacterLocalRepository(
        dao: CharacterDao
    ): LocalRepository<CharacterModel> = CharacterLocalRepository(dao)

    @Singleton
    @Provides
    fun provideEventLocalRepository(
        dao: EventDao
    ): LocalRepository<EventModel> = EventLocalRepository(dao)

    @Singleton
    @Provides
    fun provideSeriesLocalRepository(
        dao: SeriesDao
    ): LocalRepository<SeriesModel> = SeriesLocalRepository(dao)

    @Singleton
    @Provides
    fun provideCharacterRemoteRepository(
        api: ApiService
    ): RemoteRepository<CharacterModel> = CharacterRemoteRepository(api)

    @Singleton
    @Provides
    fun provideEventRemoteRepository(
        api: ApiService
    ): RemoteRepository<EventModel> = EventRemoteRepository(api)

    @Singleton
    @Provides
    fun provideSeriesRemoteRepository(
        api: ApiService
    ): RemoteRepository<SeriesModel> = SeriesRemoteRepository(api)
}