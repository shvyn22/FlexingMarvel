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
import shvyn22.flexingmarvel.domain.usecase.character.DeleteCharacterUseCase
import shvyn22.flexingmarvel.domain.usecase.character.InsertCharacterUseCase
import shvyn22.flexingmarvel.domain.usecase.character.IsCharacterFavoriteUseCase
import shvyn22.flexingmarvel.domain.usecase.event.GetEventsByTypeUseCase
import shvyn22.flexingmarvel.domain.usecase.series.GetSeriesByTypeUseCase
import shvyn22.flexingmarvel.util.DetailsStateEvent
import shvyn22.flexingmarvel.util.Resource

class DetailsCharacterViewModel @AssistedInject constructor(
    getEventsByTypeUseCase: GetEventsByTypeUseCase,
    getSeriesByTypeUseCase: GetSeriesByTypeUseCase,
    isCharacterFavoriteUseCase: IsCharacterFavoriteUseCase,
    private val insertCharacterUseCase: InsertCharacterUseCase,
    private val deleteCharacterUseCase: DeleteCharacterUseCase,
    @Assisted private val character: CharacterModel
) : ViewModel() {

    private val detailsEventChannel = Channel<DetailsStateEvent>()
    val detailsEvent = detailsEventChannel.receiveAsFlow()

    val events: StateFlow<Resource<List<EventModel>>> =
        getEventsByTypeUseCase(character)
            .stateIn(viewModelScope, SharingStarted.Lazily, Resource.Idle())

    val series: StateFlow<Resource<List<SeriesModel>>> =
        getSeriesByTypeUseCase(character)
            .stateIn(viewModelScope, SharingStarted.Lazily, Resource.Idle())

    val isCharacterFavorite: StateFlow<Boolean> =
        isCharacterFavoriteUseCase(character.id)
            .stateIn(viewModelScope, SharingStarted.Lazily, false)

    fun onToggleFavorite() {
        if (isCharacterFavorite.value) deleteCharacter()
        else insertCharacter()
    }

    private fun insertCharacter() {
        viewModelScope.launch {
            insertCharacterUseCase(character)
        }
    }

    private fun deleteCharacter() {
        viewModelScope.launch {
            deleteCharacterUseCase(character)
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