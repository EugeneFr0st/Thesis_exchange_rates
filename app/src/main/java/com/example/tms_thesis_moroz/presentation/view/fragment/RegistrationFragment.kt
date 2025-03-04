package com.example.tms_thesis_moroz.presentation.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.tms_thesis_moroz.R
import com.example.tms_thesis_moroz.databinding.FragmentRegistrationBinding
import com.example.tms_thesis_moroz.presentation.view_model.RegistrationViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegistrationFragment : Fragment() {

    private val viewModel: RegistrationViewModel by viewModel()
    private var _binding: FragmentRegistrationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegistrationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.registerButton.setOnClickListener {
            val login = binding.email.text.toString()
            val pass = binding.password.text.toString()

            if (login.isEmpty() || pass.isEmpty()) {
                showToast(getString(R.string.fill_all_fields))
                return@setOnClickListener
            }

            binding.progressBar.visibility = View.VISIBLE
            viewModel.registerUser(login, pass)
        }

        binding.goToLogin.setOnClickListener {
            findNavController().navigate(R.id.action_registration_to_login)
        }

        observeViewModel()
    }

    override fun onStart() {
        super.onStart()
        viewModel.checkCurrentUser()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeViewModel() {
        viewModel.registrationResult.observe(viewLifecycleOwner) { result ->
            binding.progressBar.visibility = View.GONE
            if (result.isSuccess) {
                showToast(getString(R.string.account_created))
                findNavController().navigate(R.id.action_registration_to_login)
            } else {
                showToast(getString(R.string.authentication_failed))
            }
        }

        viewModel.navigateToExchange.observe(viewLifecycleOwner) {
            if (it) {
                findNavController().navigate(R.id.action_registration_to_exchange)
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}