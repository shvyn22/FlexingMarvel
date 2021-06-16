package shvyn22.marvelapplication.repository

import shvyn22.marvelapplication.data.local.dao.CharacterDao
import shvyn22.marvelapplication.data.local.dao.EventDao
import shvyn22.marvelapplication.data.local.dao.SeriesDao
import shvyn22.marvelapplication.data.local.model.EventModel
import shvyn22.marvelapplication.data.local.model.CharacterModel
import shvyn22.marvelapplication.data.local.model.SeriesModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalRepository @Inject constructor(
    private val characterDao: CharacterDao,
    private val eventDao: EventDao,
    private val seriesDao: SeriesDao
){
    fun getCharacters(query: String) = characterDao.getAll(query)

    fun isCharacterFavorite(id: Int) = characterDao.exists(id)

    suspend fun insertCharacter(item: CharacterModel) = characterDao.insert(item)

    suspend fun deleteCharacter(item: CharacterModel) = characterDao.delete(item)


    fun getEvents(query: String) = eventDao.getAll(query)

    fun isEventFavorite(id: Int) = eventDao.exists(id)

    suspend fun insertEvent(item: EventModel) = eventDao.insert(item)

    suspend fun deleteEvent(item: EventModel) = eventDao.delete(item)


    fun getSeries(query: String) = seriesDao.getAll(query)

    fun isSeriesFavorite(id: Int) = seriesDao.exists(id)

    suspend fun insertSeries(item: SeriesModel) = seriesDao.insert(item)

    suspend fun deleteSeries(item: SeriesModel) = seriesDao.delete(item)
}