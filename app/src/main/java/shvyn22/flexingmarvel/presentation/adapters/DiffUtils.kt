package shvyn22.flexingmarvel.presentation.adapters

import androidx.recyclerview.widget.DiffUtil
import shvyn22.flexingmarvel.data.local.model.CharacterModel
import shvyn22.flexingmarvel.data.local.model.EventModel
import shvyn22.flexingmarvel.data.local.model.SeriesModel

val CHARACTER_COMPARATOR = object : DiffUtil.ItemCallback<CharacterModel>() {
    override fun areItemsTheSame(oldItem: CharacterModel, newItem: CharacterModel) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: CharacterModel, newItem: CharacterModel) =
        oldItem == newItem
}

val EVENT_COMPARATOR = object : DiffUtil.ItemCallback<EventModel>() {
    override fun areItemsTheSame(oldItem: EventModel, newItem: EventModel) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: EventModel, newItem: EventModel) =
        oldItem == newItem
}

val SERIES_COMPARATOR = object : DiffUtil.ItemCallback<SeriesModel>() {
    override fun areItemsTheSame(oldItem: SeriesModel, newItem: SeriesModel) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: SeriesModel, newItem: SeriesModel) =
        oldItem == newItem
}