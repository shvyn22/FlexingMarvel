package shvyn22.flexingmarvel.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import shvyn22.flexingmarvel.data.local.model.EventModel

@Dao
interface EventDao {

    @Query("SELECT * FROM Event WHERE title LIKE '%' || :query || '%'")
    fun getEvents(query: String): PagingSource<Int, EventModel>

    @Query("SELECT EXISTS (SELECT 1 FROM Event WHERE id = :id)")
    fun isEventFavorite(id: Int): Flow<Boolean>

    @Insert
    suspend fun insertEvent(item: EventModel)

    @Delete
    suspend fun deleteEvent(item: EventModel)
}