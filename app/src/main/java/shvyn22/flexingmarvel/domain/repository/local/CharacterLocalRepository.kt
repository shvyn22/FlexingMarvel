package shvyn22.flexingmarvel.domain.repository.local

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import shvyn22.flexingmarvel.data.local.dao.CharacterDao
import shvyn22.flexingmarvel.data.local.model.CharacterModel
import shvyn22.flexingmarvel.util.API_LIMIT
import shvyn22.flexingmarvel.util.MAX_PAGING_SIZE

class CharacterLocalRepository(
    private val dao: CharacterDao
) : LocalRepository<CharacterModel> {

    override fun getItems(query: String): Flow<PagingData<CharacterModel>> =
        Pager(
            config = PagingConfig(
                pageSize = API_LIMIT,
                maxSize = MAX_PAGING_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { dao.getCharacters(query) }
        ).flow

    override fun isItemFavorite(id: Int): Flow<Boolean> = dao.isCharacterFavorite(id)

    override suspend fun insertItem(item: CharacterModel) {
        dao.insertCharacter(item)
    }

    override suspend fun deleteItem(item: CharacterModel) {
        dao.deleteCharacter(item)
    }
}