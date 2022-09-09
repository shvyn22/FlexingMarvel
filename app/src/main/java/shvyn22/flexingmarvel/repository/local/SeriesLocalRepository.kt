package shvyn22.flexingmarvel.repository.local

import kotlinx.coroutines.flow.Flow
import shvyn22.flexingmarvel.data.local.dao.SeriesDao
import shvyn22.flexingmarvel.data.local.model.SeriesModel

class SeriesLocalRepository(
    private val dao: SeriesDao
) : LocalRepository<SeriesModel> {

    override fun getItems(query: String): Flow<List<SeriesModel>> = dao.getSeries(query)

    override fun isItemFavorite(id: Int): Flow<Boolean> = dao.isSeriesFavorite(id)

    override suspend fun insertItem(item: SeriesModel) {
        dao.insertSeries(item)
    }

    override suspend fun deleteItem(item: SeriesModel) {
        dao.deleteSeries(item)
    }
}