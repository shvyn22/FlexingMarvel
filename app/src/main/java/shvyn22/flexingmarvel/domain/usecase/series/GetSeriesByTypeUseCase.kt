package shvyn22.flexingmarvel.domain.usecase.series

import kotlinx.coroutines.flow.Flow
import shvyn22.flexingmarvel.data.local.model.MarvelModel
import shvyn22.flexingmarvel.data.local.model.SeriesModel
import shvyn22.flexingmarvel.domain.repository.remote.RemoteRepository
import shvyn22.flexingmarvel.domain.usecase.base.GetItemsByTypeUseCase
import shvyn22.flexingmarvel.util.Resource
import javax.inject.Inject

class GetSeriesByTypeUseCase @Inject constructor(
    private val repo: RemoteRepository<SeriesModel>
) : GetItemsByTypeUseCase<SeriesModel> {

    override fun invoke(type: MarvelModel): Flow<Resource<List<SeriesModel>>> =
        repo.getItemsByType(type)
}