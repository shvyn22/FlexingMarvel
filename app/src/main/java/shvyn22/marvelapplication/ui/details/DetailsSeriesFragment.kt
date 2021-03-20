package shvyn22.marvelapplication.ui.details

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import shvyn22.marvelapplication.R
import shvyn22.marvelapplication.data.model.EventModel
import shvyn22.marvelapplication.data.model.CharacterModel
import shvyn22.marvelapplication.databinding.FragmentDetailsSeriesBinding
import shvyn22.marvelapplication.ui.adapters.CharacterAdapter
import shvyn22.marvelapplication.ui.adapters.EventAdapter
import shvyn22.marvelapplication.util.Resource
import shvyn22.marvelapplication.util.hide
import shvyn22.marvelapplication.util.show

@AndroidEntryPoint
class DetailsSeriesFragment : Fragment(R.layout.fragment_details_series),
        EventAdapter.OnItemClickListener, CharacterAdapter.OnItemClickListener {

    private val args by navArgs<DetailsSeriesFragmentArgs>()
    private val viewModel: DetailsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navBar = activity?.findViewById<BottomNavigationView>(R.id.bottom_nav_view)
        navBar?.visibility = View.GONE

        val binding = FragmentDetailsSeriesBinding.bind(view)
        val item = args.series

        val eventAdapter = EventAdapter(this)
        val characterAdapter = CharacterAdapter(this)

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.isSeriesFavorite(item.id)
            viewModel.getSeriesItems(item.id)
            viewModel.detailsEvent.collect { event ->
                when (event) {
                    is DetailsViewModel.DetailsEvent.NavigateToEventDetails -> {
                        val action = DetailsSeriesFragmentDirections
                                .actionDetailsSeriesFragmentToDetailsEventFragment(event.item)
                        findNavController().navigate(action)
                    }
                    is DetailsViewModel.DetailsEvent.NavigateToCharacterDetails -> {
                        val action = DetailsSeriesFragmentDirections
                                .actionDetailsSeriesFragmentToDetailsCharacterFragment(event.item)
                        findNavController().navigate(action)
                    }
                }
            }
        }

        binding.apply {

            rvEvents.apply {
                adapter = eventAdapter
                setHasFixedSize(true)
            }

            viewModel.events.observe(viewLifecycleOwner) {
                when (it) {
                    is Resource.Success -> {
                        pbEvents.hide()
                        tvEventsError.hide()
                        rvEvents.show()
                        eventAdapter.submitList(it.data)
                    }
                    is Resource.Empty -> {
                        pbEvents.hide()
                        tvEventsError.show()
                        tvEventsError.text = getString(R.string.text_events_error)
                        rvEvents.hide()
                    }
                    is Resource.Loading -> {
                        pbEvents.show()
                        tvEventsError.hide()
                        rvEvents.hide()
                    }
                    is Resource.Error -> {
                        pbEvents.hide()
                        tvEventsError.show()
                        rvEvents.hide()
                    }
                }
            }

            rvCharacters.apply {
                adapter = characterAdapter
                setHasFixedSize(true)
            }

            viewModel.characters.observe(viewLifecycleOwner) {
                when (it) {
                    is Resource.Success -> {
                        pbCharacters.hide()
                        tvCharactersError.hide()
                        rvCharacters.show()
                        characterAdapter.submitList(it.data)
                    }
                    is Resource.Empty -> {
                        pbCharacters.hide()
                        tvCharactersError.show()
                        tvCharactersError.text = getString(R.string.text_characters_error)
                        rvCharacters.hide()
                    }
                    is Resource.Loading -> {
                        pbCharacters.show()
                        tvCharactersError.hide()
                        rvCharacters.hide()
                    }
                    is Resource.Error -> {
                        pbCharacters.hide()
                        tvCharactersError.show()
                        tvCharactersError.text = it.msg
                        rvCharacters.hide()
                    }
                }
            }

            Glide.with(view)
                .load(item.thumbnail.getFullUrl())
                .fitCenter()
                .transition(DrawableTransitionOptions.withCrossFade())
                .error(R.drawable.ic_error)
                .into(ivDetails)

            tvTitle.text = item.title
            item.description.let {
                if (it.isNullOrEmpty()) tvDesc.hide()
                else tvDesc.text = getString(R.string.text_desc, it)
            }

            val addTag = getString(R.string.tag_add)
            val addDrawable = R.drawable.ic_not_favorite
            val removeTag = getString(R.string.tag_remove)
            val removeDrawable = R.drawable.ic_favorite

            viewModel.isSeriesFavorite.observe(viewLifecycleOwner) {
                btnLibrary.apply {
                    if (it) {
                        tag = removeTag
                        text = removeTag
                        setCompoundDrawablesWithIntrinsicBounds(
                                removeDrawable, 0, 0, 0)
                    } else {
                        tag = addTag
                        text = addTag
                        setCompoundDrawablesWithIntrinsicBounds(
                                addDrawable, 0, 0, 0)
                    }
                }
            }

            btnLibrary.setOnClickListener { viewModel.onToggleSeriesFavorite(item) }
        }
    }

    override fun onEventItemClick(item: EventModel) {
        viewModel.onEventItemClick(item)
    }

    override fun onCharacterItemClick(item: CharacterModel) {
        viewModel.onCharacterItemClick(item)
    }
}