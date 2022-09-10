package shvyn22.flexingmarvel.domain.usecase.character

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import shvyn22.flexingmarvel.data.local.model.CharacterModel
import shvyn22.flexingmarvel.domain.repository.local.LocalRepository
import shvyn22.flexingmarvel.domain.usecase.base.GetFavoriteItemsUseCase
import javax.inject.Inject

class GetFavoriteCharactersUseCase @Inject constructor(
    private val repo: LocalRepository<CharacterModel>
): GetFavoriteItemsUseCase<CharacterModel>{

    override fun invoke(query: String): Flow<PagingData<CharacterModel>> =
        repo.getItems(query)

}