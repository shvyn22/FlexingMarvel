package shvyn22.marvelapplication.ui.details

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import shvyn22.marvelapplication.api.ApiInterface
import shvyn22.marvelapplication.data.local.dao.CharacterDao
import shvyn22.marvelapplication.data.local.dao.EventDao
import shvyn22.marvelapplication.data.local.dao.SeriesDao
import shvyn22.marvelapplication.data.model.EventModel
import shvyn22.marvelapplication.data.model.CharacterModel
import shvyn22.marvelapplication.data.model.SeriesModel

class DetailsViewModel @ViewModelInject constructor(
    private val api : ApiInterface,
    private val characterDao: CharacterDao,
    private val eventDao: EventDao,
    private val seriesDao: SeriesDao
) : ViewModel() {

    var isCharacterFavorite = MutableLiveData<Boolean>()
    var isSeriesFavorite = MutableLiveData<Boolean>()
    var isEventFavorite = MutableLiveData<Boolean>()

    var characters = MutableLiveData<List<CharacterModel>>()
    var series = MutableLiveData<List<SeriesModel>>()
    var events = MutableLiveData<List<EventModel>>()

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

    fun onCharacterItemClick(item: CharacterModel) = viewModelScope.launch {
        detailsEventChannel.send(DetailsEvent.NavigateToCharacterDetails(item))
    }

    fun onToggleCharacterFavorite(item: CharacterModel) = viewModelScope.launch {
        if (isCharacterFavorite.value!!) characterDao.delete(item) else characterDao.insert(item)
        isCharacterFavorite.value = !isCharacterFavorite.value!!
    }

    fun onSeriesItemClick(item: SeriesModel) = viewModelScope.launch {
        detailsEventChannel.send(DetailsEvent.NavigateToSeriesDetails(item))
    }

    fun onToggleSeriesFavorite(item: SeriesModel) = viewModelScope.launch {
        if (isSeriesFavorite.value!!) seriesDao.delete(item) else seriesDao.insert(item)
        isSeriesFavorite.value = !isSeriesFavorite.value!!
    }

    fun onEventItemClick(item: EventModel) = viewModelScope.launch {
        detailsEventChannel.send(DetailsEvent.NavigateToEventDetails(item))
    }

    fun onToggleEventFavorite(item: EventModel) = viewModelScope.launch {
        if (isEventFavorite.value!!) eventDao.delete(item) else eventDao.insert(item)
        isEventFavorite.value = !isEventFavorite.value!!
    }

    sealed class DetailsEvent {
        data class NavigateToCharacterDetails(val item: CharacterModel) : DetailsEvent()
        data class NavigateToSeriesDetails(val item: SeriesModel) : DetailsEvent()
        data class NavigateToEventDetails(val item: EventModel) : DetailsEvent()
    }
}