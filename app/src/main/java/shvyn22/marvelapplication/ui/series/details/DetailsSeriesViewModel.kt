package shvyn22.marvelapplication.ui.series.details

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import shvyn22.marvelapplication.data.local.model.CharacterModel
import shvyn22.marvelapplication.data.local.model.EventModel
import shvyn22.marvelapplication.data.local.model.SeriesModel
import shvyn22.marvelapplication.repository.LocalRepository
import shvyn22.marvelapplication.repository.RemoteRepository
import shvyn22.marvelapplication.util.DetailsStateEvent

class DetailsSeriesViewModel @ViewModelInject constructor(
    private val remoteRepo: RemoteRepository,
    private val localRepo: LocalRepository
): ViewModel() {

    fun getCharacters(id: Int) = liveData {
        remoteRepo.getSeriesCharacters(id).collect {
            emit(it)
        }
    }

    fun getEvents(id: Int) = liveData {
        remoteRepo.getSeriesEvents(id).collect {
            emit(it)
        }
    }

    fun isSeriesFavorite(id: Int) = liveData {
        localRepo.isSeriesFavorite(id).collect {
            emit(it)
        }
    }

    fun addToFavorite(item: SeriesModel) = viewModelScope.launch {
        localRepo.insertSeries(item)
    }

    fun removeFromFavorite(item: SeriesModel) = viewModelScope.launch {
        localRepo.deleteSeries(item)
    }

    private val detailsEventChannel = Channel<DetailsStateEvent>()
    val detailsEvent = detailsEventChannel.receiveAsFlow()

    fun onCharacterClick(item: CharacterModel) = viewModelScope.launch {
        detailsEventChannel.send(DetailsStateEvent.NavigateToCharacterDetails(item))
    }

    fun onEventClick(item: EventModel) = viewModelScope.launch {
        detailsEventChannel.send(DetailsStateEvent.NavigateToEventDetails(item))
    }
}