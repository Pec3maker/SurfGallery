package com.example.surfgallery.ui.profile

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import coil.load
import com.example.surfgallery.R
import com.example.surfgallery.common.UiState
import com.example.surfgallery.databinding.FragmentProfileBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind()
        setOnButtonClickListener()
        observeSnackbarFlow()
        observeUiStateFlow()
    }

    private fun observeUiStateFlow() {
        lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    when (state) {
                        is UiState.Loading -> {
                            binding.bvExit.setIsLoading(true)
                        }
                        is UiState.Success -> {
                            binding.bvExit.setIsLoading(false)
                        }
                        else -> Unit
                    }
                }
            }
        }
    }

    private fun setOnButtonClickListener() {
        val alertDialog: AlertDialog = activity.let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setNegativeButton(
                    R.string.dialog_cancel_label
                ) { dialog, _ ->
                    dialog.dismiss()
                }
                setPositiveButton(
                    R.string.dialog_accept_label
                ) { dialog, _ ->
                    viewModel.logout()
                    dialog.dismiss()
                }
                setMessage(R.string.dialog_message)
            }
            builder.create()
        }

        binding.bvExit.setOnClickListener {
            alertDialog.show()
        }
    }

    private fun bind() {
        val user = viewModel.user
        with(binding) {
            ivPicture.load(user.avatar)
            tvName.text = "${user.firstName}\n${user.lastName}"
            tvAbout.text = "“${user.about}”"
            tvTown.text = user.city
            tvPhone.text = user.phone
            tvEmail.text = user.email
        }
    }

    private fun observeSnackbarFlow() {
        lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.showSnackbar.collect { message ->
                    Snackbar.make(
                        binding.root,
                        getString(message),
                        Snackbar.LENGTH_LONG
                    ).setAnchorView(binding.bvExit).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}