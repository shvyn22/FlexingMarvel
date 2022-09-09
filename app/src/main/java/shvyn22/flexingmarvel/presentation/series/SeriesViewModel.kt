package shvyn22.flexingmarvel.presentation.series

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import shvyn22.flexingmarvel.data.local.model.SeriesModel
import shvyn22.flexingmarvel.repository.local.LocalRepository
import shvyn22.flexingmarvel.repository.remote.RemoteRepository
import shvyn22.flexingmarvel.util.MainStateEvent
import shvyn22.flexingmarvel.util.SEARCH_SERIES_KEY
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class SeriesViewModel @Inject constructor(
    private val remoteRepo: RemoteRepository<SeriesModel>,
    private val localRepo: LocalRepository<SeriesModel>,
    private val savedState: SavedStateHandle
) : ViewModel() {

    private val searchQuery = savedState.getStateFlow(SEARCH_SERIES_KEY, "")

    private val _isShowingFavorite = MutableStateFlow(false)
    val isShowingFavorite: StateFlow<Boolean> get() = _isShowingFavorite

    private val seriesEventChannel = Channel<MainStateEvent<SeriesModel>>()
    val seriesEvent = seriesEventChannel.receiveAsFlow()

    val pagingItems = searchQuery.flatMapConcat { query ->
        remoteRepo.getItems(query).cachedIn(viewModelScope)
    }.stateIn(viewModelScope, SharingStarted.Lazily, PagingData.empty())

    val items = searchQuery.flatMapConcat { query ->
        localRepo.getItems(query)
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun searchItems(query: String) {
        viewModelScope.launch {
            savedState[SEARCH_SERIES_KEY] = query
        }
    }

    fun onItemClick(item: SeriesModel) {
        viewModelScope.launch {
            seriesEventChannel.send(MainStateEvent.NavigateToDetails(item))
        }
    }

    fun onToggleFavoriteButton() {
        viewModelScope.launch {
            _isShowingFavorite.value = _isShowingFavorite.value
        }
    }
}