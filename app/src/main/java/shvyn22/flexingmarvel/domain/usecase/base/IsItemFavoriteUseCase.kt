package shvyn22.flexingmarvel.domain.usecase.base

import kotlinx.coroutines.flow.Flow

interface IsItemFavoriteUseCase {

    operator fun invoke(id: Int): Flow<Boolean>
}