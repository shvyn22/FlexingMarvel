package shvyn22.marvelapplication.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import shvyn22.marvelapplication.databinding.FooterLoadStateBinding

class PagingLoadStateAdapter(
    private val retry: () -> Unit
) : LoadStateAdapter<PagingLoadStateAdapter.PagingViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): PagingViewHolder {
        return PagingViewHolder(
            FooterLoadStateBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: PagingViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    inner class PagingViewHolder(
        private val binding: FooterLoadStateBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.btnRetry.setOnClickListener { retry.invoke() }
        }

        fun bind(loadState: LoadState) {
            binding.apply {
                progressBar.isVisible = loadState is LoadState.Loading
                btnRetry.isVisible = loadState !is LoadState.Loading
                tvError.isVisible = loadState !is LoadState.Loading
            }
        }
    }
}