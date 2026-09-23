package com.btjnonbrokerage.Fragment.MainFragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.btjnonbrokerage.Base.BaseFragment
import com.btjnonbrokerage.Base.MySharedPreferences
import com.btjnonbrokerage.FavoriteAdapter
import com.btjnonbrokerage.MVVM.APIModel.favorite.getFavorites.GetFavoritesRequest
import com.btjnonbrokerage.MVVM.ViewModel.MainViewModel
import com.btjnonbrokerage.databinding.FragmentFavoriteBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FavoriteFragment : BaseFragment<FragmentFavoriteBinding>() {

    @Inject
    lateinit var viewModel: MainViewModel


    private val favoriteAdapter by lazy {
        FavoriteAdapter(onClick = {
            val action =
                FavoriteFragmentDirections.actionFavoriteFragmentToDetailFullFragment(it.property_id)
            findNavController().navigate(action)
        }, clickOnLike = {
            addToWishListBaseAPI(it)
        }, clickOnUnLike = {
            removeToWishListBaseAPI(it.property_id)
        })
    }


    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentFavoriteBinding = FragmentFavoriteBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.idToolbarFavorite.toolbarBack.visibility = View.GONE
        binding.idToolbarFavorite.toolbarTitle.text = "Favorite"

        binding.rvFavorite.apply {
            adapter = favoriteAdapter
            layoutManager = GridLayoutManager(requireContext(), 2)
        }

        val localFavorites = MySharedPreferences(requireContext()).getFavoritesList()
        favoriteAdapter.submitList(localFavorites)
        if (localFavorites.isNotEmpty()) {
            binding.favoriteSimmer.visibility = View.GONE
            binding.rvFavorite.visibility = View.VISIBLE
            binding.llFavoriteProperty.visibility = View.GONE
        } else {
            binding.favoriteSimmer.visibility = View.GONE
            binding.rvFavorite.visibility = View.GONE
            binding.llFavoriteProperty.visibility = View.VISIBLE
        }


    }
}