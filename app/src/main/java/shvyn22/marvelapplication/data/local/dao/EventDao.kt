package shvyn22.marvelapplication.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import shvyn22.marvelapplication.data.model.EventModel

@Dao
interface EventDao {

    @Query("SELECT * FROM Event WHERE title LIKE '%' || :query || '%'")
    fun getAll(query: String) : Flow<List<EventModel>>

    @Query("SELECT EXISTS (SELECT 1 FROM Event WHERE id = :id)")
    suspend fun exists(id: Int) : Boolean

    @Insert
    suspend fun insert(item: EventModel)

    @Delete
    suspend fun delete(item: EventModel)
}