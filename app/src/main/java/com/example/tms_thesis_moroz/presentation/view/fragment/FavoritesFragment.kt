package com.example.tms_thesis_moroz.presentation.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tms_thesis_moroz.databinding.FragmentFavoritesBinding
import com.example.tms_thesis_moroz.presentation.adapter.FavoriteAdapter

class FavoritesFragment : Fragment() {
    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(context)
        binding.favoriteRecyclerView.layoutManager = layoutManager
        val adapter = FavoriteAdapter(emptyList())
        binding.favoriteRecyclerView.adapter = adapter

        fetchAndDisplayFavorites(adapter)
    }

    private fun fetchAndDisplayFavorites(adapter: FavoriteAdapter) {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}