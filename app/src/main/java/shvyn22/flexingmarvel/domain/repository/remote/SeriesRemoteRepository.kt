package shvyn22.flexingmarvel.domain.repository.remote

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import shvyn22.flexingmarvel.data.local.model.CharacterModel
import shvyn22.flexingmarvel.data.local.model.EventModel
import shvyn22.flexingmarvel.data.local.model.MarvelModel
import shvyn22.flexingmarvel.data.local.model.SeriesModel
import shvyn22.flexingmarvel.data.paging.SeriesPagingSource
import shvyn22.flexingmarvel.data.remote.api.ApiService
import shvyn22.flexingmarvel.util.API_LIMIT
import shvyn22.flexingmarvel.util.MAX_PAGING_SIZE
import shvyn22.flexingmarvel.util.Resource

class SeriesRemoteRepository(
    private val api: ApiService
) : RemoteRepository<SeriesModel> {
    override fun getItems(query: String): Flow<PagingData<SeriesModel>> =
        Pager(
            config = PagingConfig(
                pageSize = API_LIMIT,
                maxSize = MAX_PAGING_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { SeriesPagingSource(api, query) }
        ).flow

    override fun getItemsByType(
        type: MarvelModel
    ): Flow<Resource<List<SeriesModel>>> = flow {
        emit(Resource.Loading())
        val response = when (type) {
            is CharacterModel -> api.getCharacterSeries(type.id)
            is EventModel -> api.getEventSeries(type.id)
            is SeriesModel -> throw IllegalArgumentException()
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