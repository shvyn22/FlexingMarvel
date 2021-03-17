package shvyn22.marvelapplication

import android.app.Application
import androidx.recyclerview.widget.DiffUtil
import dagger.hilt.android.HiltAndroidApp
import shvyn22.marvelapplication.data.entity.Event
import shvyn22.marvelapplication.data.entity.MarvelCharacter
import shvyn22.marvelapplication.data.entity.Series

@HiltAndroidApp
class MarvelApplication : Application() {
    companion object {
        val CHARACTER_COMPARATOR = object : DiffUtil.ItemCallback<MarvelCharacter>() {
            override fun areItemsTheSame(oldItem: MarvelCharacter, newItem: MarvelCharacter) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: MarvelCharacter, newItem: MarvelCharacter) =
                oldItem == newItem
        }

        val EVENTS_COMPARATOR = object : DiffUtil.ItemCallback<Event>() {
            override fun areItemsTheSame(oldItem: Event, newItem: Event) = oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Event, newItem: Event) = oldItem == newItem
        }

        val SERIES_COMPARATOR = object : DiffUtil.ItemCallback<Series>() {
            override fun areItemsTheSame(oldItem: Series, newItem: Series) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Series, newItem: Series) = oldItem == newItem
        }
    }
}