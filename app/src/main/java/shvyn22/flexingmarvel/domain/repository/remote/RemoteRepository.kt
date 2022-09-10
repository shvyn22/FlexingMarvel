package shvyn22.flexingmarvel.domain.repository.remote

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import shvyn22.flexingmarvel.data.local.model.MarvelModel
import shvyn22.flexingmarvel.util.Resource

interface RemoteRepository<T : MarvelModel> {

    fun getItems(query: String): Flow<PagingData<T>>

    fun getItemsByType(type: MarvelModel): Flow<Resource<List<T>>>
}