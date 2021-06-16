package shvyn22.marvelapplication.ui.events

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
import shvyn22.marvelapplication.data.local.model.EventModel
import shvyn22.marvelapplication.databinding.FragmentEventsBinding
import shvyn22.marvelapplication.ui.adapters.PagingLoadStateAdapter
import shvyn22.marvelapplication.ui.adapters.event.EventsAdapter
import shvyn22.marvelapplication.ui.adapters.event.EventsPagingAdapter
import shvyn22.marvelapplication.util.MainStateEvent
import shvyn22.marvelapplication.util.collectOnLifecycle
import shvyn22.marvelapplication.util.recolorAppBar
import shvyn22.marvelapplication.util.showBottomBar

@AndroidEntryPoint
class EventsFragment : Fragment(R.layout.fragment_events) {

    private val viewModel: EventsViewModel by viewModels()

    private var _binding: FragmentEventsBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentEventsBinding.bind(view)

        activity?.recolorAppBar(R.color.green)

        val pagingAdapter = EventsPagingAdapter { viewModel.onItemClick(it) }
        viewModel.pagingItems.observe(viewLifecycleOwner) {
            pagingAdapter.submitData(viewLifecycleOwner.lifecycle, it)
        }

        val eventAdapter = EventsAdapter { viewModel.onItemClick(it) }
        viewModel.items.observe(viewLifecycleOwner) {
            eventAdapter.submitList(it)
        }

        binding.apply {
            rvEvents.adapter = pagingAdapter.withLoadStateHeaderAndFooter(
                header = PagingLoadStateAdapter { pagingAdapter.retry() },
                footer = PagingLoadStateAdapter { pagingAdapter.retry() }
            )

            rvFavoriteEvents.adapter = eventAdapter

            viewModel.isShowingFavorite.observe(viewLifecycleOwner) {
                rvFavoriteEvents.isVisible = it
                rvEvents.isVisible = !it
            }

            pagingAdapter.addLoadStateListener { loadState ->
                if (!rvFavoriteEvents.isVisible) {
                    progressBar.isVisible = loadState.source.refresh is LoadState.Loading
                    rvEvents.isVisible = loadState.source.refresh is LoadState.NotLoading
                    tvError.isVisible = loadState.source.refresh is LoadState.Error
                    btnRetry.isVisible = loadState.source.refresh is LoadState.Error

                    if (loadState.source.refresh is LoadState.NotLoading &&
                        loadState.append.endOfPaginationReached && pagingAdapter.itemCount < 1
                    ) {
                        rvEvents.isVisible = false
                        tvEmpty.isVisible = true
                    } else {
                        tvEmpty.isVisible = false
                    }
                }
            }
        }

        viewModel.event.collectOnLifecycle(viewLifecycleOwner) { event ->
            if (event is MainStateEvent.NavigateToDetails<EventModel>) {
                val action = EventsFragmentDirections
                    .actionEventsFragmentToDetailsEventFragment(event.item)
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
                binding.rvEvents.scrollToPosition(0)
                viewModel.searchItems("")
                return true
            }
        })

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    binding.rvEvents.scrollToPosition(0)
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