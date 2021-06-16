package shvyn22.marvelapplication.data.paging

import androidx.paging.PagingSource
import retrofit2.HttpException
import shvyn22.marvelapplication.api.ApiInterface
import shvyn22.marvelapplication.data.local.model.SeriesModel
import java.io.IOException

class SeriesPagingSource(
    private val api: ApiInterface,
    private val query: String
) : PagingSource<Int, SeriesModel>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SeriesModel> {
        val position = params.key ?: 1

        return try {
            val offset = (position - 1) * ApiInterface.LIMIT

            val response = if (query.isEmpty()) api.getSeries(offset) else
                api.getSeriesByTitle(query, offset)

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