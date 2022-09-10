package shvyn22.flexingmarvel.domain.usecase.series

import kotlinx.coroutines.flow.Flow
import shvyn22.flexingmarvel.data.local.model.SeriesModel
import shvyn22.flexingmarvel.domain.repository.local.LocalRepository
import shvyn22.flexingmarvel.domain.usecase.base.IsItemFavoriteUseCase
import javax.inject.Inject

class IsSeriesFavoriteUseCase @Inject constructor(
    private val repo: LocalRepository<SeriesModel>
) : IsItemFavoriteUseCase {

    override fun invoke(id: Int): Flow<Boolean> =
        repo.isItemFavorite(id)
}