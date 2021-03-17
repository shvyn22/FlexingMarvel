package shvyn22.marvelapplication.ui.series

import androidx.appcompat.app.AppCompatDelegate
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import androidx.paging.cachedIn
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import shvyn22.marvelapplication.data.repository.RemoteRepository
import shvyn22.marvelapplication.data.PreferencesManager
import shvyn22.marvelapplication.data.local.dao.SeriesDao
import shvyn22.marvelapplication.data.model.SeriesModel

class SeriesViewModel @ViewModelInject constructor(
    private val repository: RemoteRepository,
    private val seriesDao: SeriesDao,
    private val prefsManager: PreferencesManager,
    @Assisted val state: SavedStateHandle
) : ViewModel() {

    val isShowingFavorite = MutableLiveData<Boolean>()

    val nightMode = prefsManager.preferencesFlow.asLiveData()
    private val searchQuery = state.getLiveData("searchSeries", "")

    private val seriesEventChannel = Channel<SeriesEvent>()
    val seriesEvent = seriesEventChannel.receiveAsFlow()

    val pagingItems = searchQuery.switchMap { query ->
        repository.getSeriesResults(query).cachedIn(viewModelScope)
    }

    val items = searchQuery.switchMap { query ->
        seriesDao.getAll(query).asLiveData()
    }

    fun searchItems(query: String) {
        searchQuery.value = query
    }

    fun onItemClick(item: SeriesModel) = viewModelScope.launch {
        seriesEventChannel.send(SeriesEvent.NavigateToDetails(item))
    }

    fun onToggleMenuButton() {
        isShowingFavorite.value = !isShowingFavorite.value!!
    }

    fun onToggleModeIcon() = viewModelScope.launch {
        prefsManager.updateNightMode(if (nightMode.value?.nightMode == AppCompatDelegate.MODE_NIGHT_YES)
            AppCompatDelegate.MODE_NIGHT_NO else AppCompatDelegate.MODE_NIGHT_YES)
    }

    sealed class SeriesEvent {
        data class NavigateToDetails(val item: SeriesModel) : SeriesEvent()
    }
}