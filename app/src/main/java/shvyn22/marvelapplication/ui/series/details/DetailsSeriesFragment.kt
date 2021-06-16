package shvyn22.marvelapplication.ui.series.details

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import shvyn22.marvelapplication.R
import shvyn22.marvelapplication.databinding.FragmentDetailsSeriesBinding
import shvyn22.marvelapplication.ui.adapters.character.CharactersAdapter
import shvyn22.marvelapplication.ui.adapters.event.EventsAdapter
import shvyn22.marvelapplication.util.*

@AndroidEntryPoint
class DetailsSeriesFragment : Fragment(R.layout.fragment_details_series) {

    private val args by navArgs<DetailsSeriesFragmentArgs>()
    private val viewModel: DetailsSeriesViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentDetailsSeriesBinding.bind(view)
        activity?.hideBottomBar()

        val item = args.series

        val characterAdapter = CharactersAdapter { viewModel.onCharacterClick(it) }
        val eventAdapter = EventsAdapter { viewModel.onEventClick(it) }

        binding.apply {
            rvCharacters.apply {
                adapter = characterAdapter
                setHasFixedSize(true)
            }

            viewModel.getCharacters(item.id).observe(viewLifecycleOwner) { resource ->
                pbCharacters.isVisible = resource is Resource.Loading
                tvCharactersError.isVisible = resource is Resource.Error
                tvCharacters.isVisible = resource is Resource.Success
                rvCharacters.isVisible = resource is Resource.Success

                if (resource is Resource.Success)
                    characterAdapter.submitList(resource.data)
                else if (resource is Resource.Empty)
                    tvCharactersError.text = getString(R.string.text_characters_error)
            }

            rvEvents.apply {
                adapter = eventAdapter
                setHasFixedSize(true)
            }

            viewModel.getEvents(item.id).observe(viewLifecycleOwner) { resource ->
                pbEvents.isVisible = resource is Resource.Loading
                tvEventsError.isVisible = resource is Resource.Error
                tvEvents.isVisible = resource is Resource.Success
                rvEvents.isVisible = resource is Resource.Success

                if (resource is Resource.Success)
                    eventAdapter.submitList(resource.data)
                else if (resource is Resource.Empty)
                    tvEventsError.text = getString(R.string.text_events_error)
            }

            Glide.with(view)
                .load(item.thumbnail.getFullUrl())
                .defaultRequests()
                .into(ivDetails)

            tvTitle.text = item.title
            item.description.let {
                if (it.isNullOrEmpty()) tvDesc.hide()
                else tvDesc.text = getString(R.string.text_desc, it)
            }

            val addTag = getString(R.string.tag_add)
            val removeTag = getString(R.string.tag_remove)

            btnLibrary.setOnClickListener {
                if (btnLibrary.tag == addTag) viewModel.addToFavorite(item)
                else viewModel.removeFromFavorite(item)
            }

            viewModel.isSeriesFavorite(item.id).observe(viewLifecycleOwner) {
                btnLibrary.apply {
                    if (it) {
                        tag = removeTag
                        text = removeTag
                        setCompoundDrawablesWithIntrinsicBounds(
                            R.drawable.ic_favorite, 0, 0, 0
                        )
                    } else {
                        tag = addTag
                        text = addTag
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
}