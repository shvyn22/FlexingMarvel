package shvyn22.marvelapplication

import android.app.Application
import androidx.recyclerview.widget.DiffUtil
import dagger.hilt.android.HiltAndroidApp
import shvyn22.marvelapplication.data.model.EventModel
import shvyn22.marvelapplication.data.model.CharacterModel
import shvyn22.marvelapplication.data.model.SeriesModel

@HiltAndroidApp
class MarvelApplication : Application() {
    companion object {
        val CHARACTER_COMPARATOR = object : DiffUtil.ItemCallback<CharacterModel>() {
            override fun areItemsTheSame(oldItem: CharacterModel, newItem: CharacterModel) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: CharacterModel, newItem: CharacterModel) =
                oldItem == newItem
        }

        val EVENTS_COMPARATOR = object : DiffUtil.ItemCallback<EventModel>() {
            override fun areItemsTheSame(oldItem: EventModel, newItem: EventModel) = oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: EventModel, newItem: EventModel) = oldItem == newItem
        }

        val SERIES_COMPARATOR = object : DiffUtil.ItemCallback<SeriesModel>() {
            override fun areItemsTheSame(oldItem: SeriesModel, newItem: SeriesModel) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: SeriesModel, newItem: SeriesModel) = oldItem == newItem
        }
    }
}