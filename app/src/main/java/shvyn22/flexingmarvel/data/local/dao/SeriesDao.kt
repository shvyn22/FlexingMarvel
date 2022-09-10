package shvyn22.flexingmarvel.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import shvyn22.flexingmarvel.data.local.model.SeriesModel

@Dao
interface SeriesDao {

    @Query("SELECT * FROM Series WHERE title LIKE '%' || :query || '%'")
    fun getSeries(query: String): PagingSource<Int, SeriesModel>

    @Query("SELECT EXISTS (SELECT 1 FROM Series WHERE id = :id)")
    fun isSeriesFavorite(id: Int): Flow<Boolean>

    @Insert
    suspend fun insertSeries(item: SeriesModel)

    @Delete
    suspend fun deleteSeries(item: SeriesModel)
}