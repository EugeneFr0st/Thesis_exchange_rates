package com.example.tms_thesis_moroz.presentation.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tms_thesis_moroz.databinding.FragmentFavoritesBinding
import com.example.tms_thesis_moroz.presentation.adapter.FavoriteAdapter
import com.example.tms_thesis_moroz.presentation.view_model.FavoriteViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesFragment : Fragment() {

    private val viewModel: FavoriteViewModel by viewModel()
    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: FavoriteAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = FavoriteAdapter(mutableListOf(), { favorite ->
            viewModel.updateFavoriteStatus(favorite)
        }, { favorite ->
            viewModel.shareCurrency(favorite, requireContext())
        })

        binding.favoriteRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.favoriteRecyclerView.adapter = adapter

        viewModel.favoriteList.observe(viewLifecycleOwner) { currencies ->
            adapter.updateData(currencies)
        }

        viewModel.fetchAndDisplayFavorites()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
