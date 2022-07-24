package com.example.surfgallery.ui.gallery

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
import androidx.recyclerview.widget.GridLayoutManager
import com.example.surfgallery.R
import com.example.surfgallery.common.UiListState
import com.example.surfgallery.databinding.FragmentGalleryBinding
import com.example.surfgallery.navigation.Screen
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GalleryFragment : Fragment() {

    private var _binding: FragmentGalleryBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: PictureAdapter
    private val viewModel: GalleryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        adapter = PictureAdapter { id ->
            val action = GalleryFragmentDirections.actionGalleryFragmentToPictureInfoFragment(id)
            findNavController().navigate(action)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        observePicturesFlow()
        setRefreshListener()
        setOnClickListener()
    }

    private fun setOnClickListener() {
        binding.searchButton.setOnClickListener {
            findNavController().navigate(Screen.Search.action)
        }
        binding.updateButton.setOnClickListener {
            viewModel.updateData()
        }
    }

    private fun setRefreshListener() {
        val sr = binding.sr
        sr.setOnRefreshListener {
            viewModel.updateData()
            sr.isRefreshing = false
        }
    }

    private fun observePicturesFlow() {
        lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.pictures.collect { uiState ->
                    with(binding) {
                        when (uiState) {
                            is UiListState.Loading -> {
                                progressCircular.visibility = View.VISIBLE
                                rcView.visibility = View.GONE
                                tvGallery.visibility = View.GONE
                                updateButton.visibility = View.GONE
                                searchButton.visibility = View.GONE
                                llError.visibility = View.GONE
                                sr.visibility = View.GONE
                            }
                            is UiListState.Error -> {
                                if (adapter.itemCount == 0) {
                                    progressCircular.visibility = View.GONE
                                    rcView.visibility = View.GONE
                                    tvGallery.visibility = View.GONE
                                    updateButton.visibility = View.VISIBLE
                                    searchButton.visibility = View.GONE
                                    sr.visibility = View.GONE
                                    llError.visibility = View.VISIBLE
                                } else {
                                    progressCircular.visibility = View.GONE
                                    rcView.visibility = View.VISIBLE
                                    tvGallery.visibility = View.VISIBLE
                                    updateButton.visibility = View.GONE
                                    searchButton.visibility = View.VISIBLE
                                    sr.visibility = View.VISIBLE
                                    llError.visibility = View.GONE
                                    Snackbar.make(
                                        binding.root,
                                        getString(
                                            uiState.uiError.snackbarMessageRes
                                                ?: R.string.empty_string
                                        ),
                                        Snackbar.LENGTH_LONG
                                    ).show()
                                }
                            }
                            is UiListState.Success -> {
                                progressCircular.visibility = View.GONE
                                rcView.visibility = View.VISIBLE
                                tvGallery.visibility = View.VISIBLE
                                updateButton.visibility = View.GONE
                                searchButton.visibility = View.VISIBLE
                                sr.visibility = View.VISIBLE
                                llError.visibility = View.GONE

                                adapter.addAllPictures(uiState.data)
                            }
                            is UiListState.Empty -> Unit
                        }
                    }
                }
            }
        }
    }

    private fun init() {
        with(binding) {
            rcView.layoutManager = GridLayoutManager(context, SPAN_COUNT)
            rcView.adapter = adapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val SPAN_COUNT = 2
    }
}