package shvyn22.flexingmarvel.domain.usecase.character

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import shvyn22.flexingmarvel.data.local.model.CharacterModel
import shvyn22.flexingmarvel.domain.repository.remote.RemoteRepository
import shvyn22.flexingmarvel.domain.usecase.base.GetItemsUseCase
import javax.inject.Inject

class GetCharactersUseCase @Inject constructor(
    private val repo: RemoteRepository<CharacterModel>
) : GetItemsUseCase<CharacterModel> {

    override fun invoke(query: String): Flow<PagingData<CharacterModel>> =
        repo.getItems(query)
}