package shvyn22.flexingmarvel.domain.usecase.event

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import shvyn22.flexingmarvel.data.local.model.EventModel
import shvyn22.flexingmarvel.domain.repository.local.LocalRepository
import shvyn22.flexingmarvel.domain.usecase.base.GetFavoriteItemsUseCase
import javax.inject.Inject

class GetFavoriteEventsUseCase @Inject constructor(
    private val repo: LocalRepository<EventModel>
) : GetFavoriteItemsUseCase<EventModel> {

    override fun invoke(query: String): Flow<PagingData<EventModel>> =
        repo.getItems(query)
}