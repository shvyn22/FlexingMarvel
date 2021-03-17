package shvyn22.marvelapplication.data.paging

import androidx.paging.PagingSource
import retrofit2.HttpException
import shvyn22.marvelapplication.api.ApiInterface
import shvyn22.marvelapplication.data.entity.MarvelCharacter
import java.io.IOException

class CharacterPagingSource(
    private val api: ApiInterface,
    private val query: String
) : PagingSource<Int, MarvelCharacter>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MarvelCharacter> {
        val position = params.key ?: 1

        return try {
            val offset = (position - 1) * ApiInterface.LIMIT
            val response = if (query.isEmpty()) api.getCharacters(offset) else
                        api.getCharactersByName(query,offset)
            val items = response.data.results

            LoadResult.Page(
                data = items,
                prevKey = if (position == 1) null else position - 1,
                nextKey = if (items.isEmpty()) null else position + 1
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }
}