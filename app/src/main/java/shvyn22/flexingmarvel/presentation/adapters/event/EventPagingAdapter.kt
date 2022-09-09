package shvyn22.flexingmarvel.presentation.adapters.event

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import shvyn22.flexingmarvel.data.local.model.EventModel
import shvyn22.flexingmarvel.databinding.ItemMainBinding
import shvyn22.flexingmarvel.presentation.adapters.EVENT_COMPARATOR
import shvyn22.flexingmarvel.util.defaultRequests

class EventPagingAdapter(
    private val onClick: (EventModel) -> Unit
) : PagingDataAdapter<EventModel, EventPagingAdapter.EventViewHolder>(EVENT_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        return EventViewHolder(
            ItemMainBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    inner class EventViewHolder(
        private val binding: ItemMainBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: EventModel) {
            binding.apply {
                root.setOnClickListener { onClick(item) }

                Glide.with(itemView)
                    .load(item.thumbnail.getFullUrl())
                    .defaultRequests()
                    .into(ivItem)

                tvTitle.text = item.title
            }
        }
    }
}