package shvyn22.marvelapplication.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import shvyn22.marvelapplication.MarvelApplication.Companion.SERIES_COMPARATOR
import shvyn22.marvelapplication.R
import shvyn22.marvelapplication.data.entity.Series
import shvyn22.marvelapplication.databinding.ItemDetailsBinding

class SeriesAdapter(private val listener: OnItemClickListener)
    : ListAdapter<Series, SeriesAdapter.SeriesViewHolder>(SERIES_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeriesViewHolder {
        return SeriesViewHolder(
            ItemDetailsBinding.inflate(
            LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: SeriesViewHolder, position: Int) {
        val currentItem = getItem(position)

        if (currentItem != null) holder.bind(currentItem)
    }

    inner class SeriesViewHolder(private val binding: ItemDetailsBinding)
        : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val currentItem = getItem(position)
                    if (currentItem != null) listener.onSeriesItemClick(currentItem)
                }
            }
        }

        fun bind(item : Series) {
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
        fun onSeriesItemClick(item: Series)
    }
}