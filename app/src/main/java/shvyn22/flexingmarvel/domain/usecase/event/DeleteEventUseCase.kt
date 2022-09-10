package shvyn22.flexingmarvel.domain.usecase.event

import shvyn22.flexingmarvel.data.local.model.EventModel
import shvyn22.flexingmarvel.domain.repository.local.LocalRepository
import shvyn22.flexingmarvel.domain.usecase.base.DeleteItemUseCase
import javax.inject.Inject

class DeleteEventUseCase @Inject constructor(
    private val repo: LocalRepository<EventModel>
) : DeleteItemUseCase<EventModel> {

    override suspend fun invoke(item: EventModel) =
        repo.deleteItem(item)
}