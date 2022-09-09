package shvyn22.flexingmarvel.presentation.adapters.character

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import shvyn22.flexingmarvel.data.local.model.CharacterModel
import shvyn22.flexingmarvel.databinding.ItemDetailsBinding
import shvyn22.flexingmarvel.presentation.adapters.CHARACTER_COMPARATOR
import shvyn22.flexingmarvel.util.defaultRequests

class CharacterAdapter(
    private val onClick: (CharacterModel) -> Unit
) : ListAdapter<CharacterModel, CharacterAdapter.CharacterViewHolder>(CHARACTER_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        return CharacterViewHolder(
            ItemDetailsBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    inner class CharacterViewHolder(
        private val binding: ItemDetailsBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CharacterModel) {
            binding.apply {
                root.setOnClickListener { onClick(item) }

                Glide.with(itemView)
                    .load(item.thumbnail.getFullUrl())
                    .defaultRequests()
                    .into(ivItem)

                tvTitle.text = item.name
            }
        }
    }
}