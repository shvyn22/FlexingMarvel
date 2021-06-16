package shvyn22.marvelapplication.ui.adapters.series

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import shvyn22.marvelapplication.MarvelApplication.Companion.SERIES_COMPARATOR
import shvyn22.marvelapplication.data.local.model.SeriesModel
import shvyn22.marvelapplication.databinding.ItemMainBinding
import shvyn22.marvelapplication.util.defaultRequests

class SeriesPagingAdapter(
    private val onClick: (SeriesModel) -> Unit
) : PagingDataAdapter<SeriesModel, SeriesPagingAdapter.SeriesViewHolder>(SERIES_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeriesViewHolder {
        return SeriesViewHolder(
            ItemMainBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SeriesViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    inner class SeriesViewHolder(
        private val binding: ItemMainBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    getItem(position)?.let { onClick(it) }
                }
            }
        }

        fun bind(item: SeriesModel) {
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