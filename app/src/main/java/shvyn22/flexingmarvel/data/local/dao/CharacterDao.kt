package shvyn22.flexingmarvel.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import shvyn22.flexingmarvel.data.local.model.CharacterModel

@Dao
interface CharacterDao {

    @Query("SELECT * FROM Character WHERE name LIKE '%' || :query || '%'")
    fun getCharacters(query: String): PagingSource<Int, CharacterModel>

    @Query("SELECT EXISTS (SELECT 1 FROM Character WHERE id = :id)")
    fun isCharacterFavorite(id: Int): Flow<Boolean>

    @Insert
    suspend fun insertCharacter(item: CharacterModel)

    @Delete
    suspend fun deleteCharacter(item: CharacterModel)
}