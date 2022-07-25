package com.example.surfgallery.ui.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.surfgallery.data.local.dto.toPicture
import com.example.surfgallery.databinding.FragmentFavoritesBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: FavoritesPictureAdapter
    private val viewModel: FavoritesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        adapter = FavoritesPictureAdapter(
            onItemClick = { id ->
                val action =
                    FavoritesFragmentDirections.actionFavoritesFragmentToPictureInfoFragment(id)
                findNavController().navigate(action)
            },
            onFavoriteClick = viewModel::onFavoriteClick
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        observeStoredPictures()
    }


    private fun observeStoredPictures() {
        lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.storedPictures.collect { entityPics ->
                    adapter.addAllPictures(entityPics.map { it.toPicture() })

                    if (adapter.itemCount == 0) {
                        binding.rcView.visibility = View.GONE
                        binding.llEmpty.visibility = View.VISIBLE
                    } else {
                        binding.rcView.visibility = View.VISIBLE
                        binding.llEmpty.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun init() {
        with(binding) {
            rcView.layoutManager = LinearLayoutManager(context).apply {
                orientation = LinearLayoutManager.VERTICAL
            }
            rcView.adapter = adapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}