package com.example.tms_thesis_moroz.presentation.view.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.tms_thesis_moroz.R
import com.example.tms_thesis_moroz.databinding.FragmentSettingsBinding
import com.example.tms_thesis_moroz.presentation.view_model.SettingsViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {

    private val viewModel: SettingsViewModel by viewModel()
    private lateinit var auth: FirebaseAuth
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth.currentUser?.let {
            binding.currentUser.text = it.email
        }

        binding.logOut.setOnClickListener {
            showLogoutConfirmationDialog()
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.navigationEvent.collect { destinationId ->
                if (isAdded && destinationId != null) {
                    findNavController().navigate(destinationId)
                }
            }
        }

    }

    private fun showLogoutConfirmationDialog() {
        AlertDialog.Builder(requireActivity())
            .setTitle(getString(R.string.logout_dialog_title))
            .setMessage(getString(R.string.logout_dialog_message))
            .setPositiveButton(getString(R.string.logout_dialog_positive_button)) { _, _ ->
                viewModel.logout()
            }
            .setNegativeButton(getString(R.string.logout_dialog_negative_button), null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}