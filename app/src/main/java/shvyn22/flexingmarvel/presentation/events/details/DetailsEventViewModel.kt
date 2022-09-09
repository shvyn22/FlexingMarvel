package shvyn22.flexingmarvel.presentation.events.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import shvyn22.flexingmarvel.data.local.model.CharacterModel
import shvyn22.flexingmarvel.data.local.model.EventModel
import shvyn22.flexingmarvel.data.local.model.SeriesModel
import shvyn22.flexingmarvel.repository.local.LocalRepository
import shvyn22.flexingmarvel.repository.remote.RemoteRepository
import shvyn22.flexingmarvel.util.DetailsStateEvent
import shvyn22.flexingmarvel.util.Resource

class DetailsEventViewModel @AssistedInject constructor(
    characterRepo: RemoteRepository<CharacterModel>,
    seriesRepo: RemoteRepository<SeriesModel>,
    private val localRepo: LocalRepository<EventModel>,
    @Assisted private val event: EventModel
) : ViewModel() {

    private val detailsEventChannel = Channel<DetailsStateEvent>()
    val detailsEvent = detailsEventChannel.receiveAsFlow()

    val characters: StateFlow<Resource<List<CharacterModel>>> =
        characterRepo.getItemsByType(event)
            .stateIn(viewModelScope, SharingStarted.Lazily, Resource.Idle())

    val series: StateFlow<Resource<List<SeriesModel>>> =
        seriesRepo.getItemsByType(event)
            .stateIn(viewModelScope, SharingStarted.Lazily, Resource.Idle())

    val isEventFavorite: StateFlow<Boolean> =
        localRepo.isItemFavorite(event.id)
            .stateIn(viewModelScope, SharingStarted.Lazily, false)

    fun onToggleFavorite() {
        if (isEventFavorite.value) deleteEvent()
        else insertEvent()
    }

    private fun insertEvent() {
        viewModelScope.launch {
            localRepo.insertItem(event)
        }
    }

    private fun deleteEvent() {
        viewModelScope.launch {
            localRepo.deleteItem(event)
        }
    }

    fun onCharacterClick(item: CharacterModel) {
        viewModelScope.launch {
            detailsEventChannel.send(DetailsStateEvent.NavigateToCharacterDetails(item))
        }
    }

    fun onSeriesClick(item: SeriesModel) {
        viewModelScope.launch {
            detailsEventChannel.send(DetailsStateEvent.NavigateToSeriesDetails(item))
        }
    }

    @AssistedFactory
    interface Factory {

        fun create(event: EventModel): DetailsEventViewModel
    }

    companion object {

        fun provideFactory(
            factory: Factory,
            event: EventModel
        ) = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return factory.create(event) as T
            }
        }
    }
}