package shvyn22.marvelapplication.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import shvyn22.marvelapplication.data.local.model.CharacterModel

@Dao
interface CharacterDao {

    @Query("SELECT * FROM Character WHERE name LIKE '%' || :query || '%'")
    fun getAll(query: String): Flow<List<CharacterModel>>

    @Query("SELECT EXISTS (SELECT 1 FROM Character WHERE id = :id)")
    fun exists(id: Int): Flow<Boolean>

    @Insert
    suspend fun insert(item: CharacterModel)

    @Delete
    suspend fun delete(item: CharacterModel)
}