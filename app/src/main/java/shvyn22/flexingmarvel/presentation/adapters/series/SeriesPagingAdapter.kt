package shvyn22.flexingmarvel.presentation.adapters.series

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import shvyn22.flexingmarvel.data.local.model.SeriesModel
import shvyn22.flexingmarvel.databinding.ItemMainBinding
import shvyn22.flexingmarvel.presentation.adapters.SERIES_COMPARATOR
import shvyn22.flexingmarvel.util.defaultRequests

class SeriesPagingAdapter(
    private val onClick: (SeriesModel) -> Unit
) : PagingDataAdapter<SeriesModel, SeriesPagingAdapter.SeriesViewHolder>(SERIES_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeriesViewHolder {
        return SeriesViewHolder(
            ItemMainBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: SeriesViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    inner class SeriesViewHolder(
        private val binding: ItemMainBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: SeriesModel) {
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