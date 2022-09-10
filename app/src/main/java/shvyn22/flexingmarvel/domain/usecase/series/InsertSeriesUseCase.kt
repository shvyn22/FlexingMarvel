package shvyn22.flexingmarvel.domain.usecase.series

import shvyn22.flexingmarvel.data.local.model.EventModel
import shvyn22.flexingmarvel.data.local.model.SeriesModel
import shvyn22.flexingmarvel.domain.repository.local.LocalRepository
import shvyn22.flexingmarvel.domain.usecase.base.InsertItemUseCase
import javax.inject.Inject

class InsertSeriesUseCase @Inject constructor(
    private val repo: LocalRepository<SeriesModel>
) : InsertItemUseCase<SeriesModel> {

    override suspend fun invoke(item: SeriesModel) =
        repo.insertItem(item)
}