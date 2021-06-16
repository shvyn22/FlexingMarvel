package shvyn22.marvelapplication.repository

import android.content.Context
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.flow
import shvyn22.marvelapplication.R
import shvyn22.marvelapplication.api.ApiInterface
import shvyn22.marvelapplication.api.ApiResponse
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
) {
    fun getCharactersResults(query: String) =
        Pager(
            config = PagingConfig(
                pageSize = 33,
                maxSize = 500,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { CharacterPagingSource(api, query) }
        ).liveData

    fun getSeriesResults(query: String) =
        Pager(
            config = PagingConfig(
                pageSize = 33,
                maxSize = 500,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { SeriesPagingSource(api, query) }
        ).liveData

    fun getEventsResults(query: String) =
        Pager(
            config = PagingConfig(
                pageSize = 33,
                maxSize = 500,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { EventPagingSource(api, query) }
        ).liveData

    private fun <T> processResponse(response: ApiResponse<T>): Resource<List<T>> {
        return if (response.code != 200) Resource.Error(
            msg = context.getString(R.string.text_error_fetching)
        )
        else {
            return response.data.results.let {
                if (it.isEmpty()) Resource.Empty()
                else Resource.Success(data = it)
            }
        }
    }

    suspend fun getCharacterSeries(id: Int) = flow {
        emit(Resource.Loading())
        val resource = processResponse(api.getCharacterSeries(id))
        emit(resource)
    }

    suspend fun getCharacterEvents(id: Int) = flow {
        emit(Resource.Loading())
        val resource = processResponse(api.getCharacterEvents(id))
        emit(resource)
    }

    suspend fun getSeriesCharacters(id: Int) = flow {
        emit(Resource.Loading())
        val resource = processResponse(api.getSeriesCharacters(id))
        emit(resource)
    }

    suspend fun getSeriesEvents(id: Int) = flow {
        emit(Resource.Loading())
        val resource = processResponse(api.getSeriesEvents(id))
        emit(resource)
    }

    suspend fun getEventSeries(id: Int) = flow {
        emit(Resource.Loading())
        val resource = processResponse(api.getEventSeries(id))
        emit(resource)
    }

    suspend fun getEventCharacters(id: Int) = flow {
        emit(Resource.Loading())
        val resource = processResponse(api.getEventCharacters(id))
        emit(resource)
    }
}