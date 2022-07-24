package com.example.surfgallery.ui.search

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.surfgallery.R
import com.example.surfgallery.databinding.FragmentSearchBinding
import com.example.surfgallery.ui.common.PictureAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SearchViewModel by viewModels()
    private lateinit var adapter: PictureAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        adapter = PictureAdapter(
            onItemClick = { id ->
                val action =
                    SearchFragmentDirections.actionSearchFragmentToPictureInfoFragment(id)
                findNavController().navigate(action)
            },
            onFavoriteClick = viewModel::onFavoriteClick
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        with(binding) {
            tfSearchEdit.addTextChangedListener(
                onTextChanged = { text, _, _, count ->
                    val filteredPics = viewModel.getFilteredPictures(query = text.toString())
                    when {
                        count == 0 -> {
                            buttonClear.visibility = View.GONE
                            errorIcon.setImageResource(R.drawable.ic_initial_state)
                            errorText.text = getString(R.string.initial_search_state)
                            errorIcon.visibility = View.VISIBLE
                            errorText.visibility = View.VISIBLE
                            rcView.visibility = View.GONE
                        }
                        filteredPics.isNotEmpty() -> {
                            buttonClear.visibility = View.VISIBLE
                            errorIcon.visibility = View.GONE
                            errorText.visibility = View.GONE
                            rcView.visibility = View.VISIBLE
                            adapter.addAllPictures(pics = filteredPics)
                        }
                        else -> {
                            buttonClear.visibility = View.VISIBLE
                            errorIcon.setImageResource(R.drawable.ic_error)
                            errorText.text = getString(R.string.error_search_state)
                            errorIcon.visibility = View.VISIBLE
                            errorText.visibility = View.VISIBLE
                            rcView.visibility = View.GONE
                        }
                    }
                }
            )

            backButton.setOnClickListener {
                findNavController().popBackStack()
            }

            buttonClear.setOnClickListener {
                tfSearchEdit.text.clear()
            }

            rcView.setOnTouchListener(
                View.OnTouchListener { v, event ->
                    v.performClick()
                    val imm =
                        context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                    imm?.hideSoftInputFromWindow(rcView.windowToken, 0)
                    false
                }
            )
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