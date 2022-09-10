package shvyn22.flexingmarvel.domain.usecase.series

import shvyn22.flexingmarvel.data.local.model.SeriesModel
import shvyn22.flexingmarvel.domain.repository.local.LocalRepository
import shvyn22.flexingmarvel.domain.usecase.base.DeleteItemUseCase
import javax.inject.Inject

class DeleteSeriesUseCase @Inject constructor(
    private val repo: LocalRepository<SeriesModel>
) : DeleteItemUseCase<SeriesModel> {

    override suspend fun invoke(item: SeriesModel) =
        repo.deleteItem(item)
}