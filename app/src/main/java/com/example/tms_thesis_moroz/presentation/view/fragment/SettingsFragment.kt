package com.example.tms_thesis_moroz.presentation.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.tms_thesis_moroz.R
import com.example.tms_thesis_moroz.databinding.FragmentSettingsBinding
import com.example.tms_thesis_moroz.presentation.view_model.SettingsViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment: Fragment() {

    private val viewModel: SettingsViewModel by viewModel()
    private lateinit var auth: FirebaseAuth
    private lateinit var user: FirebaseUser
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth
        user = auth.currentUser!!

        binding.currentUser.text = user.email

        binding.logOut.setOnClickListener {
            Firebase.auth.signOut()
            findNavController().navigate(R.id.action_settings_to_login)
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}