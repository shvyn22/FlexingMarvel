package shvyn22.marvelapplication.ui.characters

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
import shvyn22.marvelapplication.data.local.model.CharacterModel
import shvyn22.marvelapplication.databinding.FragmentCharactersBinding
import shvyn22.marvelapplication.ui.adapters.PagingLoadStateAdapter
import shvyn22.marvelapplication.ui.adapters.character.CharactersAdapter
import shvyn22.marvelapplication.ui.adapters.character.CharactersPagingAdapter
import shvyn22.marvelapplication.util.MainStateEvent
import shvyn22.marvelapplication.util.collectOnLifecycle
import shvyn22.marvelapplication.util.recolorAppBar
import shvyn22.marvelapplication.util.showBottomBar

@AndroidEntryPoint
class CharactersFragment : Fragment(R.layout.fragment_characters) {

    private val viewModel: CharactersViewModel by viewModels()

    private var _binding: FragmentCharactersBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentCharactersBinding.bind(view)

        activity?.recolorAppBar(R.color.blue)

        val pagingAdapter = CharactersPagingAdapter { viewModel.onItemClick(it) }
        viewModel.pagingItems.observe(viewLifecycleOwner) {
            pagingAdapter.submitData(viewLifecycleOwner.lifecycle, it)
        }

        val characterAdapter = CharactersAdapter { viewModel.onItemClick(it) }
        viewModel.items.observe(viewLifecycleOwner) {
            characterAdapter.submitList(it)
        }

        binding.apply {
            rvCharacters.adapter = pagingAdapter.withLoadStateHeaderAndFooter(
                header = PagingLoadStateAdapter { pagingAdapter.retry() },
                footer = PagingLoadStateAdapter { pagingAdapter.retry() }
            )

            rvFavoriteCharacters.adapter = characterAdapter

            viewModel.isShowingFavorite.observe(viewLifecycleOwner) {
                rvFavoriteCharacters.isVisible = it
                rvCharacters.isVisible = !it
            }

            pagingAdapter.addLoadStateListener { loadState ->
                if (!rvFavoriteCharacters.isVisible) {
                    progressBar.isVisible = loadState.source.refresh is LoadState.Loading
                    rvCharacters.isVisible = loadState.source.refresh is LoadState.NotLoading
                    tvError.isVisible = loadState.source.refresh is LoadState.Error
                    btnRetry.isVisible = loadState.source.refresh is LoadState.Error

                    if (loadState.source.refresh is LoadState.NotLoading &&
                        loadState.append.endOfPaginationReached && pagingAdapter.itemCount < 1
                    ) {
                        rvCharacters.isVisible = false
                        tvEmpty.isVisible = true
                    } else {
                        tvEmpty.isVisible = false
                    }
                }
            }
        }

        viewModel.characterEvent.collectOnLifecycle(viewLifecycleOwner) { event ->
            if (event is MainStateEvent.NavigateToDetails<CharacterModel>) {
                val action = CharactersFragmentDirections
                    .actionCharactersFragmentToDetailsCharacterFragment(event.item)
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