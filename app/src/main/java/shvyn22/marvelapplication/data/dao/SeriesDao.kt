package shvyn22.marvelapplication.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import shvyn22.marvelapplication.data.entity.Series

@Dao
interface SeriesDao {

    @Query("SELECT * FROM Series WHERE title LIKE '%' || :query || '%'")
    fun getAll(query: String) : Flow<List<Series>>

    @Query("SELECT EXISTS (SELECT 1 FROM Series WHERE id = :id)")
    suspend fun exists(id: Int) : Boolean

    @Insert
    suspend fun insert(item: Series)

    @Delete
    suspend fun delete(item: Series)
}