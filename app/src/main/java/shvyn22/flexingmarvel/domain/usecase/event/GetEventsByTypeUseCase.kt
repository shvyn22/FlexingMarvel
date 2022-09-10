package shvyn22.flexingmarvel.domain.usecase.event

import kotlinx.coroutines.flow.Flow
import shvyn22.flexingmarvel.data.local.model.EventModel
import shvyn22.flexingmarvel.data.local.model.MarvelModel
import shvyn22.flexingmarvel.domain.repository.remote.RemoteRepository
import shvyn22.flexingmarvel.domain.usecase.base.GetItemsByTypeUseCase
import shvyn22.flexingmarvel.util.Resource
import javax.inject.Inject

class GetEventsByTypeUseCase @Inject constructor(
    private val repo: RemoteRepository<EventModel>
) : GetItemsByTypeUseCase<EventModel> {

    override fun invoke(type: MarvelModel): Flow<Resource<List<EventModel>>> =
        repo.getItemsByType(type)
}