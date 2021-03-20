package shvyn22.marvelapplication.ui.details

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import shvyn22.marvelapplication.data.model.EventModel
import shvyn22.marvelapplication.data.model.CharacterModel
import shvyn22.marvelapplication.data.model.SeriesModel
import shvyn22.marvelapplication.data.repository.LocalRepository
import shvyn22.marvelapplication.data.repository.RemoteRepository
import shvyn22.marvelapplication.util.Resource

class DetailsViewModel @ViewModelInject constructor(
    private val remoteRepo: RemoteRepository,
    private val localRepo: LocalRepository
) : ViewModel() {

    private val _isCharacterFavorite = MutableLiveData<Boolean>()
    val isCharacterFavorite: LiveData<Boolean> get() = _isCharacterFavorite

    private val _isSeriesFavorite = MutableLiveData<Boolean>()
    val isSeriesFavorite: LiveData<Boolean> get() = _isSeriesFavorite

    private val _isEventFavorite = MutableLiveData<Boolean>()
    val isEventFavorite: LiveData<Boolean> get() = _isEventFavorite

    private val _characters = MutableLiveData<Resource<List<CharacterModel>>>()
    val characters: LiveData<Resource<List<CharacterModel>>> get() = _characters

    private val _series = MutableLiveData<Resource<List<SeriesModel>>>()
    val series: LiveData<Resource<List<SeriesModel>>> get() = _series

    private val _events = MutableLiveData<Resource<List<EventModel>>>()
    val events: LiveData<Resource<List<EventModel>>> get() = _events

    private val detailsEventChannel = Channel<DetailsEvent>()
    val detailsEvent = detailsEventChannel.receiveAsFlow()

    fun isCharacterFavorite(id: Int) = viewModelScope.launch {
        _isCharacterFavorite.value = localRepo.isCharacterInLibrary(id)
    }

    fun isSeriesFavorite(id: Int) = viewModelScope.launch {
        _isSeriesFavorite.value = localRepo.isSeriesInLibrary(id)
    }

    fun isEventFavorite(id: Int) = viewModelScope.launch {
        _isEventFavorite.value = localRepo.isEventInLibrary(id)
    }

    fun getCharacterItems(id: Int) = viewModelScope.launch {
        _series.postValue(Resource.Loading())
        _series.value = remoteRepo.getCharacterSeries(id)

        _events.postValue(Resource.Loading())
        _events.value = remoteRepo.getCharacterEvents(id)
    }

    fun getSeriesItems(id: Int) = viewModelScope.launch {
        _events.postValue(Resource.Loading())
        _events.value = remoteRepo.getSeriesEvents(id)

        _characters.postValue(Resource.Loading())
        _characters.value = remoteRepo.getSeriesCharacters(id)
    }

    fun getEventItems(id: Int) = viewModelScope.launch {
        _series.postValue(Resource.Loading())
        _series.value = remoteRepo.getEventSeries(id)

        _characters.postValue(Resource.Loading())
        _characters.value = remoteRepo.getEventCharacters(id)
    }

    fun onCharacterItemClick(item: CharacterModel) = viewModelScope.launch {
        detailsEventChannel.send(DetailsEvent.NavigateToCharacterDetails(item))
    }

    fun onToggleCharacterFavorite(item: CharacterModel) = viewModelScope.launch {
        if (_isCharacterFavorite.value!!) localRepo.deleteCharacter(item)
        else localRepo.insertCharacter(item)
        _isCharacterFavorite.value = !_isCharacterFavorite.value!!
    }

    fun onSeriesItemClick(item: SeriesModel) = viewModelScope.launch {
        detailsEventChannel.send(DetailsEvent.NavigateToSeriesDetails(item))
    }

    fun onToggleSeriesFavorite(item: SeriesModel) = viewModelScope.launch {
        if (_isSeriesFavorite.value!!) localRepo.deleteSeries(item)
        else localRepo.insertSeries(item)
        _isSeriesFavorite.value = !_isSeriesFavorite.value!!
    }

    fun onEventItemClick(item: EventModel) = viewModelScope.launch {
        detailsEventChannel.send(DetailsEvent.NavigateToEventDetails(item))
    }

    fun onToggleEventFavorite(item: EventModel) = viewModelScope.launch {
        if (_isEventFavorite.value!!) localRepo.deleteEvent(item)
        else localRepo.insertEvent(item)
        _isEventFavorite.value = !_isEventFavorite.value!!
    }

    sealed class DetailsEvent {
        data class NavigateToCharacterDetails(val item: CharacterModel) : DetailsEvent()
        data class NavigateToSeriesDetails(val item: SeriesModel) : DetailsEvent()
        data class NavigateToEventDetails(val item: EventModel) : DetailsEvent()
    }
}