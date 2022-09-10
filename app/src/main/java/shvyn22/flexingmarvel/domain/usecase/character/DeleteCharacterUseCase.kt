package shvyn22.flexingmarvel.domain.usecase.character

import shvyn22.flexingmarvel.data.local.model.CharacterModel
import shvyn22.flexingmarvel.domain.repository.local.LocalRepository
import shvyn22.flexingmarvel.domain.usecase.base.DeleteItemUseCase
import javax.inject.Inject

class DeleteCharacterUseCase @Inject constructor(
    private val repo: LocalRepository<CharacterModel>
) : DeleteItemUseCase<CharacterModel> {

    override suspend fun invoke(item: CharacterModel) =
        repo.deleteItem(item)
}