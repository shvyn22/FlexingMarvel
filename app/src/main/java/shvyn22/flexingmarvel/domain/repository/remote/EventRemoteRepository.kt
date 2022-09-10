package shvyn22.flexingmarvel.domain.repository.remote

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import shvyn22.flexingmarvel.data.remote.api.ApiService
import shvyn22.flexingmarvel.data.local.model.CharacterModel
import shvyn22.flexingmarvel.data.local.model.EventModel
import shvyn22.flexingmarvel.data.local.model.MarvelModel
import shvyn22.flexingmarvel.data.local.model.SeriesModel
import shvyn22.flexingmarvel.data.paging.EventPagingSource
import shvyn22.flexingmarvel.util.API_LIMIT
import shvyn22.flexingmarvel.util.MAX_PAGING_SIZE
import shvyn22.flexingmarvel.util.Resource

class EventRemoteRepository(
    private val api: ApiService
) : RemoteRepository<EventModel> {

    override fun getItems(query: String): Flow<PagingData<EventModel>> =
        Pager(
            config = PagingConfig(
                pageSize = API_LIMIT,
                maxSize = MAX_PAGING_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { EventPagingSource(api, query) }
        ).flow

    override fun getItemsByType(
        type: MarvelModel
    ): Flow<Resource<List<EventModel>>> = flow {
        emit(Resource.Loading())
        val response = when (type) {
            is CharacterModel -> api.getCharacterEvents(type.id)
            is EventModel -> throw IllegalArgumentException()
            is SeriesModel -> api.getSeriesEvents(type.id)
        }
        if (response.code != 200) emit(Resource.Error(msg = ""))
        else {
            response.data.results.let {
                if (it.isEmpty()) emit(Resource.Idle())
                else emit(Resource.Success(data = it))
            }
        }
    }
}