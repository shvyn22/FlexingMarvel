package shvyn22.marvelapplication.ui.characters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import shvyn22.marvelapplication.MarvelApplication.Companion.CHARACTER_COMPARATOR
import shvyn22.marvelapplication.R
import shvyn22.marvelapplication.data.model.CharacterModel
import shvyn22.marvelapplication.databinding.ItemMainBinding

class CharactersPagingAdapter(private val listener: OnItemClickListener)
    : PagingDataAdapter<CharacterModel, CharactersPagingAdapter.CharacterViewHolder>(CHARACTER_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        return CharacterViewHolder(ItemMainBinding.inflate(
                LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        val currentItem = getItem(position)

        if (currentItem != null) holder.bind(currentItem)
    }

    inner class CharacterViewHolder(private val binding: ItemMainBinding)
        : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = getItem(position)
                    if (item != null) listener.onItemClick(item)
                }
            }
        }

        fun bind(item: CharacterModel) {
            binding.apply {
                Glide.with(itemView)
                    .load(item.thumbnail.getFullUrl())
                    .fitCenter()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .error(R.drawable.ic_error)
                    .into(ivItem)

                tvTitle.text = item.name
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(item: CharacterModel)
    }
}