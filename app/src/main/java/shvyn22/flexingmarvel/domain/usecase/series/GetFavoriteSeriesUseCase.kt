package shvyn22.flexingmarvel.domain.usecase.series

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import shvyn22.flexingmarvel.data.local.model.SeriesModel
import shvyn22.flexingmarvel.domain.repository.local.LocalRepository
import shvyn22.flexingmarvel.domain.usecase.base.GetFavoriteItemsUseCase
import javax.inject.Inject

class GetFavoriteSeriesUseCase @Inject constructor(
    private val repo: LocalRepository<SeriesModel>
) : GetFavoriteItemsUseCase<SeriesModel> {

    override fun invoke(query: String): Flow<PagingData<SeriesModel>> =
        repo.getItems(query)
}