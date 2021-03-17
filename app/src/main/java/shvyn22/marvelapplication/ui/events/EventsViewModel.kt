package shvyn22.marvelapplication.ui.events

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
import shvyn22.marvelapplication.data.local.dao.EventDao
import shvyn22.marvelapplication.data.model.EventModel

class EventsViewModel @ViewModelInject constructor(
    private val repository: RemoteRepository,
    private val eventDao: EventDao,
    private val prefsManager: PreferencesManager,
    @Assisted state: SavedStateHandle
) : ViewModel() {

    val isShowingFavorite = MutableLiveData<Boolean>()

    val nightMode = prefsManager.preferencesFlow.asLiveData()
    private val searchQuery = state.getLiveData("searchEvents", "")

    private val eventChannel = Channel<EventFragmentEvent>()
    val event = eventChannel.receiveAsFlow()

    val pagingItems = searchQuery.switchMap { query ->
        repository.getEventsResults(query).cachedIn(viewModelScope)
    }

    val items = searchQuery.switchMap { query ->
        eventDao.getAll(query).asLiveData()
    }

    fun searchItems(query: String) {
        searchQuery.value = query
    }

    fun onItemClick(item: EventModel) = viewModelScope.launch {
        eventChannel.send(EventFragmentEvent.NavigateToDetails(item))
    }

    fun onToggleMenuButton() {
        isShowingFavorite.value = !isShowingFavorite.value!!
    }

    fun onToggleModeIcon() = viewModelScope.launch {
        prefsManager.updateNightMode(if (nightMode.value?.nightMode == AppCompatDelegate.MODE_NIGHT_YES)
            AppCompatDelegate.MODE_NIGHT_NO else AppCompatDelegate.MODE_NIGHT_YES)
    }

    sealed class EventFragmentEvent {
        data class NavigateToDetails(val item: EventModel) : EventFragmentEvent()
    }
}