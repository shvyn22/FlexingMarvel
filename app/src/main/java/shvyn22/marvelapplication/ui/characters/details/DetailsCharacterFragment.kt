package shvyn22.marvelapplication.ui.characters.details

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
import shvyn22.marvelapplication.databinding.FragmentDetailsCharacterBinding
import shvyn22.marvelapplication.ui.adapters.event.EventsAdapter
import shvyn22.marvelapplication.ui.adapters.series.SeriesAdapter
import shvyn22.marvelapplication.util.*

@AndroidEntryPoint
class DetailsCharacterFragment : Fragment(R.layout.fragment_details_character) {

    private val args by navArgs<DetailsCharacterFragmentArgs>()
    private val viewModel: DetailsCharacterViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentDetailsCharacterBinding.bind(view)
        activity?.hideBottomBar()

        val item = args.character

        val eventAdapter = EventsAdapter { viewModel.onEventClick(it) }
        val seriesAdapter = SeriesAdapter { viewModel.onSeriesClick(it) }

        binding.apply {
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

            rvSeries.apply {
                adapter = seriesAdapter
                setHasFixedSize(true)
            }

            viewModel.getSeries(item.id).observe(viewLifecycleOwner) { resource ->
                pbSeries.isVisible = resource is Resource.Loading
                tvSeriesError.isVisible = resource is Resource.Error
                tvSeries.isVisible = resource is Resource.Success
                rvSeries.isVisible = resource is Resource.Success

                if (resource is Resource.Success)
                    seriesAdapter.submitList(resource.data)
                else if (resource is Resource.Empty)
                    tvSeriesError.text = getString(R.string.text_series_error)
            }

            Glide.with(view)
                .load(item.thumbnail.getFullUrl())
                .defaultRequests()
                .into(ivDetails)

            tvTitle.text = item.name

            item.description.let {
                if (it.isEmpty()) tvDesc.hide()
                else tvDesc.text = getString(R.string.text_desc, it)
            }

            val addTag = getString(R.string.tag_add)
            val removeTag = getString(R.string.tag_remove)

            btnLibrary.setOnClickListener {
                if (btnLibrary.tag == addTag) viewModel.addToFavorite(item)
                else viewModel.removeFromFavorite(item)
            }

            viewModel.isCharacterFavorite(item.id).observe(viewLifecycleOwner) {
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
                is DetailsStateEvent.NavigateToEventDetails -> {
                    val action = DetailsCharacterFragmentDirections
                        .actionDetailsCharacterFragmentToDetailsEventFragment(event.item)
                    findNavController().navigate(action)
                }
                is DetailsStateEvent.NavigateToSeriesDetails -> {
                    val action = DetailsCharacterFragmentDirections
                        .actionDetailsCharacterFragmentToDetailsSeriesFragment(event.item)
                    findNavController().navigate(action)
                }
                else -> throw IllegalArgumentException()
            }
        }
    }
}