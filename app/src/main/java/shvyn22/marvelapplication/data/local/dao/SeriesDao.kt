package shvyn22.marvelapplication.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import shvyn22.marvelapplication.data.model.SeriesModel

@Dao
interface SeriesDao {

    @Query("SELECT * FROM Series WHERE title LIKE '%' || :query || '%'")
    fun getAll(query: String) : Flow<List<SeriesModel>>

    @Query("SELECT EXISTS (SELECT 1 FROM Series WHERE id = :id)")
    suspend fun exists(id: Int) : Boolean

    @Insert
    suspend fun insert(item: SeriesModel)

    @Delete
    suspend fun delete(item: SeriesModel)
}