package shvyn22.marvelapplication.ui.series

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import shvyn22.marvelapplication.MarvelApplication.Companion.SERIES_COMPARATOR
import shvyn22.marvelapplication.R
import shvyn22.marvelapplication.data.model.SeriesModel
import shvyn22.marvelapplication.databinding.ItemMainBinding

class SeriesPagingAdapter(private val listener: OnItemClickListener)
    : PagingDataAdapter<SeriesModel, SeriesPagingAdapter.SeriesViewHolder>(SERIES_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeriesViewHolder {
        return SeriesViewHolder(ItemMainBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: SeriesViewHolder, position: Int) {
        val currentItem = getItem(position)

        if (currentItem != null) holder.bind(currentItem)
    }

    inner class SeriesViewHolder(private val binding: ItemMainBinding)
        : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val currentItem = getItem(position)
                    if (currentItem != null) listener.onItemClick(currentItem)
                }
            }
        }

        fun bind(item : SeriesModel) {
            binding.apply {
                Glide.with(itemView)
                        .load(item.thumbnail.getFullUrl())
                        .fitCenter()
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .error(R.drawable.ic_error)
                        .into(ivItem)

                tvTitle.text = item.title
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(item: SeriesModel)
    }
}