package shvyn22.flexingmarvel.domain.repository.local

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import shvyn22.flexingmarvel.data.local.model.MarvelModel

interface LocalRepository<T: MarvelModel> {

    fun getItems(query: String): Flow<PagingData<T>>

    fun isItemFavorite(id: Int): Flow<Boolean>

    suspend fun insertItem(item: T)

    suspend fun deleteItem(item: T)
}