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
import shvyn22.marvelapplication.data.model.CharacterModel
import shvyn22.marvelapplication.data.model.SeriesModel
import shvyn22.marvelapplication.databinding.FragmentDetailsEventsBinding
import shvyn22.marvelapplication.ui.adapters.CharacterAdapter
import shvyn22.marvelapplication.ui.adapters.SeriesAdapter

@AndroidEntryPoint
class DetailsEventFragment : Fragment(R.layout.fragment_details_events),
        CharacterAdapter.OnItemClickListener, SeriesAdapter.OnItemClickListener {

    private val args by navArgs<DetailsEventFragmentArgs>()
    private val viewModel: DetailsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navBar = activity?.findViewById<BottomNavigationView>(R.id.bottom_nav_view)
        navBar?.visibility = View.GONE

        val binding = FragmentDetailsEventsBinding.bind(view)
        val item = args.event

        val characterAdapter = CharacterAdapter(this)
        val seriesAdapter = SeriesAdapter(this)

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.isEventFavorite(item.id)
            viewModel.detailsEvent.collect { event ->
                when (event) {
                    is DetailsViewModel.DetailsEvent.NavigateToCharacterDetails -> {
                        val action = DetailsEventFragmentDirections
                                .actionDetailsEventFragmentToDetailsCharacterFragment(event.item)
                        findNavController().navigate(action)
                    }
                    is DetailsViewModel.DetailsEvent.NavigateToSeriesDetails -> {
                        val action = DetailsEventFragmentDirections
                                .actionDetailsEventFragmentToDetailsSeriesFragment(event.item)
                        findNavController().navigate(action)
                    }
                }
            }
        }

        binding.apply {

            viewModel.getEventItems(item.id)

            viewModel.characters.observe(viewLifecycleOwner) {
                if (it.isNotEmpty()) {
                    characterAdapter.submitList(it)
                    rvCharacters.apply {
                        adapter = characterAdapter
                        setHasFixedSize(true)
                    }
                } else tvCharactersError.visibility = View.VISIBLE
            }

            viewModel.series.observe(viewLifecycleOwner) {
                if (it.isNotEmpty()) {
                    seriesAdapter.submitList(it)
                    rvSeries.apply {
                        adapter = seriesAdapter
                        setHasFixedSize(true)
                    }
                } else tvSeriesError.visibility = View.VISIBLE
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

            viewModel.isEventFavorite.observe(viewLifecycleOwner) {
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

            btnLibrary.setOnClickListener { viewModel.onToggleEventFavorite(item) }
        }
    }

    override fun onCharacterItemClick(item: CharacterModel) {
        viewModel.onCharacterItemClick(item)
    }

    override fun onSeriesItemClick(item: SeriesModel) {
        viewModel.onSeriesItemClick(item)
    }
}