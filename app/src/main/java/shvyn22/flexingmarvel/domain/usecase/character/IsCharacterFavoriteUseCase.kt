package shvyn22.flexingmarvel.domain.usecase.character

import kotlinx.coroutines.flow.Flow
import shvyn22.flexingmarvel.data.local.model.CharacterModel
import shvyn22.flexingmarvel.domain.repository.local.LocalRepository
import shvyn22.flexingmarvel.domain.usecase.base.IsItemFavoriteUseCase
import javax.inject.Inject

class IsCharacterFavoriteUseCase @Inject constructor(
    private val repo: LocalRepository<CharacterModel>
) : IsItemFavoriteUseCase {

    override fun invoke(id: Int): Flow<Boolean> =
        repo.isItemFavorite(id)
}