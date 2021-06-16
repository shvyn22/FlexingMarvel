package shvyn22.marvelapplication.ui.characters.details

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
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

class DetailsCharacterViewModel @ViewModelInject constructor(
    private val remoteRepo: RemoteRepository,
    private val localRepo: LocalRepository
): ViewModel() {

    fun getEvents(id: Int) = liveData {
        remoteRepo.getCharacterEvents(id).collect {
            emit(it)
        }
    }

    fun getSeries(id: Int) = liveData {
        remoteRepo.getCharacterSeries(id).collect {
            emit(it)
        }
    }

    fun isCharacterFavorite(id: Int) = liveData {
        localRepo.isCharacterFavorite(id).collect {
            emit(it)
        }
    }

    fun addToFavorite(item: CharacterModel) = viewModelScope.launch {
        localRepo.insertCharacter(item)
    }

    fun removeFromFavorite(item: CharacterModel) = viewModelScope.launch {
        localRepo.deleteCharacter(item)
    }

    private val detailsEventChannel = Channel<DetailsStateEvent>()
    val detailsEvent = detailsEventChannel.receiveAsFlow()

    fun onEventClick(item: EventModel) = viewModelScope.launch {
        detailsEventChannel.send(DetailsStateEvent.NavigateToEventDetails(item))
    }

    fun onSeriesClick(item: SeriesModel) = viewModelScope.launch {
        detailsEventChannel.send(DetailsStateEvent.NavigateToSeriesDetails(item))
    }
}