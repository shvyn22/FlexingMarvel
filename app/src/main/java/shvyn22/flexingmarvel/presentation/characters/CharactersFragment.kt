package shvyn22.flexingmarvel.presentation.characters

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
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import dagger.hilt.android.AndroidEntryPoint
import shvyn22.flexingmarvel.R
import shvyn22.flexingmarvel.data.local.model.CharacterModel
import shvyn22.flexingmarvel.databinding.FragmentCharactersBinding
import shvyn22.flexingmarvel.presentation.adapters.PagingLoadStateAdapter
import shvyn22.flexingmarvel.presentation.adapters.character.CharacterPagingAdapter
import shvyn22.flexingmarvel.util.MainStateEvent
import shvyn22.flexingmarvel.util.collectOnLifecycle
import shvyn22.flexingmarvel.util.recolorAppBar
import shvyn22.flexingmarvel.util.showBottomBar

@AndroidEntryPoint
class CharactersFragment : Fragment(R.layout.fragment_characters) {

    private val viewModel: CharactersViewModel by viewModels()

    private val characterAdapter = CharacterPagingAdapter { viewModel.onItemClick(it) }
    private val favoriteCharacterAdapter = CharacterPagingAdapter { viewModel.onItemClick(it) }

    private var _binding: FragmentCharactersBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentCharactersBinding.bind(view)

        initUI()
        subscribeObservers()
        configureMenu()
    }

    private fun initUI() {
        activity?.recolorAppBar(R.color.blue)

        binding.apply {
            rvCharacters.adapter = characterAdapter.withLoadStateHeaderAndFooter(
                header = PagingLoadStateAdapter(characterAdapter::retry),
                footer = PagingLoadStateAdapter(characterAdapter::retry)
            )

            rvFavoriteCharacters.adapter = favoriteCharacterAdapter.withLoadStateHeaderAndFooter(
                header = PagingLoadStateAdapter(characterAdapter::retry),
                footer = PagingLoadStateAdapter(characterAdapter::retry)
            )

            val loadStateListener: (CombinedLoadStates) -> Unit = { loadState ->
                if (!rvFavoriteCharacters.isVisible) {
                    panelLoadState.apply {
                        pbLoading.isVisible = loadState.source.refresh is LoadState.Loading
                        rvCharacters.isVisible = loadState.source.refresh is LoadState.NotLoading
                        tvError.isVisible = loadState.source.refresh is LoadState.Error
                        btnRetry.isVisible = loadState.source.refresh is LoadState.Error

                        if (loadState.source.refresh is LoadState.NotLoading &&
                            loadState.append.endOfPaginationReached && characterAdapter.itemCount < 1
                        ) {
                            rvCharacters.isVisible = false
                            tvEmpty.isVisible = true
                        } else {
                            tvEmpty.isVisible = false
                        }
                    }
                }
            }

            characterAdapter.addLoadStateListener(loadStateListener)
            favoriteCharacterAdapter.addLoadStateListener(loadStateListener)
        }
    }

    private fun subscribeObservers() {
        binding.apply {
            viewModel.isShowingFavorite.collectOnLifecycle(viewLifecycleOwner) {
                rvFavoriteCharacters.isVisible = it
                rvCharacters.isVisible = !it
            }
        }

        viewModel.pagingItems.collectOnLifecycle(viewLifecycleOwner) {
            characterAdapter.submitData(viewLifecycleOwner.lifecycle, it)
        }

        viewModel.items.collectOnLifecycle(viewLifecycleOwner) {
            favoriteCharacterAdapter.submitData(viewLifecycleOwner.lifecycle, it)
        }

        viewModel.characterEvent.collectOnLifecycle(viewLifecycleOwner) { event ->
            if (event is MainStateEvent.NavigateToDetails<CharacterModel>) {
                val action = CharactersFragmentDirections
                    .actionCharactersFragmentToDetailsCharacterFragment(event.item)
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