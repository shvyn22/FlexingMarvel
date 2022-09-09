package shvyn22.flexingmarvel.repository.local

import kotlinx.coroutines.flow.Flow

interface LocalRepository<T> {

    fun getItems(query: String): Flow<List<T>>

    fun isItemFavorite(id: Int): Flow<Boolean>

    suspend fun insertItem(item: T)

    suspend fun deleteItem(item: T)
}