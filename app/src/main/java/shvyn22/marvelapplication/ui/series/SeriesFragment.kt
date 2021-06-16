package shvyn22.marvelapplication.ui.series

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import dagger.hilt.android.AndroidEntryPoint
import shvyn22.marvelapplication.R
import shvyn22.marvelapplication.data.local.model.SeriesModel
import shvyn22.marvelapplication.databinding.FragmentSeriesBinding
import shvyn22.marvelapplication.ui.adapters.PagingLoadStateAdapter
import shvyn22.marvelapplication.ui.adapters.series.SeriesAdapter
import shvyn22.marvelapplication.ui.adapters.series.SeriesPagingAdapter
import shvyn22.marvelapplication.util.MainStateEvent
import shvyn22.marvelapplication.util.collectOnLifecycle
import shvyn22.marvelapplication.util.recolorAppBar
import shvyn22.marvelapplication.util.showBottomBar

@AndroidEntryPoint
class SeriesFragment : Fragment(R.layout.fragment_series) {

    private val viewModel: SeriesViewModel by viewModels()

    private var _binding: FragmentSeriesBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentSeriesBinding.bind(view)

        activity?.recolorAppBar(R.color.yellow)

        val pagingAdapter = SeriesPagingAdapter { viewModel.onItemClick(it) }
        viewModel.pagingItems.observe(viewLifecycleOwner) {
            pagingAdapter.submitData(viewLifecycleOwner.lifecycle, it)
        }

        val seriesAdapter = SeriesAdapter { viewModel.onItemClick(it) }
        viewModel.items.observe(viewLifecycleOwner) {
            seriesAdapter.submitList(it)
        }

        binding.apply {
            rvSeries.adapter = pagingAdapter.withLoadStateHeaderAndFooter(
                header = PagingLoadStateAdapter { pagingAdapter.retry() },
                footer = PagingLoadStateAdapter { pagingAdapter.retry() }
            )

            rvFavoriteSeries.adapter = seriesAdapter

            viewModel.isShowingFavorite.observe(viewLifecycleOwner) {
                rvFavoriteSeries.isVisible = it
                rvSeries.isVisible = !it
            }

            pagingAdapter.addLoadStateListener { loadState ->
                if (!rvFavoriteSeries.isVisible) {
                    progressBar.isVisible = loadState.source.refresh is LoadState.Loading
                    rvSeries.isVisible = loadState.source.refresh is LoadState.NotLoading
                    tvError.isVisible = loadState.source.refresh is LoadState.Error
                    btnRetry.isVisible = loadState.source.refresh is LoadState.Error

                    if (loadState.source.refresh is LoadState.NotLoading &&
                        loadState.append.endOfPaginationReached && pagingAdapter.itemCount < 1
                    ) {
                        rvSeries.isVisible = false
                        tvEmpty.isVisible = true
                    } else {
                        tvEmpty.isVisible = false
                    }
                }
            }
        }

        viewModel.seriesEvent.collectOnLifecycle(viewLifecycleOwner) { event ->
            if (event is MainStateEvent.NavigateToDetails<SeriesModel>) {
                val action = SeriesFragmentDirections
                    .actionSeriesFragmentToDetailsSeriesFragment(event.item)
                findNavController().navigate(action)
            }
        }

        setHasOptionsMenu(true)
    }

    override fun onResume() {
        super.onResume()

        activity?.showBottomBar()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.menu_main, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView

        searchItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(p0: MenuItem?) = true

            override fun onMenuItemActionCollapse(p0: MenuItem?): Boolean {
                binding.rvSeries.scrollToPosition(0)
                viewModel.searchItems("")
                return true
            }
        })

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    binding.rvSeries.scrollToPosition(0)
                    searchView.clearFocus()
                    viewModel.searchItems(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?) = true
        })

        viewModel.nightMode.observe(viewLifecycleOwner) {
            AppCompatDelegate.setDefaultNightMode(it)
            menu.findItem(R.id.action_night_mode).setIcon(
                if (it == AppCompatDelegate.MODE_NIGHT_YES) R.drawable.ic_light_mode
                else R.drawable.ic_night_mode
            )
        }

        viewModel.isShowingFavorite.observe(viewLifecycleOwner) {
            menu.findItem(R.id.action_favorite).setIcon(
                if (it) R.drawable.ic_browse else R.drawable.ic_favorite
            )
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_favorite -> viewModel.onToggleFavoriteButton()
            R.id.action_night_mode -> viewModel.onToggleModeIcon()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}