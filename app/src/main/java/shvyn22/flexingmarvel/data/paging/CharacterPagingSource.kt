package shvyn22.flexingmarvel.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import retrofit2.HttpException
import shvyn22.flexingmarvel.data.remote.api.ApiService
import shvyn22.flexingmarvel.data.local.model.CharacterModel
import shvyn22.flexingmarvel.util.API_LIMIT
import java.io.IOException

class CharacterPagingSource(
    private val api: ApiService,
    private val query: String
) : PagingSource<Int, CharacterModel>() {

    override fun getRefreshKey(state: PagingState<Int, CharacterModel>): Int? {
        return state.anchorPosition?.let {
            val anchorPage = state.closestPageToPosition(it)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CharacterModel> {
        val page = params.key ?: 1

        return try {
            val offset = (page - 1) * API_LIMIT

            val response =
                if (query.isEmpty()) api.getCharacters(offset)
                else api.getCharactersByName(query, offset)

            val items = response.data.results

            LoadResult.Page(
                data = items,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (items.isEmpty()) null else page + 1
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }
}