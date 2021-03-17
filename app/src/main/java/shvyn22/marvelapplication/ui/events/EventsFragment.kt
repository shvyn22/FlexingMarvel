package shvyn22.marvelapplication.ui.events

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
import shvyn22.marvelapplication.data.entity.Event
import shvyn22.marvelapplication.databinding.FragmentEventsBinding
import shvyn22.marvelapplication.ui.adapters.EventAdapter
import shvyn22.marvelapplication.ui.adapters.PagingLoadStateAdapter

@AndroidEntryPoint
class EventsFragment : Fragment(R.layout.fragment_events),
        EventsPagingAdapter.OnItemClickListener, EventAdapter.OnItemClickListener {

    private val viewModel: EventsViewModel by viewModels()

    private var _binding : FragmentEventsBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            (activity as AppCompatActivity).supportActionBar!!.setBackgroundDrawable(
                    ColorDrawable(resources.getColor(R.color.green, activity?.theme)))
        }

        _binding = FragmentEventsBinding.bind(view)
        viewModel.isShowingFavorite.value = false

        val pagingAdapter = EventsPagingAdapter(this)
        viewModel.pagingItems.observe(viewLifecycleOwner) {
            pagingAdapter.submitData(viewLifecycleOwner.lifecycle, it)
        }

        val eventAdapter = EventAdapter(this)
        viewModel.items.observe(viewLifecycleOwner) {
            eventAdapter.submitList(it)
        }

        binding.apply {
            rvEvents.apply {
                adapter = pagingAdapter.withLoadStateHeaderAndFooter(
                    header = PagingLoadStateAdapter { pagingAdapter.retry() },
                    footer = PagingLoadStateAdapter { pagingAdapter.retry() }
                )
                setHasFixedSize(true)
            }

            rvFavoriteEvents.apply {
                adapter = eventAdapter
                setHasFixedSize(true)
            }

            viewModel.isShowingFavorite.observe(viewLifecycleOwner) {
                if (it) {
                    rvEvents.visibility = View.GONE
                    rvFavoriteEvents.visibility = View.VISIBLE
                } else {
                    rvFavoriteEvents.visibility = View.GONE
                    rvEvents.visibility = View.VISIBLE
                }
            }

            viewModel.nightMode.observe(viewLifecycleOwner) {
                AppCompatDelegate.setDefaultNightMode(it.nightMode)
            }

            pagingAdapter.addLoadStateListener { loadState ->
                if (!rvFavoriteEvents.isVisible) {
                    progressBar.isVisible = loadState.source.refresh is LoadState.Loading
                    rvEvents.isVisible = loadState.source.refresh is LoadState.NotLoading
                    tvError.isVisible = loadState.source.refresh is LoadState.Error
                    btnRetry.isVisible = loadState.source.refresh is LoadState.Error

                    if (loadState.source.refresh is LoadState.NotLoading &&
                            loadState.append.endOfPaginationReached && pagingAdapter.itemCount < 1) {
                        rvEvents.isVisible = false
                        tvEmpty.isVisible = true
                    } else {
                        tvEmpty.isVisible = false
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.event.collect { event ->
                when (event) {
                    is EventsViewModel.EventFragmentEvent.NavigateToDetails -> {
                        val action = EventsFragmentDirections
                                .actionEventsFragmentToDetailsEventFragment(event.item)
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

    override fun onItemClick(item: Event) {
        viewModel.onItemClick(item)
    }

    override fun onEventItemClick(item: Event) {
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

        val nightModeItem = menu.findItem(R.id.action_night_mode)
        nightModeItem.setIcon(if (viewModel.nightMode.value?.nightMode == AppCompatDelegate.MODE_NIGHT_YES)
            R.drawable.ic_light_mode else R.drawable.ic_night_mode)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_favorite -> {
                item.apply {
                    if (viewModel.isShowingFavorite.value!!) setIcon(R.drawable.ic_favorite)
                    else setIcon(R.drawable.ic_browse)
                }
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