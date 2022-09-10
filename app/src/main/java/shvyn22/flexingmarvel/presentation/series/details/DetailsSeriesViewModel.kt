package shvyn22.flexingmarvel.presentation.series.details

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
import shvyn22.flexingmarvel.domain.usecase.character.GetCharactersByTypeUseCase
import shvyn22.flexingmarvel.domain.usecase.event.GetEventsByTypeUseCase
import shvyn22.flexingmarvel.domain.usecase.series.DeleteSeriesUseCase
import shvyn22.flexingmarvel.domain.usecase.series.InsertSeriesUseCase
import shvyn22.flexingmarvel.domain.usecase.series.IsSeriesFavoriteUseCase
import shvyn22.flexingmarvel.util.DetailsStateEvent
import shvyn22.flexingmarvel.util.Resource

class DetailsSeriesViewModel @AssistedInject constructor(
    getCharactersByTypeUseCase: GetCharactersByTypeUseCase,
    getEventsByTypeUseCase: GetEventsByTypeUseCase,
    isSeriesFavoriteUseCase: IsSeriesFavoriteUseCase,
    private val insertSeriesUseCase: InsertSeriesUseCase,
    private val deleteSeriesUseCase: DeleteSeriesUseCase,
    @Assisted private val series: SeriesModel
) : ViewModel() {

    private val detailsEventChannel = Channel<DetailsStateEvent>()
    val detailsEvent = detailsEventChannel.receiveAsFlow()

    val characters: StateFlow<Resource<List<CharacterModel>>> =
        getCharactersByTypeUseCase(series)
            .stateIn(viewModelScope, SharingStarted.Lazily, Resource.Idle())

    val events: StateFlow<Resource<List<EventModel>>> =
        getEventsByTypeUseCase(series)
            .stateIn(viewModelScope, SharingStarted.Lazily, Resource.Idle())

    val isSeriesFavorite: StateFlow<Boolean> =
        isSeriesFavoriteUseCase(series.id)
            .stateIn(viewModelScope, SharingStarted.Lazily, false)

    fun onToggleFavorite() {
        if (isSeriesFavorite.value) deleteSeries()
        else insertSeries()
    }

    private fun insertSeries() {
        viewModelScope.launch {
            insertSeriesUseCase(series)
        }
    }

    private fun deleteSeries() {
        viewModelScope.launch {
            deleteSeriesUseCase(series)
        }
    }

    fun onCharacterClick(item: CharacterModel) {
        viewModelScope.launch {
            detailsEventChannel.send(DetailsStateEvent.NavigateToCharacterDetails(item))
        }
    }

    fun onEventClick(item: EventModel) {
        viewModelScope.launch {
            detailsEventChannel.send(DetailsStateEvent.NavigateToEventDetails(item))
        }
    }

    @AssistedFactory
    interface Factory {

        fun create(series: SeriesModel): DetailsSeriesViewModel
    }

    companion object {

        fun provideFactory(
            factory: Factory,
            series: SeriesModel
        ) = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return factory.create(series) as T
            }
        }
    }
}