package com.example.surfgallery.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.surfgallery.R
import com.example.surfgallery.common.UiState
import com.example.surfgallery.databinding.FragmentLoginBinding
import com.google.android.material.snackbar.Snackbar
import com.redmadrobot.inputmask.MaskedTextChangedListener
import com.redmadrobot.inputmask.MaskedTextChangedListener.Companion.installOn
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeUiStateFlow()
        observeSnackbarFlow()
        observeLoginErrorFlow()
        observePasswordErrorFlow()
        initLoginMask()

        binding.enterButton.setOnClickListener {
            viewModel.onEnterClick()
        }

        binding.tfPasswordEdit.doOnTextChanged { text, _, _, _ ->
            viewModel.updatePassword(text.toString())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun observeLoginErrorFlow() {
        lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.loginErrorMessage.collect { message ->
                    with(binding) {
                        if (message != R.string.empty_string) {
                            tfLogin.isErrorEnabled = true
                            tfLogin.error = getString(message)
                        } else {
                            tfLogin.isErrorEnabled = false
                            tfLogin.error = getString(R.string.empty_string)
                        }
                    }
                }
            }
        }
    }

    private fun observePasswordErrorFlow() {
        lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.passwordErrorMessage.collect { message ->
                    with(binding) {
                        if (message != R.string.empty_string) {
                            tfPassword.isErrorEnabled = true
                            tfPassword.error = getString(message)
                        } else {
                            tfPassword.isErrorEnabled = false
                            tfPassword.error = getString(R.string.empty_string)
                        }
                    }
                }
            }
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
                    ).setAnchorView(binding.enterButton).show()
                }
            }
        }
    }

    private fun observeUiStateFlow() {
        lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    with(binding) {
                        when (state) {
                            is UiState.Success -> {
                                tfLogin.isEnabled = true
                                tfPassword.isEnabled = true
                                enterButton.setIsLoading(false)
                            }
                            is UiState.Loading -> {
                                tfLogin.isEnabled = false
                                tfPassword.isEnabled = false
                                enterButton.setIsLoading(true)
                            }
                            else -> Unit
                        }
                    }
                }
            }
        }
    }

    private fun initLoginMask() {
        installOn(
            binding.tfLoginEdit,
            PHONE_MASK,
            object : MaskedTextChangedListener.ValueListener {
                override fun onTextChanged(
                    maskFilled: Boolean,
                    extractedValue: String,
                    formattedValue: String
                ) {
                    viewModel.updateLogin(
                        if (extractedValue.isNotEmpty()) COUNTRY_CODE + extractedValue else EMPTY_STRING
                    )
                }
            }
        )
    }

    companion object {
        private const val COUNTRY_CODE = "+7"
        const val PHONE_MASK = "$COUNTRY_CODE ([000]) [000] [00] [00]"
        const val EMPTY_STRING = ""
    }
}