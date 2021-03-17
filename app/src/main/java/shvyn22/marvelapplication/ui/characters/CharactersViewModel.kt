package shvyn22.marvelapplication.ui.characters

import androidx.appcompat.app.AppCompatDelegate
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import androidx.paging.cachedIn
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import shvyn22.marvelapplication.data.repository.RemoteRepository
import shvyn22.marvelapplication.data.PreferencesManager
import shvyn22.marvelapplication.data.local.dao.CharacterDao
import shvyn22.marvelapplication.data.model.CharacterModel

class CharactersViewModel @ViewModelInject constructor(
    private val repository: RemoteRepository,
    private val characterDao: CharacterDao,
    private val prefsManager: PreferencesManager,
    @Assisted val state: SavedStateHandle
): ViewModel() {

    val isShowingFavorite = MutableLiveData<Boolean>()

    val nightMode = prefsManager.preferencesFlow.asLiveData()
    private val searchQuery = state.getLiveData("searchCharacter", "")

    private val characterEventChannel = Channel<CharacterEvent>()
    val characterEvent = characterEventChannel.receiveAsFlow()

    val pagingItems = searchQuery.switchMap { query ->
        repository.getCharactersResults(query).cachedIn(viewModelScope)
    }

    val items = searchQuery.switchMap { query ->
        characterDao.getAll(query).asLiveData()
    }

    fun searchItems(query: String) {
        searchQuery.value = query
    }

    fun onItemClick(item: CharacterModel) = viewModelScope.launch {
        characterEventChannel.send(CharacterEvent.NavigateToDetails(item))
    }

    fun onToggleMenuButton() {
        isShowingFavorite.value = !isShowingFavorite.value!!
    }

    fun onToggleModeIcon() = viewModelScope.launch {
        prefsManager.updateNightMode(if (nightMode.value?.nightMode == AppCompatDelegate.MODE_NIGHT_YES)
            AppCompatDelegate.MODE_NIGHT_NO else AppCompatDelegate.MODE_NIGHT_YES)
    }

    sealed class CharacterEvent {
        data class NavigateToDetails(val item: CharacterModel) : CharacterEvent()
    }
}