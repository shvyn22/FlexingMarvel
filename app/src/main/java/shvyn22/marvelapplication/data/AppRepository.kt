package shvyn22.marvelapplication.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import shvyn22.marvelapplication.api.ApiInterface
import shvyn22.marvelapplication.data.paging.CharacterPagingSource
import shvyn22.marvelapplication.data.paging.EventPagingSource
import shvyn22.marvelapplication.data.paging.SeriesPagingSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppRepository @Inject constructor(private val api: ApiInterface){
    fun getCharactersResults(query: String) =
        Pager(
            config = PagingConfig(
                pageSize = 33,
                maxSize = 500,
                enablePlaceholders = false),
            pagingSourceFactory = {CharacterPagingSource(api, query)}
        ).liveData

    fun getSeriesResults(query: String) =
        Pager(
            config = PagingConfig(
                    pageSize = 33,
                    maxSize = 500,
                    enablePlaceholders = false),
            pagingSourceFactory = {SeriesPagingSource(api, query)}
        ).liveData

    fun getEventsResults(query: String) =
        Pager(
            config = PagingConfig(
                    pageSize = 33,
                    maxSize = 500,
                    enablePlaceholders = false),
            pagingSourceFactory = {EventPagingSource(api, query)}
        ).liveData
}