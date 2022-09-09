package shvyn22.flexingmarvel.presentation.events

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
import shvyn22.flexingmarvel.data.local.model.EventModel
import shvyn22.flexingmarvel.databinding.FragmentEventsBinding
import shvyn22.flexingmarvel.presentation.adapters.PagingLoadStateAdapter
import shvyn22.flexingmarvel.presentation.adapters.event.EventAdapter
import shvyn22.flexingmarvel.presentation.adapters.event.EventPagingAdapter
import shvyn22.flexingmarvel.util.MainStateEvent
import shvyn22.flexingmarvel.util.collectOnLifecycle
import shvyn22.flexingmarvel.util.recolorAppBar
import shvyn22.flexingmarvel.util.showBottomBar

@AndroidEntryPoint
class EventsFragment : Fragment(R.layout.fragment_events) {

    private val viewModel: EventsViewModel by viewModels()

    private val pagingAdapter = EventPagingAdapter { viewModel.onItemClick(it) }
    private val eventAdapter = EventAdapter { viewModel.onItemClick(it) }

    private var _binding: FragmentEventsBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentEventsBinding.bind(view)

        initUI()
        subscribeObservers()
        configureMenu()
    }

    private fun initUI() {
        activity?.recolorAppBar(R.color.green)

        binding.apply {
            rvEvents.adapter = pagingAdapter.withLoadStateHeaderAndFooter(
                header = PagingLoadStateAdapter(pagingAdapter::retry),
                footer = PagingLoadStateAdapter(pagingAdapter::retry)
            )

            rvFavoriteEvents.adapter = eventAdapter

            pagingAdapter.addLoadStateListener { loadState ->
                if (!rvFavoriteEvents.isVisible) {
                    panelLoadState.apply {
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
        }
    }

    private fun subscribeObservers() {
        binding.apply {
            viewModel.isShowingFavorite.collectOnLifecycle(viewLifecycleOwner) {
                rvFavoriteEvents.isVisible = it
                rvEvents.isVisible = !it
            }
        }

        viewModel.pagingItems.collectOnLifecycle(viewLifecycleOwner) {
            pagingAdapter.submitData(viewLifecycleOwner.lifecycle, it)
        }

        viewModel.items.collectOnLifecycle(viewLifecycleOwner) {
            eventAdapter.submitList(it)
        }

        viewModel.event.collectOnLifecycle(viewLifecycleOwner) { event ->
            if (event is MainStateEvent.NavigateToDetails<EventModel>) {
                val action = EventsFragmentDirections
                    .actionEventsFragmentToDetailsEventFragment(event.item)
                findNavController().navigate(action)
            }
        }
    }

    private fun configureMenu() {
        activity?.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) = Unit

            override fun onPrepareMenu(menu: Menu) {
                super.onPrepareMenu(menu)

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