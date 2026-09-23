package com.btjnonbrokerage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.btjnonbrokerage.MVVM.APIModel.favorite.getFavorites.Property
import com.btjnonbrokerage.databinding.ItemPropertyLayoutBinding

class FavoriteAdapter(val onClick: (Property) -> Unit, val clickOnLike: (Property) -> Unit, val clickOnUnLike: (Property) -> Unit) : ListAdapter<Property, FavoriteAdapter.MyViewHolder>(Diff) {
    inner class MyViewHolder(val binding: ItemPropertyLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var wishlist = 1
        fun bind(item: Property) {

            Glide.with(binding.root.context).load(item.property_images.get(0)).into(binding.ivPropertyImage)
            binding.ivHeartIcon.setOnClickListener {
                if(wishlist==1){
                    wishlist = 0
                    clickOnUnLike(item)
                    binding.ivHeartIcon.setImageResource(R.drawable.unlike_heart)
                }else{
                    wishlist = 1
                    clickOnLike(item)
                    binding.ivHeartIcon.setImageResource(R.drawable.like_heart)
                }
            }

            itemView.setOnClickListener {
                onClick(item)
            }
            try {
                binding.tvHomeSaleTitle.text = uppercaseLowerCase(item.pr_name)
                binding.tvLocationCity.text = uppercaseLowerCase(item.city_name) +","
                binding.tvLocationState.text = uppercaseLowerCase(item.state_name)
                binding.ratingTv.text = item.avg_rating.toString()
                binding.tvPricing.text = item.price+"/"
                binding.tvMonthOrYear.text = item.plan_type
                binding.propertyCategory.text = item.category
            }catch (_:Exception){

            }




        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {

        return MyViewHolder(
            ItemPropertyLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(getItem(position))

    }

    object Diff : DiffUtil.ItemCallback<Property>() {
        override fun areItemsTheSame(oldItem: Property, newItem: Property): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Property, newItem: Property): Boolean {
            return oldItem == newItem
        }
    }

    fun uppercaseLowerCase( text : String) : String {
        return   text.replaceFirstChar { it.uppercase() }.lowercase().replaceFirstChar { it.uppercase() }
    }
}