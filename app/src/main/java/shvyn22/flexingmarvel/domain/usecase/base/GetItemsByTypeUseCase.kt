package shvyn22.flexingmarvel.domain.usecase.base

import kotlinx.coroutines.flow.Flow
import shvyn22.flexingmarvel.data.local.model.MarvelModel
import shvyn22.flexingmarvel.util.Resource

interface GetItemsByTypeUseCase<T : MarvelModel> {

    operator fun invoke(type: MarvelModel): Flow<Resource<List<T>>>
}