package shvyn22.flexingmarvel.domain.usecase.character

import shvyn22.flexingmarvel.data.local.model.CharacterModel
import shvyn22.flexingmarvel.domain.repository.local.LocalRepository
import shvyn22.flexingmarvel.domain.usecase.base.InsertItemUseCase
import javax.inject.Inject

class InsertCharacterUseCase @Inject constructor(
    private val repo: LocalRepository<CharacterModel>
) : InsertItemUseCase<CharacterModel> {

    override suspend fun invoke(item: CharacterModel) =
        repo.insertItem(item)
}