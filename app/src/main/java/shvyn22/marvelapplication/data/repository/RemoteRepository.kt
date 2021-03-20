package shvyn22.marvelapplication.data.repository

import android.content.Context
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import dagger.hilt.android.qualifiers.ApplicationContext
import shvyn22.marvelapplication.R
import shvyn22.marvelapplication.api.ApiInterface
import shvyn22.marvelapplication.api.ApiResponse
import shvyn22.marvelapplication.data.model.CharacterModel
import shvyn22.marvelapplication.data.model.EventModel
import shvyn22.marvelapplication.data.model.SeriesModel
import shvyn22.marvelapplication.data.paging.CharacterPagingSource
import shvyn22.marvelapplication.data.paging.EventPagingSource
import shvyn22.marvelapplication.data.paging.SeriesPagingSource
import shvyn22.marvelapplication.util.Resource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteRepository @Inject constructor(
    private val api: ApiInterface,
    @ApplicationContext val context: Context
){
    fun getCharactersResults(query: String) =
        Pager(
            config = PagingConfig(
                pageSize = 33,
                maxSize = 500,
                enablePlaceholders = false),
            pagingSourceFactory = {CharacterPagingSource(api, query)}
        ).liveData

    fun getSeriesResults(query: String) =
        Pager(
            config = PagingConfig(
                    pageSize = 33,
                    maxSize = 500,
                    enablePlaceholders = false),
            pagingSourceFactory = {SeriesPagingSource(api, query)}
        ).liveData

    fun getEventsResults(query: String) =
        Pager(
            config = PagingConfig(
                    pageSize = 33,
                    maxSize = 500,
                    enablePlaceholders = false),
            pagingSourceFactory = {EventPagingSource(api, query)}
        ).liveData

    private fun <T> processResponse(response: ApiResponse<T>) : Resource<List<T>> {
        return if (response.code != 200) Resource.Error(
                msg = context.getString(R.string.text_error_fetching))
        else {
            return response.data.results.let {
                if (it.isEmpty()) Resource.Empty()
                else Resource.Success(data = it)
            }
        }
    }

    suspend fun getCharacterSeries(id: Int) : Resource<List<SeriesModel>> {
        val response = api.getCharacterSeries(id)
        return processResponse(response)
    }

    suspend fun getCharacterEvents(id: Int) : Resource<List<EventModel>> {
        val response = api.getCharacterEvents(id)
        return processResponse(response)
    }

    suspend fun getSeriesCharacters(id: Int) : Resource<List<CharacterModel>> {
        val response = api.getSeriesCharacters(id)
        return processResponse(response)
    }

    suspend fun getSeriesEvents(id: Int) : Resource<List<EventModel>> {
        val response = api.getSeriesEvents(id)
        return processResponse(response)
    }

    suspend fun getEventSeries(id: Int) : Resource<List<SeriesModel>> {
        val response = api.getEventSeries(id)
        return processResponse(response)
    }

    suspend fun getEventCharacters(id: Int) : Resource<List<CharacterModel>> {
        val response = api.getEventCharacters(id)
        return processResponse(response)
    }
}