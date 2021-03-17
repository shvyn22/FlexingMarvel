package shvyn22.marvelapplication.ui.characters

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
import shvyn22.marvelapplication.data.model.CharacterModel
import shvyn22.marvelapplication.databinding.FragmentCharactersBinding
import shvyn22.marvelapplication.ui.adapters.CharacterAdapter
import shvyn22.marvelapplication.ui.adapters.PagingLoadStateAdapter

@AndroidEntryPoint
class CharactersFragment : Fragment(R.layout.fragment_characters),
        CharactersPagingAdapter.OnItemClickListener, CharacterAdapter.OnItemClickListener {

    private val viewModel: CharactersViewModel by viewModels()

    private var _binding: FragmentCharactersBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentCharactersBinding.bind(view)
        viewModel.isShowingFavorite.value = false

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            (activity as AppCompatActivity).supportActionBar!!.setBackgroundDrawable(
                    ColorDrawable(resources.getColor(R.color.blue, activity?.theme)))
        }

        val pagingAdapter = CharactersPagingAdapter(this)
        viewModel.pagingItems.observe(viewLifecycleOwner) {
            pagingAdapter.submitData(viewLifecycleOwner.lifecycle, it)
        }

        val characterAdapter = CharacterAdapter(this)
        viewModel.items.observe(viewLifecycleOwner) {
            characterAdapter.submitList(it)
        }

        binding.apply {
            rvCharacters.apply {
                adapter = pagingAdapter.withLoadStateHeaderAndFooter(
                    header = PagingLoadStateAdapter { pagingAdapter.retry() },
                    footer = PagingLoadStateAdapter { pagingAdapter.retry() }
                )
                setHasFixedSize(true)
            }

            rvFavoriteCharacters.apply {
                adapter = characterAdapter
                setHasFixedSize(true)
            }

            viewModel.isShowingFavorite.observe(viewLifecycleOwner) {
                if (it) {
                    rvCharacters.visibility = View.GONE
                    rvFavoriteCharacters.visibility = View.VISIBLE
                } else {
                    rvFavoriteCharacters.visibility = View.GONE
                    rvCharacters.visibility = View.VISIBLE
                }
            }

            viewModel.nightMode.observe(viewLifecycleOwner) {
                AppCompatDelegate.setDefaultNightMode(it.nightMode)
            }

            pagingAdapter.addLoadStateListener { loadState ->
                if (!rvFavoriteCharacters.isVisible) {
                    progressBar.isVisible = loadState.source.refresh is LoadState.Loading
                    rvCharacters.isVisible = loadState.source.refresh is LoadState.NotLoading
                    tvError.isVisible = loadState.source.refresh is LoadState.Error
                    btnRetry.isVisible = loadState.source.refresh is LoadState.Error

                    if (loadState.source.refresh is LoadState.NotLoading &&
                            loadState.append.endOfPaginationReached && pagingAdapter.itemCount < 1) {
                        rvCharacters.isVisible = false
                        tvEmpty.isVisible = true
                    } else {
                        tvEmpty.isVisible = false
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.characterEvent.collect { event ->
                when (event) {
                    is CharactersViewModel.CharacterEvent.NavigateToDetails -> {
                        val action = CharactersFragmentDirections
                                .actionCharactersFragmentToDetailsCharacterFragment(event.item)
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

    override fun onItemClick(item: CharacterModel) {
        viewModel.onItemClick(item)
    }

    override fun onCharacterItemClick(item: CharacterModel) {
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
                binding.rvCharacters.scrollToPosition(0)
                viewModel.searchItems("")
                return true
            }
        })

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    binding.rvCharacters.scrollToPosition(0)
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