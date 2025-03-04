package com.example.tms_thesis_moroz.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tms_thesis_moroz.R
import com.example.tms_thesis_moroz.data.model.CurrencyRatesEntity
import com.example.tms_thesis_moroz.databinding.ItemFavoriteBinding

class FavoriteAdapter(
    private var favorites: MutableList<CurrencyRatesEntity>,
    private val onFavoriteClick: (CurrencyRatesEntity) -> Unit,
    private val onShareClick: (CurrencyRatesEntity) -> Unit
) : RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

    class FavoriteViewHolder(
        private val binding: ItemFavoriteBinding,
        private val onFavoriteClick: (CurrencyRatesEntity) -> Unit,
        private val onShareClick: (CurrencyRatesEntity) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(favorites: CurrencyRatesEntity) {
            binding.favoriteName.text = favorites.currencyPair
            binding.favoriteRate.text = favorites.exchangeRate.toString()

            binding.starButton.setImageResource(
                if (favorites.isFavorite) R.drawable.ic_gold_star else R.drawable.ic_star
            )

            binding.starButton.setOnClickListener {
                onFavoriteClick(favorites)
            }

            binding.favoriteShare.setOnClickListener {
                onShareClick(favorites)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding =
            ItemFavoriteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteViewHolder(binding, onFavoriteClick, onShareClick)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(favorites[position])
    }

    override fun getItemCount(): Int = favorites.size

    fun updateData(newList: List<CurrencyRatesEntity>) {
        favorites = newList.toMutableList()
        notifyDataSetChanged()
    }
}
