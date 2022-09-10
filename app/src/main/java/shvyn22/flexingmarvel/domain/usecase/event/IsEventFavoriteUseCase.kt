package shvyn22.flexingmarvel.domain.usecase.event

import kotlinx.coroutines.flow.Flow
import shvyn22.flexingmarvel.data.local.model.EventModel
import shvyn22.flexingmarvel.domain.repository.local.LocalRepository
import shvyn22.flexingmarvel.domain.usecase.base.IsItemFavoriteUseCase
import javax.inject.Inject

class IsEventFavoriteUseCase @Inject constructor(
    private val repo: LocalRepository<EventModel>
) : IsItemFavoriteUseCase {

    override fun invoke(id: Int): Flow<Boolean> =
        repo.isItemFavorite(id)
}