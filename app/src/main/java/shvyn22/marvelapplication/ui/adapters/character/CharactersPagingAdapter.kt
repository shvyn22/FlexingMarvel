package shvyn22.marvelapplication.ui.adapters.character

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import shvyn22.marvelapplication.MarvelApplication.Companion.CHARACTER_COMPARATOR
import shvyn22.marvelapplication.data.local.model.CharacterModel
import shvyn22.marvelapplication.databinding.ItemMainBinding
import shvyn22.marvelapplication.util.defaultRequests

class CharactersPagingAdapter(
    private val onClick: (CharacterModel) -> Unit
) : PagingDataAdapter<CharacterModel, CharactersPagingAdapter.CharacterViewHolder>(
    CHARACTER_COMPARATOR
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        return CharacterViewHolder(
            ItemMainBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    inner class CharacterViewHolder(
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

        fun bind(item: CharacterModel) {
            binding.apply {
                Glide.with(itemView)
                    .load(item.thumbnail.getFullUrl())
                    .defaultRequests()
                    .into(ivItem)

                tvTitle.text = item.name
            }
        }
    }
}