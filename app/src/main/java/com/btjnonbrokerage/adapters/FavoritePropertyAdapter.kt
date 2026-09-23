package com.btjnonbrokerage.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.btjnonbrokerage.MVVM.APIModel.favorite.getFavorites.Property
import com.btjnonbrokerage.R
import com.btjnonbrokerage.databinding.LayoutExploreNearbyPlacesBinding

class FavoritePropertyAdapter(val onClick : (Property) -> Unit, val clickOnLike : (Property)->  Unit, val clickOnUnLike : (Property)-> Unit) : ListAdapter<Property, FavoritePropertyAdapter.MyViewHolder>(Diff) {
  inner  class MyViewHolder(private val binding: LayoutExploreNearbyPlacesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var wishlist = 1
        fun bind(item: Property) {


            Glide.with(binding.root.context).load(item.property_images[0]).placeholder(R.drawable.home_ph) .into(binding.imageSale)
            binding.tvHomeSaleTitle.text = item.pr_name
            binding.locationTv.text = item.state

            binding.root.setOnClickListener {
                onClick(item)
            }
            binding.heartIcon.setOnClickListener {
                if(wishlist==1){
                    wishlist = 0
                    clickOnUnLike(item)
                    binding.heartIcon.setImageResource(R.drawable.unlike_heart)

                }else{
                    wishlist = 1
                    clickOnLike(item)
                    binding.heartIcon.setImageResource(R.drawable.like_heart)
                }

            }
        }


    }


    object Diff : DiffUtil.ItemCallback<Property>() {
        override fun areItemsTheSame(oldItem: Property, newItem: Property): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Property, newItem: Property): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
       return  MyViewHolder(LayoutExploreNearbyPlacesBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
     holder.bind(getItem(position))
    }
}