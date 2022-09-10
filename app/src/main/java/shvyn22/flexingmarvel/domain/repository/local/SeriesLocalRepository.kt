package shvyn22.flexingmarvel.domain.repository.local

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import shvyn22.flexingmarvel.data.local.dao.SeriesDao
import shvyn22.flexingmarvel.data.local.model.SeriesModel
import shvyn22.flexingmarvel.util.API_LIMIT
import shvyn22.flexingmarvel.util.MAX_PAGING_SIZE

class SeriesLocalRepository(
    private val dao: SeriesDao
) : LocalRepository<SeriesModel> {

    override fun getItems(query: String): Flow<PagingData<SeriesModel>> =
        Pager(
            config = PagingConfig(
                pageSize = API_LIMIT,
                maxSize = MAX_PAGING_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { dao.getSeries(query) }
        ).flow

    override fun isItemFavorite(id: Int): Flow<Boolean> = dao.isSeriesFavorite(id)

    override suspend fun insertItem(item: SeriesModel) {
        dao.insertSeries(item)
    }

    override suspend fun deleteItem(item: SeriesModel) {
        dao.deleteSeries(item)
    }
}