package shvyn22.flexingmarvel.domain.repository.local

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import shvyn22.flexingmarvel.data.local.dao.EventDao
import shvyn22.flexingmarvel.data.local.model.EventModel
import shvyn22.flexingmarvel.util.API_LIMIT
import shvyn22.flexingmarvel.util.MAX_PAGING_SIZE

class EventLocalRepository(
    private val dao: EventDao
) : LocalRepository<EventModel> {

    override fun getItems(query: String): Flow<PagingData<EventModel>> =
        Pager(
            config = PagingConfig(
                pageSize = API_LIMIT,
                maxSize = MAX_PAGING_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { dao.getEvents(query) }
        ).flow

    override fun isItemFavorite(id: Int): Flow<Boolean> = dao.isEventFavorite(id)

    override suspend fun insertItem(item: EventModel) {
        dao.insertEvent(item)
    }

    override suspend fun deleteItem(item: EventModel) {
        dao.deleteEvent(item)
    }
}