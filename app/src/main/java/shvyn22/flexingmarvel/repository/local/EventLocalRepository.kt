package shvyn22.flexingmarvel.repository.local

import kotlinx.coroutines.flow.Flow
import shvyn22.flexingmarvel.data.local.dao.EventDao
import shvyn22.flexingmarvel.data.local.model.EventModel

class EventLocalRepository(
    private val dao: EventDao
) : LocalRepository<EventModel> {

    override fun getItems(query: String): Flow<List<EventModel>> = dao.getEvents(query)

    override fun isItemFavorite(id: Int): Flow<Boolean> = dao.isEventFavorite(id)

    override suspend fun insertItem(item: EventModel) {
        dao.insertEvent(item)
    }

    override suspend fun deleteItem(item: EventModel) {
        dao.deleteEvent(item)
    }
}