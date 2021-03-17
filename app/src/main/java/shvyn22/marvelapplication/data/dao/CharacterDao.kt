package shvyn22.marvelapplication.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import shvyn22.marvelapplication.data.entity.MarvelCharacter

@Dao
interface CharacterDao {

    @Query("SELECT * FROM Character WHERE name LIKE '%' || :query || '%'")
    fun getAll(query: String) : Flow<List<MarvelCharacter>>

    @Query("SELECT EXISTS (SELECT 1 FROM Character WHERE id = :id)")
    suspend fun exists(id: Int) : Boolean

    @Insert
    suspend fun insert(item: MarvelCharacter)

    @Delete
    suspend fun delete(item: MarvelCharacter)
}