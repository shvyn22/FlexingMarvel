package shvyn22.flexingmarvel.presentation.events.details

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
import shvyn22.flexingmarvel.data.local.model.EventModel
import shvyn22.flexingmarvel.databinding.FragmentDetailsEventsBinding
import shvyn22.flexingmarvel.presentation.adapters.character.CharacterAdapter
import shvyn22.flexingmarvel.presentation.adapters.series.SeriesAdapter
import shvyn22.flexingmarvel.util.*
import javax.inject.Inject

@AndroidEntryPoint
class DetailsEventFragment : Fragment(R.layout.fragment_details_events) {

    private val args by navArgs<DetailsEventFragmentArgs>()

    @Inject
    lateinit var detailsEventViewModelFactory: DetailsEventViewModel.Factory
    private val viewModel: DetailsEventViewModel by viewModels {
        DetailsEventViewModel.provideFactory(detailsEventViewModelFactory, args.event)
    }

    private val characterAdapter = CharacterAdapter { viewModel.onCharacterClick(it) }
    private val seriesAdapter = SeriesAdapter { viewModel.onSeriesClick(it) }

    private var _binding: FragmentDetailsEventsBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentDetailsEventsBinding.bind(view)

        initUI(args.event)
        subscribeObservers()
        configureMenu()
    }

    private fun initUI(item: EventModel) {
        activity?.hideBottomBar()

        binding.apply {
            rvCharacters.apply {
                adapter = characterAdapter
                setHasFixedSize(true)
            }

            rvSeries.apply {
                adapter = seriesAdapter
                setHasFixedSize(true)
            }

            tvTitle.text = item.title

            Glide.with(root)
                .load(item.thumbnail.getFullUrl())
                .defaultRequests()
                .into(ivDetails)

            item.description.let {
                if (it.isEmpty()) tvDesc.isVisible = false
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

            viewModel.series.collectOnLifecycle(viewLifecycleOwner) { resource ->
                pbSeries.isVisible = resource is Resource.Loading
                tvSeriesError.isVisible = resource is Resource.Error
                tvSeries.isVisible = resource is Resource.Success
                rvSeries.isVisible = resource is Resource.Success

                if (resource is Resource.Success)
                    seriesAdapter.submitList(resource.data)
                else if (resource is Resource.Idle)
                    tvSeriesError.text = getString(R.string.text_series_error)
            }

            viewModel.isEventFavorite.collectOnLifecycle(viewLifecycleOwner) {
                btnLibrary.apply {
                    if (it) {
                        text = getString(R.string.tag_remove)
                        setCompoundDrawablesWithIntrinsicBounds(
                            R.drawable.ic_favorite, 0, 0, 0
                        )
                    } else {
                        text = getString(R.string.tag_add)
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
                    val action = DetailsEventFragmentDirections
                        .actionDetailsEventFragmentToDetailsCharacterFragment(event.item)
                    findNavController().navigate(action)
                }
                is DetailsStateEvent.NavigateToSeriesDetails -> {
                    val action = DetailsEventFragmentDirections
                        .actionDetailsEventFragmentToDetailsSeriesFragment(event.item)
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