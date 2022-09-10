package shvyn22.flexingmarvel.domain.usecase.series

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import shvyn22.flexingmarvel.data.local.model.SeriesModel
import shvyn22.flexingmarvel.domain.repository.remote.RemoteRepository
import shvyn22.flexingmarvel.domain.usecase.base.GetItemsUseCase
import javax.inject.Inject

class GetSeriesUseCase @Inject constructor(
    private val repo: RemoteRepository<SeriesModel>
) : GetItemsUseCase<SeriesModel> {

    override fun invoke(query: String): Flow<PagingData<SeriesModel>> =
        repo.getItems(query)
}