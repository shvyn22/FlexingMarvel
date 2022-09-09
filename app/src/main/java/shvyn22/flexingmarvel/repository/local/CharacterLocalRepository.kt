package shvyn22.flexingmarvel.repository.local

import kotlinx.coroutines.flow.Flow
import shvyn22.flexingmarvel.data.local.dao.CharacterDao
import shvyn22.flexingmarvel.data.local.model.CharacterModel

class CharacterLocalRepository(
    private val dao: CharacterDao
) : LocalRepository<CharacterModel> {

    override fun getItems(query: String): Flow<List<CharacterModel>> = dao.getCharacters(query)

    override fun isItemFavorite(id: Int): Flow<Boolean> = dao.isCharacterFavorite(id)

    override suspend fun insertItem(item: CharacterModel) {
        dao.insertCharacter(item)
    }

    override suspend fun deleteItem(item: CharacterModel) {
        dao.deleteCharacter(item)
    }
}