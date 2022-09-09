package shvyn22.flexingmarvel.presentation.characters

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import shvyn22.flexingmarvel.data.local.model.CharacterModel
import shvyn22.flexingmarvel.repository.local.LocalRepository
import shvyn22.flexingmarvel.repository.remote.RemoteRepository
import shvyn22.flexingmarvel.util.MainStateEvent
import shvyn22.flexingmarvel.util.SEARCH_CHARACTERS_KEY
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class CharactersViewModel @Inject constructor(
    private val remoteRepo: RemoteRepository<CharacterModel>,
    private val localRepo: LocalRepository<CharacterModel>,
    private val savedState: SavedStateHandle
) : ViewModel() {

    private val searchQuery = savedState.getStateFlow(SEARCH_CHARACTERS_KEY, "")

    private val _isShowingFavorite = MutableStateFlow(false)
    val isShowingFavorite: StateFlow<Boolean> get() = _isShowingFavorite

    private val characterEventChannel = Channel<MainStateEvent<CharacterModel>>()
    val characterEvent = characterEventChannel.receiveAsFlow()

    val pagingItems = searchQuery.flatMapConcat { query ->
        remoteRepo.getItems(query).cachedIn(viewModelScope)
    }.stateIn(viewModelScope, SharingStarted.Lazily, PagingData.empty())

    val items = searchQuery.flatMapConcat { query ->
        localRepo.getItems(query)
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun searchItems(query: String) {
        viewModelScope.launch {
            savedState[SEARCH_CHARACTERS_KEY] = query
        }
    }

    fun onItemClick(item: CharacterModel) {
        viewModelScope.launch {
            characterEventChannel.send(MainStateEvent.NavigateToDetails(item))
        }
    }

    fun onToggleFavoriteButton() {
        viewModelScope.launch {
            _isShowingFavorite.value = !_isShowingFavorite.value
        }
    }
}