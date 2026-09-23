package com.btjnonbrokerage.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.btjnonbrokerage.MVVM.APIModel.Search.Property
import com.btjnonbrokerage.R
import com.btjnonbrokerage.databinding.LayoutFoundEstateBinding

class searchItemAdapter(val onclick: (Property) -> Unit, val clickOnLike: (Property) -> Unit, val clickOnUnLike: (Property) -> Unit) : ListAdapter<Property, searchItemAdapter.MyViewHolder>(diff) {
  inner  class MyViewHolder (val binding : LayoutFoundEstateBinding) : RecyclerView.ViewHolder(binding.root){
      var favoritList = 0
        fun bind(item : Property){
            val isFavLocal = com.btjnonbrokerage.Base.MySharedPreferences(binding.root.context).getFavoritesList().any { it.property_id == item.property_id }
            Glide.with(binding.root.context).load(item.images[0]).placeholder(R.drawable.home_ph) .into(binding.searchImage)
            binding.tvPrice.text  = "₹ "+item.price
            binding.tvBridgeland.text=item.pr_name
            binding.tvLocationCitySearch.text = uppercaseLowerCase(item.city_name)+","
            binding.tvLocationStateSearch.text = uppercaseLowerCase(item.state_name)
            binding.propertyCategorySearch.text = item.category
            itemView.setOnClickListener {
                onclick(item)
            }
      //favorite click
            if(item.wishlist == 1 || isFavLocal){
                favoritList = 1
                binding.imgFavouritIcon.setImageResource(R.drawable.like_heart)
            }else{
                favoritList = 0
                binding.imgFavouritIcon.setImageResource(R.drawable.unlike_heart)
            }

            binding.imgFavouritIcon.setOnClickListener {
                if (favoritList == 1) {
                    favoritList = 0
                    clickOnUnLike(item)
                    binding.imgFavouritIcon.setImageResource(R.drawable.unlike_heart)

                } else {
                    favoritList = 1
                    clickOnLike(item)
                    binding.imgFavouritIcon.setImageResource(R.drawable.like_heart)
                }
            }

            }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
       return  MyViewHolder(LayoutFoundEstateBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
       val item = getItem(position)
        holder.bind(item)
    }


    object  diff : DiffUtil.ItemCallback<Property>() {
        override fun areItemsTheSame(oldItem: Property, newItem: Property): Boolean {
           return  oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Property, newItem: Property): Boolean {
           return  oldItem == newItem
        }
    }

    fun uppercaseLowerCase( text : String) : String {
        return   text.replaceFirstChar { it.uppercase() }.lowercase().replaceFirstChar { it.uppercase() }
    }
}