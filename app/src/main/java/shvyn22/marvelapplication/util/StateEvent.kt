package shvyn22.marvelapplication.util

import shvyn22.marvelapplication.data.local.model.CharacterModel
import shvyn22.marvelapplication.data.local.model.EventModel
import shvyn22.marvelapplication.data.local.model.SeriesModel

sealed class MainStateEvent<T> {
    data class NavigateToDetails<T>(val item: T) : MainStateEvent<T>()
}

sealed class DetailsStateEvent {
    data class NavigateToCharacterDetails(val item: CharacterModel) : DetailsStateEvent()
    data class NavigateToEventDetails(val item: EventModel) : DetailsStateEvent()
    data class NavigateToSeriesDetails(val item: SeriesModel) : DetailsStateEvent()
}
