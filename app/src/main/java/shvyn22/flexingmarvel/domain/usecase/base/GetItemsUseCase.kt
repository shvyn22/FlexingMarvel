package shvyn22.flexingmarvel.domain.usecase.base

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import shvyn22.flexingmarvel.data.local.model.MarvelModel

interface GetItemsUseCase<T : MarvelModel> {

    operator fun invoke(query: String): Flow<PagingData<T>>
}