package shvyn22.marvelapplication.ui.adapters.character

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import shvyn22.marvelapplication.MarvelApplication.Companion.CHARACTER_COMPARATOR
import shvyn22.marvelapplication.data.local.model.CharacterModel
import shvyn22.marvelapplication.databinding.ItemDetailsBinding
import shvyn22.marvelapplication.util.defaultRequests

class CharactersAdapter(
    private val onClick: (CharacterModel) -> Unit
) : ListAdapter<CharacterModel, CharactersAdapter.CharacterViewHolder>(CHARACTER_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        return CharacterViewHolder(
            ItemDetailsBinding.inflate(
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