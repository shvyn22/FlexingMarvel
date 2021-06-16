package shvyn22.marvelapplication.ui.series

import androidx.appcompat.app.AppCompatDelegate
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import androidx.paging.cachedIn
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import shvyn22.marvelapplication.repository.RemoteRepository
import shvyn22.marvelapplication.data.preferences.PreferencesManager
import shvyn22.marvelapplication.data.local.model.SeriesModel
import shvyn22.marvelapplication.repository.LocalRepository
import shvyn22.marvelapplication.util.MainStateEvent

class SeriesViewModel @ViewModelInject constructor(
    private val remoteRepo: RemoteRepository,
    private val localRepo: LocalRepository,
    private val prefsManager: PreferencesManager,
    @Assisted val state: SavedStateHandle
) : ViewModel() {

    private val _isShowingFavorite = MutableLiveData(false)
    val isShowingFavorite: LiveData<Boolean> get() = _isShowingFavorite

    val nightMode = prefsManager.preferencesFlow.asLiveData()
    private val searchQuery = state.getLiveData("searchSeries", "")

    private val seriesEventChannel = Channel<MainStateEvent<SeriesModel>>()
    val seriesEvent = seriesEventChannel.receiveAsFlow()

    val pagingItems = searchQuery.switchMap { query ->
        remoteRepo.getSeriesResults(query).cachedIn(viewModelScope)
    }

    val items = searchQuery.switchMap { query ->
        localRepo.getSeries(query).asLiveData()
    }

    fun searchItems(query: String) = viewModelScope.launch {
        searchQuery.value = query
    }

    fun onItemClick(item: SeriesModel) = viewModelScope.launch {
        seriesEventChannel.send(MainStateEvent.NavigateToDetails(item))
    }

    fun onToggleFavoriteButton() {
        _isShowingFavorite.value = !(_isShowingFavorite.value ?: false)
    }

    fun onToggleModeIcon() = viewModelScope.launch {
        prefsManager.updateNightMode(
            if (nightMode.value == AppCompatDelegate.MODE_NIGHT_YES)
                AppCompatDelegate.MODE_NIGHT_NO
            else AppCompatDelegate.MODE_NIGHT_YES
        )
    }
}