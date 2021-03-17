package shvyn22.marvelapplication.ui.details

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import shvyn22.marvelapplication.api.ApiInterface
import shvyn22.marvelapplication.data.dao.CharacterDao
import shvyn22.marvelapplication.data.dao.EventDao
import shvyn22.marvelapplication.data.dao.SeriesDao
import shvyn22.marvelapplication.data.entity.Event
import shvyn22.marvelapplication.data.entity.MarvelCharacter
import shvyn22.marvelapplication.data.entity.Series

class DetailsViewModel @ViewModelInject constructor(
    private val api : ApiInterface,
    private val characterDao: CharacterDao,
    private val eventDao: EventDao,
    private val seriesDao: SeriesDao
) : ViewModel() {

    var isCharacterFavorite = MutableLiveData<Boolean>()
    var isSeriesFavorite = MutableLiveData<Boolean>()
    var isEventFavorite = MutableLiveData<Boolean>()

    var characters = MutableLiveData<List<MarvelCharacter>>()
    var series = MutableLiveData<List<Series>>()
    var events = MutableLiveData<List<Event>>()

    private val detailsEventChannel = Channel<DetailsEvent>()
    val detailsEvent = detailsEventChannel.receiveAsFlow()

    fun isCharacterFavorite(id: Int) = viewModelScope.launch {
        isCharacterFavorite.value = characterDao.exists(id)
    }

    fun isSeriesFavorite(id: Int) = viewModelScope.launch {
        isSeriesFavorite.value = seriesDao.exists(id)
    }

    fun isEventFavorite(id: Int) = viewModelScope.launch {
        isEventFavorite.value = eventDao.exists(id)
    }

    fun getCharacterItems(id: Int) = viewModelScope.launch {
        series.value = api.getCharacterSeries(id).data.results
        events.value = api.getCharacterEvents(id).data.results
    }

    fun getSeriesItems(id: Int) = viewModelScope.launch {
        events.value = api.getSeriesEvents(id).data.results
        characters.value = api.getSeriesCharacters(id).data.results
    }

    fun getEventItems(id: Int) = viewModelScope.launch {
        series.value = api.getEventSeries(id).data.results
        characters.value = api.getEventCharacters(id).data.results
    }

    fun onCharacterItemClick(item: MarvelCharacter) = viewModelScope.launch {
        detailsEventChannel.send(DetailsEvent.NavigateToCharacterDetails(item))
    }

    fun onToggleCharacterFavorite(item: MarvelCharacter) = viewModelScope.launch {
        if (isCharacterFavorite.value!!) characterDao.delete(item) else characterDao.insert(item)
        isCharacterFavorite.value = !isCharacterFavorite.value!!
    }

    fun onSeriesItemClick(item: Series) = viewModelScope.launch {
        detailsEventChannel.send(DetailsEvent.NavigateToSeriesDetails(item))
    }

    fun onToggleSeriesFavorite(item: Series) = viewModelScope.launch {
        if (isSeriesFavorite.value!!) seriesDao.delete(item) else seriesDao.insert(item)
        isSeriesFavorite.value = !isSeriesFavorite.value!!
    }

    fun onEventItemClick(item: Event) = viewModelScope.launch {
        detailsEventChannel.send(DetailsEvent.NavigateToEventDetails(item))
    }

    fun onToggleEventFavorite(item: Event) = viewModelScope.launch {
        if (isEventFavorite.value!!) eventDao.delete(item) else eventDao.insert(item)
        isEventFavorite.value = !isEventFavorite.value!!
    }

    sealed class DetailsEvent {
        data class NavigateToCharacterDetails(val item: MarvelCharacter) : DetailsEvent()
        data class NavigateToSeriesDetails(val item: Series) : DetailsEvent()
        data class NavigateToEventDetails(val item: Event) : DetailsEvent()
    }
}