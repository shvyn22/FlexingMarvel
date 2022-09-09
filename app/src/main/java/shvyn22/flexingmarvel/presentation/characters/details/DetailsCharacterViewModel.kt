package shvyn22.flexingmarvel.presentation.characters.details

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

class DetailsCharacterViewModel @AssistedInject constructor(
    eventRepo: RemoteRepository<EventModel>,
    seriesRepo: RemoteRepository<SeriesModel>,
    private val localRepo: LocalRepository<CharacterModel>,
    @Assisted private val character: CharacterModel
) : ViewModel() {

    private val detailsEventChannel = Channel<DetailsStateEvent>()
    val detailsEvent = detailsEventChannel.receiveAsFlow()

    val events: StateFlow<Resource<List<EventModel>>> =
        eventRepo.getItemsByType(character)
            .stateIn(viewModelScope, SharingStarted.Lazily, Resource.Idle())

    val series: StateFlow<Resource<List<SeriesModel>>> =
        seriesRepo.getItemsByType(character)
            .stateIn(viewModelScope, SharingStarted.Lazily, Resource.Idle())

    val isCharacterFavorite: StateFlow<Boolean> =
        localRepo.isItemFavorite(character.id)
            .stateIn(viewModelScope, SharingStarted.Lazily, false)

    fun onToggleFavorite() {
        if (isCharacterFavorite.value) deleteCharacter()
        else insertCharacter()
    }

    private fun insertCharacter() {
        viewModelScope.launch {
            localRepo.insertItem(character)
        }
    }

    private fun deleteCharacter() {
        viewModelScope.launch {
            localRepo.deleteItem(character)
        }
    }

    fun onEventClick(item: EventModel) {
        viewModelScope.launch {
            detailsEventChannel.send(DetailsStateEvent.NavigateToEventDetails(item))
        }
    }

    fun onSeriesClick(item: SeriesModel) {
        viewModelScope.launch {
            detailsEventChannel.send(DetailsStateEvent.NavigateToSeriesDetails(item))
        }
    }

    @AssistedFactory
    interface Factory {

        fun create(character: CharacterModel): DetailsCharacterViewModel
    }

    companion object {

        fun provideFactory(
            factory: Factory,
            character: CharacterModel
        ) = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return factory.create(character) as T
            }
        }
    }
}