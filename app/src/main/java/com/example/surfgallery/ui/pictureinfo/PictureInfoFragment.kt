package com.example.surfgallery.ui.pictureinfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import com.example.surfgallery.databinding.FragmentPictureInfoBinding
import com.example.surfgallery.extensions.formatTimeStampToDate
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PictureInfoFragment : Fragment() {

    private var _binding: FragmentPictureInfoBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PictureInfoViewModel by viewModels()
    private val args: PictureInfoFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPictureInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind()
        setBackButtonListener()
    }

    private fun setBackButtonListener() {
        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun bind() {
        val picture = viewModel.getPictureInfo(id = args.pictureId)
        with(binding) {
            iv.load(picture.photoUrl)
            tvTitle.text = picture.title
            tvDate.text = picture.publicationDate.formatTimeStampToDate()
            tvDescription.text = picture.content
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}