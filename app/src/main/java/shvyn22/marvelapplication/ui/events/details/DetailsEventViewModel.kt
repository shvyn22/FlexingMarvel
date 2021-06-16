package shvyn22.marvelapplication.ui.events.details

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

class DetailsEventViewModel @ViewModelInject constructor(
    private val remoteRepo: RemoteRepository,
    private val localRepo: LocalRepository
): ViewModel() {

    fun getCharacters(id: Int) = liveData {
        remoteRepo.getEventCharacters(id).collect {
            emit(it)
        }
    }

    fun getSeries(id: Int) = liveData {
        remoteRepo.getEventSeries(id).collect {
            emit(it)
        }
    }

    fun isEventFavorite(id: Int) = liveData {
        localRepo.isEventFavorite(id).collect {
            emit(it)
        }
    }

    fun addToFavorite(item: EventModel) = viewModelScope.launch {
        localRepo.insertEvent(item)
    }

    fun removeFromFavorite(item: EventModel) = viewModelScope.launch {
        localRepo.deleteEvent(item)
    }

    private val detailsEventChannel = Channel<DetailsStateEvent>()
    val detailsEvent = detailsEventChannel.receiveAsFlow()

    fun onCharacterClick(item: CharacterModel) = viewModelScope.launch {
        detailsEventChannel.send(DetailsStateEvent.NavigateToCharacterDetails(item))
    }

    fun onSeriesClick(item: SeriesModel) = viewModelScope.launch {
        detailsEventChannel.send(DetailsStateEvent.NavigateToSeriesDetails(item))
    }
}