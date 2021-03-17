package shvyn22.marvelapplication.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import shvyn22.marvelapplication.MarvelApplication.Companion.EVENTS_COMPARATOR
import shvyn22.marvelapplication.R
import shvyn22.marvelapplication.data.entity.Event
import shvyn22.marvelapplication.databinding.ItemDetailsBinding

class EventAdapter(private val listener: OnItemClickListener)
    : ListAdapter<Event, EventAdapter.EventViewHolder>(EVENTS_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        return EventViewHolder(
            ItemDetailsBinding.inflate(
            LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val currentItem = getItem(position)

        if (currentItem != null) holder.bind(currentItem)
    }

    inner class EventViewHolder(private val binding: ItemDetailsBinding)
        : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val currentItem = getItem(position)
                    if (currentItem != null) listener.onEventItemClick(currentItem)
                }
            }
        }

        fun bind(item : Event) {
            binding.apply {
                Glide.with(itemView)
                    .load(item.thumbnail.getFullUrl())
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .error(R.drawable.ic_error)
                    .into(ivItem)

                tvTitle.text = item.title
            }
        }
    }

    interface OnItemClickListener {
        fun onEventItemClick(item: Event)
    }
}