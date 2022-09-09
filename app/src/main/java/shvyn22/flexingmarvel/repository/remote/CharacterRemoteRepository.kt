package shvyn22.flexingmarvel.repository.remote

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
import shvyn22.flexingmarvel.data.paging.CharacterPagingSource
import shvyn22.flexingmarvel.util.API_LIMIT
import shvyn22.flexingmarvel.util.MAX_PAGING_SIZE
import shvyn22.flexingmarvel.util.Resource

class CharacterRemoteRepository(
    private val api: ApiService
) : RemoteRepository<CharacterModel> {

    override fun getItems(query: String): Flow<PagingData<CharacterModel>> =
        Pager(
            config = PagingConfig(
                pageSize = API_LIMIT,
                maxSize = MAX_PAGING_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { CharacterPagingSource(api, query) }
        ).flow

    override fun getItemsByType(
        type: MarvelModel
    ): Flow<Resource<List<CharacterModel>>> = flow {
        emit(Resource.Loading())
        val response = when (type) {
            is CharacterModel -> throw IllegalArgumentException()
            is EventModel -> api.getEventCharacters(type.id)
            is SeriesModel -> api.getSeriesCharacters(type.id)
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