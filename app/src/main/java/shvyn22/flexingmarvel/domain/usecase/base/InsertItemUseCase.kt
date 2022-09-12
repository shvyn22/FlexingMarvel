package shvyn22.flexingmarvel.domain.usecase.base

import shvyn22.flexingmarvel.data.local.model.MarvelModel

interface InsertItemUseCase<T : MarvelModel> {

    suspend operator fun invoke(item: T)
}