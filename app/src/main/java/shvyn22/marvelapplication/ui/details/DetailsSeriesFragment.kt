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
import shvyn22.marvelapplication.data.entity.Event
import shvyn22.marvelapplication.data.entity.MarvelCharacter
import shvyn22.marvelapplication.databinding.FragmentDetailsSeriesBinding
import shvyn22.marvelapplication.ui.adapters.CharacterAdapter
import shvyn22.marvelapplication.ui.adapters.EventAdapter

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

            viewModel.getSeriesItems(item.id)

            viewModel.events.observe(viewLifecycleOwner) {
                if (it.isNotEmpty()) {
                    eventAdapter.submitList(it)
                    rvEvent.apply {
                        adapter = eventAdapter
                        setHasFixedSize(true)
                    }
                } else tvEventsError.visibility = View.VISIBLE
            }

            viewModel.characters.observe(viewLifecycleOwner) {
                if (it.isNotEmpty()) {
                    characterAdapter.submitList(it)
                    rvCharacters.apply {
                        adapter = characterAdapter
                        setHasFixedSize(true)
                    }
                } else tvCharactersError.visibility = View.VISIBLE
            }

            Glide.with(view)
                .load(item.thumbnail.getFullUrl())
                .fitCenter()
                .transition(DrawableTransitionOptions.withCrossFade())
                .error(R.drawable.ic_error)
                .into(ivDetails)

            tvTitle.text = item.title
            tvDesc.text = getString(R.string.text_desc, item.description)

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


    override fun onEventItemClick(item: Event) {
        viewModel.onEventItemClick(item)
    }

    override fun onCharacterItemClick(item: MarvelCharacter) {
        viewModel.onCharacterItemClick(item)
    }
}