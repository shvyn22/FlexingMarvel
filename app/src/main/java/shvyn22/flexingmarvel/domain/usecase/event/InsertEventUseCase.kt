package shvyn22.flexingmarvel.domain.usecase.event

import shvyn22.flexingmarvel.data.local.model.EventModel
import shvyn22.flexingmarvel.domain.repository.local.LocalRepository
import shvyn22.flexingmarvel.domain.usecase.base.InsertItemUseCase
import javax.inject.Inject

class InsertEventUseCase @Inject constructor(
    private val repo: LocalRepository<EventModel>
) : InsertItemUseCase<EventModel> {

    override suspend fun invoke(item: EventModel) =
        repo.insertItem(item)
}