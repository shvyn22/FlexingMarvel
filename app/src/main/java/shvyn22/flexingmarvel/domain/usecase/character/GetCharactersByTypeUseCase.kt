package shvyn22.flexingmarvel.domain.usecase.character

import kotlinx.coroutines.flow.Flow
import shvyn22.flexingmarvel.data.local.model.CharacterModel
import shvyn22.flexingmarvel.data.local.model.MarvelModel
import shvyn22.flexingmarvel.domain.repository.remote.RemoteRepository
import shvyn22.flexingmarvel.domain.usecase.base.GetItemsByTypeUseCase
import shvyn22.flexingmarvel.util.Resource
import javax.inject.Inject

class GetCharactersByTypeUseCase @Inject constructor(
    private val repo: RemoteRepository<CharacterModel>
) : GetItemsByTypeUseCase<CharacterModel> {

    override fun invoke(type: MarvelModel): Flow<Resource<List<CharacterModel>>> =
        repo.getItemsByType(type)
}