package shvyn22.marvelapplication.ui.series

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import shvyn22.marvelapplication.R
import shvyn22.marvelapplication.data.model.SeriesModel
import shvyn22.marvelapplication.databinding.FragmentSeriesBinding
import shvyn22.marvelapplication.ui.adapters.PagingLoadStateAdapter
import shvyn22.marvelapplication.ui.adapters.SeriesAdapter

@AndroidEntryPoint
class SeriesFragment : Fragment(R.layout.fragment_series),
        SeriesPagingAdapter.OnItemClickListener, SeriesAdapter.OnItemClickListener {

    private val viewModel : SeriesViewModel by viewModels()

    private var _binding : FragmentSeriesBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            (activity as AppCompatActivity).supportActionBar!!.setBackgroundDrawable(
                    ColorDrawable(resources.getColor(R.color.yellow, activity?.theme)))
        }

        _binding = FragmentSeriesBinding.bind(view)

        val pagingAdapter = SeriesPagingAdapter(this)
        viewModel.pagingItems.observe(viewLifecycleOwner) {
            pagingAdapter.submitData(viewLifecycleOwner.lifecycle, it)
        }

        val seriesAdapter = SeriesAdapter(this)
        viewModel.items.observe(viewLifecycleOwner) {
            seriesAdapter.submitList(it)
        }

        binding.apply {
            rvSeries.apply {
                adapter = pagingAdapter.withLoadStateHeaderAndFooter(
                    header = PagingLoadStateAdapter { pagingAdapter.retry() },
                    footer = PagingLoadStateAdapter { pagingAdapter.retry() }
                )
                setHasFixedSize(true)
            }

            rvFavoriteSeries.apply {
                adapter = seriesAdapter
                setHasFixedSize(true)
            }

            viewModel.isShowingFavorite.observe(viewLifecycleOwner) {
                if (it) {
                    rvSeries.visibility = View.GONE
                    rvFavoriteSeries.visibility = View.VISIBLE
                } else {
                    rvFavoriteSeries.visibility = View.GONE
                    rvSeries.visibility = View.VISIBLE
                }
            }

            pagingAdapter.addLoadStateListener { loadState ->
                if (!rvFavoriteSeries.isVisible) {
                    progressBar.isVisible = loadState.source.refresh is LoadState.Loading
                    rvSeries.isVisible = loadState.source.refresh is LoadState.NotLoading
                    tvError.isVisible = loadState.source.refresh is LoadState.Error
                    btnRetry.isVisible = loadState.source.refresh is LoadState.Error

                    if (loadState.source.refresh is LoadState.NotLoading &&
                            loadState.append.endOfPaginationReached && pagingAdapter.itemCount < 1) {
                        rvSeries.isVisible = false
                        tvEmpty.isVisible = true
                    } else {
                        tvEmpty.isVisible = false
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.seriesEvent.collect { event ->
                when (event) {
                    is SeriesViewModel.SeriesEvent.NavigateToDetails -> {
                        val action = SeriesFragmentDirections
                                .actionSeriesFragmentToDetailsSeriesFragment(event.item)
                        findNavController().navigate(action)
                    }
                }
            }
        }

        setHasOptionsMenu(true)
    }

    override fun onResume() {
        super.onResume()

        val navBar = activity?.findViewById<BottomNavigationView>(R.id.bottom_nav_view)
        navBar?.visibility = View.VISIBLE
    }

    override fun onItemClick(item: SeriesModel) {
        viewModel.onItemClick(item)
    }

    override fun onSeriesItemClick(item: SeriesModel) {
        viewModel.onItemClick(item)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.menu_main, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView

        searchItem.setOnActionExpandListener(object: MenuItem.OnActionExpandListener {
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
            AppCompatDelegate.setDefaultNightMode(it.nightMode)
            menu.findItem(R.id.action_night_mode).setIcon(
                    if (it.nightMode == AppCompatDelegate.MODE_NIGHT_YES) R.drawable.ic_light_mode
                    else R.drawable.ic_night_mode)
        }

        viewModel.isShowingFavorite.observe(viewLifecycleOwner) {
            menu.findItem(R.id.action_favorite).setIcon(
                    if (it) R.drawable.ic_browse else R.drawable.ic_favorite
            )
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_favorite -> {
                viewModel.onToggleMenuButton()
                return true
            }
            R.id.action_night_mode -> {
                viewModel.onToggleModeIcon()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}