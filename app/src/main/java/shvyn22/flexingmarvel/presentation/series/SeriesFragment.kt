package shvyn22.flexingmarvel.presentation.series

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import dagger.hilt.android.AndroidEntryPoint
import shvyn22.flexingmarvel.R
import shvyn22.flexingmarvel.data.local.model.SeriesModel
import shvyn22.flexingmarvel.databinding.FragmentSeriesBinding
import shvyn22.flexingmarvel.presentation.adapters.PagingLoadStateAdapter
import shvyn22.flexingmarvel.presentation.adapters.series.SeriesAdapter
import shvyn22.flexingmarvel.presentation.adapters.series.SeriesPagingAdapter
import shvyn22.flexingmarvel.util.MainStateEvent
import shvyn22.flexingmarvel.util.collectOnLifecycle
import shvyn22.flexingmarvel.util.recolorAppBar
import shvyn22.flexingmarvel.util.showBottomBar

@AndroidEntryPoint
class SeriesFragment : Fragment(R.layout.fragment_series) {

    private val viewModel: SeriesViewModel by viewModels()

    private val pagingAdapter = SeriesPagingAdapter { viewModel.onItemClick(it) }
    private val seriesAdapter = SeriesAdapter { viewModel.onItemClick(it) }

    private var _binding: FragmentSeriesBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentSeriesBinding.bind(view)

        initUI()
        subscribeObservers()
        configureMenu()
    }

    private fun initUI() {
        activity?.recolorAppBar(R.color.yellow)

        binding.apply {
            rvSeries.adapter = pagingAdapter.withLoadStateHeaderAndFooter(
                header = PagingLoadStateAdapter(pagingAdapter::retry),
                footer = PagingLoadStateAdapter(pagingAdapter::retry)
            )

            rvFavoriteSeries.adapter = seriesAdapter

            pagingAdapter.addLoadStateListener { loadState ->
                if (!rvFavoriteSeries.isVisible) {
                    panelLoadState.apply {
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
        }
    }

    private fun subscribeObservers() {
        binding.apply {
            viewModel.isShowingFavorite.collectOnLifecycle(viewLifecycleOwner) {
                rvFavoriteSeries.isVisible = it
                rvSeries.isVisible = !it
            }
        }

        viewModel.pagingItems.collectOnLifecycle(viewLifecycleOwner) {
            pagingAdapter.submitData(viewLifecycleOwner.lifecycle, it)
        }

        viewModel.items.collectOnLifecycle(viewLifecycleOwner) {
            seriesAdapter.submitList(it)
        }

        viewModel.seriesEvent.collectOnLifecycle(viewLifecycleOwner) { event ->
            if (event is MainStateEvent.NavigateToDetails<SeriesModel>) {
                val action = SeriesFragmentDirections
                    .actionSeriesFragmentToDetailsSeriesFragment(event.item)
                findNavController().navigate(action)
            }
        }
    }

    private fun configureMenu() {
        activity?.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {}

            override fun onPrepareMenu(menu: Menu) {
                super.onPrepareMenu(menu)

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

                viewModel.isShowingFavorite.collectOnLifecycle(viewLifecycleOwner) {
                    menu.findItem(R.id.action_favorite).setIcon(
                        if (it) R.drawable.ic_browse else R.drawable.ic_favorite
                    )
                }
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_favorite -> {
                        viewModel.onToggleFavoriteButton()
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun onResume() {
        super.onResume()
        activity?.showBottomBar()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}