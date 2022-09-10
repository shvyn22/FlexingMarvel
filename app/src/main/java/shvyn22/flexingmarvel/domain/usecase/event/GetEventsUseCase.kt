package shvyn22.flexingmarvel.domain.usecase.event

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import shvyn22.flexingmarvel.data.local.model.EventModel
import shvyn22.flexingmarvel.domain.repository.remote.RemoteRepository
import shvyn22.flexingmarvel.domain.usecase.base.GetItemsUseCase
import javax.inject.Inject

class GetEventsUseCase @Inject constructor(
    private val repo: RemoteRepository<EventModel>
) : GetItemsUseCase<EventModel> {

    override fun invoke(query: String): Flow<PagingData<EventModel>> =
        repo.getItems(query)
}