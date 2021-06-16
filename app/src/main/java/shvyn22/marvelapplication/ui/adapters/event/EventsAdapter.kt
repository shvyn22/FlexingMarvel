package shvyn22.marvelapplication.ui.adapters.event

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import shvyn22.marvelapplication.MarvelApplication.Companion.EVENTS_COMPARATOR
import shvyn22.marvelapplication.data.local.model.EventModel
import shvyn22.marvelapplication.databinding.ItemDetailsBinding
import shvyn22.marvelapplication.util.defaultRequests

class EventsAdapter(
    private val onClick: (EventModel) -> Unit
) : ListAdapter<EventModel, EventsAdapter.EventViewHolder>(EVENTS_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        return EventViewHolder(
            ItemDetailsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    inner class EventViewHolder(
        private val binding: ItemDetailsBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    getItem(position)?.let { onClick(it) }
                }
            }
        }

        fun bind(item: EventModel) {
            binding.apply {
                Glide.with(itemView)
                    .load(item.thumbnail.getFullUrl())
                    .defaultRequests()
                    .into(ivItem)

                tvTitle.text = item.title
            }
        }
    }
}