package shvyn22.flexingmarvel.presentation.series.details

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.view.MenuProvider
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import shvyn22.flexingmarvel.R
import shvyn22.flexingmarvel.data.local.model.SeriesModel
import shvyn22.flexingmarvel.databinding.FragmentDetailsSeriesBinding
import shvyn22.flexingmarvel.presentation.adapters.character.CharacterAdapter
import shvyn22.flexingmarvel.presentation.adapters.event.EventAdapter
import shvyn22.flexingmarvel.util.*
import javax.inject.Inject

@AndroidEntryPoint
class DetailsSeriesFragment : Fragment(R.layout.fragment_details_series) {

    private val args by navArgs<DetailsSeriesFragmentArgs>()

    @Inject
    lateinit var detailsSeriesViewModelFactory: DetailsSeriesViewModel.Factory
    private val viewModel: DetailsSeriesViewModel by viewModels {
        DetailsSeriesViewModel.provideFactory(detailsSeriesViewModelFactory, args.series)
    }

    private val characterAdapter = CharacterAdapter { viewModel.onCharacterClick(it) }
    private val eventAdapter = EventAdapter { viewModel.onEventClick(it) }

    private var _binding: FragmentDetailsSeriesBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentDetailsSeriesBinding.bind(view)

        initUI(args.series)
        subscribeObservers()
        configureMenu()
    }

    private fun initUI(item: SeriesModel) {
        activity?.hideBottomBar()

        binding.apply {
            rvCharacters.apply {
                adapter = characterAdapter
                setHasFixedSize(true)
            }

            rvEvents.apply {
                adapter = eventAdapter
                setHasFixedSize(true)
            }

            tvTitle.text = item.title

            Glide.with(root)
                .load(item.thumbnail.getFullUrl())
                .defaultRequests()
                .into(ivDetails)

            item.description.let {
                if (it.isNullOrEmpty()) tvDesc.isVisible = false
                else tvDesc.text = getString(R.string.text_desc, it)
            }

            btnLibrary.setOnClickListener { viewModel.onToggleFavorite() }
        }
    }

    private fun subscribeObservers() {
        binding.apply {
            viewModel.characters.collectOnLifecycle(viewLifecycleOwner) { resource ->
                pbCharacters.isVisible = resource is Resource.Loading
                tvCharactersError.isVisible = resource is Resource.Error
                tvCharacters.isVisible = resource is Resource.Success
                rvCharacters.isVisible = resource is Resource.Success

                if (resource is Resource.Success)
                    characterAdapter.submitList(resource.data)
                else if (resource is Resource.Idle)
                    tvCharactersError.text = getString(R.string.text_characters_error)
            }

            viewModel.events.collectOnLifecycle(viewLifecycleOwner) { resource ->
                pbEvents.isVisible = resource is Resource.Loading
                tvEventsError.isVisible = resource is Resource.Error
                tvEvents.isVisible = resource is Resource.Success
                rvEvents.isVisible = resource is Resource.Success

                if (resource is Resource.Success)
                    eventAdapter.submitList(resource.data)
                else if (resource is Resource.Idle)
                    tvEventsError.text = getString(R.string.text_events_error)
            }

            viewModel.isSeriesFavorite.collectOnLifecycle(viewLifecycleOwner) {
                btnLibrary.apply {
                    if (it) {
                        text = getString(R.string.text_remove_favorite)
                        setCompoundDrawablesWithIntrinsicBounds(
                            R.drawable.ic_favorite, 0, 0, 0
                        )
                    } else {
                        text = getString(R.string.text_add_favorite)
                        setCompoundDrawablesWithIntrinsicBounds(
                            R.drawable.ic_not_favorite, 0, 0, 0
                        )
                    }
                }
            }
        }

        viewModel.detailsEvent.collectOnLifecycle(viewLifecycleOwner) { event ->
            when (event) {
                is DetailsStateEvent.NavigateToCharacterDetails -> {
                    val action = DetailsSeriesFragmentDirections
                        .actionDetailsSeriesFragmentToDetailsCharacterFragment(event.item)
                    findNavController().navigate(action)
                }
                is DetailsStateEvent.NavigateToEventDetails -> {
                    val action = DetailsSeriesFragmentDirections
                        .actionDetailsSeriesFragmentToDetailsEventFragment(event.item)
                    findNavController().navigate(action)
                }
                else -> throw IllegalArgumentException()
            }
        }
    }

    private fun configureMenu() {
        activity?.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) = Unit

            override fun onPrepareMenu(menu: Menu) {
                super.onPrepareMenu(menu)
                menu.children.forEach { it.isVisible = false }
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean = false
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}